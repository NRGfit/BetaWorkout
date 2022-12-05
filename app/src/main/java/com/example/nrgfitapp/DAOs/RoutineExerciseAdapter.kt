package com.example.nrgfitapp.DAOs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val tvRoutineName: TextView
        val tvDescription: TextView
        val tvCreator: TextView

        init{
            tvDescription = itemView.findViewById(R.id.tv_Description)
            tvRoutineName = itemView.findViewById(R.id.tv_RoutineName)
            tvCreator = itemView.findViewById(R.id.tv_Creator)
        }
        var TAG = "test2"
        fun bind(routineExercise: RoutineExercise){
            tvRoutineName.text = routineExercise.getRoutineName()
            tvDescription.text = routineExercise.getDescription()
            tvCreator.text = "you"

        }
    }
}