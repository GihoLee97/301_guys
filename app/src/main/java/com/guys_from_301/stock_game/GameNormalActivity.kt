package com.guys_from_301.stock_game

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.guys_from_301.stock_game.data.*
import com.guys_from_301.stock_game.retrofit.RetrofitLevelUp
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IndexOutOfBoundsException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

////////////////////////////////////////////////////////////////////////////////////////////////////
// 자산 관련 변수들 생성
var asset: Float = 0F // 총 자산
var cash: Float = 0F // 보유 현금
var input: Float = 0F // 총 인풋
var bought: Float = 0F // 총 매수금액
var sold: Float = 0F // 총 매도금액
var evaluation: Float = 0F // 평가금액
var profit: Float = 0F // 순손익
var profitrate: Float = 0F // 수익률
var profittot: Float = 0F // 실현 순손익
var profityear: Float = 0F // 세금 계산을 위한 연 실현수익(손실이 아닌 수익만 기록)
var localdatatime: String = "0"
var endpoint: Int = 0
var monthToggle = true  // 해당 월 투자금 지급 여부

var quant1x: Int = 0 // 1x 보유 수량
var quant3x: Int = 0 // 3x 보유 수량
var quantinv1x: Int = 0 // -1x 보유 수량
var quantinv3x: Int = 0 // -3x 보유 수량

var bought1x: Float = 0F
var bought3x: Float = 0F
var boughtinv1x: Float = 0F
var boughtinv3x: Float = 0F

var aver1x: Float = 0F // 1x 평균 단가
var aver3x: Float = 0F // 3x 평균 단가
var averinv1x: Float = 0F // inv1x 평균 단가
var averinv3x: Float = 0F // inv3x 평균 단가

var buylim1x: Float = 0F // 1x 매수 한계 수량
var buylim3x: Float = 0F // 3x 매수 한계 수량
var buyliminv1x: Float = 0F // -1x 매수 한계 수량
var buyliminv3x: Float = 0F // -3x 매수 한계 수량

var price1x: Float = 100000F // 1x 현재가
var price3x: Float = 100000F // 3x 현재가
var priceinv1x: Float = 100000F // -1x 현재가
var priceinv3x: Float = 100000F // -3x 현재가

var val1x: Float = 0F // 1x 현재가치
var val3x: Float = 0F // 3x 현재가치
var valinv1x: Float = 0F // -1x 현재가치
var valinv3x: Float = 0F // -3x 현재가치

var pr1x: Float = 0F // 1x 수익률
var pr3x: Float = 0F // 3x 수익률
var prinv1x: Float = 0F // inv1x 수익률
var prinv3x: Float = 0F // inv3x 수익률


var tradecomrate: Float = 1.001F // 거래수수료(Trade Commission): 0.1% , 거래시 차감(곱하는 방식)
var tradecomtot: Float = 0F // 거래수수료 총 합

var dividendrate: Float = 0.0153F / 4F // 배당금(VOO: 1.53% / year), 분기 단위로 현금으로 입금
var dividendtot: Float = 0F // 총 배당금
var countMonth: Int = 0 // 경과 개월 수 카운트
var countYear: Int = 0 // 플레이 한 햇수 카운트
var tax: Float = 0F // 세금
var taxtot: Float = 0F // 총 세금
// var currency: Float = 0F // 환율(현재 미반영)

private var snpNowDate: String = "yyyy-mm-dd"
var snpNowdays: Int = 0
var snpNowVal: Float = 0F
var snpDiff: Float = 0F
private val feerate1x: Float = 1F - 0.00003F / 365F // 운용수수료(VOO: 0.03%/year), 일 단위로 price decay
private val feerate3x: Float = 1F - 0.0095F / 365F // 운용수수료(UPRO: 0.95%/year), 일 단위로 price decay
private val dcp1x: Float = 0.995F // 괴리율(Discrepancy): 1x, inv1x
private val dcp3x: Float = 0.99F // 괴리율(Discrepancy):  3x, inv3x
////////////////////////////////////////////////////////////////////////////////////////////////////


// Item 값들
var item1Active: Boolean = false // 시간 되돌리기
var item2Active: Boolean = false // 뉴스 제공량
var item3Active: Boolean = false // 3x, -3x 레버리지 거래 오픈
var item4Active: Boolean = false // 3x, -3x 레버리지 거래 오픈

var item1Length: Int = 0 // 되돌릴 거래일 수
var item1Able: Int = 0 // 되돌릴 수 있는 시간 범위


// 자동 기능 변수들
var autobuy: Boolean = false // 자동 매수기능 활성여부
var autoratio: Int = 0 // 월 투자금 가운데 자동 매수 비율
var auto1x: Int = 100 // 자동 매수 금액 중 1x 매수 비율 (%)


// 버튼 클릭 판별자 생성 ///////////////////////////////////////////////////////////////////////////
var click: Boolean = false // 매수, 매도, 자동, 아이템 다이얼로그의 버튼들에 적용
var gameend: Boolean = false // 게임 종료시 적용
var endsuccess: Boolean = false // 게임 정상종료


////////////////////////////////////////////////////////////////////////////////////////////////////
var islevelup: Boolean = false
var profittotal: Float = 0F

class GameNormalActivity : AppCompatActivity() {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // 차트 데이터 및 차트 설정 변수 생성
    private val given = 1250 // 게임 시작시 주어지는 과거 데이터의 구간: 5년

    // Roomdata관련 ////////////////////////////////////////////////////////////////////////////////
    private var gameNormalDb: GameNormalDB? = null
    private var gameHistory = listOf<GameNormal>()
    private var gameSetDb: GameSetDB? = null
    private var gl: Int = 0


    // 유효구간 가운데 랜덤으로 시작 시점 산출 /////////////////////////////////////////////////////
    // 5년은 대략 1250 거래일.
    // 게임 시작 시점으로부터 5년 전, 10년 후의 데이터 확보가 가능해야함.
    // 일부 데이터는 뒤에서 약 21번째 행까지 날짜는 존재하나 값들은 null 인 경우가 존재함 -> 범위에서 30만큼 빼줌.
    // 따라서 시작시점은 총 데이터 갯수로부터 15년에 해당하는 3750 + 30을 뺀 구간에서,
    // 랜덤으로 숫자를 산출한 뒤 다시 1250을 더해준 값임.


    // 차트 데이터 생성 ////////////////////////////////////////////////////////////////////////////
    // Entry 배열 생성
    private val snpEn: ArrayList<Entry> = ArrayList()
    private val fundEn: ArrayList<Entry> = ArrayList()
    private val bondEn: ArrayList<Entry> = ArrayList()
    private val indproEn: ArrayList<Entry> = ArrayList()
    private val unemEn: ArrayList<Entry> = ArrayList()
    private val infEn: ArrayList<Entry> = ArrayList()

    // 그래프 구현을 위한 LineDataSet 생성
    private val snpDs: LineDataSet = LineDataSet(snpEn, "S&P500 Index")
    private val fundDs: LineDataSet = LineDataSet(fundEn, "Fund Rate")
    private val bondDs: LineDataSet = LineDataSet(bondEn, "bond Rate")
    private val indproDs: LineDataSet = LineDataSet(indproEn, "Ind Pro Rate")
    private val unemDs: LineDataSet = LineDataSet(unemEn, "Un Em Rate")
    private val infDs: LineDataSet = LineDataSet(infEn, "Infla Rate")

    // 그래프 data 생성 -> 최종 입력 데이터
    private val snpD: LineData = LineData(snpDs)
    private val fundD: LineData = LineData(fundDs)
    private val bondD: LineData = LineData(bondDs)
    private val indproD: LineData = LineData(indproDs)
    private val unemD: LineData = LineData(unemDs)
    private val infD: LineData = LineData(infDs)

    // 차트 데이터 추가
    private var fundIndex: Int = 0
    private var bondIndex: Int = 0
    private var indproIndex: Int = 0
    private var unemIndex: Int = 0
    private var infIndex: Int = 0

    // 차트 설정
    private val snpLineColor: String = "#1A237E" // S&P 선 색깔
    private val snpFillColor: String = "#1565C0" // S&P 채움 색깔
    private val snpHighColor: String = "#B71C1C" // S&P 하이라이트 색깔
    private val ecoLineColor: String = "#1A237E" // 경제 지표 선 색깔


    // Count 값들 //////////////////////////////////////////////////////////////////////////////////
    private var dayPlus: Int = 1 // sp(Starting Point) 이후 경과한 거래일 수
    private var fundCount: Int = 0
    private var bondCount: Int = 0
    private var indproCount: Int = 0
    private var unemCount: Int = 0
    private var infCount: Int = 0


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // 시간 되돌리기 아이템 포인트 기억
    private var item1Count: Int = 0

    private var dayPlus1 = mutableListOf<Int>()

    private var fundIndex1 = mutableListOf<Int>()
    private var bondIndex1 = mutableListOf<Int>()
    private var indproIndex1 = mutableListOf<Int>()
    private var unemIndex1 = mutableListOf<Int>()
    private var infIndex1 = mutableListOf<Int>()

    private var fundCount1 = mutableListOf<Int>()
    private var bondCount1 = mutableListOf<Int>()
    private var indproCount1 = mutableListOf<Int>()
    private var unemCount1 = mutableListOf<Int>()
    private var infCount1 = mutableListOf<Int>()

    // 자산 금융 데이터 기억
    var asset1 = mutableListOf<Float>() // 총 자산
    var cash1 = mutableListOf<Float>() // 보유 현금
    var input1 = mutableListOf<Float>() // 총 인풋
    var bought1 = mutableListOf<Float>() // 총 매수금액
    var sold1 = mutableListOf<Float>() // 총 매도금액
    var evaluation1 = mutableListOf<Float>() // 평가금액
    var profit1 = mutableListOf<Float>() // 순손익
    var profitrate1 = mutableListOf<Float>() // 수익률
    var profittot1 = mutableListOf<Float>() // 실현 순손익
    var profityear1 = mutableListOf<Float>() // 세금 계산을 위한 연 실현수익(손실이 아닌 수익만 기록)

    var quant1x1 = mutableListOf<Int>() // 1x 보유 수량
    var quant3x1 = mutableListOf<Int>() // 3x 보유 수량
    var quantinv1x1 = mutableListOf<Int>() // -1x 보유 수량
    var quantinv3x1 = mutableListOf<Int>() // -3x 보유 수량

    var bought1x1 = mutableListOf<Float>()
    var bought3x1 = mutableListOf<Float>()
    var boughtinv1x1 = mutableListOf<Float>()
    var boughtinv3x1 = mutableListOf<Float>()

    var aver1x1 = mutableListOf<Float>() // 1x 평균 단가
    var aver3x1 = mutableListOf<Float>() // 3x 평균 단가
    var averinv1x1 = mutableListOf<Float>() // inv1x 평균 단가
    var averinv3x1 = mutableListOf<Float>() // inv3x 평균 단가

    var buylim1x1 = mutableListOf<Float>() // 1x 매수 한계 수량
    var buylim3x1 = mutableListOf<Float>() // 3x 매수 한계 수량
    var buyliminv1x1 = mutableListOf<Float>() // -1x 매수 한계 수량
    var buyliminv3x1 = mutableListOf<Float>() // -3x 매수 한계 수량

    var price1x1 = mutableListOf<Float>() // 1x 현재가
    var price3x1 = mutableListOf<Float>() // 3x 현재가
    var priceinv1x1 = mutableListOf<Float>() // -1x 현재가
    var priceinv3x1 = mutableListOf<Float>() // -3x 현재가

