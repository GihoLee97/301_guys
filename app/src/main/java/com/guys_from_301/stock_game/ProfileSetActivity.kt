package com.guys_from_301.stock_game

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.observe

var liveText : MutableLiveData<String> = MutableLiveData()


class ProfileSetActivity : AppCompatActivity() {
    private lateinit var cl_change_pw: ConstraintLayout
    private lateinit var tv_change_icon: TextView
    private lateinit var ib_back: ImageButton
    private lateinit var cl_change_nickname: ConstraintLayout
    private lateinit var tv_nickname : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_set)

        tv_nickname = findViewById(R.id.tv_nickname)
        cl_change_pw = findViewById(R.id.cl_change_pw)
        tv_change_icon = findViewById(R.id.tv_change_icon)
        cl_change_nickname = findViewById(R.id.cl_change_nickname)
        ib_back = findViewById(R.id.ib_back)
        tv_nickname.text = profileDbManager!!.getNickname()

        if (profileDbManager!!.getLogin() == 1) { // general login
            cl_change_pw.visibility = View.VISIBLE
        }
        else{
            cl_change_pw.visibility = View.GONE
        }

        liveText.observe(this, Observer {
            tv_nickname.text = it
        })


        cl_change_nickname.setOnClickListener{
            val dlg = Dialog_nick(this@ProfileSetActivity, false)
            dlg.start()
        }

        cl_change_pw.setOnClickListener {
            val dlg = Dialog_change_pw(this@ProfileSetActivity)
            dlg.start()
        }
        tv_change_icon.setOnClickListener {
            val dlg = Dialog_change_profile(this@ProfileSetActivity)
            dlg.start()
        }

        ib_back.setOnClickListener {
            onBackPressed()
        }

    }

}