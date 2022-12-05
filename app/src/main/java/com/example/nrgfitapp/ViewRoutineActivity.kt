package com.example.nrgfitapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nrgfitapp.DAOs.Posts
import com.example.nrgfitapp.DAOs.Routine
import com.example.nrgfitapp.DAOs.RoutineAdapter
import com.example.nrgfitapp.DAOs.RoutineExercise
import com.parse.ParseQuery
import com.parse.ParseUser
import okhttp3.OkHttpClient
import okhttp3.Request

class ViewRoutineActivity : AppCompatActivity() {
    val TAG = "ViewRoutineActivity"

    lateinit var rvRoutineExercises: RecyclerView
    lateinit var adapter: RoutineExerciseAdapter
    lateinit var swipeContainer: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_routine)
    }

    fun queryRoutineExercises(){
        // Specify which class to query
        val query: ParseQuery<RoutineExercise> = ParseQuery.getQuery(RoutineExercise::class.java)

        // Find all Routine objects
        query.include(Posts.KEY_USER)
        query.addDescendingOrder("createdAt")
        query.whereEqualTo(RoutineExercise.KEY_ROUTINE, ParseUser.getCurrentUser())
        query.findInBackground { routine, e ->
            if (e != null) {
                Log.e(TAG, "ERROR")
            } else {
                if (routine != null) {
                    allRoutineExercises.clear()
                    allRoutineExercises.addAll(routine)
                    adapter.notifyDataSetChanged()
                    swipeContainer.isRefreshing = false
                }
            }
        }
    }

    fun getExercises(){
        val policy = StrictMode.ThreadPolicy.Builder()
            .permitAll().build()
        StrictMode.setThreadPolicy(policy)

        var res: String? = null
        try {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("https://exercisedb.p.rapidapi.com/exercises/exercise/0001")
                .get()
                .addHeader("X-RapidAPI-Key", getString(R.string.X_RapidAPI_Key))
                .addHeader("X-RapidAPI-Host", getString(R.string.X_RapidAPI_Host))
                .build()

            val response = client.newCall(request).execute()

            val code = response.code;
            if (code == 200) {
                val body = response.body;
                res = body?.string();
                body?.close();
            }
        }catch (th: Throwable) {
            Log.i(TAG, th.localizedMessage)
        }

        if (res != null) {
            Log.i(TAG, res)
        }
    }
}