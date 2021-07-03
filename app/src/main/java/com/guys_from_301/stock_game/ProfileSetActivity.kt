package com.guys_from_301.stock_game

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout



class ProfileSetActivity : AppCompatActivity() {
    private lateinit var cl_change_pw: ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_set)

        cl_change_pw = findViewById(R.id.cl_change_pw)

        cl_change_pw.setOnClickListener{
            val dlg = Dialog_nick(this@ProfileSetActivity, false)
            dlg.start()
        }

    }

}