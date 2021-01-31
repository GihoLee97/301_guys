package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.myapplication.data.Setting
import com.example.myapplication.data.SettingDB
import java.lang.Math.pow
import java.util.EnumSet.range

class SettingActivity : AppCompatActivity() {
    //초기 설정값들
    val dvolume: Boolean = true
    val dpush: Boolean = false
    val dautoplay: Int = 5
    val dthema: Int = 0
    private var settingDb : SettingDB? =null
    private var setList = mutableListOf<Setting>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        settingDb = SettingDB.getInstace(this)

        //변수선언
        var bgm_sw = findViewById<Switch>(R.id.bgm_sw)
        var push_sw = findViewById<Switch>(R.id.push_sw)
        var autoplay_bar = findViewById<SeekBar>(R.id.autoPlaySpeed_seekBar)
        var thema_choose = findViewById<RadioGroup>(R.id.thema)
        var light = findViewById<RadioButton>(R.id.light_btn)
        var dark = findViewById<RadioButton>(R.id.dark_btn)
        var themas:List<RadioButton> = listOf(light, dark)
        var indexcount: Int = 5
        var interest = findViewById<CheckBox>(R.id.interest_btn)
        var exchange = findViewById<CheckBox>(R.id.exchange_btn)
        var shortlonginterest = findViewById<CheckBox>(R.id.shortlonginterest_btn)
        var gold = findViewById<CheckBox>(R.id.gold_btn)
        var money = findViewById<CheckBox>(R.id.money_btn)
        var complete = findViewById<Button>(R.id.complete_btn)
        var thema: Int = 0
        val startRunnable = Runnable {
            setList = settingDb?.settingDao()?.getAll()!!
        }

        val startThread = Thread(startRunnable)
        startThread.start()


        //화면 초기 setting
        if(settingDb?.settingDao()?.getId()?.isEmpty() == true){
            bgm_sw.isChecked=dvolume
            push_sw.isChecked=dpush
            autoplay_bar.progress=dautoplay
            thema_choose.check(themas[dthema].id)
        } else{
            bgm_sw.isChecked= settingDb?.settingDao()?.getVolume()!![0]
            push_sw.isChecked= settingDb?.settingDao()?.getPush()!![0]
            autoplay_bar.progress= settingDb?.settingDao()?.getAutoSpeed()!![0]
            thema_choose.check(themas[settingDb?.settingDao()?.getThema()!![0]].id)
            if(settingDb?.settingDao()?.getIndex()!![0]>=10000){interest.isChecked = true}
            var indexlist:MutableList<Boolean> = intToList(settingDb?.settingDao()?.getIndex()!![0], indexcount)
            interest.isChecked = indexlist[0].not()
            exchange.isChecked=indexlist[1].not()
            shortlonginterest.isChecked=indexlist[2].not()
            gold.isChecked=indexlist[3].not()
            money.isChecked=indexlist[4].not()
        }



        //확인 버튼, settingDB 저장
        complete.setOnClickListener {
            val setRunnable = Runnable {
                val newSetting = Setting()
                newSetting.id = 1
                newSetting.autospeed = autoplay_bar.progress
                newSetting.volume = bgm_sw.isChecked
                newSetting.push = push_sw.isChecked
                if(light.isChecked){ thema = 0 }
                else if(dark.isChecked){ thema = 1 }
                newSetting.thema = thema//수정필요
                newSetting.index = 10000*toInt(interest.isChecked)+1000*toInt(exchange.isChecked)+100*toInt(shortlonginterest.isChecked)+10*toInt(gold.isChecked)+toInt(money.isChecked)
                settingDb?.settingDao()?.insert(newSetting)
            }

            var setThread = Thread(setRunnable)
            setThread.start()

            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)

        }


    }




    fun toInt(bool: Boolean): Int{
        var i: Int
        if (bool==true) i = 0
        else i = 1
        return i
    }

    fun intToList(i : Int, n : Int): MutableList<Boolean>{
        var index:MutableList<Boolean> = mutableListOf()
        for(c in 1..n){index.add(true)}
        var input:Int = i
        for(a in 0..n-1){
            if(input < pow(10.0, (n-1-a).toDouble())){ index[a]=false }
            else{ input=input- pow(10.0, (n-1-a).toDouble()).toInt() }
        }
        return index
    }

    override fun onDestroy() {
        SettingDB.destroyINSTACE()
        super.onDestroy()
    }


}

