package com.example.socialmediaapp_

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmediaapp_.models.Posts
import com.example.socialmediaapp_.usersDao.PostDao
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),IPostAdapter {

    lateinit var adapter: PostAdapter
    lateinit var postDao: PostDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        floatingActionButton.setOnClickListener{
            startActivity(Intent(this,CreatePosts::class.java))
        }

        setUpRecyclerView()
    }

    override fun onStart(){
        super.onStart()
        adapter.startListening()
    }
    override fun onStop(){
        super.onStop()
        adapter.stopListening()
    }
    private fun setUpRecyclerView() {
        postDao = PostDao()
        val postCollection =postDao.postCollection
        val query = postCollection.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerOptions = FirestoreRecyclerOptions.Builder<Posts>().setQuery(query,Posts::class.java).build()

        adapter = PostAdapter(recyclerOptions,this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter =adapter

    }

    override fun OnClickLikeButton(postId: String) {
        postDao.updateLikes(postId)
    }


}