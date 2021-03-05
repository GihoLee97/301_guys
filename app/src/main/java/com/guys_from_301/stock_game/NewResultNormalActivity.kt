package com.guys_from_301.stock_game

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.guys_from_301.stock_game.data.*
import java.time.LocalDateTime

var totaltradeday: Int = 0
var isHistory: Boolean = false

class NewResultNormalActivity: AppCompatActivity() {

    //view 요소들
    private lateinit var tv_profitRateFinal: TextView
    private lateinit var tv_experience: TextView
    private lateinit var tv_rewardStack: TextView
    private lateinit var tv_tradeday: TextView
    private lateinit var tv_relativeProfitRate: TextView
    private lateinit var tv_yourProfit: TextView
    private lateinit var tv_totalTax: TextView
    private lateinit var tv_totalDividend: TextView
    private lateinit var tv_totalFee: TextView
    private lateinit var tv_realizeGainNLoss: TextView
    private lateinit var tv_game_date: TextView
    private lateinit var tv_pinPointProfitRate: TextView
    private lateinit var tv_monthly_profit: TextView
    private lateinit var cl_monthly_profitrate_chart: ConstraintLayout
    private lateinit var cht_monthlyProfitRate: LineChart
    private lateinit var ll_only_result: LinearLayout
    private lateinit var tv_only_result: TextView
    private lateinit var ll_goBackHome: LinearLayout
    private lateinit var btn_share: Button

    //Db
    private var gamenormalDb: GameNormalDB? = null
    private var gamesetDB: GameSetDB? = null

