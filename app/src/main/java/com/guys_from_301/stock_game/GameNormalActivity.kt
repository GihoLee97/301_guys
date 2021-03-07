package com.guys_from_301.stock_game

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.guys_from_301.stock_game.data.*
import com.guys_from_301.stock_game.retrofit.RetrofitLevelUp
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
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
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


const val START_SNP_VAL:Float = 100F
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
var monthToggle = 1
var setId: String = ""
var tradeday: Int = 0//누적 거래일에 더할 거래일
var marketrate: Float = 0F//시장 수익률

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

var price1x: Float = 100F // 1x 현재가
var price3x: Float = 100F // 3x 현재가
var priceinv1x: Float = 100F // -1x 현재가
var priceinv3x: Float = 100F // -3x 현재가

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

//도전과제 관련 변수들
var questAchieved: ArrayList<Quest> = ArrayList()
var surplusCount: Int = 0
var pastAsset: Float = 0F
var isSafeCompleted: Boolean = false

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

var value1now: Int = 0 // 초기화 시 ItemDB와 연동


// 자동 기능 변수들
var autobuy: Boolean = false // 자동 매수기능 활성여부
var autoratio: Int = 0 // 월 투자금 가운데 자동 매수 비율
var auto1x: Int = 0 // 매수할 종목 선택 0: 1x, 1: 3x, 2: inv1x, 3: inv3x


// 버튼 클릭 판별자 생성 ///////////////////////////////////////////////////////////////////////////
var click: Boolean = false // 매수, 매도, 자동, 아이템 다이얼로그의 버튼들에 적용
var gameend: Boolean = false // 게임 종료시 적용
var endsuccess: Boolean = false // 게임 정상종료


////////////////////////////////////////////////////////////////////////////////////////////////////
var islevelup: Boolean = false
var profittotal: Float = 0F


//뉴스 관련/////////////////////////
var newssave = mutableListOf<Int>()

//이벤트 횟수(종료 다이알로그 중복 생성 방지용)/////////////////////////
var eventCount: Int = 0

var comlast: Float = 0F

class GameNormalActivity : AppCompatActivity() {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // 차트 데이터 및 차트 설정 변수 생성
    private val given = 1250 // 게임 시작시 주어지는 과거 데이터의 구간: 5년

    // Roomdata관련 ////////////////////////////////////////////////////////////////////////////////
    private var gameNormalDb: GameNormalDB? = null
    private var questDb: QuestDB? = null
    private var questList: List<Quest>? = null
    private var relativeQuestList: List<Quest>? = null
    private var surplusQuestList: List<Quest>? = null
    private var gameSetDb: GameSetDB? = null
    private var gl: Int = 0

    private var itemDb: ItemDB? = null
    val mContext: Context = this


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
    private val snpLineColor: String = "#F4730B" // S&P 선 색깔
    private val snpFillColor: String = "#1565C0" // S&P 채움 색깔
    private val snpHighColor: String = "#B71C1C" // S&P 하이라이트 색깔
    private val ecoLineColor: String = "#F4730B" // 경제 지표 선 색깔


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

    private lateinit var btn_gameBack: Button
    private lateinit var tv_gameName: TextView
    private lateinit var tv_assetTot: TextView
    private lateinit var tv_timeleft: TextView
    private lateinit var tv_profitRate: TextView
    private lateinit var tv_profitTot: TextView

    private lateinit var tv_price1x: TextView
    private lateinit var tv_quant1x: TextView
    private lateinit var v_diff1x: View
    private lateinit var tv_diff1x: TextView

    private lateinit var tv_price3x: TextView
    private lateinit var tv_quant3x: TextView
    private lateinit var v_diff3x: View
    private lateinit var tv_diff3x: TextView
    private lateinit var ll_hide3x: LinearLayout // 언락시 visible
    private lateinit var ll_lock3x: LinearLayout // 언락시 gone
    private lateinit var tv_priceinv1x: TextView
    private lateinit var tv_quantinv1x: TextView
    private lateinit var v_diffinv1x: View
    private lateinit var tv_diffinv1x: TextView

    private lateinit var tv_priceinv3x: TextView
    private lateinit var tv_quantinv3x: TextView
    private lateinit var v_diffinv3x: View
    private lateinit var tv_diffinv3x: TextView
    private lateinit var ll_hideinv3x: LinearLayout // 언락시 visible
    private lateinit var ll_lockinv3x: LinearLayout // 언락시 gone

    private lateinit var tv_fund: TextView
    private lateinit var tv_bond: TextView
    private lateinit var tv_indpro: TextView
    private lateinit var tv_unem: TextView
    private lateinit var tv_inf: TextView

    private lateinit var cl_fund: ConstraintLayout
    private lateinit var cl_bond: ConstraintLayout
    private lateinit var cl_indpro: ConstraintLayout
    private lateinit var cl_unem: ConstraintLayout
    private lateinit var cl_inf: ConstraintLayout

    private lateinit var tv_ecoindex: TextView
    private lateinit var tv_ecoval: TextView

    private lateinit var cht_snp: LineChart
    private lateinit var cht_eco: LineChart

    private lateinit var tv_snp: TextView
    private lateinit var tv_news: TextView
    private lateinit var ll_item: LinearLayout
    private lateinit var ll_trade: LinearLayout
    private lateinit var tv_trade: TextView
    private lateinit var tv_autoon: TextView
    private lateinit var pb_fatigue: ProgressBar

    private var ecoselect: Int = 0 // 0: fund, 1: bond, 2: indpro, 3: unem, 4: inf

    private var monthlytemp: Float = 0F
    private var divlast: Float = 0F
    private var taxlast: Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_normal)
        val random = Random()
