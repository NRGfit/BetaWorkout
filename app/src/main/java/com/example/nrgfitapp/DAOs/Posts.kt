package com.example.nrgfitapp.DAOs

import com.example.nrgfitapp.TimeFormatter


import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser
import java.util.*

@ParseClassName("Posts")
class Posts : ParseObject() {

    fun getDescription(): String? {
        return getString(KEY_DESCRIPTION)
    }
    fun setDescription(description: String){
        put(KEY_DESCRIPTION, description)
    }

    fun getUsableRoutine(): ParseObject? {
        return getParseObject(KEY_USABLE_ROUTINE)
    }
    fun setUsableRoutine(routine: ParseObject){
        put(KEY_USABLE_ROUTINE, routine)
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
        const val KEY_DESCRIPTION = "description"
        const val KEY_USABLE_ROUTINE = "UsableRoutine"
        const val KEY_USER = "username"
    }
}