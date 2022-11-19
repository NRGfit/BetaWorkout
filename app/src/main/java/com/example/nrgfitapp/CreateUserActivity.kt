package com.example.nrgfitapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.ParseUser

class CreateUserActivity : AppCompatActivity() {
    val TAG = "createUserActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)


        findViewById<Button>(R.id.bt_createUserButton).setOnClickListener{
            val name = findViewById<EditText>(R.id.tv_name).text.toString()
            val username = findViewById<EditText>(R.id.tv_username).text.toString()
            val password = findViewById<EditText>(R.id.tv_password).text.toString()
            val email = findViewById<EditText>(R.id.tv_email).text.toString()

            signUpUser(name, username, password, email)
        }


    }

    private fun signUpUser(name: String, username: String, password: String, email: String){
        val user = ParseUser()

        user.put("name", name)
        user.username = username
        user.setPassword(password)
        user.email = email

        user.signUpInBackground { e->
            if(e == null){
                Log.i(TAG, "Successfully signed up")
                goToMainActivity()

            }else{
                e.printStackTrace()
                Toast.makeText(this, "Error Signing Up", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this@CreateUserActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}