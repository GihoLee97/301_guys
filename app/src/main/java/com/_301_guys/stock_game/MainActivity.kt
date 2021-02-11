package com._301_guys.stock_game

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com._301_guys.stock_game.data.GameSetDB
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {

    private var gameSetDb: GameSetDB? = null
    private lateinit var btn_profile : Button
    private lateinit var btn_setting : Button
    private lateinit var btn_game : Button
    private lateinit var btn_market : Button
    lateinit var mAdView : AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gameSetDb = GameSetDB.getInstace(this)
        // 광고 관련 코드
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        btn_profile = findViewById(R.id.btn_profile)
        btn_setting = findViewById(R.id.btn_setting)
        btn_game =  findViewById(R.id.btn_game)
        btn_market = findViewById(R.id.btn_market)

        btn_game.isEnabled = false // 로딩 미완료 상태일 때 게임 버튼 비활성화

        btn_profile.setOnClickListener{
            val intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
        }

        btn_setting.setOnClickListener {
            val intent = Intent(this,SettingActivity::class.java)
            startActivity(intent)
        }

        btn_game.setOnClickListener{
            val intentgame = Intent(this, GameNormalActivity::class.java)
            val intent = Intent(this,GameSettingActivity::class.java)
            if(gameSetDb?.gameSetDao()?.getAll()?.isEmpty() == true)      startActivity(intent)
            else {
                setCash = gameSetDb?.gameSetDao()?.getSetCash()!!
                setMonthly = gameSetDb?.gameSetDao()?.getSetMonthly()!!
                setSalaryraise = gameSetDb?.gameSetDao()?.getSetSalaryRaise()!!
                setGamespeed = gameSetDb?.gameSetDao()?.getSetGameSpeed()!!
                startActivity(intentgame)}
        }

        btn_market.setOnClickListener {
//            val intent = Intent(this,MarketActivity::class.java)
//            startActivity(intent)
            val intent = Intent(this,MarketActivity::class.java)
            startActivity(intent)
        }

        while (true) {
            if (loadcomp) {
                btn_game.isEnabled = true
                btn_game.text = "게임"
                break
            }
            Thread.sleep(50)
        }

    }

    // 두번 누르면 종료되는 코드
    var time3: Long = 0
    override fun onBackPressed() {
        val time1 = System.currentTimeMillis()
        val time2 = time1 - time3
        if (time2 in 0..2000) {
            // 이거 3줄 다 써야 안전하게 종료
            moveTaskToBack(true)
            finishAffinity()
            android.os.Process.killProcess(android.os.Process.myPid())
        }
        else {
            time3 = time1
            Toast.makeText(applicationContext, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
    }
}
