package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.GameNormal
import com.example.myapplication.data.GameNormalDB
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


////////////////////////////////////////////////////////////////////////////////////////////////////
// 자산 관련 변수들 생성
var asset: Float = 0F // 총 자산
var cash: Float = 5000000F // 보유 현금
var bought: Float = 0F // 총 매수금액
var sold: Float = 0F // 총 매도금액
var evaluation: Float = 0F // 평가금액
var profit: Float = 0F // 순손익
var profitrate: Float = 0F // 수익률

var quant1x: Float = 0F // 1x 보유 수량
var quant3x: Float = 0F // 3x 보유 수량
var quantinv1x: Float = 0F // -1x 보유 수량
var quantinv3x: Float = 0F // -3x 보유 수량

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

var tradecom: Float = 1.001F // 거래수수료(Trade Commission): 0.1% , 거래시 차감(곱하는 방식)
var tradecomtot: Float = 0F // 거래수수료 총 합
var feex1: Float = 0.00003F // 운용수수료(VOO: 0.03%), 년 단위로 매입금액에서 차감
var feex3: Float = 0.0095F // 운용수수료(UPRO: 0.95%), 년 단위로 매입금액에서 차감
var dividend: Float = 0.0153F // 배당금(VOO: 1.53%), 년 단위로 현금으로 입금
var tax: Float = 0F // 세금
// var currency: Float = 0F // 환율(현재 미반영)


// 매수, 매도 외 기타 버튼 클릭 시 사용되는 변수 /////////////////////////////////////////////////////
// 일시정지 시 현재 값 저장
private var snpNowDate: String = "yyyy-mm-dd"
private var snpNowdays: Int = 0
private var snpNowVal: Float = 0F
private var snpDiff: Float = 0F
private var discrepancy: Float = 0.995F // 괴리율
////////////////////////////////////////////////////////////////////////////////////////////////////





// 버튼 클릭 판별자 생성 /////////////////////////////////////////////////////////////////////////////
var click: Boolean = false // 매수, 매도, 자동, 아이템 다이얼로그의 버튼들에 적용
var gameend: Boolean = false // 게임 종료시 적용
////////////////////////////////////////////////////////////////////////////////////////////////////


class GameNormalActivity : AppCompatActivity() {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // 차트 데이터 및 차트 설정 변수 생성
    private val gl = 2500 // Game Length: 10, 20년(휴일, 공휴일로 인해 1년은 대략 250 거래일)
    private val given = 1250 // 게임 시작시 주어지는 과거 데이터의 구간: 5년

    // 유효구간 가운데 랜덤으로 시작 시점 산출 /////////////////////////////////////////////////////
    // 5년은 대략 1250 거래일.
    // 게임 시작 시점으로부터 5년 전, 10년 후의 데이터 확보가 가능해야함.
    // 일부 데이터는 뒤에서 약 21번째 행까지 날짜는 존재하나 값들은 null 인 경우가 존재함 -> 범위에서 30만큼 빼줌.
    // 따라서 시작시점은 총 데이터 갯수로부터 15년에 해당하는 3750 + 30을 뺀 구간에서,
    // 랜덤으로 숫자를 산출한 뒤 다시 1250을 더해준 값임.
    private val random = Random()
    private val sp = random.nextInt((snp_date.size - gl - given - 30)) + given // Starting Point

    // 값 표준화: 시작일(sp)을 100으로
    private val criteria: Float = 100/(snp_val[sp].toFloat())


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


    // Count 값들 ///////////////////////////////////////////////////////////////////////////////////
    private var dayPlus: Int = 1 // sp(Starting Point) 이후 경과한 거래일 수
    private var fundCount: Int = 0
    private var bondCount: Int = 0
    private var indproCount: Int = 0
    private var unemCount: Int = 0
    private var infCount: Int = 0


    // 시간관련 /////////////////////////////////////////////////////////////////////////////////////
    // oneday + btnRefresh = 게임상에서의 1 거래일의 실제 시간
    private val oneday: Long = 240 // 거래일 간 간격 [ms]
    private val btnRefresh: Long = 10 // 버튼 Refresh 조회 간격 [ms]
    ////////////////////////////////////////////////////////////////////////////////////////////////

