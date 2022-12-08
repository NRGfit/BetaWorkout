package com.example.nrgfitapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class ComposeRoutineActivity : AppCompatActivity() {

    lateinit var tvRoutineName: EditText
    lateinit var btnAddExercise: Button
    lateinit var btnAddRoutine: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose_routine)

    }
}