    var val1x1 = mutableListOf<Float>() // 1x 현재가치
    var val3x1 = mutableListOf<Float>() // 3x 현재가치
    var valinv1x1 = mutableListOf<Float>() // -1x 현재가치
    var valinv3x1 = mutableListOf<Float>() // -3x 현재가치

    var pr1x1 = mutableListOf<Float>() // 1x 수익률
    var pr3x1 = mutableListOf<Float>() // 3x 수익률
    var prinv1x1 = mutableListOf<Float>() // inv1x 수익률
    var prinv3x1 = mutableListOf<Float>() // inv3x 수익률

    var tradecomtot1 = mutableListOf<Float>() // 거래수수료 총 합
    var dividendtot1 = mutableListOf<Float>() // 총 배당금
    var tax1 = mutableListOf<Float>() // 세금
    var taxtot1 = mutableListOf<Float>() // 총 세금

    var countMonth1 = mutableListOf<Int>() // 경과 개월 수 카운트
    var countYear1 = mutableListOf<Int>() // 플레이 한 햇수 카운트
    ////////////////////////////////////////////////////////////////////////////////////////////////


    // 시간관련 ////////////////////////////////////////////////////////////////////////////////////
    private val btnRefresh: Long = 5L // 버튼 Refresh 조회 간격 [ms]
    ////////////////////////////////////////////////////////////////////////////////////////////////

    // 지표 설명 관련 /////////////////////////////////////////////////////////////////////
    private lateinit var layout_fund : LinearLayout
    private lateinit var layout_bond : LinearLayout
    private lateinit var layout_indpro : LinearLayout
    private lateinit var layout_unem : LinearLayout
    private lateinit var layout_inf : LinearLayout

    // 버튼 색깔 ///////////////////////////////////////////////////////////////////////////////////
    private val colorOn: String = "#FFFFFFFF" // 버튼 선택시 색깔 White
    private val colorOff: String = "#FF808080" // 미선택 버튼 색깔 Gray

    //변수 선언 ////////////////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_normal)
        val random = Random()
        val dialog = Dialog_loading(this@GameNormalActivity)
        dialog.show()


        gameSetDb = GameSetDB.getInstace(this)
        gl = 250 * gameSetDb?.gameSetDao()?.getSetGameLength()!!

        var sp = random.nextInt((snp_date.size - gl - given - 30)) + given // Starting Point

        // 값 표준화: 시작일(sp)을 100으로
        var criteria: Float = 100 / (snp_val[sp].toFloat())




        gameNormalDb = GameNormalDB.getInstace(this)
        val startRunnable = Runnable {
            gameHistory = gameNormalDb!!.gameNormalDao().getAll()
        }
        val startThread = Thread(startRunnable)
        startThread.start()

        if (gameNormalDb?.gameNormalDao()?.getAll()?.isEmpty() != true) {
            sp = gameNormalDb?.gameNormalDao()?.getAll()?.last()?.endpoint!!
            setGamelength = gameSetDb?.gameSetDao()?.getSetGameLength()!!
            // 차트 ////////////////////////////////////////////////////////////////////////////////////
            // 차트 전역 변수 초기화
            initialize(gameNormalDb!!)
            criteria = 1F
        }
        else {
            asset = setCash // 총 자산
            cash = setCash // 보유 현금
            input = setCash // 총 인풋
            bought = 0F // 총 매수금액
            sold = 0F // 총 매도금액
            evaluation = 0F// 평가금액
            profit = 0F // 순손익
            profitrate = 0F // 수익률
            profittot = 0F // 실현 순손익
            profityear = 0F // 세금 계산을 위한 연 실현수익(손실이 아닌 수익만 기록)

            quant1x = 0 // 1x 보유 수량
            quant3x = 0 // 3x 보유 수량
            quantinv1x = 0 // -1x 보유 수량
            quantinv3x = 0 // -3x 보유 수량

            bought1x = 0F
            bought3x = 0F
            boughtinv1x = 0F
            boughtinv3x = 0F

            aver1x = 0F // 1x 평균 단가
            aver3x = 0F // 3x 평균 단가
            averinv1x = 0F // inv1x 평균 단가
            averinv3x = 0F // inv3x 평균 단가

            buylim1x = 0F // 1x 매수 한계 수량
            buylim3x = 0F // 3x 매수 한계 수량
            buyliminv1x = 0F // -1x 매수 한계 수량
            buyliminv3x = 0F // -3x 매수 한계 수량

            price1x = 100000F // 1x 현재가
            price3x = 100000F // 3x 현재가
            priceinv1x = 100000F // -1x 현재가
            priceinv3x = 100000F // -3x 현재가

            val1x = 0F // 1x 현재가치
            val3x = 0F // 3x 현재가치
            valinv1x = 0F // -1x 현재가치
            valinv3x = 0F // -3x 현재가치

            pr1x = 0F // 1x 수익률
            pr3x = 0F // 3x 수익률
            prinv1x = 0F // inv1x 수익률
            prinv3x = 0F // inv3x 수익률

            tradecomrate = 1.001F // 거래수수료(Trade Commission): 0.1% , 거래시 차감(곱하는 방식)
            tradecomtot = 0F // 거래수수료 총 합

            dividendrate = 0.0153F / 4F // 배당금(VOO: 1.53% / year), 분기 단위로 현금으로 입금
            dividendtot = 0F // 총 배당금
            countMonth = 0 // 경과 개월 수 카운트
            countYear = 0 // 플레이 한 햇수 카운트
            tax = 0F // 세금
            taxtot = 0F // 총 세금
            // var currency: Float = 0F // 환율(현재 미반영)

            snpNowDate = "yyyy-mm-dd"
            snpNowdays = 0
            snpNowVal = 0F
            snpDiff = 0F
            item1Active = false
            item2Active = false
            item3Active = false
            item4Active = false
            item1Length = 0
            item1Able = 0


            setSalaryraise = gameSetDb?.gameSetDao()?.getSetSalaryRaise()!!
            setGamespeed = gameSetDb?.gameSetDao()?.getSetGameSpeed()!!
            monthToggle = true
////////////////////////////////////////////////////////////////////////////////////////////////////


// 버튼 클릭 판별자 생성 ///////////////////////////////////////////////////////////////////////////
            click = false // 매수, 매도, 자동, 아이템 다이얼로그의 버튼들에 적용
            gameend = false // 게임 종료시 적용
////////////////////////////////////////////////////////////////////////////////////////////////////
        }

        // Button 동작
        // 지표 설명
        layout_fund = findViewById(R.id.Layout_fund)
        layout_bond = findViewById(R.id.Layout_bond)
        layout_indpro = findViewById(R.id.Layout_indpro)
        layout_unem = findViewById(R.id.Layout_unem)
        layout_inf = findViewById(R.id.Layout_inf)


        findViewById<LinearLayout>(R.id.layout_btn_fund).setOnClickListener{
            if(layout_fund.visibility == View.VISIBLE){
                layout_fund.visibility = View.GONE
            }
            else{
                layout_fund.visibility = View.VISIBLE
            }
        }
        findViewById<LinearLayout>(R.id.layout_btn_bond).setOnClickListener{
            if(layout_bond.visibility == View.VISIBLE){
                layout_bond.visibility = View.GONE
            }
            else{
                layout_bond.visibility = View.VISIBLE
            }
        }
        findViewById<LinearLayout>(R.id.layout_btn_indpro).setOnClickListener{
            if(layout_indpro.visibility == View.VISIBLE){
                layout_indpro.visibility = View.GONE
            }
            else{
                layout_indpro.visibility = View.VISIBLE
            }
        }
        findViewById<LinearLayout>(R.id.layout_btn_unem).setOnClickListener{
            if(layout_unem.visibility == View.VISIBLE){
                layout_unem.visibility = View.GONE
            }
            else{
                layout_unem.visibility = View.VISIBLE
            }
        }
        findViewById<LinearLayout>(R.id.layout_btn_inf).setOnClickListener{
            if(layout_inf.visibility == View.VISIBLE){
                layout_inf.visibility = View.GONE
            }
            else{
                layout_inf.visibility = View.VISIBLE
            }
        }


        // 매수
        findViewById<Button>(R.id.btn_buy).setOnClickListener {
            val dlgBuy = Dialog_buy(this)
            dlgBuy.start()
            click = !click /////////////////////////////////////////////////////////////////////////
        }

        //매도
        findViewById<Button>(R.id.btn_sell).setOnClickListener {
            val dlgSell = Dialog_sell(this)
            dlgSell.start()
            click = !click /////////////////////////////////////////////////////////////////////////
        }

        // 자동
        findViewById<Button>(R.id.btn_auto).setOnClickListener {
            val dlgAuto = Dialog_auto(this)
            dlgAuto.start()
            click = !click ///////////////////////////////////////////////////////////////////////
        }

        // 아이템
        findViewById<Button>(R.id.btn_item).setOnClickListener {
            var profileDb: ProflieDB? = null
            profileDb = ProflieDB.getInstace(this)

            val dlgItem = Dialog_item(this, profileDb?.profileDao()?.getMoney()!!)
//            val dlgItem = Dialog_item(this)
            dlgItem.start()
            click = !click ///////////////////////////////////////////////////////////////////////
        }


