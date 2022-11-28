package com.example.nrgfitapp.fragments

import android.util.Log
import com.example.nrgfitapp.DAOs.Posts
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

class ProfileFragment : ForumFragment() {



    override fun queryPosts() {

        // Specify which class to query
        val query: ParseQuery<Posts> = ParseQuery.getQuery(Posts::class.java)

        // Find all Post objects
        query.include(Posts.KEY_USER)
        query.addDescendingOrder("createdAt")
        query.whereEqualTo(Posts.KEY_USER, ParseUser.getCurrentUser())
        query.findInBackground(object: FindCallback<Posts> {
            override fun done(posts: MutableList<Posts>?, e: ParseException?) {
                if (e != null) {
                    Log.e(TAG, "ERROR")
                } else {
                    if (posts != null) {
                        for (post in posts) {
                            Log.i(
                                TAG, "Post: " + post.getDescription() + ", Username: " +
                                        post.getUser()?.username)
                        }
                        allPosts.clear()
                        allPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                        swipeContainer.isRefreshing = false
                    }
                }
            }
        })
    }
}