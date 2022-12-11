package com.example.nrgfitapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nrgfitapp.DAOs.*
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser

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

        val `idMap` = setExercisesInPopup(showPopUp)

        showPopUp.setOnMenuItemClickListener { menuItem ->
            idMap[menuItem.itemId].getExerciseDBID()?.let { addExerciseToRV(it) }
            false
        }

        btnAddExercise.setOnClickListener {
            showPopUp.show()
        }

        btnAddRoutine.setOnClickListener{
            if(tvRoutineName.text.toString() == "" || tvDescription.text.toString() == ""){
                Toast.makeText(this, "Missing a routine name or description", Toast.LENGTH_SHORT).show()
            }else {
                val exercises = getRoutineExercises(idMap)
                if (exercises != null)
                    submitRoutine(
                        tvDescription.text.toString(),
                        tvRoutineName.text.toString(),
                        exercises
                    )
            }
        }

        adapter = RoutineCreateExerciseAdapter(this, exercisesToAdd)
        routineCreateRecyclerView.adapter = adapter
        routineCreateRecyclerView.layoutManager = LinearLayoutManager(this)

    }

    fun setExercisesInPopup(popupMenu: PopupMenu) : MutableList<Exercise>{
        val idMap: MutableList<Exercise> = mutableListOf()
        val query: ParseQuery<Exercise> = ParseQuery.getQuery(Exercise::class.java)
        query.findInBackground { exercises, e ->
            if (e != null) {
                e.printStackTrace()
                Log.e(TAG, "Error fetching posts")
            } else {
                if (exercises != null) {
                    for(i in 0 until exercises.size){
                        popupMenu.menu.add(Menu.NONE, i, i, exercises[i].getExerciseName())
                        idMap.add(exercises[i])
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

    fun getRoutineExercises(idmap: MutableList<Exercise>): MutableList<RoutineExercise>? {
        val routineExercises: MutableList<RoutineExercise> = mutableListOf()

        val childCount = adapter.itemCount
        if(childCount == 0){
            Toast.makeText(this, "Add some Exercises first!", Toast.LENGTH_SHORT).show()
            return null
        }
        for (i in 0 until childCount) {
            val holder : RoutineCreateExerciseAdapter.ViewHolder = adapter.holders[i]
            val routineExercise : RoutineExercise = RoutineExercise()

            var exercise: Exercise? = null
            for(i in 0 until idmap.size) {
                if (idmap[i].getExerciseName() == holder.tvExerciseName.text.toString()) {
                    exercise = idmap[i]
                    break
                }
            }

            if(exercise == null ||
                holder.tvExerciseNotes.text.toString() == "" ||
                holder.tvExerciseSets.text.toString() == "" ||
                holder.tvExerciseReps.text.toString() == "" ||
                holder.tvExerciseWeights.text.toString() == ""){
                Toast.makeText(this, "Fill out all the required fields", Toast.LENGTH_SHORT).show()
                return null
            }else {
                if(!holder.tvExerciseSets.text.toString().isDigitsOnly() ||
                    !holder.tvExerciseReps.text.toString().isDigitsOnly()) {
                    Toast.makeText(this, "Reps and Sets should be a number", Toast.LENGTH_SHORT).show()
                    return null
                }

                holder.tvExerciseSets.text.toString().isDigitsOnly()
                routineExercise.setExercise(exercise)
                routineExercise.setNotes(holder.tvExerciseNotes.text.toString())
                routineExercise.setSets(holder.tvExerciseSets.text.toString().toInt())
                routineExercise.setReps(holder.tvExerciseReps.text.toString().toInt())
                routineExercise.setWeights(holder.tvExerciseWeights.text.toString())
            }

            routineExercises.add(routineExercise)
        }
        return routineExercises
    }

    fun submitRoutine(description: String, routineName: String, exercises: MutableList<RoutineExercise>) {
        Log.i(TAG, "Reached")
        var routine = Routine()

        routine.setDescription(description)
        routine.setRoutineName(routineName)
        routine.save()
        Log.i(TAG, "Saved Routine")

        val query: ParseQuery<Routine> = ParseQuery.getQuery(Routine::class.java)
        query.addDescendingOrder("createdAt")
        query.findInBackground { routines, e ->
            if (e != null) {
                Log.e(TAG, "ERROR")
            } else {
                if (routines != null) {
                    routine = routines[0]

                    val usableRoutine = UsableRoutines()
                    usableRoutine.setRoutine(routine)
                    usableRoutine.setUser(ParseUser.getCurrentUser())
                    usableRoutine.setOwner(ParseUser.getCurrentUser())
                    usableRoutine.save()
                    Log.i(TAG, "Saved UsableRoutine")

                    for(i in 0 until exercises.size){
                        exercises[i].setRoutine(routine)
                        exercises[i].save()
                        Log.i(TAG, "Saved UsableRoutine $i")
                    }
                }
            }
        }

        finish()
    }
}
