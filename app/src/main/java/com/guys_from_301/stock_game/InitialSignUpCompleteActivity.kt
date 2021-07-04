package com.guys_from_301.stock_game

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class InitialSignUpCompleteActivity : AppCompatActivity() {
    var mContext : Context? = null
    private lateinit var btn_login: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        mContext = this
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_sign_up_complete)
        window?.statusBarColor = resources.getColor(R.color.themeFragment)
        btn_login = findViewById(R.id.btn_login)

        btn_login.setOnClickListener {
            val intent = Intent(mContext, InitialLoginActivity::class.java)
            startActivity(intent)
        }

    }
}