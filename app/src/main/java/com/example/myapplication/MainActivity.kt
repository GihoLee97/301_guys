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
            val intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
        }

        val setting_btn = findViewById<Button>(R.id.setting_btn)
        setting_btn.setOnClickListener {
            val intent = Intent(this,SettingActivity::class.java)
            startActivity(intent)
        }

        val game_btn = findViewById<Button>(R.id.game_btn)
        game_btn.setOnClickListener{
            val intent = Intent(this,GameNormalActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onBackPressed() {
        startActivity(Intent(this, InitialActivity::class.java))
        finish()
    }

}
