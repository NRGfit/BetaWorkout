package com.example.nrgfitapp.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nrgfitapp.ComposeActivity
import com.example.nrgfitapp.ComposeRoutineActivity
import com.example.nrgfitapp.DAOs.Likes
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
    lateinit var btFabDel: FloatingActionButton
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
        btFabDel = view.findViewById(R.id.deleteRoutine)

        val showPopUp = PopupMenu(
            requireContext(),
            btFabDel
        )

        showPopUp.inflate(R.menu.popup_exercises)

        var idMap = setRoutinesInPopup(showPopUp)

        showPopUp.setOnMenuItemClickListener { menuItem ->
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Are you sure you want to Delete?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    Log.i(TAG, "Yes works")
                    idMap[menuItem.itemId].delete()
                    idMap.clear()
                    idMap = setRoutinesInPopup(showPopUp)

                }
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    Log.i(TAG, "No works")
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
            false
        }





        btFab.setOnClickListener {
            val intent = Intent(this.context, ComposeRoutineActivity::class.java)
            Log.i(TAG, "clicked")
            startActivityForResult(intent, REQUEST_CODE)
        }
        btFabDel.setOnClickListener {
            showPopUp.show()
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
    fun setRoutinesInPopup(popupMenu: PopupMenu) : MutableList<UsableRoutines>{
        popupMenu.menu.clear()
        val routineMap: MutableList<UsableRoutines> = mutableListOf()
        val query: ParseQuery<UsableRoutines> = ParseQuery.getQuery(UsableRoutines::class.java)
        query.include(UsableRoutines.KEY_ROUTINE)
        query.whereEqualTo(UsableRoutines.KEY_USER, ParseUser.getCurrentUser())
        query.findInBackground { routines, e ->
            if (e != null) {
                e.printStackTrace()
                Log.e(TAG, "Error fetching posts")
            } else {
                if (routines != null) {
                    for(i in 0 until routines.size){
                        val routine :Routine = (routines[i].getRoutine() as Routine)
                        popupMenu.menu.add(Menu.NONE, i, i, routine.getRoutineName())
                        routineMap.add(routines[i])
                    }
                }
            }
        }
        return routineMap
    }

    open fun queryLikes() {
        // Specify which class to query
        val query: ParseQuery<Likes> = ParseQuery.getQuery(Likes::class.java)

        // Find all Routine objects
        query.include(Likes.KEY_POST)
        query.addDescendingOrder("createdAt")
        query.whereEqualTo(UsableRoutines.KEY_USER, ParseUser.getCurrentUser())
        query.findInBackground { likes, e ->
            if (e != null) {
                Log.e(TAG, "ERROR")
            } else {
                if (likes != null) {
                    allRoutines.clear()
                    allRoutines.addAll(likes)
                    adapter.notifyDataSetChanged()
                    swipeContainer.isRefreshing = false
                }
            }
        }
    }
}