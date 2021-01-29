package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.room.Entity

class SettingActivity : AppCompatActivity() {
    //초기 설정값들
    val dvolume: Boolean = true
    val dpush: Boolean = false
    val dautoplay: Int = 5
    val dthema: Int = 0
    val dindex: List<Boolean> = listOf(true, true, true, true, true)

    private var settingDb : SettingDB? =null
    private var setting = mutableListOf<Setting>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        //변수선언
        var start:Int = 0
        var print = findViewById<TextView>(R.id.print)
        var bgm_sw = findViewById<Switch>(R.id.bgm_sw)
        var push_sw = findViewById<Switch>(R.id.push_sw)
        var autoplay_bar = findViewById<SeekBar>(R.id.autoPlaySpeed_seekBar)
        var thema_choose = findViewById<RadioGroup>(R.id.thema)
        var light = findViewById<RadioButton>(R.id.light_btn)
        var dark = findViewById<RadioButton>(R.id.dark_btn)
        var themas:List<RadioButton> = listOf(light, dark)
        var interest = findViewById<CheckBox>(R.id.interest_btn)
        var exchange = findViewById<CheckBox>(R.id.exchange_btn)
        var shortlonginterest = findViewById<CheckBox>(R.id.shortlonginterest_btn)
        var gold = findViewById<CheckBox>(R.id.gold_btn)
        var money = findViewById<CheckBox>(R.id.money_btn)
        var complete = findViewById<Button>(R.id.complete_btn)
        var volume: Boolean
        var push: Boolean = false
        var autoplay: Int = 5
        var thema: Int = 0
        var index:List<Boolean> = listOf(interest.isChecked, exchange.isChecked, shortlonginterest.isChecked, gold.isChecked, money.isChecked)

        settingDb = SettingDB.getInstace(this)


        if(setting != null&&start==0){
            start=start+1
            bgm_sw.isChecked=dvolume
            push_sw.isChecked=dpush
            autoplay_bar.progress=dautoplay
            thema_choose.check(themas[dthema].id)
        }

        print.text=start.toString()//지우기


        //확인 버튼
        complete.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            volume = bgm_sw.isChecked
            push = push_sw.isChecked
            autoplay = autoplay_bar.progress
        }




    }
}

