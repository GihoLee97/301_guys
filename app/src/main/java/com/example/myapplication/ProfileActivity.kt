package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.myapplication.data.Profile
import com.example.myapplication.data.ProflieDB

class ProfileActivity : AppCompatActivity() {
    //receive profile room data
    private var profileDb: ProflieDB? = null
    private var profileList = mutableListOf<Profile>()
    private lateinit var nickname_textView: TextView
    private lateinit var accountManagement_btn: Button
    private lateinit var nickname_btn: Button
    private val profileActivityViewModel = ProfileActivityViewModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        Log.d("Giho","ProfileActivity")

        profileDb = ProflieDB.getInstace(this)
        nickname_textView = findViewById(R.id.nickName_text)
        nickname_btn = findViewById(R.id.nickname_btn)
        accountManagement_btn = findViewById(R.id.accountManagement_btn)
        val history_btn = findViewById<Button>(R.id.history_btn)
        lateinit var changenick: String
        var count:Int = 0


        profileActivityViewModel.getNickname().observe(this, Observer {
            nickname_textView.text = it
        })
        val startRunnable = Runnable {
            val newProfile = Profile()
            profileDb?.profileDao()?.update(newProfile)
            profileList = profileDb?.profileDao()?.getAll()!!

        }

        if (profileDb?.profileDao()?.getNickname()=="#########first_login##########") { // PreSet the setting in the case of first running
            nickname_textView.text = "닉네임을 정하세요."

            val dlg_nick = Dialog_nick(this,true,profileActivityViewModel)
            profileDb = ProflieDB.getInstace(this)

            dlg_nick.start(profileDb)
        }

        val startThread = Thread(startRunnable)
        startThread.start()
        nickname_btn.setOnClickListener {
            val dlg_nick = Dialog_nick(this,false, profileActivityViewModel)
            dlg_nick.start(profileDb)
        }

        accountManagement_btn.setOnClickListener {
            profileDb = ProflieDB.getInstace(this@ProfileActivity)
            val intent = Intent(this, AccountManagementActivity::class.java)
            startActivity(intent)
        }

        history_btn.setOnClickListener{
            val intent = Intent(this, GameHistoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onPause() {
        profileActivityViewModel.write2database()
        super.onPause()
    }

    override fun onRestart() {
        profileActivityViewModel.refresh()
        super.onRestart()
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}


