package com.guys_from_301.stock_game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class NewInitialActivity : AppCompatActivity() {
    private lateinit var tv_goToLoginActivity : TextView
    private lateinit var tv_goToSignUpActivity : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_initial)

        tv_goToLoginActivity = findViewById(R.id.tv_goToLoginActivity)
        tv_goToSignUpActivity = findViewById(R.id.tv_goToSignUpActivity)

        tv_goToLoginActivity.setOnClickListener {
            val intent = Intent(this,InitialLoginActivity::class.java)
            startActivity(intent)
        }

        tv_goToSignUpActivity.setOnClickListener {
            val intent = Intent(this,InitialSignUpEntranceActivity::class.java)
            startActivity(intent)
        }
    }
}