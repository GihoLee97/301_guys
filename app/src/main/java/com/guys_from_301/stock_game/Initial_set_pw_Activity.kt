package com.guys_from_301.stock_game

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Initial_set_pw_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_set_pw)
        findViewById<ImageButton>(R.id.ib_go_setid).setOnClickListener{
            onBackPressed()
        }
        findViewById<Button>(R.id.btn_go_on).setOnClickListener{
            Toast.makeText(this,"성공", Toast.LENGTH_LONG).show()
        }
    }
    fun pwcheck(){

    }
}