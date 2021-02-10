package com.example.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.myapplication.data.GameNormalDB
import com.example.myapplication.data.Profile
import com.example.myapplication.data.ProflieDB
import com.example.myapplication.retrofit.RetrofitLevelUp
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ResultNormalActivity : AppCompatActivity() {
    private var profileDb : ProflieDB? = null
    private var gamenormalDb: GameNormalDB? = null
    private lateinit var btnok : Button
    private lateinit var textprofit : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_normal)
        textprofit = findViewById(R.id.ResultProfit)
        btnok = findViewById(R.id.btn_resultOk)

        textprofit.text = "수익률: "+ profitrate + "%"

        btnok.setOnClickListener{
            GotoMainactivity()
        }
    }

    fun GotoMainactivity(){
        reflect()
        val dialog_level = Dialog_level(this)
        dialog_level.start()
    }

    override fun onBackPressed() {
        GotoMainactivity()
    }

    fun reflect(){
        //아직 profittotal 제대로 반영 안됨
        profileDb = ProflieDB.getInstace(this@ResultNormalActivity)
        gamenormalDb = GameNormalDB.getInstace(this@ResultNormalActivity)
        var profittotal: Float = profitrate
        var money = profileDb?.profileDao()?.getMoney()!!
        val newProfile = Profile()
        // 사용자 profiledb의 money 업데이트
        println("---zz"+profittotal)
        newProfile.money = Math.round(money.toFloat() * (1.0 +profittotal*0.01).toFloat())
        newProfile.id = profileDb?.profileDao()?.getId()?.toLong()
        newProfile.rank = profileDb?.profileDao()?.getRank()!!
        newProfile.nickname = profileDb?.profileDao()?.getNickname()!!
        newProfile.history = profileDb?.profileDao()?.getHistory()!!
        newProfile.level = profileDb?.profileDao()?.getLevel()!!
        newProfile.exp = profileDb?.profileDao()?.getExp()!!
        newProfile.login = profileDb?.profileDao()?.getLogin()!!
        newProfile.profit = profileDb?.profileDao()?.getProfit()!!
        newProfile.login_id = profileDb?.profileDao()?.getLoginid()!!
        newProfile.login_pw = profileDb?.profileDao()?.getLoginpw()!!
        profileDb?.profileDao()?.update(newProfile)
        // 서버에 올리는 코드
        update(getHash(profileDb?.profileDao()?.getLoginid()!!).trim(),
                getHash(profileDb?.profileDao()?.getLoginpw()!!).trim(),
                profileDb?.profileDao()?.getMoney()!!,
                5,5,5,
                profileDb?.profileDao()?.getNickname()!!,
                profileDb?.profileDao()?.getProfit()!!,
                profileDb?.profileDao()?.getHistory()!!,
                profileDb?.profileDao()?.getLevel()!!
        )
        gamenormalDb?.gameNormalDao()?.deleteAll()
    }

}