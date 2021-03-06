package com.guys_from_301.stock_game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class TipDetailActivity : AppCompatActivity() {
    private lateinit var tv_tip_detail_title: TextView
    private lateinit var tv_tip_detail_description: TextView
    private lateinit var ib_back: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tip_detail)
        tv_tip_detail_title = findViewById(R.id.tv_tip_detail_title)
        tv_tip_detail_description = findViewById(R.id.tv_tip_detail_description)
        ib_back = findViewById(R.id.ib_back)

        tv_tip_detail_title.text = tipTitle
        tv_tip_detail_description.text = tipDescription

        ib_back.setOnClickListener {
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