    // Roomdata관련 /////////////////////////////////////////////////////////////////////////////////
    private var gameNormalDb : GameNormalDB? = null
    private var gameHistory = listOf<GameNormal>()

    //변수 선언 /////////////////////////////////////////////////////////////////////////////////////



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_normal)

        gameNormalDb = GameNormalDB.getInstace(this)


        val startRunnable = Runnable {
            gameHistory = gameNormalDb!!.gameNormalDao().getAll()
        }
        val startThread = Thread(startRunnable)
        startThread.start()




        //Button 동작

        //매수
        findViewById<Button>(R.id.btn_buy).setOnClickListener {
            val dlgBuy = Dialog_buy(this)
            dlgBuy.start()
            click = !click //////////////////////////////////////////////////////////////////////////
        }

        //매도
        findViewById<Button>(R.id.btn_sell).setOnClickListener {
            val dlgSell = Dialog_sell(this)
            dlgSell.start()
            click = !click /////////////////////////////////////////////////////////////////////////
        }


        findViewById<Button>(R.id.btn_auto).setOnClickListener {
            click = !click /////////////////////////////////////////////////////////////////////////
        }


        findViewById<Button>(R.id.btn_item).setOnClickListener {
            click = !click /////////////////////////////////////////////////////////////////////////
        }


        // 차트 ////////////////////////////////////////////////////////////////////////////////////
        // 차트 click, gameend 변수 초기화
        click = false
        gameend  = false

        // 차트 코루틴 시작
        CoroutineScope(Dispatchers.Default).launch {
            val job1 = launch {
                chartdata()
            }

            job1.join()

            val job2 = launch {
                inidraw()
            }

            job2.join()

            val job3 = launch {
                nowdraw()
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////
    }

    // 데이터 가지고 오기
    fun getRoomListDataHttp() {
        val u_id = ""
        val u_pw = ""
        val u_date = ""
        val url: String = "http://stockgame.dothome.co.kr/test/call.php/"
        Log.d("데이터 받기 ", "받기시도 중")
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
        var server = retrofit.create(RetrofitGet::class.java)
        server.getdata(u_id, u_pw, u_date).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                //Toast.makeText(this@Initial, " ", Toast.LENGTH_LONG).show()
                //Log.d("data: ",data)
            }

            override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                //Toast.makeText(this@Initial, "bbbbbbb", Toast.LENGTH_LONG).show()
                if (response.isSuccessful && response.body() != null) {
                    val getted_name: String = response.body()!!
                    Toast.makeText(this@GameNormalActivity, getted_name, Toast.LENGTH_LONG).show()
                    Log.d("---:", response.isSuccessful.toString())
                    // Toast.makeText(this@GameNormalActivity, response.isSuccessful, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onBackPressed() {
        val dlg_exit = Dialog_game_exit(this@GameNormalActivity)
        dlg_exit.start()
        click = !click /////////////////////////////////////////////////////////////////////////////
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // 차트 Entry, LineData, LineDataSet 생성 및 입력, 경제지표 과거 5년 차트 생성
    private fun chartdata() {
        // Entry 배열 초기값 입력
        snpEn.add(Entry(-1250F, snp_val[sp - given].toFloat()*criteria))
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
                Entry((i + 1 - given).toFloat(), snp_val[sp - given + 1 + i].toFloat()*criteria),
                0
            )

            var sf = SimpleDateFormat("yyyy-MM-dd") // 날짜 형식
            var snpDate = snp_date[sp - given + 1 + i]
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
        println("랜덤넘버 COUNT : " + sp.toString() + " | " + "시작 날짜 : " + snp_date[sp])
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
    }


    // Real time 차트 생성 및 현재 데이터 저장
    private suspend fun nowdraw() {
        while (true) {
            if (!gameend) {
                if (!click) {
                    if (dayPlus <= gl) {

                        var sf = SimpleDateFormat("yyyy-MM-dd") // 날짜 형식
                        var snpDate = snp_date[sp + dayPlus]
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

                        snpD.addEntry(Entry(dayPlus.toFloat(), snp_val[sp + dayPlus].toFloat()*criteria), 0)

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
                        println("fund date : $fundDate")

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

                        println("S&P : " + snp_date[sp + dayPlus] + " | " + "Fund : " + fund_date[fundIndex] + " | " + "Bond : " + bond_date[bondIndex] + " | " + "IndPro : " + indpro_date[indproIndex - 1] + " | " + "UnEm : " + unem_date[unemIndex - 1] + " | " + "Inf : " + inf_date[infIndex - 1])

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

                        // 차트 축 이동
                        findViewById<LineChart>(R.id.cht_snp).moveViewToX(dayPlus.toFloat())
                        findViewById<LineChart>(R.id.cht_fund).moveViewToX(dayPlus.toFloat())
                        findViewById<LineChart>(R.id.cht_bond).moveViewToX(dayPlus.toFloat())
                        findViewById<LineChart>(R.id.cht_indpro).moveViewToX(dayPlus.toFloat())
                        findViewById<LineChart>(R.id.cht_unem).moveViewToX(dayPlus.toFloat())
                        findViewById<LineChart>(R.id.cht_inf).moveViewToX(dayPlus.toFloat())


                        // 값 OutPut ///////////////////////////////////////////////////////////////
                        // 현재 값 저장
                        snpNowDate = snp_date[sp + dayPlus]
                        snpNowdays = dayPlus
                        snpNowVal = snp_val[sp + dayPlus].toFloat()
                        snpDiff = snp_val[sp + dayPlus].toFloat() / snp_val[sp + dayPlus - 1].toFloat() - 1F
                        println("현재 날짜 : $snpNowDate | 현재 경과 거래일 : $snpNowdays | 현재 S&P 500 지수 값 : $snpNowVal | 등락 : $snpDiff")

                        // 변동 및 괴리율 반영
                        price1x *= (1F + snpDiff * discrepancy) //
                        price3x *= (1F + 3*snpDiff * discrepancy) //
                        priceinv1x *= (1F - snpDiff * discrepancy) //
                        priceinv3x *= (1F - 3*snpDiff * discrepancy) //

                        val1x = quant1x * price1x //
                        val3x = quant3x * price3x //
                        valinv1x = quantinv1x * priceinv1x //
                        valinv3x = quantinv3x * priceinv3x //

                        buylim1x = (cash / (price1x * tradecom)) //
                        buylim3x = (cash / (price3x * tradecom)) //
                        buyliminv1x = (cash / (priceinv1x * tradecom)) //
                        buyliminv3x = (cash / (priceinv3x * tradecom)) //
                        println("매수 한계 : $buylim1x | $buylim3x | $buyliminv1x | $buyliminv3x")

                        evaluation = val1x + val3x + valinv1x + valinv3x // 평가금액
                        profit = evaluation - bought + sold// 순손익
                        // 수익률
                        if (bought==0F) {
                            profitrate = 0F // Inf 방지
                        }
                        else {
                            profitrate = 100F * profit / bought
                        }
                        asset = cash + evaluation // 총 자산

                        findViewById<TextView>(R.id.tv_asset).text = "총 자산 : "+asset.toString()
                        findViewById<TextView>(R.id.tv_cash).text = "현금 : "+ cash.toString()
                        findViewById<TextView>(R.id.tv_evaluation).text = "평가금액 : "+ evaluation.toString()
                        findViewById<TextView>(R.id.tv_bought).text = "매입금액 : "+ bought.toString()
                        findViewById<TextView>(R.id.tv_profit).text = "순손익 : "+ profit.toString()
                        findViewById<TextView>(R.id. tv_profitrate).text = "수익률 : "+ profitrate.toString()+" %"



//                        val tvItem1 = findViewById<TextView>(R.id.tv_item1)
//                        val tvItem2 = findViewById<TextView>(R.id.tv_item2)
//                        val tvItem3 = findViewById<TextView>(R.id.tv_item3)

                        dayPlus += 1 // 시간 진행

                        delay(oneday) // 게임상에서 1 거래일의 실제시간
                    } else {
                        println("게임 끝")
                        break
                    }
                } else {
                }
            } else {
                break
            }
            delay(btnRefresh)
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

}