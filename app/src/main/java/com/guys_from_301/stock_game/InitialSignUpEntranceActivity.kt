package com.guys_from_301.stock_game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.google.android.gms.common.SignInButton

class InitialSignUpEntranceActivity : AppCompatActivity() {

    private lateinit var btn_googleSignIn : SignInButton
    private lateinit var btn_generalLogin : Button
    private lateinit var btn_kakaoLogin : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_sign_up_entrance)

        btn_googleSignIn = findViewById(R.id.btn_googleSignIn)
        btn_generalLogin = findViewById(R.id.btn_generalLogin)
        btn_kakaoLogin = findViewById(R.id.btn_kakaoLogin)

        btn_generalLogin.setOnClickListener {
            val intent = Intent(this,InitialSetIdActivity::class.java)
            startActivity(intent)
        }

        btn_googleSignIn.setOnClickListener {

        }

        btn_kakaoLogin.setOnClickListener {

        }

    }
}