package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Initial : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)

        val btn_login = findViewById<Button>(R.id.btn_login)
        btn_login.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java) //Main으로 이동
            startActivity(intent)
        }
    }
}