    // 자산 차트 데이터
    private val assetEn: ArrayList<Entry> = ArrayList()
    private val assetDs: LineDataSet = LineDataSet(assetEn, "asset")
    private val assetD: LineData = LineData(assetDs)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_result_normal)

        gameend = false // 게임 전역번수 초기화
        endsuccess = false // 게임 전역번수 초기화


        //view에 연결
        tv_profitRateFinal = findViewById(R.id.tv_profitRateFinal)
        tv_experience = findViewById(R.id.tv_experience)
        tv_rewardStack = findViewById(R.id.tv_rewardStack)
        tv_tradeday = findViewById(R.id.tv_tradeday)
        tv_relativeProfitRate = findViewById(R.id.tv_relativeprofitrate)
        tv_yourProfit = findViewById(R.id.tv_yourProfit)
        tv_totalTax = findViewById(R.id.tv_totalTax)
        tv_totalDividend = findViewById(R.id.tv_totalDividend)
        tv_totalFee = findViewById(R.id.tv_totalFee)
        tv_realizeGainNLoss = findViewById(R.id.tv_realizedGainNLoss)
        tv_game_date = findViewById(R.id.tv_game_date)
        tv_pinPointProfitRate = findViewById(R.id.tv_pinPointProfitRate)
        tv_monthly_profit = findViewById(R.id.tv_monthly_profit)
        cl_monthly_profitrate_chart = findViewById(R.id.cl_monthly_prforitrate_chart)
        cht_monthlyProfitRate = findViewById(R.id.cht_monthlyProfitRate)
        ll_only_result = findViewById(R.id.ll_only_result)
        tv_only_result = findViewById(R.id.tv_only_result)
        btn_share = findViewById(R.id.btn_share)

        ll_goBackHome = findViewById(R.id.ll_goBackHome)


        // 차트
        assetchart()

        // 해당 게임 db 삭제
        deletedata()


        if (isHistory) {
            tv_monthly_profit.visibility = View.INVISIBLE
            cl_monthly_profitrate_chart.visibility = View.INVISIBLE
            ll_only_result.visibility = View.INVISIBLE
            tv_only_result.visibility = View.INVISIBLE
        }

        if (profitrate > 0) tv_profitRateFinal.text = "+" + per.format(profitrate) + "%"
        else tv_profitRateFinal.text = per.format(profitrate) + "%"
        tv_experience.text = "+100"


        if (relativeprofitrate > 0) tv_relativeProfitRate.text = "+" + per.format(relativeprofitrate) + "%"
        else {
            tv_relativeProfitRate.text = per.format(relativeprofitrate) + "%"
            tv_relativeProfitRate.setTextAppearance(applicationContext, R.style.gameResult_relativeProfitValueDown)
            tv_yourProfit.setTextAppearance(applicationContext, R.style.gameResult_descriptionChangeDown)
        }
        tv_yourProfit.text = "당신은 " + dec.format(10000 * (100 + relativeprofitrate) / 100) + "원"
        tv_totalTax.text = "+$" + dec.format(taxtot)
        tv_totalDividend.text = "+$" + dec.format(dividendtot)
        tv_totalFee.text = "+$" + dec.format(tradecomtot)
        tv_realizeGainNLoss.text = "+$" + dec.format(profit)
        tv_tradeday.text = totaltradeday.toString() + "일"

        ll_goBackHome.setOnClickListener {
            GotoMainactivity()
        }

        btn_share.setOnClickListener {
            val path = captureUtil.captureAndSaveViewWithKakao(findViewById(R.id.cl_shareBox))
            shareManager.shareBinaryWithKakao(this, path)
        }

    }

    fun assetchart() {
        gamenormalDb = GameNormalDB.getInstace(this@NewResultNormalActivity)

//        assetEn.add(Entry(0F, 1000F))

        val assetMonthly = gamenormalDb?.gameNormalDao()?.getChart(accountID!!)

        if (assetMonthly != null) {
            for (i in assetMonthly.indices) {
                assetD.addEntry(Entry((i+1).toFloat(), assetMonthly[i]), 0)
                println("월: "+i.toString()+" | 자산: "+assetMonthly[i])
            }
        }

        cht_monthlyProfitRate.data = assetD

        cht_monthlyProfitRate.animateXY(1,1)
        cht_monthlyProfitRate.notifyDataSetChanged()
        assetD.notifyDataChanged()
    }

    fun deletedata() {
        gamenormalDb = GameNormalDB.getInstace(this@NewResultNormalActivity)
        gamesetDB = GameSetDB.getInstace(this@NewResultNormalActivity)

        val deleteRunnable = kotlinx.coroutines.Runnable {
            if (gamenormalDb?.gameNormalDao()?.getSetWithNormalItem1Able(setId, accountID!!)?.size!! == 0) totaltradeday = tradeday
            else totaltradeday = gamenormalDb?.gameNormalDao()?.getSetWithNormalItem1Able(setId, accountID!!)?.sum()!! + gamenormalDb?.gameNormalDao()?.getSetWithNormalItem1Able(setId, accountID!!)?.size!! * 2
            gamenormalDb?.gameNormalDao()?.deleteId(setId, accountID!!)
            gamesetDB?.gameSetDao()?.deleteId(setId, accountID!!)
            Log.d("hongz", "게임 종료 gameset, gamenormal 삭제")
        }
        val deleteThread = Thread(deleteRunnable)
        deleteThread.start()
    }

    fun GotoMainactivity() {
        if (isHistory) {
            val intent = Intent(this@NewResultNormalActivity, NewHistoryActivity::class.java)
            startActivity(intent)
        } else {
            reflect()
            val intent = Intent(this@NewResultNormalActivity, MainActivity::class.java)
            startActivity(intent)
        }
        isHistory = false

    }

    override fun onBackPressed() {
        GotoMainactivity()
        finish()
    }


    fun reflect() {
//        profileDb = ProfileDB.getInstace(this@NewResultNormalActivity)
        gamenormalDb = GameNormalDB.getInstace(this@NewResultNormalActivity)
        gamesetDB = GameSetDB.getInstace(this@NewResultNormalActivity)
        var profittotal: Float = profitrate
        var money = profileDbManager!!.getMoney()!!
        println("---zz" + profittotal)
        println("---zz" + profitrate)


        // 사용자 profiledb의 money 업데이트
        if (!java.lang.Float.isNaN(profittotal))
            profileDbManager!!.setMoney(Math.round(money.toFloat() * (1.0 + profittotal * 0.01).toFloat()))
        if (isSafeCompleted)
            profileDbManager!!.setRoundCount(profileDbManager!!.getRoundCount()!! + 1)

        // 서버에 올리는 코드
        update(getHash(profileDbManager!!.getLoginId()!!).trim(),
                getHash(profileDbManager!!.getLoginPw()!!).trim(),
                profileDbManager!!.getMoney()!!,
                profileDbManager!!.getValue1()!!,
                profileDbManager!!.getNickname()!!,
                profileDbManager!!.getProfitRate()!!,
                profileDbManager!!.getRelativeProfit()!!,
                profileDbManager!!.getRoundCount()!!,
                profileDbManager!!.getHistory()!!,
                profileDbManager!!.getLevel()!!
        )
        var localDateTime = LocalDateTime.now()
        val newGameNormalDB = GameNormal(localDateTime.toString(), asset, cash, input, bought, sold, evaluation, profit, profitrate, profittot, profityear, "최종", 0F, 0F, 0, 0, quant1x, quant3x, quantinv1x, quantinv3x,
                bought1x, bought3x, boughtinv1x, boughtinv3x, aver1x, aver3x, averinv1x, averinv3x, buylim1x, buylim3x, buyliminv1x, buyliminv3x, price1x, price3x, priceinv1x, priceinv3x, val1x, val3x, valinv1x, valinv3x,
                pr1x, pr3x, prinv1x, prinv3x, setMonthly, monthToggle, tradecomtot, 0F, dividendtot, taxtot, "nothing", item1Active, item1Length, totaltradeday, item2Active, item3Active, item4Active, autobuy, autoratio, auto1x, endpoint, countYear, countMonth, snpNowdays, snpNowVal, snpDiff, setId, relativeprofitrate, localdatatime, accountID!!)
        gamenormalDb?.gameNormalDao()?.insert(newGameNormalDB)//완료한 게임 히스토리 저장
    }

    override fun onResume() {
        super.onResume()
        profileDbManager!!.refresh(this)
    }


    override fun onStop() {
        super.onStop()
        profileDbManager!!.write2database()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}