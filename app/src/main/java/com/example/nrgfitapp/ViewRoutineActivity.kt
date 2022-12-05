package com.example.nrgfitapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request

class ViewRoutineActivity : AppCompatActivity() {
    val TAG = "ViewRoutineActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_routine)
    }

    fun getExercises(){
        val policy = StrictMode.ThreadPolicy.Builder()
            .permitAll().build()
        StrictMode.setThreadPolicy(policy)

        var res: String? = null
        try {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("https://exercisedb.p.rapidapi.com/exercises/exercise/0001")
                .get()
                .addHeader("X-RapidAPI-Key", getString(R.string.X_RapidAPI_Key))
                .addHeader("X-RapidAPI-Host", getString(R.string.X_RapidAPI_Host))
                .build()

            val response = client.newCall(request).execute()

            val code = response.code;
            if (code == 200) {
                val body = response.body;
                res = body?.string();
                body?.close();
            }
        }catch (th: Throwable) {
            Log.i(TAG, th.localizedMessage)
        }

        if (res != null) {
            Log.i(TAG, res)
        }
    }
}