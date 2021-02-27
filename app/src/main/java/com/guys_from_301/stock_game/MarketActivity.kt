package com.guys_from_301.stock_game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.guys_from_301.stock_game.data.Profile
import com.guys_from_301.stock_game.data.ProfileDB
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.guys_from_301.stock_game.data.GameSet
import com.guys_from_301.stock_game.data.GameSetDB
import kotlinx.coroutines.delay
import java.util.*

//아이템 효과 변수들
const val RAISE_SET_CASH = 1000F
const val RAISE_SET_MONTHLY = 1000F
const val RAISE_SET_SALARY_RAISE = 0.5F
val SET_CASH_STEP = listOf<Float>(10000F, 50000F, 100000F, 500000F, 1000000F)
val SET_MONTHLY_STEP = listOf<Float>(1000F, 1500F, 2000F, 5000F, 10000F)
val SET_SALARY_RAISE_STEP = listOf<Float>(4F,5F,6F,8F,10F)
const val ITEM_COST = 100000
const val STACK1_COST = 100
const val STACK2_COST = 200
const val STACK3_COST = 1000

class MarketActivity : AppCompatActivity(), RewardedVideoAdListener {
    // 결제 객체
    private lateinit var billingManager: BillingManager

    // 보상형 광고 관련 코드
    private lateinit var mRewardedVideoAd: RewardedVideoAd

    // Button
    private lateinit var cl_todayStack: ConstraintLayout
    private lateinit var cl_upgrade_asset: ConstraintLayout
    private lateinit var cl_upgrade_monthly: ConstraintLayout
    private lateinit var cl_upgrade_salary_raise: ConstraintLayout
    private lateinit var cl_buy_stack1 : ConstraintLayout
    private lateinit var cl_buy_stack2 : ConstraintLayout
    private lateinit var cl_buy_stack3 : ConstraintLayout

    //gamesetdB
    private var gameSetDB: GameSetDB? = null

    private var profileDb: ProfileDB? = null

