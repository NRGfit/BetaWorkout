package com.example.nrgfitapp.DAOs

import android.content.Context
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nrgfitapp.R
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class RoutineExerciseAdapter(val context: Context, val exercises: List<RoutineExercise>) : RecyclerView.Adapter<RoutineExerciseAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_exercises, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val routineExercise = exercises.get(position)
        holder.bind(routineExercise)
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvExerciseName: TextView
        val ivExercise: ImageView
        val tvExerciseSets: TextView
        val tvExerciseReps: TextView
        val tvExerciseWeights: TextView
        val tvExerciseNotes: TextView

        init{
            tvExerciseName = itemView.findViewById(R.id.exerciseName)
            ivExercise = itemView.findViewById(R.id.ivExercise)
            tvExerciseSets = itemView.findViewById(R.id.exerciseSets)
            tvExerciseReps = itemView.findViewById(R.id.exerciseReps)
            tvExerciseWeights = itemView.findViewById(R.id.exerciseWeights)
            tvExerciseNotes = itemView.findViewById(R.id.exerciseNotes)
        }
        var TAG = "test2"
        fun bind(routineExercise: RoutineExercise){
//            val exercise: Exercise = routineExercise.getExercise() as Exercise
//            val exerciseID: String? = exercise.getExerciseDBID()
//            if (exerciseID != null) {
//                Log.i(TAG, exerciseID)
//            }
            //tvExerciseName = itemView.findViewById(R.id.exerciseName)
            //ivExercise = itemView.findViewById(R.id.ivExercise)
            tvExerciseSets.text = routineExercise.getSets().toString()
            tvExerciseReps.text = routineExercise.getReps().toString()
            tvExerciseWeights.text = routineExercise.getWeights()
            tvExerciseNotes.text = routineExercise.getNotes()

        }

        fun getExercise(dbid: String){
            val policy = StrictMode.ThreadPolicy.Builder()
                .permitAll().build()
            StrictMode.setThreadPolicy(policy)

            var res: String? = null
            try {
                val client = OkHttpClient()

                val request = Request.Builder()
                    .url("https://exercisedb.p.rapidapi.com/exercises/exercise/$dbid")
                    .get()
                    .addHeader("X-RapidAPI-Key", R.string.X_RapidAPI_Key.toString())
                    .addHeader("X-RapidAPI-Host", R.string.X_RapidAPI_Host.toString())
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
}