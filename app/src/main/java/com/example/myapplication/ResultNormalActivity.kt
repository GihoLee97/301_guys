package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ResultNormalActivity : AppCompatActivity() {
    private lateinit var btnok : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_normal)
        btnok = findViewById(R.id.btn_resultOk)
        btnok.setOnClickListener{
            GotoMainactivity()
        }
    }

    fun GotoMainactivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        GotoMainactivity()
    }


}