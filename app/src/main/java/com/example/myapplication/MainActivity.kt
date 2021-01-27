package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val profile1_btn = findViewById<Button>(R.id.profile1_btn)
        profile1_btn.setOnClickListener{
            val intent = Intent(this,Profile::class.java)
            startActivity(intent)
        }
    }

}