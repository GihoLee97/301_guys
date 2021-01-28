package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val nickname_btn = findViewById<Button>(R.id.nickname_btn)
        nickname_btn.setOnClickListener {
            val layoutInflater: LayoutInflater = getLayoutInflater()
            val builder = AlertDialog.Builder(this)
            val dialogview = layoutInflater.inflate(R.layout.nickname_dialog,null)
            builder.setView(dialogview).show()
        }

    }
}