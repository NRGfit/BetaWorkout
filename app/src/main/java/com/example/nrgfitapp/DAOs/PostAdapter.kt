package com.example.nrgfitapp.DAOs

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nrgfitapp.R
import com.example.nrgfitapp.ViewOtherProfileActivity
import com.example.nrgfitapp.ViewRoutineActivity
import com.parse.ParseQuery
import com.parse.ParseUser

class PostAdapter(val context: Context, private val posts: List<Posts>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    val TAG = "PostAdapter"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
        holder.ivProfilePic.setOnClickListener{
            val intent = Intent(context, ViewOtherProfileActivity::class.java)
            intent.putExtra("User", post.getUser()?.objectId)
            context.startActivity(intent)
        }
        if(posts[position].getUsableRoutine() != null) {
            holder.button_share.setOnClickListener {
                val usableRoutine: UsableRoutines =
                    posts[position].getUsableRoutine() as UsableRoutines
                usableRoutine.getRoutine()?.let { it1 -> Log.i(TAG, it1.objectId) }
                val query: ParseQuery<UsableRoutines> =
                    ParseQuery.getQuery(UsableRoutines::class.java)

                query.include(UsableRoutines.KEY_ROUTINE)
                query.include(UsableRoutines.KEY_USER)
                query.addDescendingOrder("createdAt")
                query.whereEqualTo(UsableRoutines.KEY_ROUTINE, usableRoutine.getRoutine())
                query.whereEqualTo(UsableRoutines.KEY_USER, ParseUser.getCurrentUser())
                query.findInBackground { usableRoutines, e ->
                    if (e != null) {
                        Log.e(TAG, "ERROR")
                    } else {
                        if (usableRoutines.size > 0) {
                            Toast.makeText(
                                context,
                                "You already have this Workout",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val usableRoutineNew = UsableRoutines()
                            usableRoutine.getRoutine()
                                ?.let { it1 -> usableRoutineNew.setRoutine(it1) }
                            usableRoutineNew.setUser(ParseUser.getCurrentUser())
                            usableRoutine.getOwner()?.let { it1 -> usableRoutineNew.setOwner(it1) }
                            usableRoutineNew.save()
                            Log.i(TAG, "Saved UsableRoutine")
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    fun getVLocationFromPost(post: Posts): Int {
        for(i in 0..posts.size){
            if(post == posts[i]){
                return i
            }
        }
        return -1
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUsername: TextView
        val ivProfilePic: ImageView
        val tvDescription: TextView
        val itemCreatedAt: TextView
        val rvRoutinePost: RecyclerView
        val routineAdapter: RoutineAdapter
        val button_share: Button
        var routines: MutableList<UsableRoutines> = mutableListOf()

        init{
            tvUsername = itemView.findViewById(R.id.tvUsername)
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic)
            tvDescription = itemView.findViewById(R.id.description)
            itemCreatedAt = itemView.findViewById(R.id.tvDate)
            rvRoutinePost = itemView.findViewById(R.id.rv_RoutinePost)
            routineAdapter = RoutineAdapter(itemView.context, routines)
            button_share = itemView.findViewById<Button>(R.id.button_share)
        }
        var TAG = "test2"
        fun bind(post: Posts){
            tvUsername.text = post.getUser()?.username
            tvDescription.text = post.getDescription()
            itemCreatedAt.text = post.getFormattedTimestamp(post.createdAt)
            rvRoutinePost.adapter = routineAdapter
            rvRoutinePost.layoutManager = LinearLayoutManager(itemView.context)
            if(post.getUsableRoutine() != null) {
                routines.clear()
                routines.add(post.getUsableRoutine() as UsableRoutines)
                routineAdapter.notifyDataSetChanged()
            }

            Glide.with(itemView.context).load(post.getUser()?.getParseFile("pfp")?.url).into(ivProfilePic)
        }
    }
}