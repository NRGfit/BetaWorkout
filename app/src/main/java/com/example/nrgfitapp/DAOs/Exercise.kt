package com.example.nrgfitapp.DAOs

import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser


@ParseClassName("RoutineExercise")

class Exercise : ParseObject() {


    fun getExerciseName(): String? {
        return getString(KEY_EXERCISENAME)
    }

    fun setExerciseName(exerciseName: String) {
        put(KEY_EXERCISENAME, exerciseName)
    }


    fun getExerciseDBID(): String? {
        return getString(KEY_EXERCISEDBID)
    }

    fun setExerciseDBID(exerciseDBID: String) {
        put(KEY_EXERCISEDBID, exerciseDBID)
    }




    companion object {
        const val KEY_EXERCISENAME = "exerciseName"
        const val KEY_EXERCISEDBID = "exerciseDBID"



    }
}