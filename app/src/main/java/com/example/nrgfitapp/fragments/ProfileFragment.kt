package com.example.nrgfitapp.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.nrgfitapp.ComposeActivity
import com.example.nrgfitapp.DAOs.*
import com.example.nrgfitapp.R
import com.example.nrgfitapp.ViewOtherProfileActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

open class ProfileFragment : Fragment() {
    private lateinit var rvPosts: RecyclerView
    private lateinit var adapter: PostAdapter
    lateinit var swipeContainer: SwipeRefreshLayout
    private var allPosts: MutableList<Posts> = mutableListOf()
    var allRoutines: MutableList<UsableRoutines> = mutableListOf()
    private lateinit var tvWorkoutCount: TextView
    lateinit var tvFollows: TextView
    lateinit var tvFollower: TextView

    val TAG = "ProfileFragment"
    var REQUEST_CODE = 10;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = ParseUser.getCurrentUser()
        val ivProfile = view.findViewById<ImageView>(R.id.ivProfile)
        val tvProfileUsername = view.findViewById<TextView>(R.id.tvProfileUsername)
        tvWorkoutCount = view.findViewById(R.id.tvWorkoutCount)

        tvProfileUsername.text = user.username
        Glide.with(view.context).load(user.getParseFile("pfp")?.url).into(ivProfile)


        rvPosts = view.findViewById(R.id.feedRecyclerView)
        tvFollows = view.findViewById(R.id.tvFollowerCount)
        tvFollower = view.findViewById(R.id.tvFollowingCount)

        val showPopUpFollows = PopupMenu(
            requireContext(),
            tvFollows
        )
        showPopUpFollows.inflate(R.menu.popup_exercises)

        val showPopUpFollower = PopupMenu(
            requireContext(),
            tvFollower
        )
        showPopUpFollower.inflate(R.menu.popup_exercises)

        var followsMap = setFollowsInPopup(showPopUpFollows, user)
        var followersMap = setFollowersInPopup(showPopUpFollower, user)

        showPopUpFollows.setOnMenuItemClickListener { menuItem ->
            Log.i(TAG, "Yes works")
            val intent = Intent(requireContext(), ViewOtherProfileActivity::class.java)
            intent.putExtra("User", followsMap[menuItem.itemId].objectId)
            this.startActivity(intent)
            false
        }

        showPopUpFollower.setOnMenuItemClickListener { menuItem ->
            Log.i(TAG, "Yes works")
            val intent = Intent(requireContext(), ViewOtherProfileActivity::class.java)
            intent.putExtra("User", followersMap[menuItem.itemId].objectId)
            this.startActivity(intent)
            false
        }

        tvFollower.setOnClickListener {
            showPopUpFollows.show()
        }
        tvFollows.setOnClickListener {
            showPopUpFollower.show()
        }

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

        adapter = PostAdapter(requireContext(), allPosts)

        rvPosts.adapter = adapter
        rvPosts.layoutManager = LinearLayoutManager(requireContext())

        queryPosts()
        queryRoutines()
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

    open fun queryPosts() {

        // Specify which class to query
        val query: ParseQuery<Posts> = ParseQuery.getQuery(Posts::class.java)

        // Find all Post objects
        query.include(Posts.KEY_USER)
        query.include(Posts.KEY_USABLE_ROUTINE)
        query.addDescendingOrder("createdAt")
        query.whereEqualTo(Posts.KEY_USER, ParseUser.getCurrentUser())
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

    fun setFollowersInPopup(popupMenu: android.widget.PopupMenu, user: ParseUser) : MutableList<ParseUser>{
        popupMenu.menu.clear()
        val FollowsMap: MutableList<ParseUser> = mutableListOf()
        val query: ParseQuery<Follows> = ParseQuery.getQuery(Follows::class.java)
        query.include(Follows.KEY_FOLLOWING)
        query.include(Follows.KEY_FOLLOWER)
        query.addDescendingOrder("createdAt")
        query.whereEqualTo(Follows.KEY_FOLLOWING, user)

        query.findInBackground { follower, e ->
            if (e != null) {
                e.printStackTrace()
                Log.e(TAG, "Error fetching follows")
            } else {
                if (follower != null) {
                    for(i in 0 until follower.size){
                        popupMenu.menu.add(Menu.NONE, i, i, follower[i].getFollower()?.username)
                        follower[i].getFollowing()?.let { FollowsMap.add(it) }
                    }
                    tvFollows.text = follower.size.toString()
                }
            }
        }
        return FollowsMap
    }

    fun setFollowsInPopup(popupMenu: android.widget.PopupMenu, user: ParseUser) : MutableList<ParseUser>{
        popupMenu.menu.clear()
        val FollowsMap: MutableList<ParseUser> = mutableListOf()
        val query: ParseQuery<Follows> = ParseQuery.getQuery(Follows::class.java)
        query.include(Follows.KEY_FOLLOWING)
        query.include(Follows.KEY_FOLLOWER)
        query.addDescendingOrder("createdAt")
        query.whereEqualTo(Follows.KEY_FOLLOWER, user)

        query.findInBackground { follower, e ->
            if (e != null) {
                e.printStackTrace()
                Log.e(TAG, "Error fetching follows")
            } else {
                if (follower != null) {
                    for(i in 0 until follower.size){
                        popupMenu.menu.add(Menu.NONE, i, i, follower[i].getFollowing()?.username)
                        follower[i].getFollower()?.let { FollowsMap.add(it) }
                    }
                    tvFollower.text = follower.size.toString()
                }
            }
        }
        return FollowsMap
    }
}