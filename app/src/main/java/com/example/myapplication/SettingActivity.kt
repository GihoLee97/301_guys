package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.myapplication.data.Setting
import com.example.myapplication.data.SettingDB
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SettingActivity : AppCompatActivity() {
    //초기 설정값들
    val dvolume: Boolean = true
    val dpush: Boolean = false
    val dautoplay: Int = 5
    val dthema: Int = 0
    var thema:Int = 0
    private var settingDb : SettingDB? =null
    private var setList = mutableListOf<Setting>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        settingDb = SettingDB.getInstace(this)

        //변수선언
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
        var thema: Int = 0
        val startRunnable = Runnable {
            setList = settingDb?.settingDao()?.getAll()!!
        }
        var id: List<Int> = settingDb?.settingDao()?.getId()!!


        val startThread = Thread(startRunnable)
        startThread.start()

        if(settingDb==null){
            bgm_sw.isChecked=dvolume
            push_sw.isChecked=dpush
            autoplay_bar.progress=dautoplay
            thema_choose.check(themas[dthema].id)
            print.text= "SetList is Empty"
            var t = Toast.makeText(this,"실행", Toast.LENGTH_SHORT)
            t.show()

        } else{
            bgm_sw.isChecked= settingDb?.settingDao()?.getVolume()!![0]
            push_sw.isChecked= settingDb?.settingDao()?.getPush()!![0]
            autoplay_bar.progress= settingDb?.settingDao()?.getAutoSpeed()!![0]
            thema_choose.check(themas[settingDb?.settingDao()?.getThema()!![0]].id)
        }





        //확인 버튼
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
        if (bool==true)return 1
        else return 0
    }

    override fun onDestroy() {
        SettingDB.destroyINSTACE()
        super.onDestroy()
    }


}

