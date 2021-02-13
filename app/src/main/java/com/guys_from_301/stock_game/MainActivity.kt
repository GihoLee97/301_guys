package com.guys_from_301.stock_game

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.guys_from_301.stock_game.data.GameSetDB
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.guys_from_301.stock_game.retrofit.RetrofitRanking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

var rank1_nick :String = ""; var rank1_money :String = ""; var rank2_nick :String = ""; var rank2_money :String = ""
var rank3_nick :String = ""; var rank3_money :String = ""; var rank4_nick :String = ""; var rank4_money :String = ""
var rank5_nick :String = ""; var rank5_money :String = ""; var rank6_nick :String = ""; var rank6_money :String = ""
var rank7_nick :String = ""; var rank7_money :String = ""; var rank8_nick :String = ""; var rank8_money :String = ""
var rank9_nick :String = ""; var rank9_money :String = ""; var rank10_nick :String = ""; var rank10_money :String = ""

class MainActivity : AppCompatActivity() {

    private var gameSetDb: GameSetDB? = null
    private lateinit var btn_profile : Button
    private lateinit var btn_setting : Button
    private lateinit var btn_game : Button
    private lateinit var btn_market : Button
    private lateinit var btn_ranking: Button

    lateinit var mAdView : AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gameSetDb = GameSetDB.getInstace(this)
        // 광고 관련 코드
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        btn_profile = findViewById(R.id.btn_profile)
        btn_setting = findViewById(R.id.btn_setting)
        btn_game =  findViewById(R.id.btn_game)
        btn_market = findViewById(R.id.btn_market)
        btn_ranking = findViewById(R.id.btn_ranking)

        btn_game.isEnabled = false // 로딩 미완료 상태일 때 게임 버튼 비활성화

        btn_profile.setOnClickListener{
            val intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
        }

        btn_setting.setOnClickListener {
            val intent = Intent(this,SettingActivity::class.java)
            startActivity(intent)
        }

        btn_game.setOnClickListener{
            val dialog = Dialog_loading(this@MainActivity)
            dialog.show()
            val intentgame = Intent(this, GameNormalActivity::class.java)
            val intent = Intent(this,GameSettingActivity::class.java)
            if(gameSetDb?.gameSetDao()?.getAll()?.isEmpty() == true)    {
                startActivity(intent)
                dialog.dismiss()
            }
            else {
                setCash = gameSetDb?.gameSetDao()?.getSetCash()!!
                setMonthly = gameSetDb?.gameSetDao()?.getSetMonthly()!!
                setSalaryraise = gameSetDb?.gameSetDao()?.getSetSalaryRaise()!!
                setGamespeed = gameSetDb?.gameSetDao()?.getSetGameSpeed()!!
                startActivity(intentgame)
                dialog.dismiss()
            }
        }

        btn_market.setOnClickListener {
//            val intent = Intent(this,MarketActivity::class.java)
//            startActivity(intent)
            val intent = Intent(this,MarketActivity::class.java)
            startActivity(intent)
        }
        btn_ranking.setOnClickListener{
            val intent = Intent(this, RankingActivity::class.java)
            startActivity(intent)
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
}