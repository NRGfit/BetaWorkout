package com.example.nrgfitapp.DAOs

import com.parse.ParseClassName
import com.parse.ParseObject


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
    fun setExercise(exercise: Exercise){
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

    fun getWeights(): String? {
        return getString(KEY_WEIGHTS)
    }
    fun setWeights(weights: String){
        put(KEY_WEIGHTS, weights)
    }

    companion object {
        const val KEY_ROUTINE = "Routine"
        const val KEY_EXERCISE = "Exercise"
        const val KEY_SETS = "sets"
        const val KEY_REPS = "reps"
        const val KEY_NOTES = "notes"
        const val KEY_WEIGHTS = "weights"



    }
}