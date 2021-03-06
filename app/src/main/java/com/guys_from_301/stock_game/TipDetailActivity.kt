package com.guys_from_301.stock_game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class TipDetailActivity : AppCompatActivity() {
    private lateinit var tv_tip_detail_title: TextView
    private lateinit var tv_tip_detail_description: TextView
    private lateinit var btn_back: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tip_detail)
        tv_tip_detail_title = findViewById(R.id.tv_tip_detail_title)
        tv_tip_detail_description = findViewById(R.id.tv_tip_detail_description)
        btn_back = findViewById(R.id.btn_back)

        tv_tip_detail_title.text = tipTitle
        tv_tip_detail_description.text = tipDescription

        btn_back.setOnClickListener {
            onBackPressed()
            tipTitle = ""
            tipDescription = ""
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}