package com.guys_from_301.stock_game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.guys_from_301.stock_game.data.*
import org.w3c.dom.Text
import java.lang.Float.isNaN
import java.time.LocalDateTime

class ResultNormalActivity : AppCompatActivity() {
    private var profileDb : ProfileDB? = null
    private var gamenormalDb: GameNormalDB? = null
    private var gamesetDB: GameSetDB? = null
    private lateinit var btnok : Button
    private lateinit var textprofit : TextView
    private lateinit var textrelativeprofitrate : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_normal)

        gameend = false // 게임 전역번수 초기화
        endsuccess = false // 게임 전역번수 초기화

        textrelativeprofitrate = findViewById(R.id.ResultRelativeProfit)
        textprofit = findViewById(R.id.ResultProfit)
        btnok = findViewById(R.id.btn_resultOk)

        textprofit.text = "수익률: "+ profitrate + "%"
        textrelativeprofitrate.text = "시장대비 수익률: "+ relativeprofitrate+"%"

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
        gamesetDB = GameSetDB.getInstace(this@ResultNormalActivity)
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
        newProfile.nickname = profileDb?.profileDao()?.getNickname()!!
        newProfile.value1 = profileDb?.profileDao()?.getValue1()!!
        newProfile.profitrate = profileDb?.profileDao()?.getProfitRate()!!
        newProfile.relativeprofitrate = profileDb?.profileDao()?.getRelativeProfitRate()!!
        newProfile.history = profileDb?.profileDao()?.getHistory()!!
        newProfile.level = profileDb?.profileDao()?.getLevel()!!
        newProfile.exp = profileDb?.profileDao()?.getExp()!!
        newProfile.login = profileDb?.profileDao()?.getLogin()!!
        if(isSafeCompleted)  newProfile.roundcount = profileDb?.profileDao()?.getRoundCount()!!+1
        else newProfile.roundcount = profileDb?.profileDao()?.getRoundCount()!!
        newProfile.login_id = profileDb?.profileDao()?.getLoginid()!!
        newProfile.login_pw = profileDb?.profileDao()?.getLoginpw()!!
        profileDb?.profileDao()?.update(newProfile)
        // 서버에 올리는 코드
        update(getHash(profileDb?.profileDao()?.getLoginid()!!).trim(),
                getHash(profileDb?.profileDao()?.getLoginpw()!!).trim(),
                profileDb?.profileDao()?.getMoney()!!,
                profileDb?.profileDao()?.getValue1()!!,
                profileDb?.profileDao()?.getNickname()!!,
                profileDb?.profileDao()?.getProfitRate()!!,
                profileDb?.profileDao()?.getRelativeProfitRate()!!,
                profileDb?.profileDao()?.getRoundCount()!!,
                profileDb?.profileDao()?.getHistory()!!,
                profileDb?.profileDao()?.getLevel()!!
        )
        var localDateTime = LocalDateTime.now()
        val newGameNormalDB = GameNormal(localdatatime, asset, cash, input, bought, sold, evaluation, profit, profitrate, profittot, profityear,"최종", 0F,0F,0, 0 , quant1x, quant3x, quantinv1x, quantinv3x,
                bought1x, bought3x, boughtinv1x, boughtinv3x, aver1x, aver3x, averinv1x, averinv3x, buylim1x, buylim3x, buyliminv1x, buyliminv3x, price1x, price3x, priceinv1x, priceinv3x, val1x, val3x, valinv1x, valinv3x,
                pr1x, pr3x, prinv1x, prinv3x, setMonthly, monthToggle, tradecomtot,0F, dividendtot, taxtot , "nothing", item1Active, item1Length, item1Able, item2Active, item3Active, item4Active, autobuy, autoratio, auto1x, endpoint, countYear, countMonth, snpNowdays, snpNowVal, snpDiff, setId, relativeprofitrate, localDateTime.toString())
        gamenormalDb?.gameNormalDao()?.insert(newGameNormalDB)//완료한 게임 히스토리 저장
    }
}