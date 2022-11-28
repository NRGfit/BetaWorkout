package com.example.nrgfitapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nrgfitapp.DAOs.Posts
import com.example.nrgfitapp.DAOs.PostAdapter
import com.example.nrgfitapp.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery

lateinit var rvPosts: RecyclerView
lateinit var adapter: PostAdapter
lateinit var swipeContainer: SwipeRefreshLayout

var allPosts: MutableList<Posts> = mutableListOf()

open class HomeFragment : Fragment() {
    val TAG = "ForumFragment"
    override fun onCreateView(
        btFab = findViewById(R.id.btFab)

                btFab.setOnClickListener {
            val intent = Intent(this, ComposeActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }
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

        adapter = PostAdapter(requireContext(), allPosts)
        rvPosts.adapter = adapter
        rvPosts.layoutManager = LinearLayoutManager(requireContext())

        queryPosts()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            val tweet = data?.getParcelableExtra("tweet") as Tweet

            tweets.add(0, tweet)

            adapter.notifyItemInserted(0)
            rvTweets.smoothScrollToPosition(0)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
    open fun queryPosts() {
        val query: ParseQuery<Posts> = ParseQuery.getQuery(Posts::class.java)
        query.include(Posts.KEY_USER)
        query.findInBackground(object : FindCallback<Posts> {
            override fun done(posts: MutableList<Posts>?, e: ParseException?) {
                if(e != null){
                    e.printStackTrace()
                    Log.e(TAG, "Error fetching posts")
                }else{
                    if(posts != null){
                        for(post in posts) {
                            Log.i(TAG, "Post: ${post.getDescription()} Username: ${post.getUser()?.username}")
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