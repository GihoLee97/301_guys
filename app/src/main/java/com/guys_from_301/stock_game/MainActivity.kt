package com.guys_from_301.stock_game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.guys_from_301.stock_game.data.*
import com.guys_from_301.stock_game.retrofit.RetrofitRanking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.onesignal.OneSignal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


const val START_CASH = 10000000F
const val START_MONTHLY = 1000000F
const val START_SALARY_RAISE = 6F
const val START_GAME_LENGTH = 30
const val START_GAME_SPEED = 1
//푸쉬알람 ID
const val ONESIGNAL_APP_ID = "b096a9b0-2d91-48b5-886b-b3e8d6ba0116"
var rank1_nick :String = ""; var rank1_money :String = ""; var rank2_nick :String = ""; var rank2_money :String = ""
var rank3_nick :String = ""; var rank3_money :String = ""; var rank4_nick :String = ""; var rank4_money :String = ""
var rank5_nick :String = ""; var rank5_money :String = ""; var rank6_nick :String = ""; var rank6_money :String = ""
var rank7_nick :String = ""; var rank7_money :String = ""; var rank8_nick :String = ""; var rank8_money :String = ""
var rank9_nick :String = ""; var rank9_money :String = ""; var rank10_nick :String = ""; var rank10_money :String = ""

class MainActivity : AppCompatActivity() {

    private var gameSetDb: GameSetDB? = null
    var itemDb: ItemDB? = null
    val mContext: Context = this
    var profileDb : ProfileDB? = null
    private lateinit var btn_profile : Button
    private lateinit var btn_setting : Button
    private lateinit var btn_game : Button
    private lateinit var btn_market : Button
    private lateinit var btn_ranking: Button
    private lateinit var btn_friend: Button
    private lateinit var btn_sendKakaoMessageMyself: Button
    private lateinit var btn_sendKakaoMessageToOthers: Button
    private lateinit var btn_shareText: Button
    private lateinit var btn_pushAlarm: Button
    private lateinit var btn_captureView: Button


