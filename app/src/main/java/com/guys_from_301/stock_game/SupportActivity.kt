package com.guys_from_301.stock_game

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout


class SupportActivity : AppCompatActivity() {
    private lateinit var cl_support1 : ConstraintLayout
    private lateinit var cl_support2 : ConstraintLayout
    private lateinit var cl_support3 : ConstraintLayout
    private lateinit var cl_support4 : ConstraintLayout

    val subject = arrayOf("[게임 문의]","[계정 문의]","[결제 문의]","[기타 문의]")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support)

        cl_support1 = findViewById(R.id.cl_support1)
        cl_support2 = findViewById(R.id.cl_support2)
        cl_support3 = findViewById(R.id.cl_support3)
        cl_support4 = findViewById(R.id.cl_support4)

        cl_support1.setOnClickListener { sendEmail(0) }
        cl_support2.setOnClickListener { sendEmail(1) }
        cl_support3.setOnClickListener { sendEmail(2) }
        cl_support4.setOnClickListener { sendEmail(3) }
    }

    private fun sendEmail(mode : Int){
        val email = Intent(Intent.ACTION_SEND)
        email.type = "plain/text"
        val address = arrayOf("thinkers301@gmail.com")
        email.putExtra(Intent.EXTRA_EMAIL, address)
        email.putExtra(Intent.EXTRA_SUBJECT, subject[mode])
        email.putExtra(Intent.EXTRA_TEXT, subject[mode]+" 관련 문의 입니다.\n내용을 이어서 적어주세요!\n")
        startActivity(email)
    }
}