package com.guys_from_301.stock_game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.guys_from_301.stock_game.data.GameNormalDB
import com.guys_from_301.stock_game.data.Profile
import com.guys_from_301.stock_game.data.ProfileDB
import java.lang.Float.isNaN

class ResultNormalActivity : AppCompatActivity() {
    private var profileDb : ProfileDB? = null
    private var gamenormalDb: GameNormalDB? = null
    private lateinit var btnok : Button
    private lateinit var textprofit : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_normal)

        gameend = false // 게임 전역번수 초기화
        endsuccess = false // 게임 전역번수 초기화

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
        profileDb = ProfileDB.getInstace(this@ResultNormalActivity)
        gamenormalDb = GameNormalDB.getInstace(this@ResultNormalActivity)
        var profittotal: Float = profitrate
        var money = profileDb?.profileDao()?.getMoney()!!
        val newProfile = Profile()
        println("---zz"+profittotal)
        println("---zz"+ profitrate)

        // 사용자 profiledb의 money 업데이트
        if(isNaN(profittotal)){
            newProfile.money = profileDb?.profileDao()?.getMoney()!!
        }
        else{
            newProfile.money = Math.round(money.toFloat() * (1.0 +profittotal*0.01).toFloat())
        }
        newProfile.id = profileDb?.profileDao()?.getId()?.toLong()
        newProfile.rank = profileDb?.profileDao()?.getRank()!!
        newProfile.nickname = profileDb?.profileDao()?.getNickname()!!
        newProfile.history = profileDb?.profileDao()?.getHistory()!!
        newProfile.level = profileDb?.profileDao()?.getLevel()!!
        newProfile.exp = profileDb?.profileDao()?.getExp()!!
        newProfile.login = profileDb?.profileDao()?.getLogin()!!
        newProfile.profit = (profileDb?.profileDao()?.getProfit()!!*profileDb?.profileDao()?.getRoundCount()!!+relativeprofitrate)/(profileDb?.profileDao()?.getRoundCount()!!+1)
        newProfile.roundcount = profileDb?.profileDao()?.getRoundCount()!!+1
        newProfile.login_id = profileDb?.profileDao()?.getLoginid()!!
        newProfile.login_pw = profileDb?.profileDao()?.getLoginpw()!!
        profileDb?.profileDao()?.update(newProfile)
        // 서버에 올리는 코드
        update(getHash(profileDb?.profileDao()?.getLoginid()!!).trim(),
                getHash(profileDb?.profileDao()?.getLoginpw()!!).trim(),
                profileDb?.profileDao()?.getMoney()!!,
                profileDb?.profileDao()?.getValue1()!!,
                profileDb?.profileDao()?.getNickname()!!,
                profileDb?.profileDao()?.getProfit()!!,
                profileDb?.profileDao()?.getRoundCount()!!,
                profileDb?.profileDao()?.getHistory()!!,
                profileDb?.profileDao()?.getLevel()!!
        )
        gamenormalDb?.gameNormalDao()?.deleteId(setId)
    }
}