        // 차트 코루틴 시작
        CoroutineScope(Dispatchers.Default).launch {
            val job1 = launch {
                chartdata(sp, criteria)
                val addRunnable = Runnable {
                    val newGameNormalDB = GameNormal()
                    newGameNormalDB.id = localdatatime
                    newGameNormalDB.buyorsell = "시작"
                    newGameNormalDB.endpoint = sp
                    gameNormalDb?.gameNormalDao()?.insert(newGameNormalDB)
                }
                val addThread = Thread(addRunnable)
                addThread.start()
            }

            job1.join()

            val job2 = launch {
                inidraw()

            }

            job2.join()
            dialog.dismiss()


            val job3 = launch {
                nowdraw(sp, criteria, gl)
            }

            job3.join()

            val job4 = launch {
                endgame()
            }

        }
        ////////////////////////////////////////////////////////////////////////////////////////////



    }


    // 뒤로가기 눌렀을 떄 게임 종료 다이얼로그 띄움
    override fun onBackPressed() {
        val dlg_exit = Dialog_game_exit(this@GameNormalActivity)
        dlg_exit.start()
        click = !click /////////////////////////////////////////////////////////////////////////////
    }

    // 홈버튼 눌렀을 떄 게임 종료 다이얼로그 띄움(일시 정지 기능으로 사용)
    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (!gameend && !endsuccess) {
            val dlg_exit = Dialog_game_exit(this@GameNormalActivity)
            dlg_exit.start()
        }
        click = !click /////////////////////////////////////////////////////////////////////////////
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // 전역 변수 초기화
    private fun initialize(gamehistoryDb: GameNormalDB) {
        asset =  gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.assets!!// 총 자산
        cash = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.cash!! // 보유 현금
        input = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.input!! // 총 인풋
        bought = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.bought!! // 총 매수금액
        sold = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.sold!! // 총 매도금액
        evaluation = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.evaluation!!// 평가금액
        profit = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.profit!! // 순손익
        profitrate = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.profitrate!! // 수익률
        profittot = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.profittot!! // 실현 순손익
        profityear = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.profityear!! // 세금 계산을 위한 연 실현수익(손실이 아닌 수익만 기록)

        quant1x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.quant1x!! // 1x 보유 수량
        quant3x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.quant3x!! // 3x 보유 수량
        quantinv1x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.quantinv1x!! // -1x 보유 수량
        quantinv3x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.quantinv3x!! // -3x 보유 수량

        bought1x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.bought1x!!
        bought3x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.bought3x!!
        boughtinv1x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.boughtinv1x!!
        boughtinv3x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.boughtinv3x!!

        aver1x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.aver1x!! // 1x 평균 단가
        aver3x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.aver3x!! // 3x 평균 단가
        averinv1x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.averinv1x!! // inv1x 평균 단가
        averinv3x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.averinv3x!! // inv3x 평균 단가

        buylim1x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.buylim1x!! // 1x 매수 한계 수량
        buylim3x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.buylim3x!! // 3x 매수 한계 수량
        buyliminv1x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.buyliminv1x!! // -1x 매수 한계 수량
        buyliminv3x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.buyliminv3x!! // -3x 매수 한계 수량

        price1x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.price1x!! // 1x 현재가
        price3x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.price3x!! // 3x 현재가
        priceinv1x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.priceinv1x!! // -1x 현재가
        priceinv3x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.priceinv3x!! // -3x 현재가

        val1x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.val1x!! // 1x 현재가치
        val3x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.val3x!! // 3x 현재가치
        valinv1x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.valinv1x!! // -1x 현재가치
        valinv3x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.valinv3x!! // -3x 현재가치

        pr1x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.pr1x!! // 1x 수익률
        pr3x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.pr3x!! // 3x 수익률
        prinv1x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.prinv1x!! // inv1x 수익률
        prinv3x = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.prinv3x!! // inv3x 수익률

        tradecomrate = 1.001F // 거래수수료(Trade Commission): 0.1% , 거래시 차감(곱하는 방식)
        tradecomtot = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.tradecomtot!! // 거래수수료 총 합

        dividendrate = 0.0153F / 4F // 배당금(VOO: 1.53% / year), 분기 단위로 현금으로 입금
        dividendtot = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.dividendtot!! // 총 배당금
        countMonth = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.countmonth!! // 경과 개월 수 카운트
        countYear = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.countyear!! // 플레이 한 햇수 카운트
        tax = 0F // 세금
        taxtot = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.taxtot!! // 총 세금
        // var currency: Float = 0F // 환율(현재 미반영)

        snpNowDate = "yyyy-mm-dd"
        snpNowdays = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.snpnowdays!!
        snpNowVal = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.snpnowval!!
        snpDiff = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.snpdiff!!
        item1Active = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.item1active!!
        item2Active = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.item2active!!
        item3Active = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.item3active!!
        item4Active = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.item4active!!
        item1Length = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.item1length!!
        item1Able = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.item1able!!


        setSalaryraise = gameSetDb?.gameSetDao()?.getSetSalaryRaise()!!
        setGamespeed = gameSetDb?.gameSetDao()?.getSetGameSpeed()!!
        setMonthly = gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.monthly!!
        monthToggle =gamehistoryDb?.gameNormalDao()?.getAll()?.last()?.monthtoggle!!
////////////////////////////////////////////////////////////////////////////////////////////////////


// 버튼 클릭 판별자 생성 ///////////////////////////////////////////////////////////////////////////
        click = false // 매수, 매도, 자동, 아이템 다이얼로그의 버튼들에 적용
        gameend = false // 게임 종료시 적용
////////////////////////////////////////////////////////////////////////////////////////////////////
    }


    // 차트 Entry, LineData, LineDataSet 생성 및 입력, 경제지표 과거 5년 차트 생성
    private fun chartdata(start: Int, criteria: Float) {
        // Entry 배열 초기값 입력
        snpEn.add(Entry(-1250F, snp_val[start - given].toFloat() * criteria))
        fundEn.add(Entry(-1250F, fund_val[0].toFloat()))
        bondEn.add(Entry(-1250F, bond_val[0].toFloat()))
        indproEn.add(Entry(-1250F, indpro_val[0].toFloat()))
        unemEn.add(Entry(-1250F, unem_val[0].toFloat()))
        infEn.add(Entry(-1250F, inf_val[0].toFloat()))


        // 차트 설정
        snpDs.color = Color.parseColor(snpLineColor) // 차트 선
        snpDs.setDrawCircles(false)
        snpDs.setDrawValues(false) // 차트 지점마다 값 표시
        snpDs.lineWidth = 1.5F
        snpDs.fillAlpha = 80 // 차트 스커트
        snpDs.fillColor = Color.parseColor(snpFillColor)
        snpDs.setDrawFilled(true)
        snpDs.highLightColor = Color.parseColor(snpHighColor) // 터치 시 하이라이트
        snpDs.highlightLineWidth = 1F

        fundDs.color = Color.parseColor(ecoLineColor)
        fundDs.setDrawCircles(false) // 지점마다 원 표시
        fundDs.setDrawValues(false) // 지점마다 값 표시
        fundDs.lineWidth = 0.7F // 선 굵기

        bondDs.color = Color.parseColor(ecoLineColor)
        bondDs.setDrawCircles(false) // 지점마다 원 표시
        bondDs.setDrawValues(false) // 지점마다 값 표시
        bondDs.lineWidth = 0.7F // 선 굵기

        indproDs.color = Color.parseColor(ecoLineColor)
        indproDs.setDrawCircles(false) // 지점마다 원 표시
        indproDs.setDrawValues(false) // 지점마다 값 표시
        indproDs.lineWidth = 0.7F // 선 굵기

        unemDs.color = Color.parseColor(ecoLineColor)
        unemDs.setDrawCircles(false) // 지점마다 원 표시
        unemDs.setDrawValues(false) // 지점마다 값 표시
        unemDs.lineWidth = 0.7F // 선 굵기

        infDs.color = Color.parseColor(ecoLineColor)
        infDs.setDrawCircles(false) // 지점마다 원 표시
        infDs.setDrawValues(false) // 지점마다 값 표시
        infDs.lineWidth = 0.7F // 선 굵기


        for (i in 0..(given - 1)) {
            snpD.addEntry(
                Entry(
                    (i + 1 - given).toFloat(),
                    snp_val[start - given + 1 + i].toFloat() * criteria
                ),
                0
            )

            var sf = SimpleDateFormat("yyyy-MM-dd") // 날짜 형식
            var snpDate = snp_date[start - given + 1 + i]
            var snpDateSf = sf.parse(snpDate) // 기준 일자 (SNP 날짜)

            var fundDate = fund_date[fundIndex]
            var fundDateSf = sf.parse(fundDate)
            var bondDate = bond_date[bondIndex]
            var bondDateSf = sf.parse(bondDate)
            var indproDate = indpro_date[indproIndex]
            var indproDateSf = sf.parse(indproDate)
            var unemDate = unem_date[unemIndex]
            var unemDateSf = sf.parse(unemDate)
            var infDate = inf_date[infIndex]
            var infDateSf = sf.parse(infDate)

            var fundC = snpDateSf.time - fundDateSf.time
            var bondC = snpDateSf.time - bondDateSf.time
            var indproC = snpDateSf.time - indproDateSf.time
            var unemC = snpDateSf.time - unemDateSf.time
            var infC = snpDateSf.time - infDateSf.time

            while (fundC > 0) {
                fundIndex += 1
                fundDate = fund_date[fundIndex]
                fundDateSf = sf.parse(fundDate)
                fundC = snpDateSf.time - fundDateSf.time
            }
            fundCount += 1
            fundD.addEntry(Entry((fundCount - 1250).toFloat(), fund_val[fundIndex].toFloat()), 0)
            println("fund date : $fundDate")

            while (bondC > 0) {
                bondIndex += 1
                bondDate = bond_date[bondIndex]
                bondDateSf = sf.parse(bondDate)
                bondC = snpDateSf.time - bondDateSf.time
            }
            bondCount += 1
            bondD.addEntry(Entry((bondCount - 1250).toFloat(), bond_val[bondIndex].toFloat()), 0)

            while (indproC > 0) {
                indproIndex += 1
                indproDate = indpro_date[indproIndex]
                indproDateSf = sf.parse(indproDate)
                indproC = snpDateSf.time - indproDateSf.time
            }
            indproCount += 1
            indproD.addEntry(
                Entry(
                    (indproCount - 1250).toFloat(),
                    indpro_val[indproIndex - 1].toFloat()
                ), 0
            )

            while (unemC > 0) {
                unemIndex += 1
                unemDate = unem_date[unemIndex]
                unemDateSf = sf.parse(unemDate)
                unemC = snpDateSf.time - indproDateSf.time
            }
            unemCount += 1
            unemD.addEntry(
                Entry((unemCount - 1250).toFloat(), unem_val[unemIndex - 1].toFloat()),
                0
            )

            while (infC > 0) {
                infIndex += 1
                infDate = inf_date[infIndex]
                infDateSf = sf.parse(infDate)
                infC = snpDateSf.time - infDateSf.time
            }
            infCount += 1
            infD.addEntry(Entry((infCount - 1250).toFloat(), inf_val[infIndex - 1].toFloat()), 0)
            println("인덱스 : $i")
        }
        println("Fund count : $fundCount")
        println("랜덤넘버 COUNT : " + start.toString() + " | " + "시작 날짜 : " + snp_date[start])
        localdatatime = snp_date[start]
        // 차트 데이터 생성 끝 /////////////////////////////////////////////////////////////


        // layout 에 배치된 lineChart 에 데이터 연결
        findViewById<LineChart>(R.id.cht_snp).data = snpD
        findViewById<LineChart>(R.id.cht_fund).data = fundD
        findViewById<LineChart>(R.id.cht_bond).data = bondD
        findViewById<LineChart>(R.id.cht_indpro).data = indproD
        findViewById<LineChart>(R.id.cht_unem).data = unemD
        findViewById<LineChart>(R.id.cht_inf).data = infD


        // 차트 레이아웃 생성 //////////////////////////////////////////////////////////////
        runOnUiThread {
            // 차트 생성
            findViewById<LineChart>(R.id.cht_snp).animateXY(1, 1)
            findViewById<LineChart>(R.id.cht_fund).animateXY(1, 1)
            findViewById<LineChart>(R.id.cht_bond).animateXY(1, 1)
            findViewById<LineChart>(R.id.cht_indpro).animateXY(1, 1)
            findViewById<LineChart>(R.id.cht_unem).animateXY(1, 1)
            findViewById<LineChart>(R.id.cht_inf).animateXY(1, 1)
            findViewById<LineChart>(R.id.cht_fund).setTouchEnabled(false)
            findViewById<LineChart>(R.id.cht_bond).setTouchEnabled(false)
            findViewById<LineChart>(R.id.cht_indpro).setTouchEnabled(false)
            findViewById<LineChart>(R.id.cht_unem).setTouchEnabled(false)
            findViewById<LineChart>(R.id.cht_inf).setTouchEnabled(false)
            findViewById<LineChart>(R.id.cht_fund).setVisibleXRangeMaximum(1250F)
            findViewById<LineChart>(R.id.cht_bond).setVisibleXRangeMaximum(1250F)
            findViewById<LineChart>(R.id.cht_indpro).setVisibleXRangeMaximum(1250F)
            findViewById<LineChart>(R.id.cht_unem).setVisibleXRangeMaximum(1250F)
            findViewById<LineChart>(R.id.cht_inf).setVisibleXRangeMaximum(1250F)
        }

        // 추가분 반영
        findViewById<LineChart>(R.id.cht_snp).notifyDataSetChanged()
        findViewById<LineChart>(R.id.cht_fund).notifyDataSetChanged()
        findViewById<LineChart>(R.id.cht_bond).notifyDataSetChanged()
        findViewById<LineChart>(R.id.cht_indpro).notifyDataSetChanged()
        findViewById<LineChart>(R.id.cht_unem).notifyDataSetChanged()
        findViewById<LineChart>(R.id.cht_inf).notifyDataSetChanged()

        snpD.notifyDataChanged()
        fundD.notifyDataChanged()
        bondD.notifyDataChanged()
        unemD.notifyDataChanged()
        infD.notifyDataChanged()
    }

    // S&P500 과거 5년간의 차트 그리기
    private suspend fun inidraw() {
        for (i in 0..(given - 1)) {
            delay(1)
            findViewById<LineChart>(R.id.cht_snp).setVisibleXRangeMaximum(125F) // 125 거래일 ~ 6개월
            findViewById<LineChart>(R.id.cht_snp).moveViewToX((i + 1 - given).toFloat())
        }

        runOnUiThread {
            findViewById<TextView>(R.id.tv_notification).text = "알림: 게임 시작...!"
        }
    }

    // Real time 차트 생성 및 현재 데이터 저장
    private suspend fun nowdraw(start: Int, criteria: Float, gl: Int) {
        // 게임 진행속도 설정
        var oneday: Long = 0L

        fundIndex1.add(0)
        bondIndex1.add(0)
        indproIndex1.add(0)
        unemIndex1.add(0)
        infIndex1.add(0)

        fundCount1.add(0)
        bondCount1.add(0)
        indproCount1.add(0)
        unemCount1.add(0)
        infCount1.add(0)

        asset1.add(0F)
        cash1.add(0F)
        input1.add(0F)
        bought1.add(0F)
        sold1.add(0F)
        evaluation1.add(0F)
        profit1.add(0F)
        profitrate1.add(0F)
        profittot1.add(0F)
        profityear1.add(0F)

        quant1x1.add(0)
        quant3x1.add(0)
        quantinv1x1.add(0)
        quantinv3x1.add(0)

        bought1x1.add(0F)
        bought3x1.add(0F)
        boughtinv1x1.add(0F)
        boughtinv3x1.add(0F)
        aver1x1.add(0F)
        aver3x1.add(0F)
        averinv1x1.add(0F)
        averinv3x1.add(0F)

        buylim1x1.add(0F)
        buylim3x1.add(0F)
        buyliminv1x1.add(0F)
        buyliminv3x1.add(0F)

        price1x1.add(0F)
        price3x1.add(0F)
        priceinv1x1.add(0F)
        priceinv3x1.add(0F)

        val1x1.add(0F)
        val3x1.add(0F)
        valinv1x1.add(0F)
        valinv3x1.add(0F)

        pr1x1.add(0F)
        pr3x1.add(0F)
        prinv1x1.add(0F)
        prinv3x1.add(0F)

        tradecomtot1.add(0F)
        dividendtot1.add(0F)
        countMonth1.add(0)
        countYear1.add(0)
        tax1.add(0F)
        taxtot1.add(0F)

        // 날짜
        var sf = SimpleDateFormat("yyyy-MM-dd") // 날짜 형식
        val snpSP = snp_date[start] // 시작일
        val snpSP_sf = sf.parse(snpSP)
        var preMonth = snpSP_sf.month // 이전 달 저장
        var preYear = snpSP_sf.year // 이전 년도 저장

        // 자동 매수
        var monthly: Float = setMonthly // 월급
        var auto1xratio: Float = 0F
        var auto3xratio: Float = 0F
        var auto1xquant: Int = 0
        var auto3xquant: Int = 0

        while (true) {
            //dialog_result = Dialog_result(this)
            if (!gameend) {
                if (!click) {
                    if (dayPlus <= gl && !item1Active) {

                        var snpDate = snp_date[start + dayPlus]
                        endpoint = start + dayPlus
                        localdatatime = snpDate
                        var snpDate_sf = sf.parse(snpDate) // 기준 일자 (SNP 날짜)


                        var fundDate = fund_date[fundIndex]
                        var fundDate_sf = sf.parse(fundDate)
                        var bondDate = bond_date[bondIndex]
                        var bondDate_sf = sf.parse(bondDate)
                        var indproDate = indpro_date[indproIndex]
                        var indproDate_sf = sf.parse(indproDate)
                        var unemDate = unem_date[unemIndex]
                        var unemDate_sf = sf.parse(unemDate)
                        var infDate = inf_date[infIndex]
                        var infDate_sf = sf.parse(infDate)

                        var fund_c = snpDate_sf.time - fundDate_sf.time
                        var bond_c = snpDate_sf.time - bondDate_sf.time
                        var indpro_c = snpDate_sf.time - indproDate_sf.time
                        var unem_c = snpDate_sf.time - unemDate_sf.time
                        var inf_c = snpDate_sf.time - infDate_sf.time

                        snpD.addEntry(
                            Entry(
                                dayPlus.toFloat(),
                                snp_val[start + dayPlus].toFloat() * criteria
                            ), 0
                        )


                        while (fund_c > 0) {
                            fundIndex += 1
                            fundDate = fund_date[fundIndex]
                            fundDate_sf = sf.parse(fundDate)
                            fund_c = snpDate_sf.time - fundDate_sf.time
                        }
                        fundCount += 1
                        fundD.addEntry(
                            Entry(
                                (fundCount - 1250).toFloat(),
                                fund_val[fundIndex].toFloat()
                            ), 0
                        )
                        println("SNP x값 : " + dayPlus.toString() + "|" + "Fund x값 : " + (fundCount - 1250).toString())

                        while (bond_c > 0) {
                            bondIndex += 1
                            bondDate = bond_date[bondIndex]
                            bondDate_sf = sf.parse(bondDate)
                            bond_c = snpDate_sf.time - bondDate_sf.time
                        }
                        bondCount += 1
                        bondD.addEntry(
                            Entry(
                                (bondCount - 1250).toFloat(),
                                bond_val[bondIndex].toFloat()
                            ), 0
                        )

                        while (indpro_c > 0) {
                            indproIndex += 1
                            indproDate = indpro_date[indproIndex]
                            indproDate_sf = sf.parse(indproDate)
                            indpro_c = snpDate_sf.time - indproDate_sf.time
                        }
                        indproCount += 1
                        indproD.addEntry(
                            Entry(
                                (indproCount - 1250).toFloat(),
                                indpro_val[indproIndex - 1].toFloat()
                            ), 0
                        )

                        while (unem_c > 0) {
                            unemIndex += 1
                            unemDate = unem_date[unemIndex]
                            unemDate_sf = sf.parse(unemDate)
                            unem_c = snpDate_sf.time - indproDate_sf.time
                        }
                        unemCount += 1
                        unemD.addEntry(
                            Entry(
                                (unemCount - 1250).toFloat(),
                                unem_val[unemIndex - 1].toFloat()
                            ), 0
                        )

                        while (inf_c > 0) {
                            infIndex += 1
                            infDate = inf_date[infIndex]
                            infDate_sf = sf.parse(infDate)
                            inf_c = snpDate_sf.time - infDate_sf.time
                        }
                        infCount += 1
                        infD.addEntry(
                            Entry(
                                (infCount - 1250).toFloat(),
                                inf_val[infIndex - 1].toFloat()
                            ), 0
                        )

                        println("S&P : " + snp_date[start + dayPlus] + " | " + "Fund : " + fund_date[fundIndex] + " | " + "Bond : " + bond_date[bondIndex] + " | " + "IndPro : " + indpro_date[indproIndex - 1] + " | " + "UnEm : " + unem_date[unemIndex - 1] + " | " + "Inf : " + inf_date[infIndex - 1])


                        runOnUiThread {
                            // 차트에 DataSet 리프레쉬 통보
                            findViewById<LineChart>(R.id.cht_snp).notifyDataSetChanged()
                            findViewById<LineChart>(R.id.cht_fund).notifyDataSetChanged()
                            findViewById<LineChart>(R.id.cht_bond).notifyDataSetChanged()
                            findViewById<LineChart>(R.id.cht_indpro).notifyDataSetChanged()
                            findViewById<LineChart>(R.id.cht_unem).notifyDataSetChanged()
                            findViewById<LineChart>(R.id.cht_inf).notifyDataSetChanged()
                            snpD.notifyDataChanged()
                            fundD.notifyDataChanged()
                            bondD.notifyDataChanged()
                            unemD.notifyDataChanged()
                            infD.notifyDataChanged()

                            // 차트 축 최대 범위 설정
                            findViewById<LineChart>(R.id.cht_snp).setVisibleXRangeMaximum(125F) // X축 범위: 125 거래일(~6개월)
                            findViewById<LineChart>(R.id.cht_fund).setVisibleXRangeMaximum(1250F)
                            findViewById<LineChart>(R.id.cht_bond).setVisibleXRangeMaximum(1250F)
                            findViewById<LineChart>(R.id.cht_indpro).setVisibleXRangeMaximum(1250F)
                            findViewById<LineChart>(R.id.cht_unem).setVisibleXRangeMaximum(1250F)
                            findViewById<LineChart>(R.id.cht_inf).setVisibleXRangeMaximum(1250F)

                            // 차트 축 이동
                            findViewById<LineChart>(R.id.cht_snp).moveViewToX(dayPlus.toFloat())
                            findViewById<LineChart>(R.id.cht_fund).moveViewToX(dayPlus.toFloat())
                            findViewById<LineChart>(R.id.cht_bond).moveViewToX(dayPlus.toFloat())
                            findViewById<LineChart>(R.id.cht_indpro).moveViewToX(dayPlus.toFloat())
                            findViewById<LineChart>(R.id.cht_unem).moveViewToX(dayPlus.toFloat())
                            findViewById<LineChart>(R.id.cht_inf).moveViewToX(dayPlus.toFloat())
                        }


                        // 값 OutPut ///////////////////////////////////////////////////////////////
                        // 현재 값 저장
                        snpNowDate = snp_date[start + dayPlus]
                        snpNowdays = dayPlus
                        snpNowVal = snp_val[start + dayPlus].toFloat()
                        snpDiff =
                            snp_val[start + dayPlus].toFloat() / snp_val[start + dayPlus - 1].toFloat() - 1F
                        println("현재 날짜 : $snpNowDate | 현재 경과 거래일 : $snpNowdays | 현재 S&P 500 지수 값 : $snpNowVal | 등락 : $snpDiff")


                        // 월 투자금 입금 시작
                        if (snpDate_sf.date >= 10 && !monthToggle) {
                            cash += monthly // 월 투자금 현금으로 입금
                            input += monthly // 총 input 최신화
                            runOnUiThread {
                                findViewById<TextView>(R.id.tv_notification).text =
                                    "알림: 월 투자금 " + dec.format(monthly) + " 원이 입금되었습니다"
                            }

                            ////////////////////////////////////////////////////////////////////////
                            // 자동 매수
                            if (autobuy) {
                                auto1xratio = autoratio * auto1x / 10000F
                                auto3xratio = autoratio * (100 - auto1x) / 10000F

                                if (auto1xratio!=0F) {
                                    if (((monthly * auto1xratio / (price1x * tradecomrate)).roundToInt() - (monthly * auto1xratio / (price1x * tradecomrate)) > 0)) {
                                        auto1xquant = (monthly * auto1xratio / (price1x * tradecomrate)).roundToInt() - 1
                                    }
                                    else {
                                        auto1xquant = (monthly * auto1xratio / (price1x * tradecomrate)).roundToInt()
                                    }

                                    cash -= auto1xquant * price1x * tradecomrate
                                    tradecomtot += auto1xquant * price1x * (tradecomrate - 1F)
                                    quant1x += auto1xquant // 수량 반영
                                    bought1x += price1x * auto1xquant // 매입금 반영
                                    aver1x = bought1x / quant1x // 평균단가 반영

                                    // 자동 매수내역 DB 연동
                                    val addRunnable = Runnable {
                                        val newGameNormalDB = GameNormal()
                                        newGameNormalDB.id = localdatatime
                                        newGameNormalDB.buyorsell = "매수"
                                        newGameNormalDB.select = 1
                                        newGameNormalDB.price = price1x
                                        newGameNormalDB.volume = auto1xquant * price1x
                                        newGameNormalDB.quant = auto1xquant
                                        newGameNormalDB.tradecom = auto1xquant * price1x * (tradecomrate - 1F)
                                        newGameNormalDB.cash = cash
                                        gameNormalDb?.gameNormalDao()?.insert(newGameNormalDB)
                                    }
                                    val addThread = Thread(addRunnable)
                                    addThread.start()
                                }


                                if (auto3xratio!=0F) {
                                    if (((monthly * auto3xratio / (price3x * tradecomrate)).roundToInt() - (monthly * auto3xratio / (price3x * tradecomrate)) > 0)) {
                                        auto3xquant = (monthly * auto3xratio / (price3x * tradecomrate)).roundToInt() - 1
                                    }
                                    else {
                                        auto3xquant = (monthly * auto3xratio / (price3x * tradecomrate)).roundToInt()
                                    }

                                    cash -= auto3xquant * price3x * tradecomrate
                                    tradecomtot += auto3xquant * price3x * (tradecomrate - 1F)
                                    quant3x += auto3xquant
                                    bought3x += price3x * auto3xquant
                                    aver3x = bought3x / quant3x

                                    // 자동 매수내역 DB 연동
                                    val addRunnable = Runnable {
                                        val newGameNormalDB = GameNormal()
                                        newGameNormalDB.id = localdatatime
                                        newGameNormalDB.buyorsell = "매수"
                                        newGameNormalDB.select = 2
                                        newGameNormalDB.price = price3x
                                        newGameNormalDB.volume = auto3xquant * price3x
                                        newGameNormalDB.quant = auto3xquant
                                        newGameNormalDB.tradecom = auto3xquant * price3x * (tradecomrate - 1F)
                                        newGameNormalDB.cash = cash
                                        gameNormalDb?.gameNormalDao()?.insert(newGameNormalDB)
                                    }
                                    val addThread = Thread(addRunnable)
                                    addThread.start()
                                }
                            }
                            ////////////////////////////////////////////////////////////////////////
                            monthToggle = true // 해당 월 투자금 지급 여부
                        }
                        // 월 투자금 입금 코드 종료


                        // 월 바뀜 시 실행할 코드 시작
                        if (snpDate_sf.month != preMonth) {

                            monthToggle = false

                            if (countMonth < 12) {
                                countMonth += 1
                            } else {
                                countYear += 1
                                countMonth = 1
                            }

                            preMonth = snpDate_sf.month

                            // 설정한 게임 플레이 기간에 도달
                            if (countYear > setGamelength) {
                                gameend = true
                                endsuccess = true
                                break
                                break
                                break
                                break
                            } else if (snpDate_sf.month == 2 || snpDate_sf.month == 5 || snpDate_sf.month == 8 || snpDate_sf.month == 11) {
                                if (val1x > 1F) {
                                    cash += val1x * dividendrate * 0.846F // 15.4% 세금 공제
                                    dividendtot += val1x * dividendrate
                                    taxtot += val1x * dividendrate * 0.154F // 세금 납부 내역 최신화
                                    runOnUiThread {
                                        findViewById<TextView>(R.id.tv_notification).text =
                                            "알림: 배당금 " + dec.format(val1x * dividendrate) + " 원이 입금되었습니다"
                                    }
                                }
                            } else if ((snpDate_sf.month == 1) && (countYear >= 1)) {
                                monthly = setMonthly * (Math.pow((1F + setSalaryraise / 100F).toDouble(), countYear.toDouble())).toFloat() // 연봉 인상으로 월 투자금 증가
                                runOnUiThread {
                                    findViewById<TextView>(R.id.tv_notification).text =
                                        "알림: 연봉 " + per.format(setSalaryraise) + "% 인상으로 월 투자금이 " + dec.format(
                                            monthly
                                        ) + " 원이 되었습니다"
                                }
                            }

                            if (tax > 0F && snpDate_sf.month != 0) {
                                if (cash >= tax) {
                                    cash -= tax
                                    runOnUiThread {
                                        findViewById<TextView>(R.id.tv_notification).text =
                                            "알림: 세금 " + dec.format(tax) + " 원이 납부 되었습니다"
                                    }
                                    taxtot += tax
                                    delay(10L) // UI에서의 알림메시지 출력시간 확보
                                    tax = 0F
                                } else {
                                    tax *= (1.05F)
                                    runOnUiThread {
                                        findViewById<TextView>(R.id.tv_notification).text =
                                            "알림: 미납 세금이 매달 가산됩니다"
                                    }
                                }
                            }

                            // 해가 바뀔 시
                            if (snpDate_sf.year != preYear) {
                                tax += (profityear - 2500000F) * 0.22F // 양도 소득세: 연간 실현수익에서 250만원 공제 후 22% 부과
                                profityear = 0F // 연 수익률 초기화
                                if (cash >= tax && tax > 0F) {
                                    runOnUiThread {
                                        findViewById<TextView>(R.id.tv_notification).text =
                                            "알림: 세금 " + dec.format(tax) + " 원이 납부됨"
                                    }
                                    cash -= tax
                                    taxtot += tax
                                    delay(10L) // UI에서의 알림메시지 출력시간 확보
                                    tax = 0F
                                } else if (cash < tax && tax > 0F) {
                                    runOnUiThread {
                                        findViewById<TextView>(R.id.tv_notification).text =
                                            "알림: 현금이 부족해 세금을 낼 수 없습니다"
                                    }
                                }
                                preYear = snpDate_sf.year
                            }
                            println("월 바뀜!")
                        }
                        // 월 바뀜 시 실행할 코드 종료


                        // 자산 관련 데이터 계산 시작
                        // 운용수수료 및 괴리율 반영
                        price1x *= (feerate1x + snpDiff * dcp1x) //
                        price3x *= (feerate3x + 3 * snpDiff * dcp3x) //
                        priceinv1x *= (feerate1x - snpDiff * dcp1x) //
                        priceinv3x *= (feerate3x - 3 * snpDiff * dcp3x) //

                        val1x = quant1x * price1x //
                        val3x = quant3x * price3x //
                        valinv1x = quantinv1x * priceinv1x //
                        valinv3x = quantinv3x * priceinv3x //

                        buylim1x = (cash / (price1x * tradecomrate)) //
                        buylim3x = (cash / (price3x * tradecomrate)) //
                        buyliminv1x = (cash / (priceinv1x * tradecomrate)) //
                        buyliminv3x = (cash / (priceinv3x * tradecomrate)) //
                        println("매수 한계 : $buylim1x | $buylim3x | $buyliminv1x | $buyliminv3x")


                        // 수익률
                        if (aver1x != 0F) {
                            pr1x = 100F * (price1x - aver1x) / aver1x
                        } else {
                            pr1x = 0F
                        }

                        if (aver3x != 0F) {
                            pr3x = 100F * (price3x - aver3x) / aver3x
                        } else {
                            pr3x = 0F
                        }

                        if (averinv1x != 0F) {
                            prinv1x = 100F * (priceinv1x - averinv1x) / averinv1x
                        } else {
                            prinv1x = 0F
                        }

                        if (averinv3x != 0F) {
                            prinv3x = 100F * (priceinv3x - averinv3x) / averinv3x
                        } else {
                            prinv3x = 0F
                        }


                        evaluation = val1x + val3x + valinv1x + valinv3x // 평가금액
                        profitrate = 100F * profit / input
                        profit = evaluation + cash - input // input 대비 수익률
                        asset = cash + evaluation // 총 자산
                        // 자산 관련 데이터 계산 종료


                        runOnUiThread {
                            // 경과 기간 최신화
                            findViewById<TextView>(R.id.tv_year).text = "${countYear} 년"
                            findViewById<TextView>(R.id.tv_month).text = "${countMonth} 개월"

                            // 자산 가치 관련 값들 최신화
                            findViewById<TextView>(R.id.tv_asset).text =
                                "총 자산 : " + dec.format(asset) + " 원"
                            findViewById<TextView>(R.id.tv_cash).text =
                                "현금 : " + dec.format(cash) + " 원"
                            findViewById<TextView>(R.id.tv_evaluation).text =
                                "평가금액 : " + dec.format(evaluation) + " 원"
                            findViewById<TextView>(R.id.tv_profit).text =
                                "순손익 : " + dec.format(profit) + " 원"
                            findViewById<TextView>(R.id.tv_profitrate).text =
                                "수익률 : " + per.format(profitrate) + " %"
                            findViewById<TextView>(R.id.tv_dividend).text =
                                "배당금 : " + dec.format(dividendtot) + " 원"
                            findViewById<TextView>(R.id.tv_taxtot).text =
                                "세금 : " + dec.format(taxtot) + " 원"
                            findViewById<TextView>(R.id.tv_profityear).text =
                                "당해 실현 수익 : " + dec.format(profityear) + " 원"
                            findViewById<TextView>(R.id.tv_tradecomtot).text =
                                "수수료 : " + dec.format(tradecomtot) + " 원"

                            findViewById<TextView>(R.id.tv_price1x).text =
                                dec.format(price1x) + " 원"
                            findViewById<TextView>(R.id.tv_price3x).text =
                                dec.format(price3x) + " 원"
                            findViewById<TextView>(R.id.tv_priceinv1x).text =
                                dec.format(priceinv1x) + " 원"
                            findViewById<TextView>(R.id.tv_priceinv3x).text =
                                dec.format(priceinv3x) + " 원"

                            findViewById<TextView>(R.id.tv_aver1x).text = dec.format(aver1x) + " 원"
                            findViewById<TextView>(R.id.tv_aver3x).text = dec.format(aver3x) + " 원"
                            findViewById<TextView>(R.id.tv_averinv1x).text =
                                dec.format(averinv1x) + " 원"
                            findViewById<TextView>(R.id.tv_averinv3x).text =
                                dec.format(averinv3x) + " 원"

                            findViewById<TextView>(R.id.tv_quant1x).text =
                                dec.format(quant1x) + " 주"
                            findViewById<TextView>(R.id.tv_quant3x).text =
                                dec.format(quant3x) + " 주"
                            findViewById<TextView>(R.id.tv_quantinv1x).text =
                                dec.format(quantinv1x) + " 주"
                            findViewById<TextView>(R.id.tv_quantinv3x).text =
                                dec.format(quantinv3x) + " 주"

                            findViewById<TextView>(R.id.tv_val1x).text = dec.format(val1x) + " 원"
                            findViewById<TextView>(R.id.tv_val3x).text = dec.format(val3x) + " 원"
                            findViewById<TextView>(R.id.tv_valinv1x).text =
                                dec.format(valinv1x) + " 원"
                            findViewById<TextView>(R.id.tv_valinv3x).text =
                                dec.format(valinv3x) + " 원"

                            findViewById<TextView>(R.id.tv_profit1x).text = per.format(pr1x) + " %"
                            findViewById<TextView>(R.id.tv_profit3x).text = per.format(pr3x) + " %"
                            findViewById<TextView>(R.id.tv_profitinv1x).text =
                                per.format(prinv1x) + " %"
                            findViewById<TextView>(R.id.tv_profitinv3x).text =
                                per.format(prinv3x) + " %"

                        }

                        ////////////////////////////////////////////////////////////////////////////
                        // 시간역행 아이템용 데이터 저장 시작
                        println("현재 dayPlus: " + dayPlus.toString())

                        item1Able = dayPlus - 2 // 시간 역행 가능한 범위

                        fundIndex1.add(fundIndex)
                        bondIndex1.add(bondIndex)
                        indproIndex1.add(indproIndex)
                        unemIndex1.add(unemIndex)
                        infIndex1.add(infIndex)
                        println("현재 fundIndex Size: " + fundIndex1.size.toString()+" | 현재 asset1 Size: " + asset1.size.toString())

                        fundCount1.add(fundCount)
                        bondCount1.add(bondCount)
                        indproCount1.add(indproCount)
                        unemCount1.add(unemCount)
                        infCount1.add(infCount)

                        asset1.add(asset)
                        cash1.add(cash)
                        input1.add(input)
                        bought1.add(bought)
                        sold1.add(sold)
                        evaluation1.add(evaluation)
                        profit1.add(profit)
                        profitrate1.add(profitrate)
                        profittot1.add(profittot)
                        profityear1.add(profityear)

                        quant1x1.add(quant1x)
                        quant3x1.add(quant3x)
                        quantinv1x1.add(quantinv1x)
                        quantinv3x1.add(quantinv3x)

                        bought1x1.add(bought1x)
                        bought3x1.add(bought3x)
                        boughtinv1x1.add(boughtinv1x)
                        boughtinv3x1.add(boughtinv3x)
                        aver1x1.add(aver1x)
                        aver3x1.add(aver3x)
                        averinv1x1.add(averinv1x)
                        averinv3x1.add(averinv3x)

                        buylim1x1.add(buylim1x)
                        buylim3x1.add(buylim3x)
                        buyliminv1x1.add(buyliminv1x)
                        buyliminv3x1.add(buyliminv3x)

                        price1x1.add(price1x)
                        price3x1.add(price3x)
                        priceinv1x1.add(priceinv1x)
                        priceinv3x1.add(priceinv3x)

                        val1x1.add(val1x)
                        val3x1.add(val3x)
                        valinv1x1.add(valinv1x)
                        valinv3x1.add(valinv3x)

                        pr1x1.add(pr1x)
                        pr3x1.add(pr3x)
                        prinv1x1.add(prinv1x)
                        prinv3x1.add(prinv3x)

                        tradecomtot1.add(tradecomtot)
                        dividendtot1.add(dividendtot)
                        countMonth1.add(countMonth)
                        countYear1.add(countYear)
                        tax1.add(tax)
                        taxtot1.add(taxtot)

                        println("현재 fundIndex Size: " + fundIndex1.size.toString()+" | 현재 asset1 Size: " + asset1.size.toString())
                        // 시간역행 아이템용 데이터 저장 종료
                        ////////////////////////////////////////////////////////////////////////////

                        ////////////////////////////////////////////////////////////////////////////
                        // 시간 진행 속도 조절 아이템
                        if (setGamespeed == 0) {
                            oneday = 1995L // 0.5 day/sec
                        } else if (setGamespeed == 1) {
                            oneday = 995L // 1 day/sec
                        } else if (setGamespeed == 2) {
                            oneday = 495L // 2 day/sec
                        } else if (setGamespeed == 3) {
                            oneday = 1000L / 3L - 5L // 3 day/sec
                        } else if (setGamespeed == 4) {
                            oneday = 245L // 4 day/sec
                        } else if (setGamespeed == 5) {
                            oneday = 195L // 5 day/sec
                        } else if (setGamespeed == 6) {
                            oneday = 115L // 8 day/sec
                        } else if (setGamespeed == 7) {
                            oneday = 95L // 10 day/sec
                        }
                        ////////////////////////////////////////////////////////////////////////////

                        dayPlus += 1 // 시간 진행
                        delay(oneday) // 게임 진행 속도 조절
                    }


                    // 정상 진행 시 코드 종료, 시간 역행 아이템 사용시 실행할 코드 시작
                    else if (dayPlus <= gl && item1Active) {
                        runOnUiThread {
                            findViewById<Button>(R.id.btn_buy).isEnabled = false
                            findViewById<Button>(R.id.btn_sell).isEnabled = false
                            findViewById<Button>(R.id.btn_auto).isEnabled = false
                            findViewById<Button>(R.id.btn_item).isEnabled = false
                            findViewById<TextView>(R.id.tv_notification).text = "알림: 시간역행을 통해 $item1Length 거래일 전으로 돌아가는 중..."
                        }
                        dayPlus -= 1
                        var timeTrableStart = dayPlus
                        println("시간역행 시작 : " + dayPlus.toString())


                        try {
                            while (dayPlus > (timeTrableStart - item1Length)) {

                                snpD.removeEntry(dayPlus.toFloat(), 0)
                                fundD.removeEntry(dayPlus.toFloat(), 0)
                                bondD.removeEntry(dayPlus.toFloat(), 0)
                                indproD.removeEntry(dayPlus.toFloat(), 0)
                                unemD.removeEntry(dayPlus.toFloat(), 0)
                                infD.removeEntry(dayPlus.toFloat(), 0)

                                fundIndex1.removeAt(dayPlus)
                                bondIndex1.removeAt(dayPlus)
                                indproIndex1.removeAt(dayPlus)
                                unemIndex1.removeAt(dayPlus)
                                infIndex1.removeAt(dayPlus)

                                fundCount1.removeAt(dayPlus)
                                bondCount1.removeAt(dayPlus)
                                indproCount1.removeAt(dayPlus)
                                unemCount1.removeAt(dayPlus)
                                infCount1.removeAt(dayPlus)

                                asset1.removeAt(dayPlus)
                                cash1.removeAt(dayPlus)
                                input1.removeAt(dayPlus)
                                bought1.removeAt(dayPlus)
                                sold1.removeAt(dayPlus)
                                evaluation1.removeAt(dayPlus)
                                profit1.removeAt(dayPlus)
                                profitrate1.removeAt(dayPlus)
                                profittot1.removeAt(dayPlus)
                                profityear1.removeAt(dayPlus)

                                quant1x1.removeAt(dayPlus)
                                quant3x1.removeAt(dayPlus)
                                quantinv1x1.removeAt(dayPlus)
                                quantinv3x1.removeAt(dayPlus)

                                bought1x1.removeAt(dayPlus)
                                bought3x1.removeAt(dayPlus)
                                boughtinv1x1.removeAt(dayPlus)
                                boughtinv3x1.removeAt(dayPlus)
                                aver1x1.removeAt(dayPlus)
                                aver3x1.removeAt(dayPlus)
                                averinv1x1.removeAt(dayPlus)
                                averinv3x1.removeAt(dayPlus)

                                buylim1x1.removeAt(dayPlus)
                                buylim3x1.removeAt(dayPlus)
                                buyliminv1x1.removeAt(dayPlus)
                                buyliminv3x1.removeAt(dayPlus)

                                price1x1.removeAt(dayPlus)
                                price3x1.removeAt(dayPlus)
                                priceinv1x1.removeAt(dayPlus)
                                priceinv3x1.removeAt(dayPlus)

                                val1x1.removeAt(dayPlus)
                                val3x1.removeAt(dayPlus)
                                valinv1x1.removeAt(dayPlus)
                                valinv3x1.removeAt(dayPlus)

                                pr1x1.removeAt(dayPlus)
                                pr3x1.removeAt(dayPlus)
                                prinv1x1.removeAt(dayPlus)
                                prinv3x1.removeAt(dayPlus)

                                tradecomtot1.removeAt(dayPlus)
                                dividendtot1.removeAt(dayPlus)
                                countMonth1.removeAt(dayPlus)
                                countYear1.removeAt(dayPlus)
                                tax1.removeAt(dayPlus)
                                taxtot1.removeAt(dayPlus)

                                println("시간역행 중 : " + dayPlus.toString())

                                fundIndex = fundIndex1[dayPlus - 1]
                                bondIndex = bondIndex1[dayPlus - 1]
                                indproIndex = indproIndex1[dayPlus - 1]
                                unemIndex = unemIndex1[dayPlus - 1]
                                infIndex = infIndex1[dayPlus - 1]

                                fundCount = fundCount1[dayPlus - 1]
                                bondCount = bondCount1[dayPlus - 1]
                                indproCount = indproCount1[dayPlus - 1]
                                unemCount = unemCount1[dayPlus - 1]
                                infCount = infCount1[dayPlus - 1]

                                asset = asset1[dayPlus - 1] // 총 자산
                                cash = cash1[dayPlus - 1]// 보유 현금
                                input = input1[dayPlus - 1] // 총 인풋
                                bought = bought1[dayPlus - 1] // 총 매수금액
                                sold = sold1[dayPlus - 1]// 총 매도금액
                                evaluation = evaluation1[dayPlus - 1] // 평가금액
                                profit = profit1[dayPlus - 1] // 순손익
                                profitrate = profitrate1[dayPlus - 1] // 수익률
                                profittot = profittot1[dayPlus - 1] // 실현 순손익
                                profityear = profityear1[dayPlus - 1] // 세금 계산을 위한 연 실현수익(손실이 아닌 수익만 기록)

                                quant1x = quant1x1[dayPlus - 1] // 1x 보유 수량
                                quant3x = quant3x1[dayPlus - 1] // 3x 보유 수량
                                quantinv1x = quantinv1x1[dayPlus - 1] // -1x 보유 수량
                                quantinv3x = quantinv3x1[dayPlus - 1] // -3x 보유 수량

                                bought1x = bought1x1[dayPlus - 1]
                                bought3x = bought3x1[dayPlus - 1]
                                boughtinv1x = boughtinv1x1[dayPlus - 1]
                                boughtinv3x = boughtinv3x1[dayPlus - 1]
                                aver1x = aver1x1[dayPlus - 1] // 1x 평균 단가
                                aver3x = aver3x1[dayPlus - 1] // 3x 평균 단가
                                averinv1x = averinv1x1[dayPlus - 1] // inv1x 평균 단가
                                averinv3x = averinv3x1[dayPlus - 1] // inv3x 평균 단가

                                buylim1x = buylim1x1[dayPlus - 1] // 1x 매수 한계 수량
                                buylim3x = buylim3x1[dayPlus - 1] // 3x 매수 한계 수량
                                buyliminv1x = buyliminv1x1[dayPlus - 1] // -1x 매수 한계 수량
                                buyliminv3x = buyliminv3x1[dayPlus - 1] // -3x 매수 한계 수량

                                price1x = price1x1[dayPlus - 1] // 1x 현재가
                                price3x = price3x1[dayPlus - 1] // 3x 현재가
                                priceinv1x = priceinv1x1[dayPlus - 1] // -1x 현재가
                                priceinv3x = priceinv3x1[dayPlus - 1] // -3x 현재가

                                val1x = val1x1[dayPlus - 1] // 1x 현재가치
                                val3x = val3x1[dayPlus - 1] // 3x 현재가치
                                valinv1x = valinv1x1[dayPlus - 1] // -1x 현재가치
                                valinv3x = valinv3x1[dayPlus - 1]// -3x 현재가치

                                pr1x = pr1x1[dayPlus - 1] // 1x 수익률
                                pr3x = pr3x1[dayPlus - 1] // 3x 수익률
                                prinv1x = prinv1x1[dayPlus - 1] // inv1x 수익률
                                prinv3x = prinv3x1[dayPlus - 1] // inv3x 수익률

                                tradecomtot = tradecomtot1[dayPlus - 1] // 거래수수료 총 합
                                dividendtot = dividendtot1[dayPlus - 1] // 총 배당금
                                countMonth = countMonth1[dayPlus - 1] // 경과 개월 수 카운트
                                countYear = countYear1[dayPlus - 1] // 플레이 한 햇수 카운트
                                tax = tax1[dayPlus - 1] // 세금
                                taxtot = taxtot1[dayPlus - 1] // 총 세금


                                runOnUiThread {
                                    // 차트에 DataSet 리프레쉬 통보
                                    findViewById<LineChart>(R.id.cht_snp).notifyDataSetChanged()
                                    findViewById<LineChart>(R.id.cht_fund).notifyDataSetChanged()
                                    findViewById<LineChart>(R.id.cht_bond).notifyDataSetChanged()
                                    findViewById<LineChart>(R.id.cht_indpro).notifyDataSetChanged()
                                    findViewById<LineChart>(R.id.cht_unem).notifyDataSetChanged()
                                    findViewById<LineChart>(R.id.cht_inf).notifyDataSetChanged()

                                    snpD.notifyDataChanged()
                                    fundD.notifyDataChanged()
                                    bondD.notifyDataChanged()
                                    indproD.notifyDataChanged()
                                    unemD.notifyDataChanged()
                                    infD.notifyDataChanged()

                                    // 차트 축 최대 범위 설정
                                    findViewById<LineChart>(R.id.cht_snp).setVisibleXRangeMaximum(125F) // X축 범위: 125 거래일(~6개월)
                                    findViewById<LineChart>(R.id.cht_fund).setVisibleXRangeMaximum(1250F)
                                    findViewById<LineChart>(R.id.cht_bond).setVisibleXRangeMaximum(1250F)
                                    findViewById<LineChart>(R.id.cht_indpro).setVisibleXRangeMaximum(1250F)
                                    findViewById<LineChart>(R.id.cht_unem).setVisibleXRangeMaximum(1250F)
                                    findViewById<LineChart>(R.id.cht_inf).setVisibleXRangeMaximum(1250F)

                                    // 차트 축 이동
                                    findViewById<LineChart>(R.id.cht_snp).moveViewToX(dayPlus.toFloat())
                                    findViewById<LineChart>(R.id.cht_fund).moveViewToX((dayPlus).toFloat())
                                    findViewById<LineChart>(R.id.cht_bond).moveViewToX((dayPlus).toFloat())
                                    findViewById<LineChart>(R.id.cht_indpro).moveViewToX((dayPlus).toFloat())
                                    findViewById<LineChart>(R.id.cht_unem).moveViewToX((dayPlus).toFloat())
                                    findViewById<LineChart>(R.id.cht_inf).moveViewToX((dayPlus).toFloat())

                                    // 경과 기간 최신화
                                    findViewById<TextView>(R.id.tv_year).text = "${countYear} 년"
                                    findViewById<TextView>(R.id.tv_month).text = "${countMonth} 개월"

                                    // 자산 가치 관련 값들 최신화
                                    findViewById<TextView>(R.id.tv_asset).text =
                                        "총 자산 : " + dec.format(asset) + " 원"
                                    findViewById<TextView>(R.id.tv_cash).text =
                                        "현금 : " + dec.format(cash) + " 원"
                                    findViewById<TextView>(R.id.tv_evaluation).text =
                                        "평가금액 : " + dec.format(evaluation) + " 원"
                                    findViewById<TextView>(R.id.tv_profit).text =
                                        "순손익 : " + dec.format(profit) + " 원"
                                    findViewById<TextView>(R.id.tv_profitrate).text =
                                        "수익률 : " + per.format(profitrate) + " %"
                                    findViewById<TextView>(R.id.tv_dividend).text =
                                        "배당금 : " + dec.format(dividendtot) + " 원"
                                    findViewById<TextView>(R.id.tv_taxtot).text =
                                        "세금 : " + dec.format(taxtot) + " 원"
                                    findViewById<TextView>(R.id.tv_profityear).text =
                                        "당해 실현 수익 : " + dec.format(profityear) + " 원"
                                    findViewById<TextView>(R.id.tv_tradecomtot).text =
                                        "수수료 : " + dec.format(tradecomtot) + " 원"

                                    findViewById<TextView>(R.id.tv_price1x).text =
                                        dec.format(price1x) + " 원"
                                    findViewById<TextView>(R.id.tv_price3x).text =
                                        dec.format(price3x) + " 원"
                                    findViewById<TextView>(R.id.tv_priceinv1x).text =
                                        dec.format(priceinv1x) + " 원"
                                    findViewById<TextView>(R.id.tv_priceinv3x).text =
                                        dec.format(priceinv3x) + " 원"

                                    findViewById<TextView>(R.id.tv_aver1x).text =
                                        dec.format(aver1x) + " 원"
                                    findViewById<TextView>(R.id.tv_aver3x).text =
                                        dec.format(aver3x) + " 원"
                                    findViewById<TextView>(R.id.tv_averinv1x).text =
                                        dec.format(averinv1x) + " 원"
                                    findViewById<TextView>(R.id.tv_averinv3x).text =
                                        dec.format(averinv3x) + " 원"

                                    findViewById<TextView>(R.id.tv_quant1x).text =
                                        dec.format(quant1x) + " 주"
                                    findViewById<TextView>(R.id.tv_quant3x).text =
                                        dec.format(quant3x) + " 주"
                                    findViewById<TextView>(R.id.tv_quantinv1x).text =
                                        dec.format(quantinv1x) + " 주"
                                    findViewById<TextView>(R.id.tv_quantinv3x).text =
                                        dec.format(quantinv3x) + " 주"

                                    findViewById<TextView>(R.id.tv_val1x).text =
                                        dec.format(val1x) + " 원"
                                    findViewById<TextView>(R.id.tv_val3x).text =
                                        dec.format(val3x) + " 원"
                                    findViewById<TextView>(R.id.tv_valinv1x).text =
                                        dec.format(valinv1x) + " 원"
                                    findViewById<TextView>(R.id.tv_valinv3x).text =
                                        dec.format(valinv3x) + " 원"

                                    findViewById<TextView>(R.id.tv_profit1x).text =
                                        per.format(pr1x) + " %"
                                    findViewById<TextView>(R.id.tv_profit3x).text =
                                        per.format(pr3x) + " %"
                                    findViewById<TextView>(R.id.tv_profitinv1x).text =
                                        per.format(prinv1x) + " %"
                                    findViewById<TextView>(R.id.tv_profitinv3x).text =
                                        per.format(prinv3x) + " %"
                                }
                                delay(70L) // UI 쓰레드 동작 시간 확보, UI 쓰레드 문제로 강제 종료시 이 값을 증가시킬것
                                dayPlus -= 1
                            }
                        } catch (e: IndexOutOfBoundsException) {
                            e.printStackTrace()
                            gameend = true
                            break
                            break
                            break
                            break
                        }

                        ////////////////////////////////////////////////////////////////////////////
                        fundIndex = fundIndex1[dayPlus - 1]
                        bondIndex = bondIndex1[dayPlus - 1]
                        indproIndex = indproIndex1[dayPlus - 1]
                        unemIndex = unemIndex1[dayPlus - 1]
                        infIndex = infIndex1[dayPlus - 1]

                        fundCount = fundCount1[dayPlus - 1]
                        bondCount = bondCount1[dayPlus - 1]
                        indproCount = indproCount1[dayPlus - 1]
                        unemCount = unemCount1[dayPlus - 1]
                        infCount = infCount1[dayPlus - 1]

                        asset = asset1[dayPlus - 1] // 총 자산
                        cash = cash1[dayPlus - 1]// 보유 현금
                        input = input1[dayPlus - 1] // 총 인풋
                        bought = bought1[dayPlus - 1] // 총 매수금액
                        sold = sold1[dayPlus - 1]// 총 매도금액
                        evaluation = evaluation1[dayPlus - 1] // 평가금액
                        profit = profit1[dayPlus - 1] // 순손익
                        profitrate = profitrate1[dayPlus - 1] // 수익률
                        profittot = profittot1[dayPlus - 1] // 실현 순손익
                        profityear = profityear1[dayPlus - 1] // 세금 계산을 위한 연 실현수익(손실이 아닌 수익만 기록)

                        quant1x = quant1x1[dayPlus - 1] // 1x 보유 수량
                        quant3x = quant3x1[dayPlus - 1] // 3x 보유 수량
                        quantinv1x = quantinv1x1[dayPlus - 1] // -1x 보유 수량
                        quantinv3x = quantinv3x1[dayPlus - 1] // -3x 보유 수량

                        bought1x = bought1x1[dayPlus - 1]
                        bought3x = bought3x1[dayPlus - 1]
                        boughtinv1x = boughtinv1x1[dayPlus - 1]
                        boughtinv3x = boughtinv3x1[dayPlus - 1]
                        aver1x = aver1x1[dayPlus - 1] // 1x 평균 단가
                        aver3x = aver3x1[dayPlus - 1] // 3x 평균 단가
                        averinv1x = averinv1x1[dayPlus - 1] // inv1x 평균 단가
                        averinv3x = averinv3x1[dayPlus - 1] // inv3x 평균 단가

                        buylim1x = buylim1x1[dayPlus - 1] // 1x 매수 한계 수량
                        buylim3x = buylim3x1[dayPlus - 1] // 3x 매수 한계 수량
                        buyliminv1x = buyliminv1x1[dayPlus - 1] // -1x 매수 한계 수량
                        buyliminv3x = buyliminv3x1[dayPlus - 1] // -3x 매수 한계 수량

                        price1x = price1x1[dayPlus - 1] // 1x 현재가
                        price3x = price3x1[dayPlus - 1] // 3x 현재가
                        priceinv1x = priceinv1x1[dayPlus - 1] // -1x 현재가
                        priceinv3x = priceinv3x1[dayPlus - 1] // -3x 현재가

                        val1x = val1x1[dayPlus - 1] // 1x 현재가치
                        val3x = val3x1[dayPlus - 1] // 3x 현재가치
                        valinv1x = valinv1x1[dayPlus - 1] // -1x 현재가치
                        valinv3x = valinv3x1[dayPlus - 1]// -3x 현재가치

                        pr1x = pr1x1[dayPlus - 1] // 1x 수익률
                        pr3x = pr3x1[dayPlus - 1] // 3x 수익률
                        prinv1x = prinv1x1[dayPlus - 1] // inv1x 수익률
                        prinv3x = prinv3x1[dayPlus - 1] // inv3x 수익률

                        tradecomtot = tradecomtot1[dayPlus - 1] // 거래수수료 총 합
                        dividendtot = dividendtot1[dayPlus - 1] // 총 배당금
                        countMonth = countMonth1[dayPlus - 1] // 경과 개월 수 카운트
                        countYear = countYear1[dayPlus - 1] // 플레이 한 햇수 카운트
                        tax = tax1[dayPlus - 1] // 세금
                        taxtot = taxtot1[dayPlus - 1] // 총 세금

                        if (sf.parse(snp_date[start + dayPlus - 1]).month < 1) {
                            monthly = setMonthly * (Math.pow((1F + setSalaryraise / 100F).toDouble(), (countYear-1).toDouble())).toFloat() // 월 투자금 정상화
                        }
                        else {
                            monthly = setMonthly * (Math.pow((1F + setSalaryraise / 100F).toDouble(), countYear.toDouble())).toFloat() // 월 투자금 정상화
                        }

                        snpD.removeEntry(dayPlus.toFloat(), 0)
                        fundD.removeEntry(dayPlus.toFloat(), 0)
                        bondD.removeEntry(dayPlus.toFloat(), 0)
                        indproD.removeEntry(dayPlus.toFloat(), 0)
                        unemD.removeEntry(dayPlus.toFloat(), 0)
                        infD.removeEntry(dayPlus.toFloat(), 0)

                        fundIndex1.removeAt(dayPlus)
                        bondIndex1.removeAt(dayPlus)
                        indproIndex1.removeAt(dayPlus)
                        unemIndex1.removeAt(dayPlus)
                        infIndex1.removeAt(dayPlus)

                        fundCount1.removeAt(dayPlus)
                        bondCount1.removeAt(dayPlus)
                        indproCount1.removeAt(dayPlus)
                        unemCount1.removeAt(dayPlus)
                        infCount1.removeAt(dayPlus)

                        asset1.removeAt(dayPlus)
                        cash1.removeAt(dayPlus)
                        input1.removeAt(dayPlus)
                        bought1.removeAt(dayPlus)
                        sold1.removeAt(dayPlus)
                        evaluation1.removeAt(dayPlus)
                        profit1.removeAt(dayPlus)
                        profitrate1.removeAt(dayPlus)
                        profittot1.removeAt(dayPlus)
                        profityear1.removeAt(dayPlus)

                        quant1x1.removeAt(dayPlus)
                        quant3x1.removeAt(dayPlus)
                        quantinv1x1.removeAt(dayPlus)
                        quantinv3x1.removeAt(dayPlus)

                        bought1x1.removeAt(dayPlus)
                        bought3x1.removeAt(dayPlus)
                        boughtinv1x1.removeAt(dayPlus)
                        boughtinv3x1.removeAt(dayPlus)
                        aver1x1.removeAt(dayPlus)
                        aver3x1.removeAt(dayPlus)
                        averinv1x1.removeAt(dayPlus)
                        averinv3x1.removeAt(dayPlus)

                        buylim1x1.removeAt(dayPlus)
                        buylim3x1.removeAt(dayPlus)
                        buyliminv1x1.removeAt(dayPlus)
                        buyliminv3x1.removeAt(dayPlus)

                        price1x1.removeAt(dayPlus)
                        price3x1.removeAt(dayPlus)
                        priceinv1x1.removeAt(dayPlus)
                        priceinv3x1.removeAt(dayPlus)

                        val1x1.removeAt(dayPlus)
                        val3x1.removeAt(dayPlus)
                        valinv1x1.removeAt(dayPlus)
                        valinv3x1.removeAt(dayPlus)

                        pr1x1.removeAt(dayPlus)
                        pr3x1.removeAt(dayPlus)
                        prinv1x1.removeAt(dayPlus)
                        prinv3x1.removeAt(dayPlus)

                        tradecomtot1.removeAt(dayPlus)
                        dividendtot1.removeAt(dayPlus)
                        countMonth1.removeAt(dayPlus)
                        countYear1.removeAt(dayPlus)
                        tax1.removeAt(dayPlus)
                        taxtot1.removeAt(dayPlus)

                        println("시간역행 중 : " + dayPlus.toString())
                        ////////////////////////////////////////////////////////////////////////////


                        runOnUiThread {
                            findViewById<LineChart>(R.id.cht_snp).notifyDataSetChanged()
                            findViewById<LineChart>(R.id.cht_fund).notifyDataSetChanged()
                            findViewById<LineChart>(R.id.cht_bond).notifyDataSetChanged()
                            findViewById<LineChart>(R.id.cht_indpro).notifyDataSetChanged()
                            findViewById<LineChart>(R.id.cht_unem).notifyDataSetChanged()
                            findViewById<LineChart>(R.id.cht_inf).notifyDataSetChanged()

                            snpD.notifyDataChanged()
                            fundD.notifyDataChanged()
                            bondD.notifyDataChanged()
                            indproD.notifyDataChanged()
                            unemD.notifyDataChanged()
                            infD.notifyDataChanged()

                            findViewById<LineChart>(R.id.cht_snp).setVisibleXRangeMaximum(125F)
                            findViewById<LineChart>(R.id.cht_fund).setVisibleXRangeMaximum(1250F)
                            findViewById<LineChart>(R.id.cht_bond).setVisibleXRangeMaximum(1250F)
                            findViewById<LineChart>(R.id.cht_indpro).setVisibleXRangeMaximum(1250F)
                            findViewById<LineChart>(R.id.cht_unem).setVisibleXRangeMaximum(1250F)
                            findViewById<LineChart>(R.id.cht_inf).setVisibleXRangeMaximum(1250F)

                            findViewById<LineChart>(R.id.cht_snp).moveViewToX((dayPlus - 1).toFloat())
                            findViewById<LineChart>(R.id.cht_fund).moveViewToX((dayPlus - 1).toFloat())
                            findViewById<LineChart>(R.id.cht_bond).moveViewToX((dayPlus - 1).toFloat())
                            findViewById<LineChart>(R.id.cht_indpro).moveViewToX((dayPlus - 1).toFloat())
                            findViewById<LineChart>(R.id.cht_unem).moveViewToX((dayPlus - 1).toFloat())
                            findViewById<LineChart>(R.id.cht_inf).moveViewToX((dayPlus - 1).toFloat())

                            // 경과 기간 최신화
                            findViewById<TextView>(R.id.tv_year).text = "${countYear} 년"
                            findViewById<TextView>(R.id.tv_month).text = "${countMonth} 개월"

                            // 자산 가치 관련 값들 최신화
                            findViewById<TextView>(R.id.tv_asset).text =
                                "총 자산 : " + dec.format(asset) + " 원"
                            findViewById<TextView>(R.id.tv_cash).text =
                                "현금 : " + dec.format(cash) + " 원"
                            findViewById<TextView>(R.id.tv_evaluation).text =
                                "평가금액 : " + dec.format(evaluation) + " 원"
                            findViewById<TextView>(R.id.tv_profit).text =
                                "순손익 : " + dec.format(profit) + " 원"
                            findViewById<TextView>(R.id.tv_profitrate).text =
                                "수익률 : " + per.format(profitrate) + " %"
                            findViewById<TextView>(R.id.tv_dividend).text =
                                "배당금 : " + dec.format(dividendtot) + " 원"
                            findViewById<TextView>(R.id.tv_taxtot).text =
                                "세금 : " + dec.format(taxtot) + " 원"
                            findViewById<TextView>(R.id.tv_profityear).text =
                                "당해 실현 수익 : " + dec.format(profityear) + " 원"
                            findViewById<TextView>(R.id.tv_tradecomtot).text =
                                "수수료 : " + dec.format(tradecomtot) + " 원"

                            findViewById<TextView>(R.id.tv_price1x).text =
                                dec.format(price1x) + " 원"
                            findViewById<TextView>(R.id.tv_price3x).text =
                                dec.format(price3x) + " 원"
                            findViewById<TextView>(R.id.tv_priceinv1x).text =
                                dec.format(priceinv1x) + " 원"
                            findViewById<TextView>(R.id.tv_priceinv3x).text =
                                dec.format(priceinv3x) + " 원"

                            findViewById<TextView>(R.id.tv_aver1x).text =
                                dec.format(aver1x) + " 원"
                            findViewById<TextView>(R.id.tv_aver3x).text =
                                dec.format(aver3x) + " 원"
                            findViewById<TextView>(R.id.tv_averinv1x).text =
                                dec.format(averinv1x) + " 원"
                            findViewById<TextView>(R.id.tv_averinv3x).text =
                                dec.format(averinv3x) + " 원"

                            findViewById<TextView>(R.id.tv_quant1x).text =
                                dec.format(quant1x) + " 주"
                            findViewById<TextView>(R.id.tv_quant3x).text =
                                dec.format(quant3x) + " 주"
                            findViewById<TextView>(R.id.tv_quantinv1x).text =
                                dec.format(quantinv1x) + " 주"
                            findViewById<TextView>(R.id.tv_quantinv3x).text =
                                dec.format(quantinv3x) + " 주"

                            findViewById<TextView>(R.id.tv_val1x).text =
                                dec.format(val1x) + " 원"
                            findViewById<TextView>(R.id.tv_val3x).text =
                                dec.format(val3x) + " 원"
                            findViewById<TextView>(R.id.tv_valinv1x).text =
                                dec.format(valinv1x) + " 원"
                            findViewById<TextView>(R.id.tv_valinv3x).text =
                                dec.format(valinv3x) + " 원"

                            findViewById<TextView>(R.id.tv_profit1x).text =
                                per.format(pr1x) + " %"
                            findViewById<TextView>(R.id.tv_profit3x).text =
                                per.format(pr3x) + " %"
                            findViewById<TextView>(R.id.tv_profitinv1x).text =
                                per.format(prinv1x) + " %"
                            findViewById<TextView>(R.id.tv_profitinv3x).text =
                                per.format(prinv3x) + " %"

                            findViewById<TextView>(R.id.tv_notification).text = "알림: 시간역행을 통해 $item1Length 거래일 전으로 돌아왔습니다..!"
                        }

                        delay(100L) // UI 쓰레드 동작 시간 확보

                        println("시간역행 끝 : " + dayPlus.toString())
                        item1Length = 0
                        item1Active = false //

                        runOnUiThread {
                            findViewById<Button>(R.id.btn_buy).isEnabled = true
                            findViewById<Button>(R.id.btn_sell).isEnabled = true
                            findViewById<Button>(R.id.btn_auto).isEnabled = true
                            findViewById<Button>(R.id.btn_item).isEnabled = true
                        }
                    }
                    // 시간 역행 아이템 사용 시 코드 종료, 설정한 게임 플레이 시간 도달 시 실핼할 코드 시작
                    else {
                        println("게임 끝")
                        endsuccess = true
                        break
                        break
                        break
                    }
                    // 설정한 게임 플레이 시간 도달 시 실핼할 코드 종료
                }
            }
            else {
                println("게임 끝")
                break
            }
            delay(btnRefresh)
        }
    }

    // 게임 종료 시 결과창으로 이동
    private fun endgame() {
        if (endsuccess) {
            var profileDb: ProflieDB? = null
            profileDb = ProflieDB.getInstace(this@GameNormalActivity)
            funlevelup(
                profileDb?.profileDao()?.getLoginid()!!,
                profileDb?.profileDao()?.getLoginpw()!!,
                100
            )
            val deleteRunnable = Runnable {
                gameNormalDb?.gameNormalDao()?.deleteAll()
                gameSetDb?.gameSetDao()?.deleteAll()
            }
            val deleteThread = Thread(deleteRunnable)
            deleteThread.start()
            val intent = Intent(this@GameNormalActivity, ResultNormalActivity::class.java)
            startActivity(intent)
        }
        else {
            val intent = Intent(this@GameNormalActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    fun funlevelup(u_id: String, u_pw: String, u_exp: Int) {
        val mContext: Context = this
        var profileDb: ProflieDB? = null
        var funlevel_up: RetrofitLevelUp? = null
        profileDb = ProflieDB.getInstace(mContext)
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
                        profileDb = ProflieDB?.getInstace(mContext)
                        if (profileDb?.profileDao()?.getLevel()!! != data?.LEVEL) {
                            islevelup = true
                        }
                        val newProfile = Profile()
                        newProfile.id = profileDb?.profileDao()?.getId()?.toLong()
                        newProfile.nickname = profileDb?.profileDao()?.getNickname()!!
                        newProfile.history = profileDb?.profileDao()?.getHistory()!!
                        newProfile.level = data?.LEVEL!!
                        newProfile.exp = data?.EXP!!
                        newProfile.login = profileDb?.profileDao()?.getLogin()!!
                        newProfile.money = profileDb?.profileDao()?.getMoney()!!
                        newProfile.profit = profileDb?.profileDao()?.getProfit()!!
                        newProfile.login_id = profileDb?.profileDao()?.getLoginid()!!
                        newProfile.login_pw = profileDb?.profileDao()?.getLoginpw()!!
                        profileDb?.profileDao()?.update(newProfile)
                        profileDb = ProflieDB?.getInstace(mContext)
                    }
                }
            })

    }
}