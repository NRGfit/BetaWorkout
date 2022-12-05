package com.example.nrgfitapp.DAOs

import com.example.nrgfitapp.TimeFormatter
import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser
import java.util.*


@ParseClassName("RoutineExercise")

class RoutineExercise : ParseObject() {


    fun getRoutine(): ParseObject? {
        return getParseObject(KEY_ROUTINE)
    }
    fun setRoutine(routine: ParseObject){
        put(KEY_ROUTINE, routine)
    }


    fun getExercise(): ParseObject? {
        return getParseUser(KEY_EXERCISE)
    }
    fun setExercise(exercise: RoutineExercise){
        put(KEY_EXERCISE, exercise)
    }


    fun getSets(): Number? {
        return getNumber(KEY_SETS)
    }
    fun setSets(sets: Number){
        put(KEY_SETS, sets)
    }


    fun getReps(): Number? {
        return getNumber(KEY_REPS)
    }
    fun setReps(reps: Number){
        put(KEY_REPS, reps)
    }

    fun getNotes(): String? {
        return getString(KEY_NOTES)
    }
    fun setNotes(notes: String){
        put(KEY_NOTES, notes)
    }

    companion object {
        const val KEY_ROUTINE = "Routine"
        const val KEY_EXERCISE = "Exercise"
        const val KEY_SETS = "sets"
        const val KEY_REPS = "reps"
        const val KEY_NOTES = "notes"



    }
}