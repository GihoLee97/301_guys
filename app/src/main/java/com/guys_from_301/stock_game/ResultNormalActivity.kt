package com.guys_from_301.stock_game

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.guys_from_301.stock_game.data.*
import com.guys_from_301.stock_game.retrofit.RetrofitLevelUp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import kotlin.math.roundToInt

var totaltradeday: Int = 0
var isHistory: Boolean = false

class NewResultNormalActivity: AppCompatActivity() {

    //view 요소들
    private lateinit var tv_profitRateFinal: TextView
    private lateinit var tv_experience: TextView
    private lateinit var tv_levelup : TextView
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
    private lateinit var cht_result: LineChart
    private lateinit var ll_only_result: LinearLayout
    private lateinit var tv_only_result: TextView
    private lateinit var ll_goBackHome: LinearLayout
    private lateinit var btn_share: Button

    //Db
    private var gamenormalDb: GameNormalDB? = null
    private var gamesetDB: GameSetDB? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_normal)
        window?.statusBarColor = resources.getColor(R.color.themeFragment)

        gameend = false // 게임 전역번수 초기화
        endsuccess = false // 게임 전역번수 초기화


        //view에 연결
        tv_profitRateFinal = findViewById(R.id.tv_profitRateFinal)
        tv_experience = findViewById(R.id.tv_experience)
        tv_levelup = findViewById(R.id.tv_level_up)
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
        cht_result = findViewById(R.id.cht_result)
        ll_only_result = findViewById(R.id.ll_only_result)
        tv_only_result = findViewById(R.id.tv_only_result)
        btn_share = findViewById(R.id.btn_share)

        ll_goBackHome = findViewById(R.id.ll_goBackHome)


        // 차트
        assetchart()

        // 해당 게임 db 삭제
        deletedata()

        var gamePlayDay = totaltradeday+ tradeday
        if (isHistory) {
            tv_monthly_profit.visibility = View.INVISIBLE
            cl_monthly_profitrate_chart.visibility = View.INVISIBLE
            ll_only_result.visibility = View.INVISIBLE
            tv_only_result.visibility = View.INVISIBLE
        }
        else{
            funlevelup(
                    profileDbManager!!.getLoginId()!!,
                    profileDbManager!!.getLoginPw()!!,
                    Math.round(gamePlayDay*Math.pow(0.99, profileDbManager!!.getLevel()!!.toDouble())).toInt()
            )
            rewardByStack((tradeday * (relativeprofitrate + 100) / 500).roundToInt())
        }

        if (profitrate > 0) tv_profitRateFinal.text = "+" + per.format(profitrate) + "%"
        else tv_profitRateFinal.text = per.format(profitrate) + "%"
        tv_experience.text = "+"+Math.round(gamePlayDay*Math.pow(0.99, profileDbManager!!.getLevel()!!.toDouble())).toInt()+"exp"
        tv_rewardStack.text = "+"+dec.format((tradeday * (relativeprofitrate + 100) / 500).roundToInt())
        if(profileDbManager!!.getExp()!! + Math.round(gamePlayDay*Math.pow(0.99, profileDbManager!!.getLevel()!!.toDouble())).toInt() >= profileDbManager!!.getLevel()!! * 1000){
            tv_levelup.text = "레벨 업! " + profileDbManager!!.getLevel()!! +" -> " + (profileDbManager!!.getLevel()!! + 1).toString()
        }

        if (relativeprofitrate > 0) tv_relativeProfitRate.text = "+" + per.format(relativeprofitrate) + "%"
        else {
            tv_relativeProfitRate.text = per.format(relativeprofitrate) + "%"
            tv_relativeProfitRate.setTextAppearance(applicationContext, R.style.gameResult_relativeProfitValueDown)
            tv_yourProfit.setTextAppearance(applicationContext, R.style.gameResult_descriptionChangeDown)
        }
        tv_yourProfit.text = "당신은 " + dec.format(10000 * (100 + relativeprofitrate) / 100) + "원"
        tv_totalTax.text = "$ " + dec.format(taxtot)
        tv_totalDividend.text = "$ " + dec.format(dividendtot)
        tv_totalFee.text = "$ " + dec.format(tradecomtot)
        tv_realizeGainNLoss.text = "$ " + dec.format(profit)
        tv_tradeday.text =tradeday.toString() + "일"

        ll_goBackHome.setOnClickListener {
            GotoMainactivity()
        }

        btn_share.setOnClickListener {
            Toast.makeText(this, "히스토리 창으로 이동해서 공유하세요!", Toast.LENGTH_LONG).show()
//            val path = captureUtil.captureAndSaveViewWithKakao(findViewById(R.id.cl_shareBox), this, this)
//            shareManager.shareBinaryWithKakao(this, path)
        }

    }

    fun assetchart() {
        gamenormalDb = GameNormalDB.getInstace(this@NewResultNormalActivity)
        gamesetDB = GameSetDB.getInstace(this@NewResultNormalActivity)

        // 자산 차트 데이터
        val assetEn: ArrayList<Entry> = ArrayList()
        val inputEn: ArrayList<Entry> = ArrayList()

        val assetMonthly = gamenormalDb?.gameNormalDao()?.getAsset(accountID!!, setId)
        val inputMonthly = gamenormalDb?.gameNormalDao()?.getInput(accountID!!, setId)

        if (assetMonthly != null && inputMonthly != null) {
            for (i in assetMonthly.indices) {
                assetEn.add(Entry((i + 1).toFloat(), assetMonthly[i]))
                inputEn.add(Entry((i + 1).toFloat(), inputMonthly[i]))
                println("월: " + i.toString() + " | 자산: " + assetMonthly[i] + " | 인풋: " + inputMonthly[i])
            }
        }

        val assetDs: LineDataSet = LineDataSet(assetEn, "asset")
        val inputDs: LineDataSet = LineDataSet(inputEn, "input")

        val resultDs: ArrayList<ILineDataSet> = ArrayList()

        resultDs.add(inputDs)
        resultDs.add(assetDs)

        val resultD: LineData = LineData(resultDs)

        cht_result.data = resultD

        cht_result.isClickable = false

        // 차트 레이아웃 설정
        val legend_result : Legend = cht_result.legend
        val x_result : XAxis = cht_result.xAxis
        val yl_result : YAxis = cht_result.getAxis(YAxis.AxisDependency.LEFT)
        val yr_result : YAxis = cht_result.getAxis(YAxis.AxisDependency.RIGHT)

        cht_result.isClickable = false
        cht_result.description.isEnabled = false

        legend_result.isEnabled = true
        x_result.isEnabled = false
        yl_result.isEnabled = false
        yr_result.isEnabled = false

        assetDs.color = Color.parseColor("#F4730B") // 차트 선
        assetDs.setDrawCircles(false)
        assetDs.setDrawValues(false) // 차트 지점마다 값 표시
        assetDs.lineWidth = 1.5F
        assetDs.highLightColor = Color.parseColor("#B71C1C") // 터치 시 하이라이트
        assetDs.highlightLineWidth = 1F

        inputDs.color = Color.parseColor("#B7B1B3") // 차트 선
        inputDs.setDrawCircles(false)
        inputDs.setDrawValues(false) // 차트 지점마다 값 표시
        inputDs.lineWidth = 1.5F
        inputDs.highLightColor = Color.parseColor("#B71C1C") // 터치 시 하이라이트
        inputDs.highlightLineWidth = 1F

        // 차트 생성
        cht_result.animateXY(1, 1)
        cht_result.notifyDataSetChanged()
        resultD.notifyDataChanged()
    }

    fun deletedata() {
        gamenormalDb = GameNormalDB.getInstace(this@NewResultNormalActivity)
        gamesetDB = GameSetDB.getInstace(this@NewResultNormalActivity)

        val deleteRunnable = kotlinx.coroutines.Runnable {
            gamenormalDb?.gameNormalDao()?.deleteId(setId, accountID!!)
            gamesetDB?.gameSetDao()?.deleteId(setId, accountID!!)
            Log.d("hongz", "게임 종료 gameset"+ setId+", gamenormal 삭제")
        }
        val deleteThread = Thread(deleteRunnable)
        deleteThread.start()
    }

    fun GotoMainactivity() {
        if (isHistory) {
            finish()
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
                pr1x, pr3x, prinv1x, prinv3x, setMonthly, monthToggle, tradecomtot, 0F, dividendtot, taxtot, "nothing", item1Active, item1Length, tradeday, item2Active, item3Active, item4Active, autobuy, autoratio, auto1x, endpoint, countYear, countMonth, snpNowdays, snpNowVal, snpDiff, setId, relativeprofitrate, localdatatime, accountID!!)
        gamenormalDb?.gameNormalDao()?.insert(newGameNormalDB)//완료한 게임 히스토리 저장
    }

    fun funlevelup(u_id: String, u_pw: String, u_exp: Int) {
        val mContext: Context = this
        var funlevel_up: RetrofitLevelUp? = null
        val url = "http://stockgame.dothome.co.kr/test/levelup.php/"
        var gson: Gson = GsonBuilder()
                .setLenient()
                .create()
        //creating retrofit object
        var retrofit =
                Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
        //creating our api
        funlevel_up = retrofit.create(RetrofitLevelUp::class.java)

        funlevel_up.levelup(getHash(u_id).trim(), getHash(u_pw).trim(), u_exp)
                .enqueue(object : Callback<DATACLASS> {
                    override fun onFailure(call: Call<DATACLASS>, t: Throwable) {
                        println("---" + t.message)
                    }

                    override fun onResponse(
                            call: Call<DATACLASS>,
                            response: retrofit2.Response<DATACLASS>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            var data: DATACLASS = response.body()!!
                            if (profileDbManager!!.getLevel()!! != data?.LEVEL) {
                                islevelup = true
                            }
                            profileDbManager!!.setLevel(data?.LEVEL)
                            profileDbManager!!.setExp(data?.EXP)
                        }
                    }
                })

    }

    //도전과제 스텍 보상 함수
    fun rewardByStack(reward: Int) {
        if (!profileDbManager!!.isEmpty(this)) {
            var temp = profileDbManager!!.getMoney()!!
            if (temp >= 2000000000) {
                profileDbManager!!.setMoney(temp)
            } else {
                profileDbManager!!.setMoney(temp + reward)
                temp = profileDbManager!!.getMoney()!!
                if (temp < 0) {
                    profileDbManager!!.setMoney(0)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        profileDbManager!!.refresh(this)
    }

    override fun onPause() {
        //새로운 빈 게임 생성
        gamenormalDb = GameNormalDB.getInstace(this@NewResultNormalActivity)
        gamesetDB = GameSetDB.getInstace(this@NewResultNormalActivity)
        //초기자산값 변수
        val initialgameset = gamesetDB?.gameSetDao()?.getSetWithId(accountID+0, accountID!!)
        var gamesetList = gamesetDB?.gameSetDao()?.getPick(accountID!!,accountID!!+1,accountID!!+2,accountID!!+3)
        var existence = false   //새 게임 존재 여부
        for (game in gamesetDB?.gameSetDao()?.getPick(accountID!!,accountID!!+1,accountID!!+2,accountID!!+3)!!) {
            var emptyCount : Int = 0
            if (game.endtime == "") {
                existence = true
                emptyCount++
            }
            if(emptyCount == 2) gamesetDB?.gameSetDao()?.delete(game)
        }
        if (gamesetList != null) {
            if (!existence && gamesetList.size < 3) {
                val newGameSet = GameSet()

                if (gamesetList.size == 1) {
                    if (gamesetList.last().id.last() == '1') newGameSet.id = initialgameset?.accountId+2
                    else if (gamesetList.last().id.last() == '2') newGameSet.id = initialgameset?.accountId+3
                    else if (gamesetList.last().id.last() == '3') newGameSet.id = initialgameset?.accountId+1
                    else newGameSet.id = "예외1"
                } else if (gamesetList.size == 2) {
                    if (gamesetList[0].id.last().toInt() + gamesetList[1].id.last().toInt() == '1'.toInt()+'2'.toInt()) newGameSet.id = initialgameset?.accountId+3
                    else if (gamesetList[0].id.last().toInt() + gamesetList[1].id.last().toInt() == '1'.toInt()+'3'.toInt()) newGameSet.id = initialgameset?.accountId+2
                    else if (gamesetList[0].id.last().toInt() + gamesetList[1].id.last().toInt() == '3'.toInt()+'2'.toInt()) newGameSet.id = initialgameset?.accountId+1
                    else newGameSet.id = "예외2"
                } else if (gamesetList.size == 0){
                    newGameSet.id = accountID+1
                }
                //예전거 누적
                if (initialgameset != null) {
                    val id = newGameSet.id
                    newGameSet.setcash = initialgameset.setcash
                    newGameSet.setmonthly = initialgameset.setmonthly
                    newGameSet.setsalaryraise = initialgameset.setsalaryraise
                    newGameSet.setgamespeed = initialgameset.setgamespeed
                    newGameSet.setgamelength = initialgameset.setgamelength
                    newGameSet.accountId = initialgameset.accountId
                    Log.d("hongz", "MainActivity: 새gameset 추가 [id]: "+id)
                }
                newGameSet.endtime = ""
                gamesetDB?.gameSetDao()?.insert(newGameSet)
            }
        }
        super.onPause()
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