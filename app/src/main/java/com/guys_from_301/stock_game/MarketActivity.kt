package com.guys_from_301.stock_game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.guys_from_301.stock_game.data.Profile
import com.guys_from_301.stock_game.data.ProfileDB
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import java.text.DecimalFormat

class MarketActivity : AppCompatActivity(), RewardedVideoAdListener {
    // 결제 객체
    private lateinit var billingManager : BillingManager
    // 보상형 광고 관련 코드
    private lateinit var mRewardedVideoAd: RewardedVideoAd
    // Button
    private lateinit var btn_ad : Button
    private lateinit var btn_purchase : Button
    private lateinit var btn_home : Button
    private lateinit var btn_ranking : Button
    private lateinit var btn_market : Button
    private lateinit var btn_profile : Button
    private lateinit var btn_receipt : Button

    private var profileDb: ProfileDB? = null
    //TextView
    private lateinit var tv_stack : TextView
    var isLoading = false
    var money: Int = 0
    var value1: Int = 0
    val moneyreward: Int = 1000000
    val value1reward: Int = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market)
        profileDb = ProfileDB.getInstace(this)

        //btn
        btn_ad = findViewById(R.id.btn_ad)
        btn_purchase = findViewById(R.id.btn_purchase)
        btn_home = findViewById(R.id.btn_home)
        btn_ranking = findViewById(R.id.btn_ranking)
        btn_market = findViewById(R.id.btn_market)
        btn_profile = findViewById(R.id.btn_profile)
        btn_receipt = findViewById(R.id.btn_receipt)

        //tv
        tv_stack = findViewById(R.id.tv_stack)
        tv_stack.text = dec.format(profileDb?.profileDao()?.getMoney()!!).toString()
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this)
        mRewardedVideoAd.rewardedVideoAdListener = this

        loadad()
        initLayout()

        // billing Manager for purchase process
        billingManager = BillingManager(this)

        btn_purchase.setOnClickListener {
            billingManager.startConnection()
        }



        btn_receipt.setOnClickListener{
            Toast.makeText(this, "구매 내역!", Toast.LENGTH_LONG).show()
        }
        // 하단바 버튼들
        btn_home.setOnClickListener{
            Toast.makeText(this, "아직 구현 안됨ㅎㅎ", Toast.LENGTH_LONG).show()
        }
        btn_ranking.setOnClickListener{
            Toast.makeText(this, "아직 구현 안됨ㅎㅎ", Toast.LENGTH_LONG).show()
        }
        btn_market.setOnClickListener{
            Toast.makeText(this, "아직 구현 안됨ㅎㅎ", Toast.LENGTH_LONG).show()
        }
        btn_profile.setOnClickListener{
            Toast.makeText(this, "아직 구현 안됨ㅎㅎ", Toast.LENGTH_LONG).show()
        }
    }

    fun initLayout(){
        btn_ad.setOnClickListener{
            if (mRewardedVideoAd.isLoaded) {
                mRewardedVideoAd.show()
            }

        }
    }

    fun loadad(){
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917", AdRequest.Builder().build())
    }

    override fun onRewardedVideoAdClosed() {
        Log.d("SSS", "onRewardVideoAdClosed()")
        loadad()
    }

    override fun onRewardedVideoAdLeftApplication() {
        Log.d("SSS", "onRewardedVideoAdLeftApplication()")

    }

    override fun onRewardedVideoAdLoaded() {
        Log.d("SSS", "onRewardedVideoAdLoaded()")
    }

    override fun onRewardedVideoAdOpened() {
        Log.d("SSS", "onRewardedVideoAdOpened()")
    }

    override fun onRewardedVideoCompleted() {
        Log.d("SSS", "onRewardedVideoCompleted()")
    }

    override fun onRewarded(rItem: RewardItem?) {
        var profileDb: ProfileDB? = null
        profileDb = ProfileDB.getInstace(this)
        money = profileDb?.profileDao()?.getMoney()!!.toInt()+moneyreward
        value1 = profileDb?.profileDao()?.getValue1()!!.toInt()

        if (value1 >= value1reward) {
            value1 -= value1reward
        } else {
            value1 = 0
        }

        Toast.makeText(this, "보상지급: +100만 스택, -5000 피로도\n현재 보유 스택: "+money+"\n현재 피로도: "+value1, Toast.LENGTH_LONG).show()
        // 서버에 업데이트
        update(getHash(profileDb?.profileDao()?.getLoginid()!!).trim(),
                getHash(profileDb?.profileDao()?.getLoginpw()!!).trim(),
                money, value1,
                profileDb?.profileDao()?.getNickname()!!,
                profileDb?.profileDao()?.getProfit()!!,
                profileDb?.profileDao()?.getRoundCount()!!,
                profileDb?.profileDao()?.getHistory()!!,
                profileDb?.profileDao()?.getLevel()!!
        )
        // profiledb에 업데이트
        dbupdate()

    }

    override fun onRewardedVideoStarted() {
        Log.d("SSS", "onRewardedVideoStarted()")
    }

    override fun onRewardedVideoAdFailedToLoad(p0: Int) {
        Log.d("SSS", "onRewardedVideoAdFailedToLoad($p0)")
    }

    fun dbupdate(){
        var profileDb: ProfileDB? = null
        profileDb = ProfileDB.getInstace(this)
        val newProfile = Profile()

        newProfile.id = profileDb?.profileDao()?.getId()!!
        newProfile.login =profileDb?.profileDao()?.getLogin()!!
        newProfile.login_id = profileDb?.profileDao()?.getLoginid()!!
        newProfile.login_pw = profileDb?.profileDao()?.getLoginpw()!!
        newProfile.history = profileDb?.profileDao()?.getHistory()!!
        newProfile.profit = profileDb?.profileDao()?.getProfit()!!
        newProfile.nickname = profileDb?.profileDao()?.getNickname()!!
        newProfile.level = profileDb?.profileDao()?.getLevel()!!
        newProfile.exp = profileDb?.profileDao()?.getExp()!!
        newProfile.rank = profileDb?.profileDao()?.getRank()!!
        newProfile.money = money
        newProfile.value1 = value1
        profileDb?.profileDao()?.update(newProfile)
    }

}