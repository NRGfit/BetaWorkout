package com.example.nrgfitapp.DAOs

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.nrgfitapp.R
import com.example.nrgfitapp.ViewRoutineActivity
import com.parse.ParseQuery
import com.parse.ParseUser


class RoutineAdapter(val context: Context, val routines: List<UsableRoutines>) : RecyclerView.Adapter<RoutineAdapter.ViewHolder>() {
    val TAG = "Routine"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_routine, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val routine = routines.get(position)
        holder.bind(routine)
        holder.clRoutine.setOnClickListener{
            val intent = Intent(context, ViewRoutineActivity::class.java)
            intent.putExtra("routineID", routine.getRoutine()?.objectId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return routines.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRoutineName: TextView
        val tvDescription: TextView
        val tvCreator: TextView
        val clRoutine: ConstraintLayout

        init{
            tvDescription = itemView.findViewById(R.id.tv_Description)
            tvRoutineName = itemView.findViewById(R.id.tv_RoutineName)
            tvCreator = itemView.findViewById(R.id.tv_Creator)
            clRoutine = itemView.findViewById(R.id.cl_Routine)
        }
        var TAG = "test2"
        fun bind(usableRoutine: UsableRoutines){

            val query: ParseQuery<UsableRoutines> = ParseQuery.getQuery(UsableRoutines::class.java)

            query.include(UsableRoutines.KEY_OWNER)
            query.include(UsableRoutines.KEY_ROUTINE)
            query.whereEqualTo("objectId", usableRoutine.objectId)
            query.findInBackground { usableRoutines, e ->
                if (e != null) {
                    Log.e(TAG, "ERROR")
                } else {
                    if (usableRoutines != null) {
                        for(usableRoutine in usableRoutines){
                            val routine : Routine = usableRoutine.getRoutine() as Routine
                            tvRoutineName.text = routine.getRoutineName()
                            tvDescription.text = routine.getDescription()
                            if(usableRoutine.getOwner()?.username == ParseUser.getCurrentUser().username)
                                tvCreator.text = "you"
                            else
                                tvCreator.text = usableRoutine.getOwner()?.username
                        }
                    }
                }
            }
        }
    }
}
