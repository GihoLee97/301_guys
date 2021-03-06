package com.guys_from_301.stock_game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView

class NoticeDetailActivity : AppCompatActivity() {
    private lateinit var tv_notice_title : TextView
    private lateinit var tv_notice_date : TextView
    private lateinit var tv_notice_discription : TextView
    private lateinit var ib_back : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_detail)

        tv_notice_title = findViewById(R.id.tv_notice_title)
        tv_notice_date = findViewById(R.id.tv_notice_date)
        tv_notice_discription = findViewById(R.id.tv_notice_description)
        ib_back = findViewById(R.id.ib_back)

        tv_notice_title.text = noticeTitle
        tv_notice_date.text = noticeDate
        tv_notice_discription.text = noticeContent

        ib_back.setOnClickListener {
            onBackPressed()
        }
    }
}