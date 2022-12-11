package com.example.nrgfitapp.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nrgfitapp.ComposeActivity
import com.example.nrgfitapp.DAOs.PostAdapter
import com.example.nrgfitapp.DAOs.Posts
import com.example.nrgfitapp.DAOs.Routine
import com.example.nrgfitapp.DAOs.RoutineAdapter
import com.example.nrgfitapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import okhttp3.OkHttpClient
import okhttp3.Request


open class ForumFragment : Fragment() {
    lateinit var rvPosts: RecyclerView
    lateinit var adapter: PostAdapter
    lateinit var swipeContainer: SwipeRefreshLayout
    lateinit var btFab: FloatingActionButton
    var allPosts: MutableList<Posts> = mutableListOf()

    val TAG = "ForumFragment"
    var REQUEST_CODE = 10;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forum, container, false)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            adapter.notifyDataSetChanged()
            rvPosts.smoothScrollToPosition(0)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    open fun queryPosts() {
        val query: ParseQuery<Posts> = ParseQuery.getQuery(Posts::class.java)
        query.include(Posts.KEY_USER)
        query.include(Posts.KEY_USABLE_ROUTINE)
        query.addDescendingOrder("createdAt")
        query.findInBackground { posts, e ->
            if (e != null) {
                e.printStackTrace()
                Log.e(TAG, "Error fetching posts")
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