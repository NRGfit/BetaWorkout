package com.example.nrgfitapp.DAOs

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nrgfitapp.R

class RoutineAdapter(val context: Context, val routines: List<Routine>) : RecyclerView.Adapter<RoutineAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_routine, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val routine = routines.get(position)
        holder.bind(routine)
    }

    override fun getItemCount(): Int {
        return routines.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //needs to be edited to match item_routine.xml
        val tvUsername: TextView
        val ivProfilePic: ImageView
        val tvDescription: TextView
        val itemCreatedAt: TextView

        init{
            tvUsername = itemView.findViewById(R.id.tvUsername)
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic)
            tvDescription = itemView.findViewById(R.id.description)
            itemCreatedAt = itemView.findViewById(R.id.tvDate)
        }
        var TAG = "test2"
        fun bind(routine: Routine){
            tvUsername.text = routine.getUser()?.username
            tvDescription.text = routine.getDescription()
            itemCreatedAt.text = routine.getFormattedTimestamp(routine.createdAt)
        }
    }
}
