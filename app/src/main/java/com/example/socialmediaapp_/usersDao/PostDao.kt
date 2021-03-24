package com.example.socialmediaapp_.usersDao

import android.util.Log
import com.example.socialmediaapp_.models.Posts
import com.example.socialmediaapp_.models.Users
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostDao {
    private val db = FirebaseFirestore.getInstance()
    val postCollection =db.collection("Posts")

    val auth= FirebaseAuth.getInstance()
    fun addPosts(text:String){
        val userId=auth.currentUser!!.uid
        GlobalScope.launch(Dispatchers.IO) {
            val userDao=UserDao()
            val users: Users = userDao.getUserById(userId).await().toObject(Users::class.java)!!
            val currentTime =System.currentTimeMillis()

            val posts: Posts =Posts(text,users,currentTime)
            postCollection.document().set(posts)
                .addOnSuccessListener { Log.d("Post", "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w("Post", "Error writing document", e) }
        }

    }
    fun getPostById(postId: String): Task<DocumentSnapshot> {
        return postCollection.document(postId).get()
    }
    fun updateLikes(postId: String) {
        GlobalScope.launch {
            val currentUserId = auth.currentUser!!.uid
            val post = getPostById(postId).await().toObject(Posts::class.java)!!
            val isLiked = post.likedBy.contains(currentUserId)
            if(isLiked ){
                post.likedBy.remove(currentUserId)
            }
            else{
                post.likedBy.add(currentUserId)
            }
            postCollection.document(postId).set(post)
        }
    }
}