    lateinit var mAdView : AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        itemDb = ItemDB?.getInstace(mContext!!)
        profileDb = ProfileDB?.getInstace(mContext!!)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gameSetDb = GameSetDB.getInstace(this)
        // 광고 관련 코드
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        //푸쉬알람 관련 코드
        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)

        btn_profile = findViewById(R.id.btn_profile)
        btn_setting = findViewById(R.id.btn_setting)
        btn_game =  findViewById(R.id.btn_game)
        btn_market = findViewById(R.id.btn_market)
        btn_ranking = findViewById(R.id.btn_ranking)
        btn_friend = findViewById(R.id.btn_friend)
        btn_sendKakaoMessageMyself = findViewById(R.id.btn_sendKakaoMessageMyself)
        btn_sendKakaoMessageToOthers = findViewById(R.id.btn_sendKakaoMessageToOthers)
        btn_shareText = findViewById(R.id.btn_shareText)
        btn_pushAlarm = findViewById(R.id.btn_pushAlarm)
        btn_captureView = findViewById(R.id.btn_captureView)
        btn_game.isEnabled = false // 로딩 미완료 상태일 때 게임 버튼 비활성화

        btn_friend.setOnClickListener{
            val intent = Intent(this,FriendActivity::class.java)
            startActivity(intent)
        }


        btn_profile.setOnClickListener{
            val intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
        }

        btn_setting.setOnClickListener {
            val intent = Intent(this,SettingActivity::class.java)
            startActivity(intent)
        }

        btn_game.setOnClickListener{
            var profileDb: ProfileDB? = null
            profileDb = ProfileDB.getInstace(this)
            var money = profileDb?.profileDao()?.getMoney()!!

            if(money <= 0){
                Toast.makeText(this, "현금이 없습니다.", Toast.LENGTH_LONG).show()
            }
            else{
                val dialog = Dialog_loading(this@MainActivity)
                dialog.show()
                val intentgame = Intent(this, PickGameActivity::class.java)
                val intent = Intent(this,GameNormalActivity::class.java)
                if(gameSetDb?.gameSetDao()?.getAll()?.isEmpty() == true)    {
                    val addRunnable = Runnable {
                        val newGameSetDB = GameSet()
                        setId = 1
                        newGameSetDB.id = 1
                        newGameSetDB.setcash = START_CASH
                        newGameSetDB.setgamelength = START_GAME_LENGTH
                        newGameSetDB.setgamespeed = START_GAME_SPEED
                        newGameSetDB.setmonthly = START_MONTHLY
                        newGameSetDB.setsalaryraise = START_SALARY_RAISE
                        gameSetDb?.gameSetDao()?.insert(newGameSetDB)
                    }
                    val addThread = Thread(addRunnable)
                    addThread.start()
                    startActivity(intent)
                    dialog.dismiss()
                }
                else {
//                    setCash = gameSetDb?.gameSetDao()?.getSetCash()!!
//                    setMonthly = gameSetDb?.gameSetDao()?.getSetMonthly()!!
//                    setSalaryraise = gameSetDb?.gameSetDao()?.getSetSalaryRaise()!!
//                    setGamespeed = gameSetDb?.gameSetDao()?.getSetGameSpeed()!!
                    startActivity(intentgame)
                    dialog.dismiss()
                }
            }
        }

        btn_market.setOnClickListener {
            val intent = Intent(this,MarketActivity::class.java)
            startActivity(intent)
        }
        btn_ranking.setOnClickListener{
            val intent = Intent(this, RankingActivity::class.java)
            startActivity(intent)
        }
        btn_sendKakaoMessageMyself.setOnClickListener {
            kakaoMessageManager.sendMessageMyself()
        }
        btn_sendKakaoMessageToOthers.setOnClickListener {
            kakaoMessageManager.sendMessage()
        }
        btn_shareText.setOnClickListener {
            shareManager.shareText(this,"텍스트 공유하기 성공~")
        }
        btn_pushAlarm.setOnClickListener {
            pushAlarmManager.generateAndPushAlarm(this)
        }
        btn_captureView.setOnClickListener {
            captureUtil.captureAndSaveView(findViewById(R.id.textView2))
        }

        // 피로도 저감 코드
        CoroutineScope(Dispatchers.IO).launch {
            val job1 = launch {
                value1reduc()
            }
        }

        while (true) {
            if (loadcomp) {
                btn_game.isEnabled = true
                btn_game.text = "게임"
                break
            }
            Thread.sleep(50)
        }
        ranking("a")
    }

    override fun onStart() {
        super.onStart()
        ranking("a")
    }
    // 두번 누르면 종료되는 코드
    var time3: Long = 0
    override fun onBackPressed() {
        // 서버에 올리는 코드(피로도)
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
        val time1 = System.currentTimeMillis()
        val time2 = time1 - time3
        if (time2 in 0..2000) {
            // 이거 3줄 다 써야 안전하게 종료
            moveTaskToBack(true)
            finishAffinity()
            android.os.Process.killProcess(android.os.Process.myPid())
        }
        else {
            time3 = time1
            Toast.makeText(applicationContext, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 랭킹 받아오는 코드

    fun ranking(u_id: String) {
        var funranking: RetrofitRanking? = null
        val url = "http://stockgame.dothome.co.kr/test/ranking.php/"
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
        funranking = retrofit.create(RetrofitRanking::class.java)
        funranking.funranking(u_id).enqueue(object : Callback<Array<String>> {
            override fun onFailure(call: Call<Array<String>>, t: Throwable) {
                //Toast.makeText(this@InitialActivity, "아이디나 비밀번호가 맞지 않습니다.", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Array<String>>, response: retrofit2.Response<Array<String>>) {
                if (response.isSuccessful && response.body() != null) {
                    rank1_nick = response.body()!![0]; rank1_money = response.body()!![10]
                    rank2_nick = response.body()!![1]; rank2_money = response.body()!![11]
                    rank3_nick = response.body()!![2]; rank3_money = response.body()!![12]
                    rank4_nick = response.body()!![3]; rank4_money = response.body()!![13]
                    rank5_nick = response.body()!![4]; rank5_money = response.body()!![14]
                    rank6_nick = response.body()!![5]; rank6_money = response.body()!![15]
                    rank7_nick = response.body()!![6]; rank7_money = response.body()!![16]
                    rank8_nick = response.body()!![7]; rank8_money = response.body()!![17]
                    rank9_nick = response.body()!![8]; rank9_money = response.body()!![18]
                    rank10_nick = response.body()!![9]; rank10_money = response.body()!![19]
                }
            }
        })
    }

    // 피로도 저감 코드
    suspend fun value1reduc() {
        while (true) {
            var lasttime = itemDb?.itemDao()?.getLasttime()!! // 마지막 피로도 저감 시간
            var nowtime = System.currentTimeMillis() // 현재 시간

            if ((nowtime - lasttime) >= 1000L) {
                var nowvalue1 = profileDb?.profileDao()?.getValue1()!!
                var value1temp = 1 * (((nowtime - lasttime)/1000L).toFloat()).roundToInt() // 분당 1 씩 저감

                if ((nowvalue1 - value1temp) <= 0 ) {
                    nowvalue1 = 0
                } else {
                    nowvalue1 -= value1temp
                }

                // DB 피로도 업데이트
                val newProfile = Profile()
                newProfile.id = profileDb?.profileDao()?.getId()?.toLong()
                newProfile.nickname = profileDb?.profileDao()?.getNickname()!!
                newProfile.history = profileDb?.profileDao()?.getHistory()!!
                newProfile.level = profileDb?.profileDao()?.getLevel()!!
                newProfile.exp = profileDb?.profileDao()?.getExp()!!
                newProfile.rank = profileDb?.profileDao()?.getRank()!!
                newProfile.login = profileDb?.profileDao()?.getLogin()!!
                newProfile.money = profileDb?.profileDao()?.getMoney()!!
                newProfile.value1 = nowvalue1
                newProfile.profit = profileDb?.profileDao()?.getProfit()!!
                newProfile.login_id = profileDb?.profileDao()?.getLoginid()!!
                newProfile.login_pw = profileDb?.profileDao()?.getLoginpw()!!
                profileDb?.profileDao()?.update(newProfile)
                //

                // 피로도 저감 시간 저장
                val newItem = Item()
                newItem.id = itemDb?.itemDao()!!.getId()
                newItem.lasttime = nowtime
                itemDb?.itemDao()?.update(newItem)
                //

                runOnUiThread{
                    findViewById<TextView>(R.id.tv_value1).text = "피로도: "+profileDb?.profileDao()?.getValue1()!!
                    findViewById<ProgressBar>(R.id.progress_value1).progress = profileDb?.profileDao()?.getValue1()!!
                }
            }
        }
        delay(1000L) // 50초에 한 번씩 확인
    }

}
