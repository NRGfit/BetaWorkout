package com.example.nrgfitapp

import android.content.Intent
import android.graphics.Color.argb
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import com.example.nrgfitapp.DAOs.Posts
import com.parse.ParseFile
import com.parse.ParseUser
import okhttp3.Headers
import org.json.JSONObject
import java.io.File

class ComposeActivity : AppCompatActivity() {

    lateinit var tvPostCompose: EditText
    lateinit var btnPost: Button
    lateinit var tvCharCount: TextView


    val TAG = "Compose Activity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        tvPostCompose = findViewById(R.id.tvPostCompose)
        btnPost = findViewById(R.id.btnPost)
        tvCharCount = findViewById(R.id.tvCharCount)

        tvPostCompose.doAfterTextChanged {
            val length = tvPostCompose.text.toString().length
            if(length > 280) {
                tvCharCount.text = length.toString().plus("/280")
                tvCharCount.setTextColor(argb(255,255,0,0))
            }else{
                tvCharCount.text = length.toString().plus("/280")
                tvCharCount.setTextColor(argb(255,0,0,255))
            }
        }

        btnPost.setOnClickListener {
            val postContent = tvPostCompose.text.toString()

            if(postContent.isEmpty())
                Toast.makeText(this, "Empty posts not allowed!", Toast.LENGTH_SHORT).show()
            else if(postContent.length > 280)
                Toast.makeText(this, "Post is too long! Limit is 280 characters", Toast.LENGTH_SHORT).show()
            else {
                submitPost(postContent, ParseUser.getCurrentUser())
                setResult(RESULT_OK, intent)
                finish()
            }

        }
    }

    fun submitPost(description: String, user: ParseUser) {
        val post = Posts()
        post.setDescription(description)
        post.setUser(user)
        post.saveInBackground { exception ->
            if (exception != null) {
                Log.e(TAG, "ERROW HILE SAVING")
                exception.printStackTrace()
            } else {
                Log.i(TAG, "Succes saving post")
            }
        }
    }
}
