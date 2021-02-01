package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.Dialog_nick
import com.example.myapplication.data.Profile
import com.example.myapplication.data.ProflieDB

class ProfileActivity : AppCompatActivity() {
    //receive profile room data
    private var profileDb: ProflieDB? = null
    private var profileList = mutableListOf<Profile>()
    private lateinit var nickname_textView: TextView
    private lateinit var accountManagement_btn: Button
    private lateinit var nickname_btn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileDb = ProflieDB.getInstace(this)

        nickname_textView = findViewById(R.id.nickName_text)
        nickname_btn = findViewById(R.id.nickname_btn)
        accountManagement_btn = findViewById(R.id.accountManagement_btn)

        val startRunnable = Runnable {
            profileList = profileDb?.profileDao()?.getAll()!!
        }

        if (profileDb?.profileDao()?.getAll().isNullOrEmpty()) { // PreSet the setting in the case of first running
            nickname_textView.text = "닉네임을 아직 정하지 않았습니다."
            // preinsert profile for call_dialog_nick which just update the database
            val setRunnable = Runnable {
                val newProfile = Profile(1, "닉네임을 아직 정하지 않았습니다.", 0, "", 1, "")
                profileDb?.profileDao()?.insert(newProfile)
            }
            var setThread = Thread(setRunnable)
            setThread.start()
            // update nickname
            call_dialog_nick()
        } else { // recall database
            nickname_textView.text = profileDb?.profileDao()?.getNickname().toString()
        }

        val startThread = Thread(startRunnable)
        startThread.start()

        nickname_btn.setOnClickListener {
            call_dialog_nick()
        }

        accountManagement_btn.setOnClickListener {
            val intent = Intent(this, AccountManagementActivity::class.java) //Main으로 이동
            startActivity(intent)
        }
    }

    // call dialog_nick and update nickname
    private fun call_dialog_nick() {
        val dlg_nick = Dialog_nick(this)
        dlg_nick.start(profileDb)
        nickname_textView.text = profileDb?.profileDao()?.getNickname() //TODO!!!!!
    }
}


