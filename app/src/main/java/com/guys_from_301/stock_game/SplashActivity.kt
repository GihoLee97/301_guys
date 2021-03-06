package com.guys_from_301.stock_game

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.guys_from_301.stock_game.data.*
import com.guys_from_301.stock_game.retrofit.quest
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.talk.TalkApiClient
import com.opencsv.CSVReader
import com.opencsv.CSVReaderBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

////////////////////////////////////////////////////////////////////////////////////////////////////
// CSV to 차트 데이터 ArrayList 생성
val snp_date: ArrayList<String> = ArrayList() // S&P500 날짜
val snp_val: ArrayList<String> = ArrayList() // S&P500 지수 값
val snp_vol: ArrayList<String> = ArrayList() // S&P500 거래량
val fund_date: ArrayList<String> = ArrayList() // Fed Fund Rate 날짜
val fund_val: ArrayList<String> = ArrayList() // Fed Fund Rate 값
val bond_date: ArrayList<String> = ArrayList() // 10 Year Treasury Bond Rate 날짜
val bond_val: ArrayList<String> = ArrayList() // 10 Year Treasury Bond Rate 값
val indpro_date: ArrayList<String> = ArrayList() // 미국 산업 생산량 날짜
val indpro_val: ArrayList<String> = ArrayList() // 미국 산성 생산량 값
val unem_date: ArrayList<String> = ArrayList() // 미국 실업률 날짜
val unem_val: ArrayList<String> = ArrayList() // 미국 실업률 값
val inf_date: ArrayList<String> = ArrayList() // Inflation rate 날짜
val inf_val: ArrayList<String> = ArrayList() // Inflation rate 값
var loadcomp: Boolean = false // 데이터 로드 완료 여부(미완료:0, 완료:1)
////////////////////////////////////////////////////////////////////////////////////////////////////
// 뉴스 데이터//////////////////////
val news_date: ArrayList<String> = ArrayList() // 뉴스 날짜
val news_information: ArrayList<String> = ArrayList() // 뉴스 정보
// 팁 데이터///////////////////////
val tip_title: ArrayList<String> = ArrayList() // 팁 제목
val tip_information: ArrayList<String> = ArrayList() // 팁 내용



//MUST BE INITIALIZED AT FIRST!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!//////////////////////////////////
//push alarm
val CHANNEL_ID = "tardis"
lateinit var notificationManager : NotificationManager
val pushAlarmManager = PushAlarmManager()
//Kakao message Manager
val kakaoMessageManager = KakaoMessageManager()
//share manager
val shareManager = ShareManager()
//capture Manager
val captureUtil = CaptureUtil()
//profileDb Manager
var profileDbManager : ProfileDbManager? = null
//MUST BE INITIALIZED AT FIRST-END!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!//////////////////////////////////

val windowMetrics = DisplayMetrics()
var dpWidth :Int = 0
var dpHeight :Int = 0


class SplashActivity : AppCompatActivity() {
    lateinit var mAdView : AdView
    private lateinit var mAuth : FirebaseAuth
    private var profileDb : ProfileDB? = null
    val SPLASH_VIEW_TIME: Long = 2000 //2초간 스플래시 화면을 보여줌 (ms)

    val printCheck = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash)