//       val dialog = Dialog_loading(this@GameNormalActivity)
//        dialog.show()
        val dialog = Dialog_loading_tip(this@GameNormalActivity)
        dialog.show()

        // view 연결
        btn_gameBack = findViewById(R.id.btn_gameBack)
        tv_gameName = findViewById(R.id.tv_gameName)
        tv_assetTot = findViewById(R.id.tv_assetTot)
        tv_timeleft = findViewById(R.id.tv_timeleft)
        tv_profitRate = findViewById(R.id.tv_profitRate)
        tv_profitTot = findViewById(R.id.tv_profitTot)

        tv_price1x = findViewById(R.id.tv_price1x)
        tv_quant1x = findViewById(R.id.tv_quant1x)
        v_diff1x = findViewById(R.id.v_diff1x)
        tv_diff1x = findViewById(R.id.tv_diff1x)

        tv_price3x = findViewById(R.id.tv_price3x)
        tv_quant3x = findViewById(R.id.tv_quant3x)
        v_diff3x = findViewById(R.id.v_diff3x)
        tv_diff3x = findViewById(R.id.tv_diff3x)
        ll_hide3x = findViewById(R.id.ll_hide3x) // 언락시 invisible 로 변경
        ll_lock3x = findViewById(R.id.ll_lock3x) // 언락시 invisible 로 변경

        tv_priceinv1x = findViewById(R.id.tv_priceinv1x)
        tv_quantinv1x = findViewById(R.id.tv_quantinv1x)
        v_diffinv1x = findViewById(R.id.v_diffinv1x)
        tv_diffinv1x = findViewById(R.id.tv_diffinv1x)

        tv_priceinv3x = findViewById(R.id.tv_priceinv3x)
        tv_quantinv3x = findViewById(R.id.tv_quantinv3x)
        v_diffinv3x = findViewById(R.id.v_diffinv3x)
        tv_diffinv3x = findViewById(R.id.tv_diffinv3x)
        ll_hideinv3x = findViewById(R.id.ll_hideinv3x) // 언락시 invisible 로 변경
        ll_lockinv3x = findViewById(R.id.ll_lockinv3x) // 언락시 invisible 로 변경

        tv_fund = findViewById(R.id.tv_fund)
        tv_bond = findViewById(R.id.tv_bond)
        tv_indpro = findViewById(R.id.tv_indpro)
        tv_unem = findViewById(R.id.tv_unem)
        tv_inf = findViewById(R.id.tv_inf)

        cl_fund = findViewById(R.id.cl_fund)
        cl_bond = findViewById(R.id.cl_bond)
        cl_indpro = findViewById(R.id.cl_indpro)
        cl_unem = findViewById(R.id.cl_unem)
        cl_inf = findViewById(R.id.cl_inf)

        tv_ecoindex = findViewById(R.id.tv_ecoindex)
        tv_ecoval = findViewById(R.id.tv_ecoval)

        cht_snp = findViewById(R.id.cht_snp)
        cht_eco = findViewById(R.id.cht_eco)

        tv_snp = findViewById(R.id.tv_snp)
        tv_news = findViewById(R.id.tv_news)
        ll_item = findViewById(R.id.ll_item)
        ll_trade = findViewById(R.id.ll_trade)
        tv_trade = findViewById(R.id.tv_trade)
        tv_autoon = findViewById(R.id.tv_autoon)
        pb_fatigue = findViewById(R.id.pb_fatigue)

        // findViewById<Button>(R.id.btn_auto).setBackgroundResource(R.drawable.ic_check_intersection)

        profileDbManager!!.refresh(this)
        questDb = QuestDB.getInstance(this)
        gameSetDb = GameSetDB.getInstace(this)
        //달성할 도전과제 불러오기
        questList = questDb?.questDao()?.getQuestByTheme("수익률")!!
        relativeQuestList = questDb?.questDao()?.getQuestByTheme("시장대비 수익률")!!
        surplusQuestList = questDb?.questDao()?.getQuestByTheme("연속 흑자")
        val initialgameset = gameSetDb?.gameSetDao()?.getSetWithId(accountID+0, accountID!!)
        var gameset = gameSetDb?.gameSetDao()?.getSetWithId(setId, accountID!!)
        //setId = gameSetDb?.gameSetDao()?.getId()!!
        if (gameset != null) { gl = 250 * gameset.setgamelength }
        var sp = random.nextInt((snp_date.size - gl - given - 30)) + given // Starting Point

        // 값 표준화: 시작일(sp)을 100으로
        var criteria: Float = 100 / (snp_val[sp].toFloat())
        tradeday = 0

        gameNormalDb = GameNormalDB.getInstace(this)
        tv_gameName.text = "투자공간"+ setId.last()
        if (gameNormalDb?.gameNormalDao()?.getSetWithNormal(setId, accountID!!)?.isEmpty() == false) {
            sp = gameNormalDb?.gameNormalDao()?.getSetWithNormal(setId, accountID!!)?.last()?.endpoint!!
            // 차트 ////////////////////////////////////////////////////////////////////////////////////
            val gamehistory = gameNormalDb?.gameNormalDao()?.getSetWithNormal(setId, accountID!!)!!.last()
            // 차트 전역 변수 초기화
            if (gameset != null) {
                setGamelength = gameset.setgamelength!!
                initialize(gamehistory!!, gameset)
            }
            criteria = 1F
        }
        else {
            if (gameset != null) {
                if (initialgameset != null) {
                    gameset.setcash = initialgameset.setcash
                    gameset.setmonthly = initialgameset.setmonthly
                    gameset.setsalaryraise = initialgameset.setsalaryraise
                    gameset.setgamelength = initialgameset.setgamelength
                    gameset.setgamespeed = initialgameset.setgamespeed
                }
                gameSetDb?.gameSetDao()?.insert(gameset)
            }
            asset = setCash
            cash = setCash
            input = setCash
            setGamelength = setGamelength
            setGamespeed = setGamespeed
            setMonthly = setMonthly
            setSalaryraise = setSalaryraise
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

            price1x = 100F // 1x 현재가
            price3x = 100F // 3x 현재가
            priceinv1x = 100F // -1x 현재가
            priceinv3x = 100F // -3x 현재가

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

            autobuy = false
            autoratio = 0
            auto1x = 100

            monthToggle = 1
////////////////////////////////////////////////////////////////////////////////////////////////////


            // 피로도 불러오기
            value1now = profileDbManager!!.getValue1()!!


// 버튼 클릭 판별자 생성 ///////////////////////////////////////////////////////////////////////////
            click = false // 매수, 매도, 자동, 아이템 다이얼로그의 버튼들에 적용
            gameend = false // 게임 종료시 적용
////////////////////////////////////////////////////////////////////////////////////////////////////
        }


        // Button 동작
