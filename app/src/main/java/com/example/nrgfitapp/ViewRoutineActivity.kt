package com.example.nrgfitapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nrgfitapp.DAOs.*
import com.parse.ParseQuery
import com.parse.ParseUser
import okhttp3.OkHttpClient
import okhttp3.Request

class ViewRoutineActivity : AppCompatActivity() {
    val TAG = "ViewRoutineActivity"

    lateinit var rvRoutineExercises: RecyclerView
    lateinit var adapter: RoutineExerciseAdapter
    lateinit var swipeContainer: SwipeRefreshLayout
    var allRoutineExercises: MutableList<RoutineExercise> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_routine)

        rvRoutineExercises = findViewById(R.id.rv_RoutineExercises)
        swipeContainer = findViewById(R.id.swipeContainer)

        swipeContainer.setOnRefreshListener {
            queryRoutineExercises()
        }

        swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        );

        adapter = RoutineExerciseAdapter(this, allRoutineExercises)
        rvRoutineExercises.adapter = adapter
        rvRoutineExercises.layoutManager = LinearLayoutManager(this)

        queryRoutineExercises()
    }

    fun queryRoutineExercises(){
        // Specify which class to query
        val query: ParseQuery<RoutineExercise> = ParseQuery.getQuery(RoutineExercise::class.java)

        // Find all Routine objects
        query.include(RoutineExercise.KEY_ROUTINE)
        //query.whereEqualTo(RoutineExercise.KEY_ROUTINE, intent.getStringExtra("routineID"))
        query.findInBackground { routineExercises, e ->
            if (e != null) {
                Log.e(TAG, "ERROR")
            } else {
                if (routineExercises != null) {
                    allRoutineExercises.clear()
                    allRoutineExercises.addAll(routineExercises)
                    adapter.notifyDataSetChanged()
                    swipeContainer.isRefreshing = false
                }
            }
        }
    }
}