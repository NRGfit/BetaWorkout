package com.example.nrgfitapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nrgfitapp.ComposeActivity
import com.example.nrgfitapp.ComposeRoutineActivity
import com.example.nrgfitapp.DAOs.Routine
import com.example.nrgfitapp.DAOs.RoutineAdapter
import com.example.nrgfitapp.DAOs.UsableRoutines
import com.example.nrgfitapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.parse.ParseQuery
import com.parse.ParseUser


class RoutineFragment : Fragment() {
    lateinit var rvRoutines: RecyclerView
    lateinit var adapter: RoutineAdapter
    lateinit var swipeContainer: SwipeRefreshLayout
    lateinit var btFab: FloatingActionButton
    var allRoutines: MutableList<UsableRoutines> = mutableListOf()

    val TAG = "WorkoutCreateFragment"
    var REQUEST_CODE = 10;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_routine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvRoutines = view.findViewById(R.id.routineRecyclerView)
        swipeContainer = view.findViewById(R.id.swipeContainer)

        swipeContainer.setOnRefreshListener {
            queryRoutines()
        }

        swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        );

        btFab = view.findViewById(R.id.createRoutine)

        btFab.setOnClickListener {
            val intent = Intent(this.context, ComposeRoutineActivity::class.java)
            Log.i(TAG, "clicked")
            startActivityForResult(intent, REQUEST_CODE)
        }

        adapter = RoutineAdapter(requireContext(), allRoutines)
        rvRoutines.adapter = adapter
        rvRoutines.layoutManager = LinearLayoutManager(requireContext())

        queryRoutines()
    }

    open fun queryRoutines() {
        // Specify which class to query
        val query: ParseQuery<UsableRoutines> = ParseQuery.getQuery(UsableRoutines::class.java)

        // Find all Routine objects
        query.include(UsableRoutines.KEY_ROUTINE)
        query.addDescendingOrder("createdAt")
        query.whereEqualTo(UsableRoutines.KEY_USER, ParseUser.getCurrentUser())
        query.findInBackground { usableRoutines, e ->
            if (e != null) {
                Log.e(TAG, "ERROR")
            } else {
                if (usableRoutines != null) {
                    allRoutines.clear()
                    allRoutines.addAll(usableRoutines)
                    adapter.notifyDataSetChanged()
                    swipeContainer.isRefreshing = false
                }
            }
        }
    }
}