//        profileDb = ProfileDB.getInstace(this)
//        var profile = profileDb?.profileDao()?.getAll()!![0]
//        profileDbManager = ProfileDbManager(this, profile)
        profileDbManager = ProfileDbManager(this)
        KakaoSdk.init(this, "0c9ac0ead6e3f965c35fa7c9d0973b7f")
        TalkApiClient.instance
        accountID = profileDbManager!!.getLoginId()
        //광고
        MobileAds.initialize(this){}
        //getWindowSize
        getWindowManager().getDefaultDisplay().getMetrics(windowMetrics)
        val density = getBaseContext().getResources().getDisplayMetrics().density
        dpWidth  = windowMetrics.widthPixels/density.toInt()
        dpHeight = windowMetrics.heightPixels/density.toInt()


        //push alarm channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

        Handler().postDelayed({ //delay를 위한 handler
            if(!profileDbManager!!.isEmpty(this)){
                val loginMethod = profileDbManager!!.getLogin()
                if(loginMethod?.and(1)==1){ // general login
                    getuserinformation(profileDbManager!!.getLoginId()!!, profileDbManager!!.getLoginPw()!!)
                    go2MainActivity()
                }
                else if(loginMethod?.and(2)==2) { // google login
                    if (isGoogleAuthChecked()) {
                        getuserinformation(profileDbManager!!.getLoginId()!!, profileDbManager!!.getLoginPw()!!)
                        go2MainActivity()
                    }
                }
                else if(loginMethod?.and(4)==4){ // kakao login
                    getuserinformation(profileDbManager!!.getLoginId()!!, profileDbManager!!.getLoginPw()!!)
                    KakaoLogin()
                    go2MainActivity()
                }
                else{
                    go2InitialActivity()
                }
            } else{
                go2InitialActivity()
            }
            finish()
        }, SPLASH_VIEW_TIME)

        ////////////////////////////////////////////////////////////////////////////////////////////
        // CSV to 차트 데이터 로드 코루틴 시작
        GlobalScope.launch(Dispatchers.Default) {

            var fileReader: BufferedReader? = null
            var csvReader: CSVReader? = null
            var count = 0

            // 모든 금융 데이터 csv 파일들은 \app\src\main\assets 에 저장
            // 금융 데이터 업데이트 시 해당 파일들 만을 최신화, 나머지는 모두 자동화

            // "news_korean.csv" 파일에서 뉴스 정보 추출
            /////////////////////////////////////////////////////////////////////////////

            try{
//                fileReader = BufferedReader()
                fileReader = BufferedReader(InputStreamReader(getAssets().open("news_korean.csv"),"utf-8"))
                // 헤더 없음 40601 행까지 데이터 있음
                csvReader = CSVReaderBuilder(fileReader).withSkipLines(0).build()
                val news_rs = csvReader.readAll()
                count = 0
                for (news_r in news_rs){
                    news_date.add(count, news_r[0])
                    news_information.add(count, news_r[1])
                    count += 1
                }
            } catch (e: Exception) {
                println("Reading CSV Error : NEWS !") // 에러 메시지 출력
                e.printStackTrace()
            } finally {
                try {
                    fileReader!!.close()
                    csvReader!!.close()
                    count = 0
                } catch (e: IOException) {
                    println("Closing fileReader/csvParser Error : NEWS !") // 에러 메시지 출력
                    e.printStackTrace()
                    count = 0
                }
            }


            // 팁 데이터

            try{
                fileReader = BufferedReader(InputStreamReader(getAssets().open("tip.csv"),"utf-8"))
                // 헤더 없음 61 행까지 데이터 있음
                csvReader = CSVReaderBuilder(fileReader).withSkipLines(0).build()
                val tip_rs = csvReader.readAll()
                count = 0
                for (tip_r in tip_rs){
                    tip_title.add(count, tip_r[0])
                    tip_information.add(count, tip_r[1])
                    count += 1
                }
            } catch (e: Exception) {
                println("Reading CSV Error : TIP !") // 에러 메시지 출력
                e.printStackTrace()
            } finally {
                try {
                    fileReader!!.close()
                    csvReader!!.close()
                    count = 0
                } catch (e: IOException) {
                    println("Closing fileReader/csvParser Error : TIP !") // 에러 메시지 출력
                    e.printStackTrace()
                    count = 0
                }
            }

            ///////////////////////////////////////////////////////////////////////////////////////
            // "^GSPC.csv" 파일로 부터 S&P500 historical data 추출
            try {
                //println("\n--- S&P 500 ---")

                fileReader = BufferedReader(InputStreamReader(getAssets().open("^GSPC.csv")))
                // 1950-01-03 (헤더 포함 5498번쨰 행)부터 거래량 정보 유효(이전은 0, null 아님).
                // 헤더 스킵 및 1962-01-02 (헤더 포함 8511번쨰 행)부터 다른 모든 데이터 유효(bondrate).
                csvReader = CSVReaderBuilder(fileReader).withSkipLines(8510).build()

                val snp_rs = csvReader.readAll()
                count = 0
                for (snp_r in snp_rs) {
                    // snp500에 데이터 추가
                    snp_date.add(count, snp_r[0])
                    snp_val.add(count, snp_r[4])
                    snp_vol.add(count, snp_r[6])
                    count += 1

                    // 입력 확인
                    if(printCheck)
                        println("[SNP] 날짜 : " + snp_date[count - 1] + " | " + "값 : " + snp_val[count - 1] + " | " + "거래량 : " + snp_vol[count - 1] + " | " + "COUNT = $count")
                    // [0]: Date, [1]: Open, [2]: High, [3]: Low, [4]: Close, [5]: Adj Close, [6]: Volume
                    // 1950-01-03 (헤더 포함 5498번쨰 행)부터 거래량 정보 유효(이전은 0, null 아님).
                    // Open, High, Low, Close 값은 1967-06-30 까지 동일, 1967-07-03 (헤더 포함 9897번째 행)부터 세분화 되어 각각의 값이 달라짐.
                    // 참고) 2021-01-27 까지의 데이터 수(거래일)는 총 23380-1개
                }
            } catch (e: Exception) {
                println("Reading CSV Error : SNP !") // 에러 메시지 출력
                println("[SNP] size : " + snp_date.size.toString())
                e.printStackTrace()
            } finally {
                try {
                    fileReader!!.close()
                    csvReader!!.close()
                    if(printCheck)
                        println("[SNP] size : " + snp_date.size.toString())
                    count = 0
                } catch (e: IOException) {
                    println("Closing fileReader/csvParser Error : SNP !") // 에러 메시지 출력
                    e.printStackTrace()
                    count = 0
                }
            }

            ///////////////////////////////////////////////////////////////////////////////////////
            // "fed-funds-rate-historical-chart.csv" 파일로 부터 FED Fund Rate historical data 추출
            try {
                println("\n--- FED Fund Rate ---")

                fileReader = BufferedReader(InputStreamReader(getAssets().open("fed-funds-rate-historical-chart.csv")))
                // 헤더 스킵 및 1962-01-02 (헤더 포함 2759번쨰 행)부터 다른 모든 데이터 유효(bondrate).
                csvReader = CSVReaderBuilder(fileReader).withSkipLines(2758).build()

                val fundrate_rs = csvReader.readAll()
                for (fundrate_r in fundrate_rs) {
                    fund_date.add(count, fundrate_r[0])
                    fund_val.add(count, fundrate_r[1])
                    count += 1

                    //입력 확인
                    if(printCheck)
                        println("[FundRate] 날짜 : " + fund_date[count - 1] + " | " + "값 : " + fund_val[count - 1])
                    // [0]: Date, [1]: value
                    // 1954-07-01 (헤더 포함 17번쨰 행)부터 정보 유효.
                    // 이후 일별 데이터(매일 O, 거래일 X)
                    // 참고) 2021-02-17 까지의 데이터 수는 24048-16개
                    // CSV 파일 끝부분에 날짜는 존재하나 이율 값이 null 인 경우가 있음(# of null 행 < 25)
                }
            } catch (e: Exception) {
                println("Reading CSV Error : Fund Rate !") // 에러 메시지 출력
                println("[FundRate] size : " + fund_date.size.toString())
                e.printStackTrace()
            } finally {
                try {
                    fileReader!!.close()
                    csvReader!!.close()
                    if(printCheck)
                        println("[FundRate] size : " + fund_date.size.toString())
                    count = 0
                } catch (e: IOException) {
                    println("Closing fileReader/csvParser Error : !") // 에러 메시지 출력
                    e.printStackTrace()
                    count = 0
                }
            }

            ///////////////////////////////////////////////////////////////////////////////////////
            // "10-year-treasury-bond-rate-yield-chart.csv" 파일로 부터 미 국채 10년물 이율 historical data 추출
            try {
                println("\n--- 미 국채 10년물 이율 ---")

                fileReader = BufferedReader(InputStreamReader(getAssets().open("10-year-treasury-bond-rate-yield-chart.csv")))
                // 헤더 스킵(16행)
                csvReader = CSVReaderBuilder(fileReader).withSkipLines(16).build()

                val bondrate_rs = csvReader.readAll()
                for (bondrate_r in bondrate_rs) {
                    bond_date.add(count, bondrate_r[0])
                    bond_val.add(count, bondrate_r[1])
                    count += 1

                    //입력 확인
                    if(printCheck)
                        println("[BondRate] 날짜 : " + bond_date[count - 1] + " | " + "값 : " + bond_val[count - 1])
                    // [0]: Date, [1]: value
                    // 1962-01-02 (헤더 포함 17번쨰 행)부터 정보 유효.
                    // 이후 일별 데이터(거래일)
                    // 참고) 2021-01-27 까지의 데이터 수는 14786-16개
                    // CSV 파일 끝부분에 날짜는 존재하나 이율 값이 null 인 경우가 있음(# of null 행 < 25)
                }
            } catch (e: Exception) {
                println("Reading CSV Error : BondRate !") // 에러 메시지 출력
                println("[Bondrate] size : " + bond_date.size.toString())
                e.printStackTrace()
            } finally {
                try {
                    fileReader!!.close()
                    csvReader!!.close()
                    if(printCheck)
                        println("[Bondrate] size : " + bond_date.size.toString())
                    count = 0
                } catch (e: IOException) {
                    println("Closing fileReader/csvParser Error : BondRate !") // 에러 메시지 출력
                    e.printStackTrace()
                    count = 0
                }
            }

            ///////////////////////////////////////////////////////////////////////////////////////
            // "industrial-production-historical-chart.csv" 파일로 부터 Industrial production historical data 추출
            try {
                println("\n--- Industrial production ---")

                fileReader = BufferedReader(InputStreamReader(getAssets().open("industrial-production-historical-chart.csv")))
                // 헤더 스킵 및 1962-01-02 (헤더 포함 521번쨰 행)부터 다른 모든 데이터 유효(bondrate).
                // 521행은 1962-01-01
                csvReader = CSVReaderBuilder(fileReader).withSkipLines(520).build()

                val indpro_rs = csvReader.readAll()
                for (indpro_r in indpro_rs) {
                    indpro_date.add(count, indpro_r[0])
                    indpro_val.add(count, indpro_r[1])
                    count += 1

                    //입력 확인
                    if(printCheck)
                        println("[IndPro] 날짜 : " + indpro_date[count - 1] + " | " + "값 : " + indpro_val[count - 1])
                    // [0]: Date, [1]: value
                    // 1919-01-01 (헤더 포함 17번쨰 행)부터 정보 유효.
                    // 이후 월별 데이터(매월 1일)
                    // 참고) 2020-12-01 까지의 데이터 수는 1240-16개
                    // null 없음
                }
            } catch (e: Exception) {
                println("Reading CSV Error : IndPro !") // 에러 메시지 출력
                println("[IndPro] size : " + indpro_date.size.toString())
                e.printStackTrace()
            } finally {
                try {
                    fileReader!!.close()
                    csvReader!!.close()
                    if(printCheck)
                        println("[IndPro] size : " + indpro_date.size.toString())
                    count = 0
                } catch (e: IOException) {
                    println("Closing fileReader/csvParser Error : IndPro !") // 에러 메시지 출력
                    e.printStackTrace()
                    count = 0
                }
            }

            ///////////////////////////////////////////////////////////////////////////////////////
            // "us-national-unemployment-rate.csv" 파일로 부터 S&P500 historical data 추출
            try {
                println("\n--- Us National Unemployment Rate ---")

                fileReader = BufferedReader(InputStreamReader(getAssets().open("us-national-unemployment-rate.csv")))
                // 헤더 스킵 및 1962-01-02 (헤더 포함 185번쨰 행)부터 다른 모든 데이터 유효(bondrate).
                // 185행은 1962-01-01
                csvReader = CSVReaderBuilder(fileReader).withSkipLines(184).build()

                val unem_rs = csvReader.readAll()
                for (unem_r in unem_rs) {
                    unem_date.add(count, unem_r[0])
                    unem_val.add(count, unem_r[1])
                    count += 1

                    //입력 확인
                    if(printCheck)
                        println("[UnEm] 날짜 : " + unem_date[count - 1] + " | " + "값 : " + unem_val[count - 1])
                    // [0]: Date, [1]: value
                    // 1948-01-01 (헤더 포함 17번쨰 행)부터 정보 유효.
                    // 이후 월별 데이터(표기상 매월 1일)
                    // 참고) 2020-12-01 까지의 데이터 수는 892-16개
                }
            } catch (e: Exception) {
                println("Reading CSV Error : UnEm !") // 에러 메시지 출력
                println("[UnEm] size : " + unem_date.size.toString())
                e.printStackTrace()
            } finally {
                try {
                    fileReader!!.close()
                    csvReader!!.close()
                    println("[UnEm] size : " + unem_date.size.toString())
                    count = 0
                } catch (e: IOException) {
                    println("Closing fileReader/csvParser Error : UnEm !") // 에러 메시지 출력
                    e.printStackTrace()
                    count = 0
                }
            }

            ///////////////////////////////////////////////////////////////////////////////////////
            // "historical-inflation-rate-by-year.csv" 파일로 부터 Inflation rate historical data 추출
            try {
                println("\n--- Inflation rate ---")

                fileReader = BufferedReader(InputStreamReader(getAssets().open("historical-inflation-rate-by-year.csv")))
                // 헤더 스킵 및 1962-01-02 (헤더 포함 63번쨰 행)부터 다른 모든 데이터 유효(bondrate).
                // 521행은 1961-12-01
                csvReader = CSVReaderBuilder(fileReader).withSkipLines(62).build()

                val infrate_rs = csvReader.readAll()
                for (infrate_r in infrate_rs) {
                    inf_date.add(count, infrate_r[0])
                    inf_val.add(count, infrate_r[1])
                    count += 1

                    //입력 확인
                    if(printCheck)
                        println("[InfRate] 날짜 : " + inf_date[count - 1] + " | " + "값 : " + inf_val[count - 1])
                    // [0]: Date, [1]: value
                    // 1914-12-01 (헤더 포함 17번쨰 행)부터 정보 유효.
                    // 이후 년별 데이터(표기상 매년 12월 1일)
                    // 참고) 2020-12-01 까지의 데이터 수는 123-16개
                    // CSV 파일 마지막 행은 유효하지 않은 데이터(null 은 아님)
                }
            } catch (e: Exception) {
                println("Reading CSV Error : InfRate !") // 에러 메시지 출력
                println("[InfRate] size : " + inf_date.size.toString())
                e.printStackTrace()
            } finally {
                try {
                    fileReader!!.close()
                    csvReader!!.close()
                    println("[InfRate] size : " + inf_date.size.toString())
                    count = 0
                } catch (e: IOException) {
                    println("Closing fileReader/csvParser Error : InfRate !") // 에러 메시지 출력
                    e.printStackTrace()
                    count = 0
                }
            }
            println("Data Load Complete!") // 데이터 입력 종료

            loadcomp = true
        }
        ////////////////////////////////////////////////////////////////////////////////////////////

    }

    private fun isGoogleAuthChecked() : Boolean{
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        if(user!=null){ // available access authority
            val intent = Intent(this, MainActivity::class.java) //Main으로 이동
            startActivity(intent)
            return true
        }
        return false
    }

    private fun KakaoLogin(){
        data class Check(var x: Boolean = true)
        // 로그인 공통 callback 구성
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("KAKAO", "로그인 실패", error)
            }
            else if (token != null) {
                Log.i("KAKAO", "로그인 성공 ${token.accessToken}")
            }
        }
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (LoginClient.instance.isKakaoTalkLoginAvailable(this)) {
            LoginClient.instance.loginWithKakaoTalk(this, callback = callback)
        } else {
            LoginClient.instance.loginWithKakaoAccount(this, callback = callback)
        }

    }

    private fun go2MainActivity(){
        val intent = Intent(this, MainActivity::class.java) //Main으로 이동
        startActivity(intent)
    }

    private fun go2InitialActivity(){
        val intent = Intent(this, NewInitialActivity::class.java) //Initial으로 이동
        startActivity(intent)
    }

    override fun onStop() {
        super.onStop()
        profileDbManager!!.write2database()
    }

    private var gameSetDb: GameSetDB? = null
    //noticeDb
    private var noticeDb: NoticeDB? = null
    private var itemDb : ItemDB? = null
    private var questDb : QuestDB? = null
    fun getuserinformation(u_id: String, u_pw: String) {
        var fungetuserinformation: RetrofitGet? = null
        accountID = u_id//사용자 아이디 식별에 사용할 변수 설정
        val url = "http://stockgame.dothome.co.kr/test/getall.php/"
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
        fungetuserinformation = retrofit.create(RetrofitGet::class.java)
        fungetuserinformation.getall(
            com.guys_from_301.stock_game.getHash(u_id).trim(), com.guys_from_301.stock_game.getHash(u_pw).trim()).enqueue(object :
            Callback<DATACLASS> {
            override fun onFailure(call: Call<DATACLASS>, t: Throwable) {
                println("---"+t.message)
            }
            override fun onResponse(call: Call<DATACLASS>, response: retrofit2.Response<DATACLASS>) {
                if (response.isSuccessful && response.body() != null) {
                    var data: DATACLASS? = response.body()!!
                    println("---1: "+data?.NICKNAME!!)
                    println("---2: "+data?.NICKNAME!!)
                    gameSetDb = GameSetDB?.getInstace(this@SplashActivity)
                    itemDb = ItemDB?.getInstace(this@SplashActivity)
                    profileDbManager!!.refresh(this@SplashActivity)
                    var serverProfile = Profile(profileDbManager!!.getId(), data?.NICKNAME!!, data?.MONEY, data?.VALUE1!!, data?.PROFITRATE!!, data?.RELATIVEPROFITRATE, data?.ROUNDCOUNT, data?.HISTORY,data?.LEVEL,data?.EXP,0,profileDbManager!!.getLogin()!!,u_id,u_pw,data?.IMAGENUMBER!!,"")
                    if(data?.OVERWRITE!! == 0){
                        if(profileDbManager!!.getNickname()=="#########first_login##########"){
                            Log.d("Giho","#########first_login##########AndReadFromServer")
                            val newProfile = Profile()
                            newProfile.id = profileDbManager!!.getId()
                            newProfile.nickname = data?.NICKNAME!!
                            newProfile.history = data?.HISTORY!!
                            newProfile.level = data?.LEVEL!!
                            newProfile.exp = data?.EXP!!
                            newProfile.login = profileDbManager!!.getLogin()!!
                            newProfile.money = data?.MONEY!!
                            newProfile.value1 = data?.VALUE1!!
                            newProfile.relativeprofitrate = data?.RELATIVEPROFITRATE!!
                            newProfile.roundcount = data?.ROUNDCOUNT!!
                            newProfile.login_id = u_id
                            newProfile.login_pw = u_pw
                            newProfile.imageNum = data?.IMAGENUMBER!!
                            newProfile.profitrate = data?.PROFITRATE

                            //초기 게임 setting 저장
                            gameSetDb = GameSetDB.getInstace(this@SplashActivity)
                            val r = Runnable {
                                val newGameSet = GameSet()
                                setId = u_id
                                newGameSet.id = accountID+0
                                newGameSet.setcash = 0
                                newGameSet.setgamelength = START_GAME_LENGTH
                                newGameSet.setgamespeed = START_GAME_SPEED
                                newGameSet.setmonthly = 0
                                newGameSet.setsalaryraise = 0
                                newGameSet.accountId = accountID!!
                                gameSetDb?.gameSetDao()?.insert(newGameSet)
                                newGameSet.id = accountID+0
                                gameSetDb?.gameSetDao()?.insert(newGameSet)
                                Log.d("hongz", "초기 gaemset 추가 [id]: " +accountID+0 )
                            }
                            val t = Thread(r)
                            t.start()

                            profileDbManager!!.updateManager(newProfile)
                        }
                        else if(profileDbManager!!.getHashRespectFromDbManager()== profileDbManager!!.getHashRespectFromInput(serverProfile)){
                            Log.d("Giho","서버와 동일")
                        } else {
                            if (profileDbManager!!.getHashRespectFromDbManager() == profileDbManager!!.getHash()) {
                                Log.d("Giho", "기기가 옳음")
                                //TODO: 서버로 전송 : 기기의 정보가 신뢰할 수 있으며 최신
                                update(
                                    getHash(profileDbManager!!.getLoginId()!!).trim(),
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
                            } else { // 신뢰할 수 없는 기기정보 -> 서버 것으로 리셋
                                Log.d("Giho", "신뢰할 수 없는 기기 정보")
                                Log.d(
                                    "Giho",
                                    "manager nickname : " + profileDbManager!!.getNickname()
                                )
                                val newProfile = Profile()
                                newProfile.id = profileDbManager!!.getId()
                                newProfile.nickname = data?.NICKNAME!!
                                newProfile.history = data?.HISTORY!!
                                newProfile.level = data?.LEVEL!!
                                newProfile.exp = data?.EXP!!
                                newProfile.login = profileDbManager!!.getLogin()!!
                                newProfile.money = data?.MONEY!!
                                newProfile.value1 = data?.VALUE1!!
                                newProfile.relativeprofitrate = data?.RELATIVEPROFITRATE!!
                                newProfile.roundcount = data?.ROUNDCOUNT!!
                                newProfile.login_id = u_id
                                newProfile.login_pw = u_pw
                                newProfile.imageNum = data?.IMAGENUMBER!!
                                newProfile.profitrate = data?.PROFITRATE

                                profileDbManager!!.updateManager(newProfile)
                                Log.d(
                                    "Giho",
                                    "manager nickname : " + profileDbManager!!.getNickname()
                                )
                            }
                        }
                    }
                    else if(data?.OVERWRITE!! == 1) {
                        Log.d("Giho", "이전과 동일한 계정으로 로그인")
                        val newProfile = Profile()
                        newProfile.id = profileDbManager!!.getId()
                        newProfile.nickname = data?.NICKNAME!!
                        newProfile.history = data?.HISTORY!!
                        newProfile.level = data?.LEVEL!!
                        newProfile.exp = data?.EXP!!
                        newProfile.login = profileDbManager!!.getLogin()!!
                        newProfile.money = data?.MONEY!!
                        newProfile.value1 = data?.VALUE1!!
                        newProfile.relativeprofitrate = data?.RELATIVEPROFITRATE!!
                        newProfile.roundcount = data?.ROUNDCOUNT!!
                        newProfile.login_id = u_id
                        newProfile.login_pw = u_pw
                        newProfile.imageNum = data?.IMAGENUMBER!!
                        newProfile.profitrate = data?.PROFITRATE
                        profileDbManager!!.updateManager(newProfile)
                    }
                    questdecode(data?.QUEST!!)

                    val newQuest = Quest()
                    newQuest.id = 31
                    newQuest.achievement = data?.QUEST2!!
                    newQuest.theme = questDb?.questDao()?.getAll()!![30].theme
                    newQuest.questcontents = questDb?.questDao()?.getAll()!![30].questcontents
                    newQuest.reward = questDb?.questDao()?.getAll()!![30].reward
                    questDb?.questDao()?.update(newQuest)

                    val newGameset = GameSet()
                    newGameset.id = u_id+"0"

                    if(data?.SETCASH == 10000F) newGameset.setcash = 0
                    else if(data?.SETCASH == 50000F) newGameset.setcash = 1
                    else if(data?.SETCASH == 100000F) newGameset.setcash = 2
                    else if(data?.SETCASH == 500000F) newGameset.setcash = 3
                    else if(data?.SETCASH == 1000000F) newGameset.setcash = 4

                    if(data?.SETMONTHLY == 1000F ) newGameset.setmonthly = 0
                    else if(data?.SETMONTHLY == 1500F ) newGameset.setmonthly = 1
                    else if(data?.SETMONTHLY == 2000F ) newGameset.setmonthly = 2
                    else if(data?.SETMONTHLY == 5000F ) newGameset.setmonthly = 3
                    else if(data?.SETMONTHLY == 10000F ) newGameset.setmonthly = 4

                    if(data?.SETSALARYRAISE == 4F) newGameset.setsalaryraise = 0
                    else if(data?.SETSALARYRAISE == 5F) newGameset.setsalaryraise = 1
                    else if(data?.SETSALARYRAISE == 6F) newGameset.setsalaryraise = 2
                    else if(data?.SETSALARYRAISE == 8F) newGameset.setsalaryraise = 3
                    else if(data?.SETSALARYRAISE == 10F) newGameset.setsalaryraise = 4
                    newGameset.setgamelength = START_GAME_LENGTH
                    newGameset.setgamespeed = START_GAME_SPEED
                    newGameset.accountId = u_id
                    accountID = u_id
                    Log.d("hongz", "초기 gameset 추가 2 [id]: "+u_id+"0")
                    println("---"+ newGameset.setsalaryraise)
                    println("---")
                    gameSetDb?.gameSetDao()?.insert(newGameset)

                    val newItem = Item()
                    newItem.id = 1
                    newItem.lasttime = itemDb?.itemDao()?.getLasttime()!!
                    newItem.potion = data?.POTION
                    itemDb?.itemDao()?.update(newItem)
                }
            }
        })
    }

    //업적 디코딩
    private fun questdecode(questcode : Int){
        var cnt = 0
        var tmp = 0
        questDb = QuestDB.getInstance(this@SplashActivity)
        var stringquest = Integer.toBinaryString(questcode)

        //가져온 string은 숫자앞에서부터 저장된다. 13=1101(2) 1, 1, 0, 1 순서
        // '0' 은 48 '1'은 49
        for(i in 0..stringquest.length - 1){
            val newQuest = Quest()
            newQuest.id = stringquest.length -i
            newQuest.achievement = stringquest[i].toInt()-48
            newQuest.theme = questDb?.questDao()?.getAll()!![stringquest.length -i -1].theme
            newQuest.questcontents = questDb?.questDao()?.getAll()!![stringquest.length -i -1].questcontents
            newQuest.reward = questDb?.questDao()?.getAll()!![stringquest.length -i -1].reward
            questDb?.questDao()?.update(newQuest)
        }
    }


}