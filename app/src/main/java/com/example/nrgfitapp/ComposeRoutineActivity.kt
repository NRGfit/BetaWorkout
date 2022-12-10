package com.example.nrgfitapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nrgfitapp.DAOs.*
import com.parse.ParseQuery

class ComposeRoutineActivity : AppCompatActivity() {
    val TAG = "ComposeRoutineActivity"

    lateinit var tvRoutineName: EditText
    lateinit var tvDescription: EditText
    lateinit var btnAddExercise: Button
    lateinit var btnAddRoutine: Button
    lateinit var routineCreateRecyclerView: RecyclerView
    lateinit var adapter: RoutineCreateExerciseAdapter
    var exercisesToAdd: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose_routine)

        tvRoutineName = findViewById(R.id.tvRoutineName)
        tvDescription = findViewById(R.id.tvDescription)
        btnAddExercise = findViewById(R.id.btnAddExercise)
        btnAddRoutine = findViewById(R.id.btnAddRoutine)
        routineCreateRecyclerView = findViewById(R.id.routineCreateRecyclerView)

        val showPopUp = PopupMenu(
            this,
            btnAddExercise
        )

        showPopUp.inflate(R.menu.popup_exercises)

        val idMap = setExercisesInPopup(showPopUp)

        showPopUp.setOnMenuItemClickListener { menuItem ->
            addExerciseToRV(idMap[menuItem.itemId])
            false
        }

        btnAddExercise.setOnClickListener {
            showPopUp.show()
        }

        adapter = RoutineCreateExerciseAdapter(this, exercisesToAdd)
        routineCreateRecyclerView.adapter = adapter
        routineCreateRecyclerView.layoutManager = LinearLayoutManager(this)

    }

    fun setExercisesInPopup(popupMenu: PopupMenu) : MutableList<String>{
        val idMap: MutableList<String> = mutableListOf()
        val query: ParseQuery<Exercise> = ParseQuery.getQuery(Exercise::class.java)
        query.findInBackground { exercises, e ->
            if (e != null) {
                e.printStackTrace()
                Log.e(TAG, "Error fetching posts")
            } else {
                if (exercises != null) {
                    for(i in 0 until exercises.size){
                        popupMenu.menu.add(Menu.NONE, i, i, exercises[i].getExerciseName())
                        exercises[i].getExerciseDBID()?.let { idMap.add(i, it) }
                    }
                }
            }
        }
        return idMap
    }

    fun addExerciseToRV(exercise: String){
        //Log.i(TAG, exercise)
        exercisesToAdd.add(exercise)
        adapter.notifyDataSetChanged()
    }

    fun submitRoutine(description: String, routineName: String, exercises: MutableList<Exercise>) {
        val routine = Routine()
        routine.setDescription(description)
        routine.setRoutineName(routineName)
        routine.saveInBackground { exception ->
            if (exception != null) {
                Log.e(TAG, "ERROR WHILE SAVING")
                exception.printStackTrace()
            } else {
                Log.i(TAG, "Success saving post")
            }
        }
    }
}