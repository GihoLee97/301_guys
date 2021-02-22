package com.guys_from_301.stock_game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.guys_from_301.stock_game.data.Profile
import com.guys_from_301.stock_game.data.ProfileDB
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener

class MarketActivity : AppCompatActivity(), RewardedVideoAdListener {
    // 결제 객체
    private lateinit var billingManager : BillingManager
    // 보상형 광고 관련 코드
    private lateinit var mRewardedVideoAd: RewardedVideoAd
    var profileDb: ProfileDB? = null
    // Button
    private lateinit var btn_ad : Button
    private lateinit var btn_purchase : Button
    var isLoading = false
    var money: Int = 0
    var value1: Int = 0
    val moneyreward: Int = 1000000
    val value1reward: Int = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market)

        btn_ad = findViewById(R.id.btn_ad)
        btn_purchase = findViewById(R.id.btn_purchase)
        //
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this)
        mRewardedVideoAd.rewardedVideoAdListener = this

        loadad()
        initLayout()

        // billing Manager for purchase process
        billingManager = BillingManager(this)

        btn_purchase.setOnClickListener {
            billingManager.startConnection()
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
                profileDb?.profileDao()?.getLevel()!!,
                0
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