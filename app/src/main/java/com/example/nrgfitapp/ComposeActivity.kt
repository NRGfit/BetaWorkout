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
import com.example.nrgfitapp.DAOs.Exercise
import com.example.nrgfitapp.DAOs.Posts
import com.example.nrgfitapp.DAOs.Routine
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
    lateinit var tvCharCount: TextView


    val TAG = "Compose Activity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        tvPostCompose = findViewById(R.id.tvPostCompose)
        btnPost = findViewById(R.id.btnPost)
        btnRoutineDrop = findViewById(R.id.btnRoutineDrop)
        tvCharCount = findViewById(R.id.tvCharCount)

        val showPopUp = PopupMenu(
            this,
            btnRoutineDrop
        )

        showPopUp.inflate(R.menu.popup_exercises)

        //val idMap = setRoutinesInPopup(showPopUp)

        btnRoutineDrop.setOnClickListener {
            showPopUp.show()
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
    }

    fun setRoutinesInPopup(popupMenu: PopupMenu) : MutableList<String>{
        val idMap: MutableList<String> = mutableListOf()
        val query: ParseQuery<Routine> = ParseQuery.getQuery(Routine::class.java)
        query.findInBackground { routines, e ->
            if (e != null) {
                e.printStackTrace()
                Log.e(TAG, "Error fetching posts")
            } else {
                if (routines != null) {
                    for(i in 0 until routines.size){
                        popupMenu.menu.add(Menu.NONE, i, i, routines[i].getRoutineName())
                        //routines[i].getExerciseDBID()?.let { idMap.add(i, it) }
                    }
                }
            }
        }
        return idMap
    }

    fun submitPost(description: String, user: ParseUser) {
        val post = Posts()
        post.setDescription(description)
        post.setUser(user)
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
