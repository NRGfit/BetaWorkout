package com.example.nrgfitapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.*
import com.example.nrgfitapp.fragments.HomeFragment


class MainActivity : AppCompatActivity() {


    val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager: FragmentManager = supportFragmentManager
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener {
                item ->

            var fragmentToShow: Fragment? =null
            when(item.itemId){
                R.id.action_forum -> {
                    fragmentToShow = HomeFragment()
                    Toast.makeText(this, "Forum", Toast.LENGTH_SHORT).show()
                }
//                R.id.action_compose -> {
//                    fragmentToShow = ComposeFragment()
//                    //Toast.makeText(this, "Compose", Toast.LENGTH_SHORT).show()
//                }
//                R.id.action_profile -> {
//                    fragmentToShow = ProfileFragment()
//                    //Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
//                }
            }

            if(fragmentToShow != null) {
                fragmentManager.beginTransaction().replace(R.id.frameContainer, fragmentToShow).commit()
            }

            true
        }
        bottomNavigation.selectedItemId = R.id.action_forum
    }
}