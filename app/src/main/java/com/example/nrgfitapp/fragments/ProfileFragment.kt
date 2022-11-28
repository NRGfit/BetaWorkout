package com.example.nrgfitapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nrgfitapp.ComposeActivity
import com.example.nrgfitapp.DAOs.PostAdapter
import com.example.nrgfitapp.DAOs.Posts
import com.example.nrgfitapp.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

class ProfileFragment : ForumFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvPosts = view.findViewById(R.id.feedRecyclerView)
        swipeContainer = view.findViewById(R.id.swipeContainer)

        swipeContainer.setOnRefreshListener {
            queryPosts()
        }

        swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        );

        btFab = view.findViewById(R.id.createPost)

        btFab.setOnClickListener {
            val intent = Intent(this.context, ComposeActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }

        adapter = PostAdapter(requireContext(), allPosts)
        rvPosts.adapter = adapter
        rvPosts.layoutManager = LinearLayoutManager(requireContext())

        queryPosts()
    }

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