    //TextView
    private lateinit var tv_initial_asset: TextView
    private lateinit var tv_initial_monthly: TextView
    private lateinit var tv_initial_salary_raise: TextView
    private lateinit var tv_mountOfStack: TextView
    var isLoading = false
    var money: Int = 0
    var value1: Int = 0
    val moneyreward: Int = 1000000
    val value1reward: Int = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market)
        profileDb = ProfileDB.getInstace(this)
        gameSetDB = GameSetDB.getInstace(this)
        var gameset = gameSetDB?.gameSetDao()?.getAll()?.get(0)
        //viewmodel
        val marketViewModel = MarketViewModel(this)

        //cl
        cl_todayStack = findViewById(R.id.cl_todayStack)
        cl_upgrade_asset = findViewById(R.id.cl_upgrade_asset)
        cl_upgrade_monthly = findViewById(R.id.cl_upgrade_monthly)
        cl_upgrade_salary_raise = findViewById(R.id.cl_upgrade_salary_raise)
        cl_buy_stack1 = findViewById(R.id.cl_buy_stack1)
        cl_buy_stack2 = findViewById(R.id.cl_buy_stack2)
        cl_buy_stack3 = findViewById(R.id.cl_buy_stack3)

        //tv
        tv_initial_asset = findViewById(R.id.tv_initial_asset)
        tv_initial_monthly = findViewById(R.id.tv_initial_monthly)
        tv_initial_salary_raise = findViewById(R.id.tv_initial_salary_raise)

        //viewmodel 적용
        marketViewModel.getStack().observe(this, Observer { tv_mountOfStack.text = it.toString() })
        marketViewModel.getInitialAsset().observe(this, Observer { tv_initial_asset.text = "$" + SET_CASH_STEP[it] })
        marketViewModel.getInitialMonthly().observe(this, Observer { tv_initial_monthly.text = "$" + SET_MONTHLY_STEP[it] })
        marketViewModel.getInitialSalaryRaise().observe(this, Observer { tv_initial_salary_raise.text = SET_SALARY_RAISE_STEP[it].toString() + "%" })

        tv_mountOfStack = findViewById(R.id.tv_mountOfStack)
        tv_mountOfStack.text = dec.format(profileDb?.profileDao()?.getMoney()!!).toString()
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this)
        mRewardedVideoAd.rewardedVideoAdListener = this

        loadad()
        initLayout(marketViewModel)

        // billing Manager for purchase process
        billingManager = BillingManager(this)


        //초기자산 아이템 구매
        cl_upgrade_asset.setOnClickListener {
            //billingManager.startConnection()
            if (marketViewModel.getStack().value!! >= ITEM_COST){
                if(marketViewModel.getInitialAsset().value == 4) Toast.makeText(this, "더 이상 협상을 진행할 수 없습니다", Toast.LENGTH_LONG).show()
                else {
                    val dialog = Dialog_loading(this@MarketActivity)
                    dialog.show()
                    if(negotiation(marketViewModel.getInitialAsset().value!!)) {
                        marketViewModel.applyItemRaiseSetCash()
                        Toast.makeText(this, "협상에 성공했습니다!", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        marketViewModel.applyItmeFailtoSetCash()
                        Toast.makeText(this, "협상에 실패했습니다", Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }
            }
            else Toast.makeText(this, "스택이 부족합니다", Toast.LENGTH_SHORT).show()
        }

        //초기월금 아이템 구매
        cl_upgrade_monthly.setOnClickListener {
            if (marketViewModel.getStack().value!! >= ITEM_COST){
                if(marketViewModel.getInitialMonthly().value == 4) Toast.makeText(this, "더 이상 협상을 진행할 수 없습니다", Toast.LENGTH_LONG).show()
                else {
                    val dialog = Dialog_loading(this@MarketActivity)
                    dialog.show()
                    if(negotiation(marketViewModel.getInitialMonthly().value!!)) {
                        marketViewModel.applyItemRaiseSetMonthly()
                        Toast.makeText(this, "협상에 성공했습니다!", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        marketViewModel.applyItmeFailtoSetMonthly()
                        Toast.makeText(this, "협상에 실패했습니다", Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }
            }
            else Toast.makeText(this, "스택이 부족합니다", Toast.LENGTH_SHORT).show()
        }

        //연봉상승률 아이템 구매
        cl_upgrade_salary_raise.setOnClickListener {
            if (marketViewModel.getStack().value!! >= ITEM_COST){
                if(marketViewModel.getInitialSalaryRaise().value == 4) Toast.makeText(this, "더 이상 협상을 진행할 수 없습니다", Toast.LENGTH_LONG).show()
                else {
                    val dialog = Dialog_loading(this@MarketActivity)
                    dialog.show()
                    if(negotiation(marketViewModel.getInitialSalaryRaise().value!!)) {
                        marketViewModel.applyItemRaiseSetSalaryRaise()
                        Toast.makeText(this, "협상에 성공했습니다!", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        marketViewModel.applyItmeFailtoSetSalaryRaise()
                        Toast.makeText(this, "협상에 실패했습니다", Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }
            }
            else Toast.makeText(this, "스택이 부족합니다", Toast.LENGTH_SHORT).show()
        }

        //스택 구매
        cl_buy_stack1.setOnClickListener{
            if(marketViewModel.getStack().value!! >= STACK1_COST) marketViewModel.BuyStack(STACK1_COST)
            else Toast.makeText(this, "스택이 부족합니다", Toast.LENGTH_SHORT).show()
        }
        cl_buy_stack2.setOnClickListener{
            if(marketViewModel.getStack().value!! >= STACK2_COST) marketViewModel.BuyStack(STACK2_COST)
            else Toast.makeText(this, "스택이 부족합니다", Toast.LENGTH_SHORT).show()
        }
        cl_buy_stack3.setOnClickListener{
            if(marketViewModel.getStack().value!! >= STACK3_COST) marketViewModel.BuyStack(STACK3_COST)
            else Toast.makeText(this, "스택이 부족합니다", Toast.LENGTH_SHORT).show()
        }

    }
    fun dbupdate() {
        var profileDb: ProfileDB? = null
        profileDb = ProfileDB.getInstace(this)
        val newProfile = Profile()

        newProfile.id = profileDb?.profileDao()?.getId()!!
        newProfile.login = profileDb?.profileDao()?.getLogin()!!
        newProfile.login_id = profileDb?.profileDao()?.getLoginid()!!
        newProfile.login_pw = profileDb?.profileDao()?.getLoginpw()!!
        newProfile.history = profileDb?.profileDao()?.getHistory()!!
        newProfile.relativeprofitrate = profileDb?.profileDao()?.getRelativeProfitRate()!!
        newProfile.nickname = profileDb?.profileDao()?.getNickname()!!
        newProfile.level = profileDb?.profileDao()?.getLevel()!!
        newProfile.exp = profileDb?.profileDao()?.getExp()!!
        newProfile.rank = profileDb?.profileDao()?.getRank()!!
        newProfile.money = money
        newProfile.value1 = value1
        profileDb?.profileDao()?.update(newProfile)
    }

    fun initLayout(marketViewModel: MarketViewModel) {
        cl_todayStack.setOnClickListener {
            if (mRewardedVideoAd.isLoaded) {
                mRewardedVideoAd.show()
                marketViewModel.BuyStack(moneyreward)
            }

        }
    }

    fun loadad() {
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
        value1 = profileDb?.profileDao()?.getValue1()!!.toInt()

        if (value1 >= value1reward) {
            value1 -= value1reward
        } else {
            value1 = 0
        }

        Toast.makeText(this, "보상지급: +100만 스택, -5000 피로도\n현재 보유 스택: " + money + "\n현재 피로도: " + value1, Toast.LENGTH_LONG).show()
        // 서버에 업데이트
        update(getHash(profileDb?.profileDao()?.getLoginid()!!).trim(),
                getHash(profileDb?.profileDao()?.getLoginpw()!!).trim(),
                money, value1,
                profileDb?.profileDao()?.getNickname()!!,
                profileDb?.profileDao()?.getRelativeProfitRate()!!,
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

    fun negotiation(level: Int): Boolean{
        var percentage = 0
        val random = Random()
        val num = random.nextInt(99)
        when(level){
            0-> percentage = 80
            1-> percentage = 50
            2-> percentage = 25
            3-> percentage = 10
        }
        return num<percentage
    }
}