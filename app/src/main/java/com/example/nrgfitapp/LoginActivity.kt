package com.example.nrgfitapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.ParseUser

class LoginActivity : AppCompatActivity() {
    val TAG = "LoginActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if(ParseUser.getCurrentUser() != null){
            goToMainActivity()
        }

        findViewById<Button>(R.id.signInButton).setOnClickListener{
            val username = findViewById<EditText>(R.id.login_username).text.toString()
            val password = findViewById<EditText>(R.id.login_password).text.toString()

            loginUser(username, password)
        }

        findViewById<Button>(R.id.signUpButton).setOnClickListener{
            goToCreateUserActivity()
        }
    }



    private fun loginUser(username: String, password: String){
        ParseUser.logInInBackground(username, password, ({user, e->
            if(user != null){
                Log.i(TAG, "Successfully logged in")
                goToMainActivity()

            }else{
                e.printStackTrace()
                Toast.makeText(this, "Error Logging in", Toast.LENGTH_SHORT).show()
            }
        }))
    }

    private fun goToMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToCreateUserActivity() {
        val intent = Intent(this@LoginActivity, CreateUserActivity::class.java)
        startActivity(intent)
        finish()
    }

}