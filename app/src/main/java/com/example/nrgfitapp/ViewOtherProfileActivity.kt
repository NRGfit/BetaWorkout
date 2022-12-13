package com.example.nrgfitapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.bumptech.glide.Glide
import com.example.nrgfitapp.DAOs.*
import com.parse.ParseQuery
import com.parse.ParseUser

class ViewOtherProfileActivity : AppCompatActivity() {

    private lateinit var rvPosts: RecyclerView
    private lateinit var adapter: PostAdapter
    lateinit var swipeContainer: SwipeRefreshLayout
    private var allPosts: MutableList<Posts> = mutableListOf()
    private lateinit var tvWorkoutCount: TextView
    lateinit var profileFollow: Button
    lateinit var tvFollows: TextView

    val TAG = "OtherProfileActivity"
    var REQUEST_CODE = 10;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_other_profile)

        var user:ParseUser = ParseUser.getCurrentUser()

        // Specify which class to query
        val query: ParseQuery<ParseUser> = ParseQuery.getQuery(ParseUser::class.java)

        // Find all Routine objects
        query.whereEqualTo("objectId", intent.getStringExtra("User"))
        val list = query.find()

        if(list.size>0)
            user = list[0]


        val ivProfile = findViewById<ImageView>(R.id.ivProfile)
        val tvProfileUsername = findViewById<TextView>(R.id.tvProfileUsername)

        tvProfileUsername.text = user.username
        Glide.with(this).load(user.getParseFile("pfp")?.url).into(ivProfile)

        rvPosts = findViewById(R.id.feedRecyclerView)

        tvWorkoutCount = findViewById(R.id.tvWorkoutCount)
        tvFollows = findViewById(R.id.tvFollowerCount)
        swipeContainer = findViewById(R.id.swipeContainer)

        profileFollow = findViewById(R.id.profileFollow)
        updateFollowsButton(profileFollow, user, tvFollows)
        profileFollow.setOnClickListener{
            // Specify which class to query
            val query: ParseQuery<Follows> = ParseQuery.getQuery(Follows::class.java)

            // Find all Routine objects
            query.include(Follows.KEY_FOLLOWER)
            query.include(Follows.KEY_FOLLOWING)
            query.addDescendingOrder("createdAt")
            query.whereEqualTo(Follows.KEY_FOLLOWER, ParseUser.getCurrentUser())
            query.whereEqualTo(Follows.KEY_FOLLOWING, user)
            val follows = query.find()
            if (follows == null) {
                Log.e(TAG, "ERROR")
            } else {
                if (follows.size == 0) {
                    Log.i(TAG, "I haven't followed it")
                    val follow = Follows()
                    follow.setFollowing(user)
                    follow.setFollower(ParseUser.getCurrentUser())
                    follow.save()
                    Log.i(TAG, "Saved follow")
                } else {
                    Log.i(TAG, "I have followed it")
                    follows[0].delete()
                    Log.i(TAG, "Unfollowed")
                }
                updateFollowsButton(profileFollow, user, tvFollows)
            }
        }

        val refreshListener = OnRefreshListener {
            queryPosts(user)
        }.also { var refreshListener = it }

        swipeContainer.setOnRefreshListener(refreshListener)


        swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        );

        adapter = PostAdapter(this, allPosts)

        rvPosts.adapter = adapter
        rvPosts.layoutManager = LinearLayoutManager(this)

        queryPosts(user)
        queryRoutines(user)
        refreshListener.onRefresh()
    }

    open fun queryRoutines(user: ParseUser) {
        // Specify which class to query
        val query: ParseQuery<UsableRoutines> = ParseQuery.getQuery(UsableRoutines::class.java)

        // Find all Routine objects
        query.include(UsableRoutines.KEY_ROUTINE)
        query.addDescendingOrder("createdAt")
        query.whereEqualTo(UsableRoutines.KEY_USER, user)
        query.count()
        tvWorkoutCount.text = query.count().toString()


    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            adapter.notifyDataSetChanged()
            rvPosts.smoothScrollToPosition(0)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    open fun updateFollowsButton(profileFollow: Button, user: ParseUser, tvFollows: TextView) {
        val query: ParseQuery<Follows> = ParseQuery.getQuery(Follows::class.java)
        query.include(Follows.KEY_FOLLOWING)
        query.include(Follows.KEY_FOLLOWER)
        query.addDescendingOrder("createdAt")
        query.whereEqualTo(Follows.KEY_FOLLOWING, user)
        var count = query.count()
        tvFollows.text =  "$count"
        query.whereEqualTo(Follows.KEY_FOLLOWER, ParseUser.getCurrentUser())
        count = query.count()
        if (count > 0) {
            profileFollow.background.setTint(0xFF004225.toInt())

        }
        else {
            profileFollow.background.setTint(0xFF2196F3.toInt())
        }

    }
    open fun queryPosts(user:ParseUser) {
        // Specify which class to query
        val query: ParseQuery<Posts> = ParseQuery.getQuery(Posts::class.java)

        // Find all Post objects
        query.include(Posts.KEY_USER)
        query.include(Posts.KEY_USABLE_ROUTINE)
        query.addDescendingOrder("createdAt")
        query.whereEqualTo(Posts.KEY_USER, user)
        query.findInBackground { posts, e ->
            if (e != null) {
                Log.e(TAG, "ERROR")
            } else {
                if (posts != null) {
                    allPosts.clear()
                    allPosts.addAll(posts)
                    adapter.notifyDataSetChanged()
                    swipeContainer.isRefreshing = false
                }
            }
        }
    }

}