package com.example.nrgfitapp.DAOs

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser
import java.util.*

@ParseClassName("Follows")
class Follows : ParseObject() {

    fun getFollowing(): ParseUser? {
        return getParseUser(KEY_FOLLOWING)
    }

    fun setFollowing(user: ParseUser){
        put(KEY_FOLLOWING, user)
    }

    fun getFollower(): ParseUser? {
        return getParseUser(KEY_FOLLOWER)
    }

    fun setFollower(user: ParseUser){
        put(KEY_FOLLOWER, user)
    }
    companion object {
        const val KEY_FOLLOWING = "Following"
        const val KEY_FOLLOWER = "Follower"
    }
}