//        // 지표 설명
//        layout_fund = findViewById(R.id.Layout_fund)
//        layout_bond = findViewById(R.id.Layout_bond)
//        layout_indpro = findViewById(R.id.Layout_indpro)
//        layout_unem = findViewById(R.id.Layout_unem)
//        layout_inf = findViewById(R.id.Layout_inf)
//
//
//        findViewById<LinearLayout>(R.id.layout_btn_fund).setOnClickListener{
//            if(layout_fund.visibility == View.VISIBLE){
//                layout_fund.visibility = View.GONE
//            }
//            else{
//                layout_fund.visibility = View.VISIBLE
//                layout_bond.visibility = View.GONE
//                layout_indpro.visibility = View.GONE
//                layout_unem.visibility = View.GONE
//                layout_inf.visibility = View.GONE
//            }
//        }
//        findViewById<LinearLayout>(R.id.layout_btn_bond).setOnClickListener{
//            if(layout_bond.visibility == View.VISIBLE){
//                layout_bond.visibility = View.GONE
//            }
//            else{
//                layout_fund.visibility = View.GONE
//                layout_bond.visibility = View.VISIBLE
//                layout_indpro.visibility = View.GONE
//                layout_unem.visibility = View.GONE
//                layout_inf.visibility = View.GONE
//            }
//        }
//        findViewById<LinearLayout>(R.id.layout_btn_indpro).setOnClickListener{
//            if(layout_indpro.visibility == View.VISIBLE){
//                layout_indpro.visibility = View.GONE
//            }
//            else{
//                layout_fund.visibility = View.GONE
//                layout_bond.visibility = View.GONE
//                layout_indpro.visibility = View.VISIBLE
//                layout_unem.visibility = View.GONE
//                layout_inf.visibility = View.GONE
//            }
//        }
//        findViewById<LinearLayout>(R.id.layout_btn_unem).setOnClickListener{
//            if(layout_unem.visibility == View.VISIBLE){
//                layout_unem.visibility = View.GONE
//            }
//            else{
//                layout_fund.visibility = View.GONE
//                layout_bond.visibility = View.GONE
//                layout_indpro.visibility = View.GONE
//                layout_unem.visibility = View.VISIBLE
//                layout_inf.visibility = View.GONE
//            }
//        }
//        findViewById<LinearLayout>(R.id.layout_btn_inf).setOnClickListener{
//            if(layout_inf.visibility == View.VISIBLE){
//                layout_inf.visibility = View.GONE
//            }
//            else{
//                layout_fund.visibility = View.GONE
//                layout_bond.visibility = View.GONE
//                layout_indpro.visibility = View.GONE
//                layout_unem.visibility = View.GONE
//                layout_inf.visibility = View.VISIBLE
//            }
//        }


        pb_fatigue.setOnClickListener {
            value1now = 100
        }

        val tradeBottomSheetDialog = TradeBottomDialogFragment(applicationContext)
        val autoBottomSheetDialog = AutoBottomDialogFragment(applicationContext)
        val itemBottomSheetDialog = ItemBottomDialogFragment(applicationContext)

        // 거래하기
        ll_trade.setOnClickListener {
            tradeBottomSheetDialog.show(supportFragmentManager, tradeBottomSheetDialog.tag)
            click = true /////////////////////////////////////////////////////////////////////////
        }

        // 자동
        ll_trade.setOnLongClickListener {
            if (!autobuy) {
                autoBottomSheetDialog.show(supportFragmentManager, tradeBottomSheetDialog.tag)
                click = true ///////////////////////////////////////////////////////////////////////
            } else {
                autobuy = false
                autoratio = 0
                tv_trade.setTextAppearance(R.style.game_tradeoff)
                tv_autoon.setTextAppearance(R.style.game_tradeautooff)
                ll_trade.setBackgroundResource(R.drawable.trade_autooff)
                Toast.makeText(this, "자동 매수를 사용하지 않습니다", Toast.LENGTH_SHORT).show()
            }
            true
        }

        // 아이템
        ll_item.setOnClickListener {
            itemBottomSheetDialog.show(supportFragmentManager, itemBottomSheetDialog.tag)
            click = true /////////////////////////////////////////////////////////////////////////
        }

        tv_assetTot.setOnClickListener {
            val dlg_asset = Dialog_asset(this@GameNormalActivity, monthlytemp, divlast, taxlast)
            dlg_asset.start()
            click = true /////////////////////////////////////////////////////////////////////////
        }

        // 게임 내 뒤로가기 버튼
        btn_gameBack.setOnClickListener {
            onBackPressed()
        }

        // 레버리지 언락
        if (item4Active) {
            ll_hide3x.visibility = VISIBLE
            ll_lock3x.visibility = GONE

            ll_hideinv3x.visibility = VISIBLE
            ll_lockinv3x.visibility = GONE
        }

        // 경제 지표 차트
        cl_fund.setOnClickListener {
            ecoselect = 0
            cht_eco.data = fundD
            tv_ecoindex.text = "연준 기금 금리"
            tv_ecoval.text = per.format(fund_val[fundIndex - 1].toFloat()) + " %"
        }
        cl_bond.setOnClickListener {
            ecoselect = 1
            cht_eco.data = bondD
            tv_ecoindex.text = "10년 만기 국채 이율"
            tv_ecoval.text = per.format(bond_val[bondIndex - 1].toFloat()) + " %"
        }
        cl_indpro.setOnClickListener {
            ecoselect = 2
            cht_eco.data = indproD
            tv_ecoindex.text = "산업생산량"
            tv_ecoval.text = per.format(indpro_val[indproIndex - 1].toFloat()) + " "
        }
        cl_unem.setOnClickListener {
            ecoselect = 3
            cht_eco.data = unemD
            tv_ecoindex.text = "실업률"
            tv_ecoval.text = per.format(unem_val[unemIndex - 1].toFloat()) + " %"
        }
        cl_inf.setOnClickListener {
            ecoselect = 4
            cht_eco.data = infD
            tv_ecoindex.text = "인플레이션"
            tv_ecoval.text = per.format(inf_val[infIndex - 1].toFloat()) + " %"
        }


        // 차트 코루틴 시작
        CoroutineScope(Dispatchers.Default).launch {
            val job1 = launch {
                chartdata(sp, criteria)
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
        tradeday = dayPlus
        val dlg_exit = Dialog_game_exit(this@GameNormalActivity)
        eventCount = eventCount+1
        if(eventCount == 1)   dlg_exit.start()
        if(click == false)  click = !click /////////////////////////////////////////////////////////////////////////////
    }

    // 홈버튼 눌렀을 떄 게임 종료 다이얼로그 띄움(일시 정지 기능으로 사용)
    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        tradeday = dayPlus
        if (!gameend && !endsuccess) {
            eventCount = eventCount+1
            val dlg_exit = Dialog_game_exit(this@GameNormalActivity)
            if(eventCount == 1) dlg_exit.start()
        }
        if(click == false)  click = !click /////////////////////////////////////////////////////////////////////////////
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // 전역 변수 초기화
    private fun initialize(gamehistory: GameNormal, gameset: GameSet) {
        asset =  gamehistory?.assets!!// 총 자산
        cash = gamehistory?.cash!! // 보유 현금
        input = gamehistory?.input!! // 총 인풋
        bought = gamehistory?.bought!! // 총 매수금액
        sold = gamehistory?.sold!! // 총 매도금액
        evaluation = gamehistory?.evaluation!!// 평가금액
        profit = gamehistory?.profit!! // 순손익
        profitrate = gamehistory?.profitrate!! // 수익률
        profittot = gamehistory?.profittot!! // 실현 순손익
        profityear = gamehistory?.profityear!! // 세금 계산을 위한 연 실현수익(손실이 아닌 수익만 기록)

        quant1x = gamehistory?.quant1x!! // 1x 보유 수량
        quant3x = gamehistory?.quant3x!! // 3x 보유 수량
        quantinv1x = gamehistory?.quantinv1x!! // -1x 보유 수량
        quantinv3x = gamehistory?.quantinv3x!! // -3x 보유 수량

        bought1x = gamehistory?.bought1x!!
        bought3x = gamehistory?.bought3x!!
        boughtinv1x = gamehistory?.boughtinv1x!!
        boughtinv3x = gamehistory?.boughtinv3x!!

        aver1x = gamehistory?.aver1x!! // 1x 평균 단가
        aver3x = gamehistory?.aver3x!! // 3x 평균 단가
        averinv1x = gamehistory?.averinv1x!! // inv1x 평균 단가
        averinv3x = gamehistory?.averinv3x!! // inv3x 평균 단가

        buylim1x = gamehistory?.buylim1x!! // 1x 매수 한계 수량
        buylim3x = gamehistory?.buylim3x!! // 3x 매수 한계 수량
        buyliminv1x = gamehistory?.buyliminv1x!! // -1x 매수 한계 수량
        buyliminv3x = gamehistory?.buyliminv3x!! // -3x 매수 한계 수량

        price1x = gamehistory?.price1x!! // 1x 현재가
        price3x = gamehistory?.price3x!! // 3x 현재가
        priceinv1x = gamehistory?.priceinv1x!! // -1x 현재가
        priceinv3x = gamehistory?.priceinv3x!! // -3x 현재가

        val1x = gamehistory?.val1x!! // 1x 현재가치
        val3x = gamehistory?.val3x!! // 3x 현재가치
        valinv1x = gamehistory?.valinv1x!! // -1x 현재가치
        valinv3x = gamehistory?.valinv3x!! // -3x 현재가치

        pr1x = gamehistory?.pr1x!! // 1x 수익률
        pr3x = gamehistory?.pr3x!! // 3x 수익률
        prinv1x = gamehistory?.prinv1x!! // inv1x 수익률
        prinv3x = gamehistory?.prinv3x!! // inv3x 수익률

        tradecomrate = 1.001F // 거래수수료(Trade Commission): 0.1% , 거래시 차감(곱하는 방식)
        tradecomtot = gamehistory?.tradecomtot!! // 거래수수료 총 합

        dividendrate = 0.0153F / 4F // 배당금(VOO: 1.53% / year), 분기 단위로 현금으로 입금
        dividendtot = gamehistory?.dividendtot!! // 총 배당금
        countMonth = gamehistory?.countmonth!! // 경과 개월 수 카운트
        countYear = gamehistory?.countyear!! // 플레이 한 햇수 카운트
        tax = 0F // 세금
        taxtot = gamehistory?.taxtot!! // 총 세금
        // var currency: Float = 0F // 환율(현재 미반영)

        snpNowDate = "yyyy-mm-dd"
        snpNowdays = gamehistory?.snpnowdays!!
        snpNowVal = gamehistory?.snpnowval!!
        snpDiff = gamehistory?.snpdiff!!
        item1Active = gamehistory?.item1active!!
        item2Active = gamehistory?.item2active!!
        item3Active = gamehistory?.item3active!!
        item4Active = gamehistory?.item4active!!
        item1Length = gamehistory?.item1length!!
        item1Able = gamehistory?.item1able!!

        autobuy = gamehistory?.autobuy!!
        autoratio = gamehistory?.autoratio!!
        auto1x = gamehistory?.auto1x!!


        setSalaryraise = SET_SALARY_RAISE_STEP[gameset.setsalaryraise]
        setGamespeed = gameset.setgamespeed
        setMonthly = gamehistory?.monthly!!
        monthToggle =gamehistory?.monthtoggle!!
////////////////////////////////////////////////////////////////////////////////////////////////////

        // 피로도 불러오기
        value1now = profileDbManager!!.getValue1()!!

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
        // TODO
        val legend_snp : Legend = cht_snp.legend
        val x_snp : XAxis = cht_snp.xAxis
        val yl_snp : YAxis = cht_snp.getAxis(YAxis.AxisDependency.LEFT)
        val yr_snp : YAxis = cht_snp.getAxis(YAxis.AxisDependency.RIGHT)

        cht_snp.description.isEnabled = false

        legend_snp.isEnabled = false
        x_snp.isEnabled = false
        yl_snp.isEnabled = false
        yr_snp.isEnabled = false

        snpDs.color = Color.parseColor(snpLineColor) // 차트 선
        snpDs.setDrawCircles(false)
        snpDs.setDrawValues(false) // 차트 지점마다 값 표시
        snpDs.lineWidth = 1.5F
//        snpDs.fillAlpha = 80 // 차트 스커트
//        snpDs.fillColor = Color.parseColor(snpFillColor)
//        snpDs.setDrawFilled(true)
//        snpDs.setDrawAxisLine(false)
        snpDs.highLightColor = Color.parseColor(snpHighColor) // 터치 시 하이라이트
        snpDs.highlightLineWidth = 1F

        val legend_eco : Legend = cht_eco.getLegend()
        val x_eco : XAxis = cht_eco.xAxis
        val yl_eco : YAxis = cht_eco.getAxis(YAxis.AxisDependency.LEFT)
        val yr_eco : YAxis = cht_eco.getAxis(YAxis.AxisDependency.RIGHT)

        cht_eco.description.isEnabled = false

        legend_eco.isEnabled = false
        x_eco.isEnabled = false
        yl_eco.isEnabled = false
        yr_eco.isEnabled = false

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
        cht_snp.data = snpD
        cht_eco.data = fundD
//        findViewById<LineChart>(R.id.cht_bond).data = bondD
//        findViewById<LineChart>(R.id.cht_indpro).data = indproD
//        findViewById<LineChart>(R.id.cht_unem).data = unemD
//        findViewById<LineChart>(R.id.cht_inf).data = infD


        // 차트 레이아웃 생성 //////////////////////////////////////////////////////////////
        runOnUiThread {
            tv_timeleft.text = (29).toString()+"년 "+(12).toString()+"개월"

            // 차트 생성
            cht_snp.animateXY(1, 1)
            cht_eco.animateXY(1, 1)
            //TODO
            cht_snp.setBackgroundColor(Color.parseColor("#F8FAFB"))
            cht_snp.setDrawGridBackground(false)
            cht_snp.setDrawBorders(false)

            cht_eco.setBackgroundColor(Color.parseColor("#FFFFFF"))
            cht_eco.setDrawGridBackground(false)
            cht_eco.setDrawBorders(false)

            cht_eco.setTouchEnabled(false)
            cht_eco.setVisibleXRangeMaximum(1250F)
        }

        // 추가분 반영
        cht_snp.notifyDataSetChanged()
        cht_eco.notifyDataSetChanged()

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
        var autoprice: Float = 0F
        var autoquant: Int = 0

        // 피로도
        var value1diff: Int = 0

        // 뉴스
        var newsindex: Int = 1 // 뉴스 인덱스
        var newscount: Int = 0 // 일별 뉴스 길이 카운트. 현재 < 5

        while (true) {
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

                        // 차트 최신화
                        Updatecht()

                        // 값 OutPut ///////////////////////////////////////////////////////////////
                        // 현재 값 저장
                        snpNowDate = snp_date[start + dayPlus]
                        snpNowdays = dayPlus
                        snpNowVal = snp_val[start + dayPlus].toFloat()
                        marketrate = (price1x/ START_SNP_VAL)*100
                        snpDiff =
                            snp_val[start + dayPlus].toFloat() / snp_val[start + dayPlus - 1].toFloat() - 1F
                        println("현재 날짜 : $snpNowDate | 현재 경과 거래일 : $snpNowdays | 현재 S&P 500 지수 값 : $snpNowVal | 등락 : $snpDiff")

                        // 뉴스
                        var newsDate = news_date[newsindex]
                        var newsDate_sf = sf.parse(newsDate)

                        if (newsDate_sf.time < snpDate_sf.time) {
                            while (newsDate_sf.time < snpDate_sf.time) {
                                // newsDate = snpDate 일 때 멈춤
                                newsindex += 1
                                newsDate = news_date[newsindex]
                                newsDate_sf = sf.parse(newsDate)
                            }
                        }
                        else if (newsDate_sf.time >= snpDate_sf.time) {
                            while (newsDate_sf.time >= snpDate_sf.time) {
                                // newsDat = snpDate - 1  일 때 멈춤
                                newsindex -= 1
                                newsDate = news_date[newsindex]
                                newsDate_sf = sf.parse(newsDate)
                            }
                            newsindex += 1
                        }

                        while ((newsDate_sf.time==snpDate_sf.time) && (newscount<=5)) {
                            newssave.add(newsindex)
                            newsindex += 1
                            newscount += 1
                        }
                        newscount = 0 // newscount 초기화

                        for(i in 0..40600){
                            if(news_date[i] == snpNowDate){
                                newssave.add(i)
                            }
                        }

                        if(newssave.size==0){
                            runOnUiThread {
                                tv_news.text = "뉴스 없음"
//                                findViewById<TextView>(R.id.news2).text = "뉴스 없음"
//                                findViewById<TextView>(R.id.news3).text = "뉴스 없음"
//                                findViewById<TextView>(R.id.news4).text = "뉴스 없음"
//                                findViewById<TextView>(R.id.news5).text = "뉴스 없음"
                            }
                        }
                        else if(newssave.size ==1){
                            var tmp0 = newssave[0]
                            runOnUiThread {
                                tv_news.text = news_information[tmp0]
//                                findViewById<TextView>(R.id.news2).text = "뉴스 없음"
//                                findViewById<TextView>(R.id.news3).text = "뉴스 없음"
//                                findViewById<TextView>(R.id.news4).text = "뉴스 없음"
//                                findViewById<TextView>(R.id.news5).text = "뉴스 없음"
                            }
                        }
                        else if(newssave.size ==2){
                            var tmp0 = newssave[0]; var tmp1 = newssave[1]
                            runOnUiThread {
                                tv_news.text = news_information[tmp0]
//                                findViewById<TextView>(R.id.news2).text = news_information[tmp1]
//                                findViewById<TextView>(R.id.news3).text ="뉴스 없음"
//                                findViewById<TextView>(R.id.news4).text ="뉴스 없음"
//                                findViewById<TextView>(R.id.news5).text ="뉴스 없음"
                            }
                        }
                        else if(newssave.size ==3){
                            var tmp0 = newssave[0]; var tmp1 = newssave[1]; var tmp2 = newssave[2]
                            runOnUiThread {
                                tv_news.text = news_information[tmp0]
//                                findViewById<TextView>(R.id.news2).text = news_information[tmp1]
//                                findViewById<TextView>(R.id.news3).text = news_information[tmp2]
//                                findViewById<TextView>(R.id.news4).text ="뉴스 없음"
//                                findViewById<TextView>(R.id.news5).text ="뉴스 없음"
                            }
                        }
                        else if(newssave.size ==4){
                            var tmp0 = newssave[0]; var tmp1 = newssave[1]; var tmp2 = newssave[2]; var tmp3 = newssave[3]
                            runOnUiThread {
                                tv_news.text = news_information[tmp0]
//                                findViewById<TextView>(R.id.news2).text = news_information[tmp1]
//                                findViewById<TextView>(R.id.news3).text = news_information[tmp2]
//                                findViewById<TextView>(R.id.news4).text = news_information[tmp3]
//                                findViewById<TextView>(R.id.news5).text ="뉴스 없음"
                            }
                        }
                        else if(newssave.size >= 5){
                            var tmp0 = newssave[0]; var tmp1 = newssave[1]; var tmp2 = newssave[2]; var tmp3 = newssave[3]; var tmp4 = newssave[4]
                            runOnUiThread {
                                tv_news.text = news_information[tmp0]
//                                findViewById<TextView>(R.id.news2).text = news_information[tmp1]
//                                findViewById<TextView>(R.id.news3).text = news_information[tmp2]
//                                findViewById<TextView>(R.id.news4).text = news_information[tmp3]
//                                findViewById<TextView>(R.id.news5).text = news_information[tmp4]
                            }
                        }
                        newssave.clear()


                        if (autobuy) {
                            runOnUiThread {
                                tv_trade.setTextAppearance(R.style.game_tradeon)
                                tv_autoon.setTextAppearance(R.style.game_tradeautoon)
                                tv_autoon.text = "(길게 눌러 자동 매수 해제하기)"
                                ll_trade.setBackgroundResource(R.drawable.trade_autoon)
                            }
                        } else {
                            runOnUiThread {
                                tv_trade.setTextAppearance(R.style.game_tradeoff)
                                tv_autoon.setTextAppearance(R.style.game_tradeautooff)
                                tv_autoon.text = "(길게 눌러 자동 매수하기)"
                                ll_trade.setBackgroundResource(R.drawable.trade_autooff)
                            }
                        }

                        // 월 투자금 입금 시작
                        if (snpDate_sf.date >= 10 && monthToggle==0) {
                            cash += monthly // 월 투자금 현금으로 입금
                            input += monthly // 총 input 최신화
                            monthlytemp = monthly
                            // 자산내역 DB 저장
                            val addRunnable = Runnable {
                                var localDateTime = LocalDateTime.now()
                                val newGameNormalDB = GameNormal(localDateTime.toString(), asset, cash, input, bought, sold, evaluation, profit, profitrate, profittot, profityear, "자산 차트", 0F, 0F, 0, 0, quant1x, quant3x, quantinv1x, quantinv3x,
                                        bought1x, bought3x, boughtinv1x, boughtinv3x, aver1x, aver3x, averinv1x, averinv3x, buylim1x, buylim3x, buyliminv1x, buyliminv3x, price1x, price3x, priceinv1x, priceinv3x, val1x, val3x, valinv1x, valinv3x,
                                        pr1x, pr3x, prinv1x, prinv3x, setMonthly, monthToggle, tradecomtot, 0F, dividendtot, taxtot, "nothing", item1Active, item1Length, item1Able, item2Active, item3Active, item4Active, autobuy, autoratio, auto1x, endpoint, countYear, countMonth, snpNowdays, snpNowVal, snpDiff, setId, relativeprofitrate, localdatatime, accountID!!)
                                gameNormalDb?.gameNormalDao()?.insert(newGameNormalDB)
                            }
                            val addThread = Thread(addRunnable)
                            addThread.start()

                            ////////////////////////////////////////////////////////////////////////
                            // 자동 매수
                            if (autobuy) {
                                when (auto1x) {
                                    0 -> {
                                        if (((monthly / (price1x * tradecomrate) * (autoratio/100F)).roundToInt() - (monthly / (price1x * tradecomrate) * (autoratio/100F)) > 0)) {
                                            autoquant = (monthly / (price1x * tradecomrate) * (autoratio/100F)).roundToInt() - 1
                                        }
                                        else {
                                            autoquant = (monthly / (price1x * tradecomrate) * (autoratio/100F)).roundToInt()
                                        }
                                        cash -= autoquant * price1x * tradecomrate
                                        tradecomtot += autoquant * price1x * (tradecomrate - 1F)
                                        comlast = autoquant * price1x * (tradecomrate - 1F)
                                        quant1x += autoquant // 수량 반영
                                        bought1x += price1x * autoquant // 매입금 반영
                                        aver1x = bought1x / quant1x // 평균단가 반영

                                        autoprice = autoquant * price1x
                                    }
                                    1 -> {
                                        if (((monthly / (price3x * tradecomrate) * (autoratio/100F)).roundToInt() - (monthly / (price3x * tradecomrate) * (autoratio/100F)) > 0)) {
                                            autoquant = (monthly / (price3x * tradecomrate) * (autoratio/100F)).roundToInt() - 1
                                        }
                                        else {
                                            autoquant = (monthly / (price3x * tradecomrate) * (autoratio/100F)).roundToInt()
                                        }
                                        cash -= autoquant * price3x * tradecomrate
                                        tradecomtot += autoquant * price3x * (tradecomrate - 1F)
                                        comlast = autoquant * price3x * (tradecomrate - 1F)
                                        quant3x += autoquant // 수량 반영
                                        bought3x += price3x * autoquant // 매입금 반영
                                        aver3x = bought3x / quant3x // 평균단가 반영

                                        autoprice = autoquant * price3x
                                    }
                                    2 -> {
                                        if (((monthly / (priceinv1x * tradecomrate) * (autoratio/100F)).roundToInt() - (monthly / (priceinv1x * tradecomrate) * (autoratio/100F)) > 0)) {
                                            autoquant = (monthly / (priceinv1x * tradecomrate) * (autoratio/100F)).roundToInt() - 1
                                        }
                                        else {
                                            autoquant = (monthly / (priceinv1x * tradecomrate) * (autoratio/100F)).roundToInt()
                                        }
                                        cash -= autoquant * priceinv1x * tradecomrate
                                        tradecomtot += autoquant * priceinv1x * (tradecomrate - 1F)
                                        comlast = autoquant * priceinv1x * (tradecomrate - 1F)
                                        quantinv1x += autoquant // 수량 반영
                                        boughtinv1x += priceinv1x * autoquant // 매입금 반영
                                        averinv1x = boughtinv1x / quantinv1x // 평균단가 반영

                                        autoprice = autoquant * priceinv1x
                                    }
                                    3 -> {
                                        if (((monthly / (priceinv3x * tradecomrate) * (autoratio/100F)).roundToInt() - (monthly / (priceinv3x * tradecomrate) * (autoratio/100F)) > 0)) {
                                            autoquant = (monthly / (priceinv3x * tradecomrate) * (autoratio/100F)).roundToInt() - 1
                                        }
                                        else {
                                            autoquant = (monthly / (priceinv3x * tradecomrate) * (autoratio/100F)).roundToInt()
                                        }
                                        cash -= autoquant * priceinv3x * tradecomrate
                                        tradecomtot += autoquant * priceinv3x * (tradecomrate - 1F)
                                        comlast = autoquant * priceinv3x * (tradecomrate - 1F)
                                        quantinv3x += autoquant // 수량 반영
                                        boughtinv3x += priceinv3x * autoquant // 매입금 반영
                                        averinv3x = boughtinv3x / quantinv3x // 평균단가 반영

                                        autoprice = autoquant * priceinv3x
                                    }
                                }

//                                // 자동 매수내역 DB 저장
//                                val addRunnable = Runnable {
//                                    var localDateTime = LocalDateTime.now()
//                                    val newGameNormalDB = GameNormal(localDateTime.toString(), asset, cash, input, bought, sold, evaluation, profit, profitrate, profittot, profityear, "자동 매수", 0F, 0F, 0, 0, quant1x, quant3x, quantinv1x, quantinv3x,
//                                            bought1x, bought3x, boughtinv1x, boughtinv3x, aver1x, aver3x, averinv1x, averinv3x, buylim1x, buylim3x, buyliminv1x, buyliminv3x, price1x, price3x, priceinv1x, priceinv3x, val1x, val3x, valinv1x, valinv3x,
//                                            pr1x, pr3x, prinv1x, prinv3x, setMonthly, monthToggle, tradecomtot, 0F, dividendtot, taxtot, "nothing", item1Active, item1Length, item1Able, item2Active, item3Active, item4Active, autobuy, autoratio, auto1x, endpoint, countYear, countMonth, snpNowdays, snpNowVal, snpDiff, setId, relativeprofitrate, localdatatime, accountID!!)
//                                    gameNormalDb?.gameNormalDao()?.insert(newGameNormalDB)
//                                }
//                                val addThread = Thread(addRunnable)
//                                addThread.start()
                            }
                            ////////////////////////////////////////////////////////////////////////
                            monthToggle = 1 // 해당 월 투자금 지급 여부
                        }
                        // 월 투자금 입금 코드 종료


                        // 월 바뀜 시 실행할 코드 시작
                        if (snpDate_sf.month != preMonth) {

                            monthToggle = 0

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
                                    taxlast = val1x * dividendrate * 0.154F
                                    divlast = val1x * dividendrate
//                                    runOnUiThread {
//                                        findViewById<TextView>(R.id.tv_notification).text =
//                                            "알림: 배당금 " + dec.format(val1x * dividendrate) + " 원이 입금되었습니다"
//                                    }
                                }
                            } else if ((snpDate_sf.month == 1) && (countYear >= 1)) {
                                monthly = setMonthly * (Math.pow((1F + setSalaryraise / 100F).toDouble(), countYear.toDouble())).toFloat() // 연봉 인상으로 월 투자금 증가
//                                runOnUiThread {
//                                    findViewById<TextView>(R.id.tv_notification).text =
//                                        "알림: 연봉 " + per.format(setSalaryraise) + "% 인상으로 월 투자금이 " + dec.format(
//                                            monthly
//                                        ) + " 원이 되었습니다"
//                                }
                            }

                            if (tax > 0F && snpDate_sf.month != 0) {
                                if (cash >= tax) {
                                    cash -= tax
//                                    runOnUiThread {
//                                        findViewById<TextView>(R.id.tv_notification).text =
//                                            "알림: 세금 " + dec.format(tax) + " 원이 납부 되었습니다"
//                                    }
                                    taxtot += tax
                                    taxlast = tax
                                    delay(10L) // UI에서의 알림메시지 출력시간 확보
                                    tax = 0F
                                } else {
                                    tax *= (1.05F)
//                                    runOnUiThread {
//                                        findViewById<TextView>(R.id.tv_notification).text =
//                                            "알림: 미납 세금이 매달 가산됩니다"
//                                    }
                                }
                            }

                            // 해가 바뀔 시
                            if (snpDate_sf.year != preYear) {
                                if (profityear>2200F) {
                                    tax += (profityear - 2200F) * 0.22F // 양도 소득세: 연간 실현수익에서 250만원 공제 후 22% 부과
                                }
                                profityear = 0F // 연 수익률 초기화
                                if (cash >= tax && tax > 0F) {
//                                    runOnUiThread {
//                                        findViewById<TextView>(R.id.tv_notification).text =
//                                            "알림: 세금 " + dec.format(tax) + " 원이 납부됨"
//                                    }
                                    cash -= tax
                                    taxtot += tax
                                    taxlast = tax
                                    delay(10L) // UI에서의 알림메시지 출력시간 확보
                                    tax = 0F
                                } else if (cash < tax && tax > 0F) {
//                                    runOnUiThread {
//                                        findViewById<TextView>(R.id.tv_notification).text =
//                                            "알림: 현금이 부족해 세금을 낼 수 없습니다"
//                                    }
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
                        profit = evaluation + cash - input // input 대비 수익률
                        profitrate = 100F * profit / input
                        relativeprofitrate = (profitrate+100) / marketrate*100-100
                        questList?.let { checkQuestInGameProfit(profitrate, it) }
                        relativeQuestList?.let { checkQuestRelativeProfit(relativeprofitrate, it) }
                        pastAsset = asset
                        asset = cash + evaluation // 총 자산
                        // 자산 관련 데이터 계산 종료
                        if(asset> pastAsset) surplusCount++
                        else surplusCount = 0
                        surplusQuestList?.let { checkQuestSurplus(surplusCount, it) }

                        // UI 업데이트
                        Updateui(start, criteria)

                        ////////////////////////////////////////////////////////////////////////////
                        // 시간역행 아이템용 데이터 저장 시작
                        println("현재 dayPlus: " + dayPlus.toString())

                        item1Able = dayPlus - 2 // 시간 역행 가능한 범위

                        fundIndex1.add(fundIndex)
                        bondIndex1.add(bondIndex)
                        indproIndex1.add(indproIndex)
                        unemIndex1.add(unemIndex)
                        infIndex1.add(infIndex)
                        println("현재 fundIndex Size: " + fundIndex1.size.toString() + " | 현재 asset1 Size: " + asset1.size.toString())

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

                        println("현재 fundIndex Size: " + fundIndex1.size.toString() + " | 현재 asset1 Size: " + asset1.size.toString())
                        // 시간역행 아이템용 데이터 저장 종료
                        ////////////////////////////////////////////////////////////////////////////

                        ////////////////////////////////////////////////////////////////////////////
                        // 시간 진행 속도 조절 아이템
                        if ((setGamespeed==0) && (value1now <= 10000 - 3)) {
                            oneday = 7995L - 95L // 8 sec/day
                            value1diff = 3
                            value1now += value1diff
                        } else if ((setGamespeed==1) && (value1now <= 10000 - 2)) {
                            oneday = 3995L - 95L // 4 sec/day
                            value1diff = 2
                            value1now += value1diff
                        } else if ((setGamespeed==2) && (value1now <= 10000 - 1)) {
                            oneday = 1995L - 95L // 2 sec/day
                            value1diff = 1
                            value1now += value1diff
                        } else if ((setGamespeed==4) && (value1now <= 10000 - 1)) {
                            oneday = 495L - 95L // 2 day/sec
                            value1diff = 1
                            value1now += value1diff
                        } else if ((setGamespeed==5) && (value1now <= 10000 - 2)) {
                            oneday = 245L - 95L // 4 day/sec
                            value1diff = 2
                            value1now += value1diff
                        } else if ((setGamespeed==6) && (value1now <= 10000 - 3)) {
                            oneday = 115L - 95L // 8 day/sec
                            value1diff = 3
                            value1now += value1diff
                        } else if ((setGamespeed==7) && (value1now <= 10000 - 4)) {
                            oneday = 95L - 95L // 10 day/sec
                            value1diff = 4
                            value1now += value1diff
                        } else { // 피로도 == 10000, or setGamespeed == 3
                            oneday = 995L - 95L // 1 day/sec
                            setGamespeed = 3
                        }
                        ////////////////////////////////////////////////////////////////////////////
                        runOnUiThread{
                            pb_fatigue.progress = value1now
                        }

                        dayPlus += 1 // 시간 진행
                        delay(oneday) // 게임 진행 속도 조절
                    }


                    // 정상 진행 시 코드 종료, 시간 역행 아이템 사용시 실행할 코드 시작
                    else if (dayPlus <= gl && item1Active) {
                        runOnUiThread {
                            ll_item.isEnabled = false
                            ll_trade.isEnabled = false
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


                                value1now += 50 // 피로도 증가
                                runOnUiThread {
                                    pb_fatigue.progress = value1now
                                }

                                // UI 업데이트
                                Updatecht()
                                Updateui(start, criteria)

                                delay(80L) // UI 쓰레드 동작 시간 확보, UI 쓰레드 문제로 강제 종료시 이 값을 증가시킬것
                                dayPlus -= 1
                            }
                        } catch (e: IndexOutOfBoundsException) {
                            e.printStackTrace()
                            gameend = true
                            endsuccess = true
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
                            monthly = setMonthly * (Math.pow((1F + setSalaryraise / 100F).toDouble(), (countYear - 1).toDouble())).toFloat() // 월 투자금 정상화
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


                        // UI 업데이트
                        Updatecht()
                        Updateui(start, criteria)

                        delay(100L) // UI 쓰레드 동작 시간 확보

                        println("시간역행 끝 : " + dayPlus.toString())
                        item1Length = 0
                        item1Active = false //

                        runOnUiThread {
                            ll_item.isEnabled = true
                            ll_trade.isEnabled = true
                        }
                    }
                    // 시간 역행 아이템 사용 시 코드 종료, 설정한 게임 플레이 시간 도달 시 실핼할 코드 시작
                    else {
                        println("게임 끝")
                        gameend = true
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
                break
            }
            delay(btnRefresh)
        }
    }

    // UI 업데이트 코드
    // TODO
    private fun Updatecht() {
        runOnUiThread {
            // 차트에 DataSet 리프레쉬 통보
            cht_snp.notifyDataSetChanged()
            cht_eco.notifyDataSetChanged()

            snpD.notifyDataChanged()
            fundD.notifyDataChanged()
            bondD.notifyDataChanged()
            indproD.notifyDataChanged()
            unemD.notifyDataChanged()
            infD.notifyDataChanged()

            // 차트 축 최대 범위 설정
            cht_snp.setVisibleXRangeMaximum(125F) // X축 범위: 125 거래일(~6개월)
            cht_eco.setVisibleXRangeMaximum(1250F)

            // 차트 축 이동
            cht_snp.moveViewToX(dayPlus.toFloat())
            cht_eco.moveViewToX((dayPlus).toFloat())
        }
    }

    private fun Updateui(start: Int, criteria: Float) {
        runOnUiThread {
            if (item4Active) {
                ll_hide3x.visibility = VISIBLE
                ll_lock3x.visibility = GONE

                ll_hideinv3x.visibility = VISIBLE
                ll_lockinv3x.visibility = GONE
            }
            // 자산 가치 관련 값들 최신화
            tv_assetTot.text = "$ " + dec.format(asset)
            tv_profitRate.text = per.format(profitrate) + " %"
            tv_profitTot.text = "$ " + dec.format(profit)
            tv_snp.text = per.format(snp_val[start + dayPlus].toFloat() * criteria)

            tv_price1x.text = "$ " + dec.format(price1x)
            tv_price3x.text = "$ " + dec.format(price3x)
            tv_priceinv1x.text = "$ " + dec.format(priceinv1x)
            tv_priceinv3x.text = "$ " + dec.format(priceinv3x)

            tv_quant1x.text = dec.format(quant1x) + " 주"
            tv_quant3x.text = dec.format(quant3x) + " 주"
            tv_quantinv1x.text = dec.format(quantinv1x) + " 주"
            tv_quantinv3x.text = dec.format(quantinv3x) + " 주"

            tv_diff1x.text = per.format(pr1x) + " %"
            tv_diff3x.text = per.format(pr3x) + " %"
            tv_diffinv1x.text = per.format(prinv1x) + " %"
            tv_diffinv3x.text = per.format(prinv3x) + " %"

            tv_fund.text = per.format(fund_val[fundIndex - 1].toFloat()) + " %"
            tv_bond.text = per.format(bond_val[bondIndex - 1].toFloat()) + " %"
            tv_indpro.text = per.format(indpro_val[indproIndex - 1].toFloat()) +" "
            tv_unem.text = per.format(unem_val[unemIndex - 1].toFloat()) + " %"
            tv_inf.text = per.format(inf_val[infIndex - 1].toFloat()) + " %"
        }

        // 수익률에 따른 화살표 이미지 및 색상 변경
        if (pr1x >= 0) {
            runOnUiThread {
                v_diff1x.setBackgroundResource(R.drawable.ic_polygon_2)
                tv_diff1x.setTextAppearance(R.style.game_etfdiff2)
            }
        } else {
            runOnUiThread {
                v_diff1x.setBackgroundResource(R.drawable.ic_polygon_3)
                tv_diff1x.setTextAppearance(R.style.game_etfdiff3)
            }
        }

        if (pr3x >= 0) {
            runOnUiThread {
                v_diff3x.setBackgroundResource(R.drawable.ic_polygon_2)
                tv_diff3x.setTextAppearance(R.style.game_etfdiff2)
            }
        } else {
            runOnUiThread {
                v_diff3x.setBackgroundResource(R.drawable.ic_polygon_3)
                tv_diff3x.setTextAppearance(R.style.game_etfdiff3)
            }
        }

        if (prinv1x >= 0) {
            runOnUiThread {
                v_diffinv1x.setBackgroundResource(R.drawable.ic_polygon_2)
                tv_diffinv1x.setTextAppearance(R.style.game_etfdiff2)
            }
        } else {
            runOnUiThread {
                v_diffinv1x.setBackgroundResource(R.drawable.ic_polygon_3)
                tv_diffinv1x.setTextAppearance(R.style.game_etfdiff3)
            }
        }

        if (prinv3x >= 0) {
            runOnUiThread {
                v_diffinv3x.setBackgroundResource(R.drawable.ic_polygon_2)
                tv_diffinv3x.setTextAppearance(R.style.game_etfdiff2)
            }
        } else {
            runOnUiThread {
                v_diffinv3x.setBackgroundResource(R.drawable.ic_polygon_3)
                tv_diffinv3x.setTextAppearance(R.style.game_etfdiff3)
            }
        }

        runOnUiThread {
            when (ecoselect) {
                0 -> {
                    tv_ecoindex.text = "연준 기금 금리"
                    tv_ecoval.text = per.format(fund_val[fundIndex - 1].toFloat()) + " %"
                }
                1 -> {
                    tv_ecoindex.text = "10년 만기 국채 이율"
                    tv_ecoval.text = per.format(bond_val[bondIndex - 1].toFloat()) + " %"
                }
                2 -> {
                    tv_ecoindex.text = "산업생산량"
                    tv_ecoval.text = per.format(indpro_val[indproIndex - 1].toFloat()) + " "
                }
                3 -> {
                    tv_ecoindex.text = "실업률"
                    tv_ecoval.text = per.format(unem_val[unemIndex - 1].toFloat()) + " %"
                }
                4 -> {
                    tv_ecoindex.text = "인플레이션"
                    tv_ecoval.text = per.format(inf_val[infIndex - 1].toFloat()) + " %"
                }
            }
        }

        if (countMonth==12) {
            runOnUiThread {
                tv_timeleft.text = (29 - countYear - 1).toString()+"년 "+(12).toString()+"개월"
            }
        } else {
            runOnUiThread {
                tv_timeleft.text = (29 - countYear).toString()+"년 "+(12-countMonth).toString()+"개월"
            }
        }
    }

    // 게임 종료 시 결과창으로 이동
    private fun endgame() {
        if (gameend && endsuccess) {
            cash += (quant1x*price1x+quant3x*price3x+quantinv1x*priceinv1x+quantinv3x*priceinv3x) * (2F - tradecomrate)// 현금에 매도금액 추가
            sold += quant1x*price1x+quant3x*price3x+quantinv1x*priceinv1x+quantinv3x*priceinv3x // 총 매도 금액 최신화
            tradecomtot += (quant1x*price1x+quant3x*price3x+quantinv1x*priceinv1x+quantinv3x*priceinv3x) * (tradecomrate - 1F) // 총 수수로 최신화
            comlast = (quant1x*price1x+quant3x*price3x+quantinv1x*priceinv1x+quantinv3x*priceinv3x) * (tradecomrate - 1F)
            if (gameNormalDb?.gameNormalDao()?.getSetWithNormalItem1Able(setId, accountID!!)?.size!! == 0) totaltradeday = tradeday
            else totaltradeday = gameNormalDb?.gameNormalDao()?.getSetWithNormalItem1Able(setId, accountID!!)?.sum()!! + gameNormalDb?.gameNormalDao()?.getSetWithNormalItem1Able(setId, accountID!!)?.size!! * 2

            if (price1x >= aver1x) {
                profityear += (price1x - aver1x) * quant1x
            }
            if (price3x >= aver3x) {
                profityear += (price3x - aver3x) * quant3x
            }
            if (priceinv1x >= averinv1x) {
                profityear += (priceinv1x - averinv1x) * quantinv1x
            }
            if (priceinv3x >= averinv3x) {
                profityear += (priceinv3x - averinv3x) * quantinv3x
            }

            quant1x -= quant1x
            quant3x -= quant3x
            quantinv1x -= quantinv1x
            quantinv3x -= quantinv3x

            bought1x -= aver1x * quant1x
            bought3x -= aver3x * quant3x
            boughtinv1x -= averinv1x * quantinv1x
            boughtinv3x -= averinv3x * quantinv3x

            aver1x = 0F
            aver3x = 0F
            averinv1x = 0F
            averinv3x = 0F

            if (profityear>2200F) {
                tax += (profityear - 2200F) * 0.22F // 양도 소득세: 연간 실현수익에서 250만원 공제 후 22% 부과
            }
            profityear = 0F // 연 수익률 초기화
            cash -= tax
            taxtot += tax
            taxlast = tax
            tax = 0F

            val1x = quant1x * price1x //
            val3x = quant3x * price3x //
            valinv1x = quantinv1x * priceinv1x //
            valinv3x = quantinv3x * priceinv3x //

            buylim1x = (cash / (price1x * tradecomrate)) //
            buylim3x = (cash / (price3x * tradecomrate)) //
            buyliminv1x = (cash / (priceinv1x * tradecomrate)) //
            buyliminv3x = (cash / (priceinv3x * tradecomrate)) //

            evaluation = val1x + val3x + valinv1x + valinv3x // 평가금액
            profit = evaluation + cash - input // input 대비 수익률
            profitrate = 100F * profit / input
            relativeprofitrate = (profitrate+100) / marketrate*100-100
            questList?.let { checkQuestInGameProfit(profitrate, it) }
            relativeQuestList?.let { checkQuestRelativeProfit(relativeprofitrate, it) }
            pastAsset = asset
            asset = cash + evaluation // 총 자산
            // 자산 관련 데이터 계산 종료
            if(asset> pastAsset) surplusCount++
            else surplusCount = 0
            surplusQuestList?.let { checkQuestSurplus(surplusCount, it) }

            // 자산내역 DB 저장
            val addRunnable = Runnable {
                var localDateTime = LocalDateTime.now()
                val newGameNormalDB = GameNormal(localDateTime.toString(), asset, cash, input, bought, sold, evaluation, profit, profitrate, profittot, profityear, "자산 차트", 0F, 0F, 0, 0, quant1x, quant3x, quantinv1x, quantinv3x,
                        bought1x, bought3x, boughtinv1x, boughtinv3x, aver1x, aver3x, averinv1x, averinv3x, buylim1x, buylim3x, buyliminv1x, buyliminv3x, price1x, price3x, priceinv1x, priceinv3x, val1x, val3x, valinv1x, valinv3x,
                        pr1x, pr3x, prinv1x, prinv3x, setMonthly, monthToggle, tradecomtot, 0F, dividendtot, taxtot, "nothing", item1Active, item1Length, item1Able, item2Active, item3Active, item4Active, autobuy, autoratio, auto1x, endpoint, countYear, countMonth, snpNowdays, snpNowVal, snpDiff, setId, relativeprofitrate, localdatatime, accountID!!)
                gameNormalDb?.gameNormalDao()?.insert(newGameNormalDB)
            }
            val addThread = Thread(addRunnable)
            addThread.start()

            isSafeCompleted = true
            val intent = Intent(this@GameNormalActivity, NewResultNormalActivity::class.java)
            startActivity(intent)
            finish()
        }
        else {
            // save gameSpeed setting on the database
            val gameDb = GameSetDB.getInstace(this)
            val gameset = gameSetDb?.gameSetDao()?.getSetWithId(setId, accountID!!)
//            val updateSettingRunnable = Runnable {
//                var newGameSetDB = GameSet()
//                newGameSetDB.id =  gameset!!.id
//                newGameSetDB.setcash = gameset.setcash
//                newGameSetDB.setgamelength = gameset.setgamelength
//                newGameSetDB.setgamespeed = setGamespeed
//                newGameSetDB.setmonthly = gameset.setmonthly
//                newGameSetDB.setsalaryraise = gameset.setsalaryraise
//                newGameSetDB.profitrate = profitrate
//                gameDb?.gameSetDao()?.update(newGameSetDB)
//                Log.d("hongz","이건 뭘까")
//            }
//            val updateSettingThread = Thread(updateSettingRunnable)
//            updateSettingThread.start()
            val intent = Intent(this@GameNormalActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


//    fun funlevelup(u_id: String, u_pw: String, u_exp: Int) {
//        val mContext: Context = this
//        var funlevel_up: RetrofitLevelUp? = null
//        val url = "http://stockgame.dothome.co.kr/test/levelup.php/"
//        var gson: Gson = GsonBuilder()
//            .setLenient()
//            .create()
//        //creating retrofit object
//        var retrofit =
//            Retrofit.Builder()
//                .baseUrl(url)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build()
//        //creating our api
//        funlevel_up = retrofit.create(RetrofitLevelUp::class.java)
//
//        funlevel_up.levelup(getHash(u_id).trim(), getHash(u_pw).trim(), u_exp)
//            .enqueue(object : Callback<DATACLASS> {
//                override fun onFailure(call: Call<DATACLASS>, t: Throwable) {
//                    println("---" + t.message)
//                }
//
//                override fun onResponse(
//                        call: Call<DATACLASS>,
//                        response: retrofit2.Response<DATACLASS>
//                ) {
//                    if (response.isSuccessful && response.body() != null) {
//                        var data: DATACLASS = response.body()!!
//                        if (profileDbManager!!.getLevel()!! != data?.LEVEL) {
//                            islevelup = true
//                        }
//                        profileDbManager!!.setLevel(data?.LEVEL)
//                        profileDbManager!!.setExp(data?.EXP)
//                    }
//                }
//            })
//
//    }

    //도전과제 스텍 보상 함수
    fun rewardByStack(reward: Int){
        if (!profileDbManager!!.isEmpty(this@GameNormalActivity)) {
            profileDbManager!!.setMoney(profileDbManager!!.getMoney()!!+reward)
        }
    }


    fun checkQuestInGameProfit(profitrate: Float, questList: List<Quest>){
        for (i in 0..questList.size-1){
            if(questList?.get(i)?.achievement  == 0){
                when(i){
                    0-> if(profitrate>=10F){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = Runnable{questList?.get(i)?.let {
                            questDb?.questDao()?.insert(it)
                            rewardByStack(10000)
                        }}
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        questAchieved.add(questList[i])
                    }
                    1-> if(profitrate>=20F){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = Runnable{questList?.get(i)?.let {
                            questDb?.questDao()?.insert(it)
                            rewardByStack(20000)
                        }}
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        questAchieved.add(questList[i])
                    }
                    2-> if(profitrate>=30F){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = Runnable{questList?.get(i)?.let {
                            questDb?.questDao()?.insert(it)
                            rewardByStack(30000)
                        }}
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        questAchieved.add(questList[i])
                    }
                    3-> if(profitrate>=50F){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = Runnable{questList?.get(i)?.let {
                            questDb?.questDao()?.insert(it)
                            rewardByStack(50000)
                        }}
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        questAchieved.add(questList[i])
                    }
                    4-> if(profitrate>=100F){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = Runnable{questList?.get(i)?.let {
                            questDb?.questDao()?.insert(it)
                            rewardByStack(100000)
                        }}
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        questAchieved.add(questList[i])
                    }
                    5-> if(profitrate>=200F){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = Runnable{questList?.get(i)?.let {
                            questDb?.questDao()?.insert(it)
                            rewardByStack(200000)
                        }}
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        questAchieved.add(questList[i])
                    }
                    6-> if(profitrate>=300F){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = Runnable{questList?.get(i)?.let {
                            questDb?.questDao()?.insert(it)
                            rewardByStack(300000)
                        }}
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        questAchieved.add(questList[i])
                    }
                    7-> if(profitrate>=400F){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = Runnable{questList?.get(i)?.let {
                            questDb?.questDao()?.insert(it)
                            rewardByStack(400000)
                        }}
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        questAchieved.add(questList[i])
                    }
                    8-> if(profitrate>=500F){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = Runnable{questList?.get(i)?.let {
                            questDb?.questDao()?.insert(it)
                            rewardByStack(500000)
                        }}
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        questAchieved.add(questList[i])
                    }
                    9-> if(profitrate>=1000F){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = Runnable{questList?.get(i)?.let {
                            questDb?.questDao()?.insert(it)
                            rewardByStack(1000000)
                        }}
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        questAchieved.add(questList[i])
                    }
                }
            }
        }
   }


    fun checkQuestRelativeProfit(relativeprofitrate: Float, questList: List<Quest>){
        for(i in 0 .. questList.size-1){
            if(questList?.get(i)?.achievement  == 0){
                when(i){
                    0-> if(relativeprofitrate>=10F){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = Runnable{questList?.get(i)?.let {
                            questDb?.questDao()?.insert(it)
                            rewardByStack(10000)
                        }}
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        questAchieved.add(questList[i])
                    }
                    1-> if(relativeprofitrate>=20F){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = Runnable{questList?.get(i)?.let {
                            questDb?.questDao()?.insert(it)
                            rewardByStack(20000)
                        }}
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        questAchieved.add(questList[i])
                    }
                    2-> if(relativeprofitrate>=50F){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = Runnable{questList?.get(i)?.let {
                            questDb?.questDao()?.insert(it)
                            rewardByStack(50000)
                        }}
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        questAchieved.add(questList[i])
                    }
                    3-> if(relativeprofitrate>=100F){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = Runnable{questList?.get(i)?.let {
                            questDb?.questDao()?.insert(it)
                            rewardByStack(100000)
                        }}
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        questAchieved.add(questList[i])
                    }
                    4-> if(relativeprofitrate>=200F){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = Runnable{questList?.get(i)?.let {
                            questDb?.questDao()?.insert(it)
                            rewardByStack(200000)
                        }}
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        questAchieved.add(questList[i])
                    }

                }
            }
        }
    }

    fun checkQuestSurplus(surplusCount: Int, questList: List<Quest>){
        for(i in 0 .. questList.size-1){
            if(questList?.get(i)?.achievement  == 0){
                when(i){
                    0-> if(surplusCount>=10){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = Runnable{questList?.get(i)?.let {
                            questDb?.questDao()?.insert(it)
                            rewardByStack(10000)
                        }}
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        questAchieved.add(questList[i])
                    }
                    1-> if(surplusCount>=20){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = Runnable{
                            questList?.get(i)?.let { questDb?.questDao()?.insert(it)
                            rewardByStack(20000)
                            }}
                        val addThread = Thread(addRunnable)
                        addThread.start()

                        questAchieved.add(questList[i])
                    }
                    2-> if(surplusCount>=30){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = Runnable{questList?.get(i)?.let {
                            questDb?.questDao()?.insert(it)
                            rewardByStack(30000)
                        }}
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        questAchieved.add(questList[i])
                    }
                    3-> if(surplusCount>=40){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = Runnable{questList?.get(i)?.let {
                            questDb?.questDao()?.insert(it)
                            rewardByStack(50000)
                        }}
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        questAchieved.add(questList[i])
                    }
                    4-> if(surplusCount>=50){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = Runnable{questList?.get(i)?.let {
                            questDb?.questDao()?.insert(it)
                            rewardByStack(100000)
                        }}
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        questAchieved.add(questList[i])
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        profileDbManager!!.write2database()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}