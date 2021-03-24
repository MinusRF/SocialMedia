package com.example.socialmediaapp_

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.socialmediaapp_.usersDao.PostDao
import kotlinx.android.synthetic.main.activity_create.*

class CreatePosts : AppCompatActivity() {
    private lateinit var postDao: PostDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        postDao =PostDao()
        post.setOnClickListener {
            val text = editTextTextPersonName.text.toString().trim()
            if(text.isNotEmpty()){

                postDao.addPosts(text)
                finish()
            }
        }
    }
}