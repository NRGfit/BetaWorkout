package com.example.nrgfitapp.DAOs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nrgfitapp.R

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
            //tvExerciseName = itemView.findViewById(R.id.exerciseName)
            //ivExercise = itemView.findViewById(R.id.ivExercise)
            tvExerciseSets.text = routineExercise.getSets().toString()
            tvExerciseReps.text = routineExercise.getReps().toString()
            //tvExerciseWeights = itemView.findViewById(R.id.exerciseWeights)
            tvExerciseNotes.text = routineExercise.getNotes()

        }
    }
}