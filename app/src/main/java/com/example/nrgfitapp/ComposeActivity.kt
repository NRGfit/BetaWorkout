package com.example.nrgfitapp

import android.content.Intent
import android.graphics.Color.argb
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.widget.*
import androidx.appcompat.widget.PopupMenu
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nrgfitapp.DAOs.*
import com.parse.ParseFile
import com.parse.ParseQuery
import com.parse.ParseUser
import okhttp3.Headers
import org.json.JSONObject
import java.io.File

class ComposeActivity : AppCompatActivity() {

    lateinit var tvPostCompose: EditText
    lateinit var btnPost: Button
    lateinit var btnRoutineDrop: Button
    lateinit var btnRoutineRemove: Button
    lateinit var tvCharCount: TextView
    lateinit var adapter: RoutineAdapter
    lateinit var routinePost: RecyclerView
    var routineToAdd: MutableList<UsableRoutines> = mutableListOf()

    val TAG = "Compose Activity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        tvPostCompose = findViewById(R.id.tvPostCompose)
        btnPost = findViewById(R.id.btnPost)
        btnRoutineRemove = findViewById(R.id.btnRoutineRemove)
        btnRoutineDrop = findViewById(R.id.btnRoutineDrop)
        tvCharCount = findViewById(R.id.tvCharCount)
        routinePost = findViewById(R.id.routinePost)

        val showPopUp = PopupMenu(
            this,
            btnRoutineDrop
        )

        showPopUp.inflate(R.menu.popup_exercises)

        val idMap = setRoutinesInPopup(showPopUp)

        showPopUp.setOnMenuItemClickListener { menuItem ->
            addRoutineToRV(idMap[menuItem.itemId])
            false
        }

        btnRoutineDrop.setOnClickListener {
            showPopUp.show()
        }

        btnRoutineRemove.setOnClickListener {
            routineToAdd.clear()
            adapter.notifyDataSetChanged()
        }

        tvPostCompose.doAfterTextChanged {
            val length = tvPostCompose.text.toString().length
            if(length > 280) {
                tvCharCount.text = length.toString().plus("/280")
                tvCharCount.setTextColor(argb(255,255,0,0))
            }else{
                tvCharCount.text = length.toString().plus("/280")
                tvCharCount.setTextColor(argb(255,0,0,255))
            }
        }


        btnPost.setOnClickListener {
            val postContent = tvPostCompose.text.toString()

            if(postContent.isEmpty())
                Toast.makeText(this, "Empty posts not allowed!", Toast.LENGTH_SHORT).show()
            else if(postContent.length > 280)
                Toast.makeText(this, "Post is too long! Limit is 280 characters", Toast.LENGTH_SHORT).show()
            else {
                submitPost(postContent, ParseUser.getCurrentUser())
                setResult(RESULT_OK, intent)
                finish()
            }

        }

        adapter = RoutineAdapter(this, routineToAdd)
        routinePost.adapter = adapter
        routinePost.layoutManager = LinearLayoutManager(this)
    }

    fun addRoutineToRV(routine: UsableRoutines){
        //Log.i(TAG, exercise)
        if(routineToAdd.size<1) {
            routineToAdd.add(routine)
            adapter.notifyDataSetChanged()
        }else{
            Toast.makeText(this, "Only allowed 1 Routine per post", Toast.LENGTH_SHORT).show()
        }
    }

    fun setRoutinesInPopup(popupMenu: PopupMenu) : MutableList<UsableRoutines>{
        val routineMap: MutableList<UsableRoutines> = mutableListOf()
        val query: ParseQuery<UsableRoutines> = ParseQuery.getQuery(UsableRoutines::class.java)
        query.include(UsableRoutines.KEY_ROUTINE)
        query.whereEqualTo(UsableRoutines.KEY_USER, ParseUser.getCurrentUser())
        query.findInBackground { routines, e ->
            if (e != null) {
                e.printStackTrace()
                Log.e(TAG, "Error fetching posts")
            } else {
                if (routines != null) {
                    for(i in 0 until routines.size){
                        val routine :Routine = (routines[i].getRoutine() as Routine)
                        popupMenu.menu.add(Menu.NONE, i, i, routine.getRoutineName())
                        routineMap.add(routines[i])
                    }
                }
            }
        }
        return routineMap
    }

    fun submitPost(description: String, user: ParseUser) {
        val post = Posts()
        post.setDescription(description)
        post.setUser(user)
        if(!routineToAdd.isEmpty())
            post.setUsableRoutine(routineToAdd[0])
        post.saveInBackground { exception ->
            if (exception != null) {
                Log.e(TAG, "ERROR WHILE SAVING")
                exception.printStackTrace()
            } else {
                Log.i(TAG, "Success saving post")
            }
        }
    }
}
