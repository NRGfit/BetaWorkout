package com.example.nrgfitapp

import android.content.Intent
import android.graphics.Color.argb
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import okhttp3.Headers
import org.json.JSONObject

class ComposeActivity : AppCompatActivity() {

    lateinit var tvTweetCompose: EditText
    lateinit var btnTweet: Button
    lateinit var tvCharCount: TextView


    val TAG = "debug"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        tvTweetCompose = findViewById(R.id.tvTweetCompose)
        btnTweet = findViewById(R.id.btnTweet)
        tvCharCount = findViewById(R.id.tvCharCount)

        tvTweetCompose.doAfterTextChanged {
            val length = tvTweetCompose.text.toString().length
            if(length > 280) {
                tvCharCount.text = length.toString().plus("/280")
                tvCharCount.setTextColor(argb(255,255,0,0))
            }else{
                tvCharCount.text = length.toString().plus("/280")
                tvCharCount.setTextColor(argb(255,29,161,242))
            }
        }

        btnTweet.setOnClickListener {

        }
    }
}
