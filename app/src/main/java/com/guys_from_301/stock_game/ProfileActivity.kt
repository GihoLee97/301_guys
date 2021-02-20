package com.guys_from_301.stock_game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.guys_from_301.stock_game.data.Profile
import com.guys_from_301.stock_game.data.ProfileDB

class ProfileActivity : AppCompatActivity() {
    //receive profile room data
    val mContext : Context = this
    private var profileDb: ProfileDB? = null
    private var profileList = mutableListOf<Profile>()
    private lateinit var profitrate_textView: TextView
    private lateinit var nickname_textView: TextView
    private lateinit var level_textView: TextView
    private lateinit var money_textView: TextView
    private lateinit var value1_textView: TextView
    private lateinit var tradeday_textView: TextView
    private lateinit var completegame_textView: TextView
    private lateinit var accountManagement_btn: Button
    private lateinit var nickname_btn: Button
    private val profileActivityViewModel = ProfileActivityViewModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        Log.d("Giho","ProfileActivity")

        profileDb = ProfileDB.getInstace(this)
        profitrate_textView = findViewById(R.id.profitrate)
        nickname_textView = findViewById(R.id.nickName_text)
        level_textView = findViewById(R.id.level)
        money_textView = findViewById(R.id.money)
        value1_textView = findViewById(R.id.value1)
        tradeday_textView = findViewById(R.id.tradeday)
        completegame_textView = findViewById(R.id.completegame)
        nickname_btn = findViewById(R.id.nickname_btn)
        accountManagement_btn = findViewById(R.id.accountManagement_btn)
        val history_btn = findViewById<Button>(R.id.history_btn)
        lateinit var changenick: String
        var count:Int = 0

        profitrate_textView.text = "시장대비 평균수익률: "+profileDb?.profileDao()?.getProfit()!!
        level_textView.text = "레벨: "+profileDb?.profileDao()?.getLevel()!!
        money_textView.text = "현금: "+profileDb?.profileDao()?.getMoney()!!
        value1_textView.text = "피로도: "+profileDb?.profileDao()?.getValue1()!!
        tradeday_textView.text = "누적거래일: "+profileDb?.profileDao()?.getHistory()!!
        completegame_textView.text = "완료한 게임 횟수: "+profileDb?.profileDao()?.getRoundCount()!!
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
            profileDb = ProfileDB.getInstace(this)

            dlg_nick.start(profileDb)
        }

        val startThread = Thread(startRunnable)
        startThread.start()
        nickname_btn.setOnClickListener {
            val dlg_nick = Dialog_nick(this,false, profileActivityViewModel)
            dlg_nick.start(profileDb)
        }

        accountManagement_btn.setOnClickListener {
            profileDb = ProfileDB.getInstace(this@ProfileActivity)
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


