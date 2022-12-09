package com.example.nrgfitapp.DAOs

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nrgfitapp.R

class PostAdapter(val context: Context, val posts: List<Posts>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts.get(position)
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
        fun bind(post: Posts){
            tvUsername.text = post.getUser()?.username
            tvDescription.text = post.getDescription()
            itemCreatedAt.text = post.getFormattedTimestamp(post.createdAt)

            Glide.with(itemView.context).load(post.getUser()?.getParseFile("pfp")?.url).into(ivProfilePic)
        }
    }
}