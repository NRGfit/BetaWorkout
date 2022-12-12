package com.example.nrgfitapp

import android.app.Application
import com.example.nrgfitapp.DAOs.*
import com.parse.Parse
import com.parse.ParseObject

class NRGfitParseApp : Application() {
    override fun onCreate() {
        super.onCreate()

        ParseObject.registerSubclass(Posts::class.java)
        ParseObject.registerSubclass(Routine::class.java)
        ParseObject.registerSubclass(RoutineExercise::class.java)
        ParseObject.registerSubclass(Exercise::class.java)
        ParseObject.registerSubclass(UsableRoutines::class.java)
        ParseObject.registerSubclass(Likes::class.java)

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }
}