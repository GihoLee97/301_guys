package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.myapplication.data.GameNormalDB
import com.example.myapplication.data.Profile
import com.example.myapplication.data.ProflieDB

class ResultNormalActivity : AppCompatActivity() {
    private var profileDb : ProflieDB? = null
    private var gamenormalDb: GameNormalDB? = null
    private lateinit var btnok : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_normal)
        btnok = findViewById(R.id.btn_resultOk)
        btnok.setOnClickListener{
            GotoMainactivity()
        }
    }

    fun GotoMainactivity(){
        reflect()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        GotoMainactivity()
    }

    fun reflect(){
        //아직 profittotal 제대로 반영 안됨
        
//        profileDb = ProflieDB.getInstace(this@ResultNormalActivity)
//        gamenormalDb = GameNormalDB.getInstace(this@ResultNormalActivity)
//
//        var profittotal = gamenormalDb?.gameNormalDao()?.getProfitTot()!!
//
//        var money = profileDb?.profileDao()?.getMoney()!!
//        val newProfile = Profile()
//        // 사용자 profiledb의 money 업데이트
//        println("---zz"+profittotal)
//        newProfile.money = Math.round(money * (1+profittotal))
//
//        newProfile.id = profileDb?.profileDao()?.getId()?.toLong()
//        newProfile.rank = profileDb?.profileDao()?.getRank()!!
//        newProfile.nickname = profileDb?.profileDao()?.getNickname()!!
//        newProfile.history = profileDb?.profileDao()?.getHistory()!!
//        newProfile.level = profileDb?.profileDao()?.getLevel()!!
//        newProfile.exp = profileDb?.profileDao?.getExp()!!
//        newProfile.login = profileDb?.profileDao()?.getLogin()!!
//        newProfile.profit = profileDb?.profileDao()?.getProfit()!!
//        newProfile.login_id = profileDb?.profileDao()?.getLoginid()!!
//        newProfile.login_pw = profileDb?.profileDao()?.getLoginpw()!!
//        profileDb?.profileDao()?.update(newProfile)
//
//        // 서버에 올리는 코드
//        update(profileDb?.profileDao()?.getLoginid()!!,
//                profileDb?.profileDao()?.getLoginpw()!!,
//                profileDb?.profileDao()?.getMoney()!!,
//                5,5,5,
//                profileDb?.profileDao()?.getNickname()!!,
//                profileDb?.profileDao()?.getProfit()!!,
//                profileDb?.profileDao()?.getHistory()!!,
//                profileDb?.profileDao()?.getLevel()!!
//        )
//        gamenormalDb?.gameNormalDao()?.deleteAll()
    }

}