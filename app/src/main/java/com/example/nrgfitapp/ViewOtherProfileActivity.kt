package com.example.nrgfitapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.bumptech.glide.Glide
import com.example.nrgfitapp.DAOs.PostAdapter
import com.example.nrgfitapp.DAOs.Posts
import com.example.nrgfitapp.DAOs.UsableRoutines
import com.parse.ParseQuery
import com.parse.ParseUser

class ViewOtherProfileActivity : AppCompatActivity() {

    private lateinit var rvPosts: RecyclerView
    private lateinit var adapter: PostAdapter
    lateinit var swipeContainer: SwipeRefreshLayout
    private var allPosts: MutableList<Posts> = mutableListOf()
    private lateinit var tvWorkoutCount: TextView


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

        swipeContainer = findViewById(R.id.swipeContainer)

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
        queryRoutines()
        refreshListener.onRefresh()
    }

    open fun queryRoutines() {
        // Specify which class to query
        val query: ParseQuery<UsableRoutines> = ParseQuery.getQuery(UsableRoutines::class.java)

        // Find all Routine objects
        query.include(UsableRoutines.KEY_ROUTINE)
        query.addDescendingOrder("createdAt")
        query.whereEqualTo(UsableRoutines.KEY_USER, ParseUser.getCurrentUser())
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