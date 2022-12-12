package com.example.nrgfitapp.DAOs

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser
import java.util.*

@ParseClassName("Likes")
class Likes : ParseObject() {

    fun getUser(): ParseUser? {
        return getParseUser(KEY_USER)
    }

    fun setUser(user: ParseUser){
        put(KEY_USER, user)
    }

    fun getPost(): ParseObject? {
        return ParseObject(KEY_POST)
    }
    fun setPost(post: ParseObject) {
        put(KEY_POST, post)
    }
    companion object {
        const val KEY_USER = "User"
        const val KEY_POST = "Posts"
    }
}
