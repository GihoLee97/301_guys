package com.guys_from_301.stock_game

import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class Initial_signup_final_Activity : AppCompatActivity()  {
    var mContext : Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        mContext = this
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_signup_final)
        findViewById<ImageButton>(R.id.ib_go_setpw).setOnClickListener {
            onBackPressed()
        }
    }
}