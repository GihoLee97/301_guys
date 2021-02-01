package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.Profile
import com.example.myapplication.data.ProflieDB
import org.w3c.dom.Text

class ProfileActivity : AppCompatActivity() {
    //receive profile room data
    private var profileDb: ProflieDB? = null
    private var profileList = mutableListOf<Profile>()
    private lateinit var nickname_text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileDb = ProflieDB.getInstace(this)

        nickname_text = findViewById<TextView>(R.id.nickName_text)

        val startRunnable = Runnable {
            profileList = profileDb?.profileDao()?.getAll()!!
        }

        if (profileDb?.profileDao()?.getAll().isNullOrEmpty()) { // PreSet the setting in the case of first running
            nickname_text.text = "닉네임을 아직 정하지 않았습니다."
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
            nickname_text.text = profileDb?.profileDao()?.getNickname()
        }

        val startThread = Thread(startRunnable)
        startThread.start()

        val nickname_btn = findViewById<Button>(R.id.nickname_btn)
        nickname_btn.setOnClickListener {
            call_dialog_nick()
        }
    }

    // call dialog_nick and update nickname
    private fun call_dialog_nick() {
        val dlg_nick = Dialog_nick(this)
        dlg_nick.start(profileDb)
        nickname_text.text = profileDb?.profileDao()?.getNickname() //TODO!!!!!
    }
}

