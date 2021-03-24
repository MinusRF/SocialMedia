package com.example.socialmediaapp_

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmediaapp_.models.Posts
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PostAdapter(options: FirestoreRecyclerOptions<Posts>, val listener:IPostAdapter): FirestoreRecyclerAdapter<Posts, PostAdapter.PostViewHolder>(options) {

    class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val postText:TextView = itemView.findViewById(R.id.postText)
        val userText:TextView = itemView.findViewById(R.id.userText)
        val createdAt:TextView = itemView.findViewById(R.id.createdAt)
        val userImage: ImageView = itemView.findViewById(R.id.userImage)
        val likedBy: TextView = itemView.findViewById(R.id.likedByCount)
        val likedButton: ImageView =itemView.findViewById(R.id.imageView3)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val viewHolder = PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.post_item,parent,false))
        viewHolder.likedButton.setOnClickListener{
            listener.OnClickLikeButton(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }


        return viewHolder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Posts) {
        holder.postText.text = model.text
        holder.userText.text =model.createdBy.displayName
        holder.createdAt.text = Utils.getTimeAgo(model.createdAt)
        holder.likedBy.text = model.likedBy.size.toString()
        Glide.with(holder.userImage.context).load(model.createdBy.imageUrl).circleCrop().into(holder.userImage)

        val currUserId = Firebase.auth.currentUser!!.uid
        val isLiked = model.likedBy.contains(currUserId)
        if(isLiked){
            holder.likedButton.setImageDrawable(ContextCompat.getDrawable(holder.likedButton.context,R.drawable.ic_red_favorite_24))
        }
        else{
            holder.likedButton.setImageDrawable(ContextCompat.getDrawable(holder.likedButton.context,R.drawable.ic_baseline_favorite_border_24))
        }
    }
}

interface IPostAdapter{
    fun OnClickLikeButton(postId:String)
}