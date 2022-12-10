package com.example.nrgfitapp.DAOs

import com.example.nrgfitapp.TimeFormatter
import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser
import java.util.*

@ParseClassName("UsableRoutines")

class UsableRoutines : ParseObject() {
    fun getRoutine(): ParseObject? {
        return getParseObject(KEY_ROUTINE)
    }
    fun setRoutine(routine: String){
        put(KEY_ROUTINE, routine)
    }

    fun getUser(): ParseUser? {
        return getParseUser(KEY_USER)
    }
    fun setUser(user: String){
        put(KEY_USER, user)
    }

    companion object {
        const val KEY_ROUTINE = "Routine"
        const val KEY_USER = "User"
    }
}