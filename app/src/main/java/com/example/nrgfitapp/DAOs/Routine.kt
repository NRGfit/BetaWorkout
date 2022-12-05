package com.example.nrgfitapp.DAOs

import com.example.nrgfitapp.TimeFormatter
import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser
import java.util.*

@ParseClassName("Routine")

class Routine : ParseObject() {
    fun getRoutineName(): String? {
        return getString(KEY_ROUTINE_NAME)
    }
    fun setRoutineName(description: String){
        put(KEY_ROUTINE_NAME, description)
    }


    fun getUser(): ParseUser? {
        return getParseUser(KEY_USER)
    }
    fun setUser(user: ParseUser){
        put(KEY_USER, user)
    }

    fun getFormattedTimestamp(createdAt: Date): String? {
        return TimeFormatter.getTimeDifference(createdAt.toString())
    }
    companion object {
        const val KEY_ROUTINE_NAME = "routineName"
        const val KEY_USER = "username"
    }
}