package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
    val diindex: Int = 0

    var volume: Boolean = true
    var push: Boolean = false
    var autoplay: Int = 5
    var thema: Int = 0
    var index:MutableList<Boolean> = mutableListOf(false,false,false,false,false)
    var iindex : Int = 0

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


        settingDb = SettingDB.getInstace(this)


        if(setting != null&&start==0){
            start=start+1
            bgm_sw.isChecked=dvolume
            push_sw.isChecked=dpush
            autoplay_bar.progress=dautoplay
            thema_choose.check(themas[dthema].id)
        }
        else{
            start+=1
            bgm_sw.isChecked = SettingDB.getInstace(this)?.settingDao()?.getVolume()!!
            push_sw.isChecked = SettingDB.getInstace(this)?.settingDao()?.getPush()!!
            autoplay_bar.progress = SettingDB.getInstace(this)?.settingDao()?.getAutoSpeed()!!
            thema_choose.check(themas[SettingDB.getInstace(this)?.settingDao()?.getThema()!!].id)
        }

        print.text=start.toString()//지우기


        //확인 버튼
        complete.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            volume = bgm_sw.isChecked
            push = push_sw.isChecked
            autoplay = autoplay_bar.progress
            thema = thema_choose.checkedRadioButtonId
            index = mutableListOf(interest.isChecked, exchange.isChecked, shortlonginterest.isChecked, gold.isChecked, money.isChecked)
            iindex = index2Int(mutableListOf(interest.isChecked, exchange.isChecked, shortlonginterest.isChecked, gold.isChecked, money.isChecked))
            val newSetting = Setting(1,bgm_sw.isChecked,push_sw.isChecked,autoplay_bar.progress,thema_choose.checkedRadioButtonId,iindex)
            SettingDB?.getInstace(this)?.settingDao()?.deleteAll()
            SettingDB?.getInstace(this)?.settingDao()?.insert(newSetting)
        }
    }

    // convert int to boolean list 1 --> 00001 / 3 --> 00011
    private fun int2Index(input : Int, indexNum : Int): MutableList<Boolean>{
        var output : MutableList<Boolean> = mutableListOf()
        var cnt = indexNum
        while(cnt>0){
            if(input.and(1)==1){
                output.add(true)
            }
            else {
                output.add(false)
            }
            input.ushr(1)
            cnt -= 1
        }
        return output
    }

    private fun index2Int(index : MutableList<Boolean>): Int{
        var output = 0
        for (i in index.size..1){
            if(index[i-1]) output +=1
            output.shl(1)
        }
        return output
    }

    override fun onStart() {
        super.onStart()

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

        print.text=print.text.toString()+"onStart"//지우기


//
//        complete.setOnClickListener {
//            val intent = Intent(this,MainActivity::class.java)
//            startActivity(intent)
//            volume = bgm_sw.isChecked
//            push = push_sw.isChecked
//            autoplay = autoplay_bar.progress
//            thema = thema_choose.checkedRadioButtonId
//            index = mutableListOf(interest.isChecked, exchange.isChecked, shortlonginterest.isChecked, gold.isChecked, money.isChecked)
//            iindex = index2Int(mutableListOf(interest.isChecked, exchange.isChecked, shortlonginterest.isChecked, gold.isChecked, money.isChecked))
//            val newSetting = Setting(1,bgm_sw.isChecked,push_sw.isChecked,autoplay_bar.progress,thema_choose.checkedRadioButtonId,iindex)
//            SettingDB?.getInstace(this)?.settingDao()?.update(newSetting)
//        }


    }

    override fun onResume() {
        super.onResume()

        var print = findViewById<TextView>(R.id.print)
//        var bgm_sw = findViewById<Switch>(R.id.bgm_sw)
//        var push_sw = findViewById<Switch>(R.id.push_sw)
//        var autoplay_bar = findViewById<SeekBar>(R.id.autoPlaySpeed_seekBar)
//        var thema_choose = findViewById<RadioGroup>(R.id.thema)
//        var light = findViewById<RadioButton>(R.id.light_btn)
//        var dark = findViewById<RadioButton>(R.id.dark_btn)
//        var themas:List<RadioButton> = listOf(light, dark)
//        var interest = findViewById<CheckBox>(R.id.interest_btn)
//        var exchange = findViewById<CheckBox>(R.id.exchange_btn)
//        var shortlonginterest = findViewById<CheckBox>(R.id.shortlonginterest_btn)
//        var gold = findViewById<CheckBox>(R.id.gold_btn)
//        var money = findViewById<CheckBox>(R.id.money_btn)
//        var complete = findViewById<Button>(R.id.complete_btn)

        print.text=print.text.toString()+"onResume"//지우기


//        volume = bgm_sw.isChecked
//        push = push_sw.isChecked
//        autoplay = autoplay_bar.progress
//        thema = thema_choose.checkedRadioButtonId
//        index = mutableListOf(interest.isChecked, exchange.isChecked, shortlonginterest.isChecked, gold.isChecked, money.isChecked)
//        iindex = index2Int(mutableListOf(interest.isChecked, exchange.isChecked, shortlonginterest.isChecked, gold.isChecked, money.isChecked))
//
//        bgm_sw.isChecked = SettingDB.getInstace(this)?.settingDao()?.getVolume()!!
//        push_sw.isChecked = SettingDB.getInstace(this)?.settingDao()?.getPush()!!
//        autoplay_bar.progress = SettingDB.getInstace(this)?.settingDao()?.getAutoSpeed()!!
//        thema_choose.check(themas[SettingDB.getInstace(this)?.settingDao()?.getThema()!!].id)

//        complete.setOnClickListener {
//            val intent = Intent(this,MainActivity::class.java)
//            startActivity(intent)
//            volume = bgm_sw.isChecked
//            push = push_sw.isChecked
//            autoplay = autoplay_bar.progress
//            thema = thema_choose.checkedRadioButtonId
//            index = mutableListOf(interest.isChecked, exchange.isChecked, shortlonginterest.isChecked, gold.isChecked, money.isChecked)
//            iindex = index2Int(mutableListOf(interest.isChecked, exchange.isChecked, shortlonginterest.isChecked, gold.isChecked, money.isChecked))
//            val newSetting = Setting(1,bgm_sw.isChecked,push_sw.isChecked,autoplay_bar.progress,thema_choose.checkedRadioButtonId,iindex)
//            SettingDB?.getInstace(this)?.settingDao()?.update(newSetting)
//        }


    }

}

