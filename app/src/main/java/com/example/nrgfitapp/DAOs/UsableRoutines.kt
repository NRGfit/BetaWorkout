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
    fun setRoutine(routine: ParseObject){
        put(KEY_ROUTINE, routine)
    }

    fun getUser(): ParseUser? {
        return getParseUser(KEY_USER)
    }
    fun setUser(user: ParseUser){
        put(KEY_USER, user)
    }

    fun getOwner(): ParseUser? {
        return getParseUser(KEY_OWNER)
    }
    fun setOwner(user: ParseUser){
        put(KEY_OWNER, user)
    }

    companion object {
        const val KEY_ROUTINE = "Routine"
        const val KEY_USER = "User"
        const val KEY_OWNER = "Owner"
    }
}