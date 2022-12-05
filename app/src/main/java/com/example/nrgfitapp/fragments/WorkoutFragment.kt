package com.example.nrgfitapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nrgfitapp.DAOs.PostAdapter
import com.example.nrgfitapp.DAOs.Posts
import com.example.nrgfitapp.DAOs.Routine
import com.example.nrgfitapp.DAOs.RoutineAdapter
import com.example.nrgfitapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser


class WorkoutFragment : Fragment() {
    lateinit var rvPosts: RecyclerView
    lateinit var adapter: RoutineAdapter
    lateinit var swipeContainer: SwipeRefreshLayout
    lateinit var btFab: FloatingActionButton
    var allPosts: MutableList<Routine> = mutableListOf()

    val TAG = "WorkoutFragment"
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
    }

    open fun queryRoutines() {
        // Specify which class to query
        val query: ParseQuery<Routine> = ParseQuery.getQuery(Routine::class.java)

        // Find all Routine objects
        query.include(Posts.KEY_USER)
        query.addDescendingOrder("createdAt")
        query.whereEqualTo(Routine.KEY_USER, ParseUser.getCurrentUser())
        query.findInBackground { routine, e ->
            if (e != null) {
                Log.e(TAG, "ERROR")
            } else {
                if (routine != null) {
                    allPosts.clear()
                    allPosts.addAll(routine)
                    adapter.notifyDataSetChanged()
                    swipeContainer.isRefreshing = false
                }
            }
        }
    }
}