package com.example.nrgfitapp.DAOs

import android.content.Context
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nrgfitapp.R
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class RoutineCreateExerciseAdapter(val context: Context, val exercises: MutableList<String>) : RecyclerView.Adapter<RoutineCreateExerciseAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_created_exercises, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val routineExercise = exercises.get(position)
        holder.bind(routineExercise)
//        holder.btDelete.setOnClickListener {
//            exercises.removeAt(position)
//        }
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvExerciseName: TextView
        val ivExercise: ImageView
        val btDelete: Button

        init{
            tvExerciseName = itemView.findViewById(R.id.exerciseName)
            ivExercise = itemView.findViewById(R.id.ivExercise)
            btDelete = itemView.findViewById<Button>(R.id.btDelete)
        }

        var TAG = "test2"
        fun bind(routineExercise: String){
            val exerciseID: String = routineExercise

            val exerciseInfo : JSONObject
            if (exerciseID != null) {
                exerciseInfo = getExercise(exerciseID, itemView)!!
                tvExerciseName.text = exerciseInfo.getString("name")
                Glide.with(itemView.context).asGif().load(exerciseInfo.getString("gifUrl")).into(ivExercise)
            }
//            tvExerciseSets.text = routineExercise.getSets().toString()
//            tvExerciseReps.text = routineExercise.getReps().toString()
//            tvExerciseWeights.text = routineExercise.getWeights()
//            tvExerciseNotes.text = routineExercise.getNotes()

        }

        fun getExercise(dbid: String, view: View): JSONObject? {
            val policy = StrictMode.ThreadPolicy.Builder()
                .permitAll().build()
            StrictMode.setThreadPolicy(policy)

            var res: JSONObject? = null
            try {
                val client = OkHttpClient()

                val request = Request.Builder()
                    .url("https://exercisedb.p.rapidapi.com/exercises/exercise/$dbid")
                    .get()
                    .addHeader("X-RapidAPI-Key", view.context.resources.getString(R.string.X_RapidAPI_Key))
                    .addHeader("X-RapidAPI-Host", view.context.resources.getString(R.string.X_RapidAPI_Host))
                    .build()

                val response = client.newCall(request).execute()

                val code = response.code;
                if (code == 200) {
                    val body = response.body;
                    res = JSONObject(body?.string());
                    body?.close();
                }
            }catch (th: Throwable) {
                Log.i(TAG, th.localizedMessage)
            }

            if (res != null) {
                return res
            }else{
                return null
            }
        }
    }
}