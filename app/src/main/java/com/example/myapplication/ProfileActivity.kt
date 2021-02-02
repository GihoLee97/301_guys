package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
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
        val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ProfileActivityViewModel::class.java)
        lateinit var changenick: String
        var count:Int = 0


        viewModel.nickname().observe(this, Observer {
            nickname_textView.text = it
        })
        val startRunnable = Runnable {
            profileList = profileDb?.profileDao()?.getAll()!!
        }

        if (profileDb?.profileDao()?.getNickname()=="") { // PreSet the setting in the case of first running
            nickname_textView.text = "닉네임을 아직 정하지 않았습니다."
            // preinsert profile for call_dialog_nick which just update the database
            viewModel.initialize("닉네임을 아직 정하지 않았습니다.")
            // update nickname
            viewModel.nicknameChange("닉네임을 아직 정하지 않았습니다.")
            val dlg_nick = Dialog_nick(this)
            profileDb = ProflieDB.getInstace(this)
            dlg_nick.start(profileDb)
            dlg_nick.setOnNicknameClickedListener { content->
                changenick=content
                viewModel.nicknameChange(changenick)
            }
//            count=count+1
        } else  { // recall database
            viewModel.initialize(profileDb?.profileDao()?.getNickname().toString())
//            count=count+1
        }

        val startThread = Thread(startRunnable)
        startThread.start()

        nickname_btn.setOnClickListener {
            val dlg_nick = Dialog_nick(this)
            dlg_nick.start(profileDb)
            dlg_nick.setOnNicknameClickedListener { content->
                changenick=content
                viewModel.nicknameChange(changenick)
            }
        }

        accountManagement_btn.setOnClickListener {
            val intent = Intent(this, AccountManagementActivity::class.java)
            startActivity(intent)
        }
    }


}


