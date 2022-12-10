package com.example.nrgfitapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nrgfitapp.DAOs.Exercise
import com.example.nrgfitapp.DAOs.Posts
import com.example.nrgfitapp.DAOs.Routine
import com.parse.ParseUser

class ComposeRoutineActivity : AppCompatActivity() {
    val TAG = "ComposeRoutineActivity"

    lateinit var tvRoutineName: EditText
    lateinit var tvDescription: EditText
    lateinit var btnAddExercise: Button
    lateinit var btnAddRoutine: Button
    lateinit var routineCreateRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose_routine)

        tvRoutineName = findViewById(R.id.tvRoutineName)
        tvDescription = findViewById(R.id.tvDescription)
        btnAddExercise = findViewById(R.id.btnAddExercise)
        btnAddRoutine = findViewById(R.id.btnAddRoutine)
        routineCreateRecyclerView = findViewById(R.id.routineCreateRecyclerView)




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