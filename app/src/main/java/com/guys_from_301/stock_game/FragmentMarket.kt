package com.guys_from_301.stock_game

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.guys_from_301.stock_game.data.*
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
const val POTION_COST = 500000

class FragmentMarket : Fragment() ,  RewardedVideoAdListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


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
    private lateinit var cl_buy_potion : ConstraintLayout

    //gamesetdB
    private var gameSetDB: GameSetDB? = null
    private lateinit var tv_marketReceipt : TextView

    //TextView
    private lateinit var tv_mountOfStack : TextView
    private lateinit var tv_initial_asset: TextView
    private lateinit var tv_initial_monthly: TextView
    private lateinit var tv_initial_salary_raise: TextView
    var isLoading = false
    var money: Int = 0
    var value1: Int = 0
    val moneyreward: Int = 1000000
    val value1reward: Int = 5000

    var mContext : Context = _MainActivity!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v : View = inflater.inflate(R.layout.fragment_market, container, false)
        gameSetDB = GameSetDB.getInstace(mContext)
        var gameset = gameSetDB?.gameSetDao()?.getAll(accountID!!)?.get(0)
        //viewmodel
        val marketViewModel = MarketViewModel(_MainActivity!!)

        //cl
        cl_todayStack = v.findViewById(R.id.cl_todayStack)
        cl_upgrade_asset = v.findViewById(R.id.cl_upgrade_asset)
        cl_upgrade_monthly = v.findViewById(R.id.cl_upgrade_monthly)
        cl_upgrade_salary_raise = v.findViewById(R.id.cl_upgrade_salary_raise)
        cl_buy_stack1 = v.findViewById(R.id.cl_buy_stack1)
        cl_buy_stack2 = v.findViewById(R.id.cl_buy_stack2)
        cl_buy_stack3 = v.findViewById(R.id.cl_buy_stack3)
        cl_buy_potion = v.findViewById(R.id.cl_potion)
        //btn
        cl_todayStack = v.findViewById(R.id.cl_todayStack)
        //btn_purchase = v.findViewById(R.id.btn_purchase)
        tv_marketReceipt = v.findViewById(R.id.tv_marketReceipt)

        //tv
        tv_mountOfStack = v.findViewById(R.id.tv_mountOfStack)

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(mContext)
        mRewardedVideoAd.rewardedVideoAdListener = FragmentMarket()!!
        tv_initial_asset = v.findViewById(R.id.tv_initial_asset)
        tv_initial_monthly = v.findViewById(R.id.tv_initial_monthly)
        tv_initial_salary_raise = v.findViewById(R.id.tv_initial_salary_raise)

        //viewmodel 적용
        marketViewModel.getStack().observe(viewLifecycleOwner, Observer { tv_mountOfStack.text = dec.format(it).toString() })
        marketViewModel.getInitialAsset().observe(viewLifecycleOwner, Observer { tv_initial_asset.text = "$" + dec.format(SET_CASH_STEP[it]) })
        marketViewModel.getInitialMonthly().observe(viewLifecycleOwner, Observer { tv_initial_monthly.text = "$" + dec.format(SET_MONTHLY_STEP[it]) })
        marketViewModel.getInitialSalaryRaise().observe(viewLifecycleOwner, Observer { tv_initial_salary_raise.text = SET_SALARY_RAISE_STEP[it].toString() + "%" })

        tv_mountOfStack = v.findViewById(R.id.tv_mountOfStack)
        tv_mountOfStack.text = dec.format(profileDbManager!!.getMoney()).toString()
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(mContext)
        mRewardedVideoAd.rewardedVideoAdListener = this

        loadad()
        cl_todayStack.setOnClickListener {
            if (mRewardedVideoAd.isLoaded) {
                mRewardedVideoAd.show()
            }
            else{
                Toast.makeText(mContext, "광고가 준비될 때까지 기다려주세요", Toast.LENGTH_LONG).show()
            }
        }

        // billing Manager for purchase process
        billingManager = BillingManager(mContext as Activity)


        //초기자산 협상
        cl_upgrade_asset.setOnClickListener {
            //billingManager.startConnection()
            if (marketViewModel.getStack().value!! >= ITEM_COST){
                if(marketViewModel.getInitialMonthly().value == 4) {
                    val dialogUnsuccess = mContext?.let { Dialog_negotiation_unsuccess(it) }
                    dialogUnsuccess.start(5, 0)
                }
                else {
                    if (gameset != null) {
                        val dialogNegotiation = Dialog_negotiation(_MainActivity!!)
                        dialogNegotiation.start(1,marketViewModel.getInitialAsset().value!!,marketViewModel)
                    }
                }
            }
            else {
                val dialogUnsuccess = mContext?.let { Dialog_negotiation_unsuccess(it) }
                dialogUnsuccess.start(4, 0)
            }
        }
//        btn_purchase.setOnClickListener {
//            billingManager.startConnection()
//        }
        tv_marketReceipt.setOnClickListener{
            Toast.makeText(mContext, "구매 내역!", Toast.LENGTH_LONG).show()

        }

        //초기월금 협상
        cl_upgrade_monthly.setOnClickListener {
            if (marketViewModel.getStack().value!! >= ITEM_COST){
                if(marketViewModel.getInitialMonthly().value == 4) {
                    val dialogUnsuccess = mContext?.let { Dialog_negotiation_unsuccess(it) }
                    dialogUnsuccess.start(5, 0)
                }
                else {
                    if (gameset != null) {
                        val dialogNegotiation = Dialog_negotiation(_MainActivity!!)
                        dialogNegotiation.start(2,marketViewModel.getInitialMonthly().value!!,marketViewModel)
                    }
                }
            }
            else {
                val dialogUnsuccess = mContext?.let { Dialog_negotiation_unsuccess(it) }
                dialogUnsuccess.start(4, 0)
            }
        }

        //연봉 인상률 협상
        cl_upgrade_salary_raise.setOnClickListener {
            if (marketViewModel.getStack().value!! >= ITEM_COST){
                if(marketViewModel.getInitialMonthly().value == 4) {
                    val dialogUnsuccess = mContext?.let { Dialog_negotiation_unsuccess(it) }
                    dialogUnsuccess.start(5, 0)
                }
                else {
                    if (gameset != null) {
                        val dialogNegotiation = Dialog_negotiation(_MainActivity!!)
                        dialogNegotiation.start(3,marketViewModel.getInitialSalaryRaise().value!!,marketViewModel)
                    }
                }
            }
            else {
                val dialogUnsuccess = mContext?.let { Dialog_negotiation_unsuccess(it) }
                dialogUnsuccess.start(4, 0)
            }
        }

        //스텍 구매
        cl_buy_stack1.setOnClickListener{
            marketViewModel.BuyStack(100)
        }
        cl_buy_stack2.setOnClickListener{
            marketViewModel.BuyStack(200)
        }
        cl_buy_stack3.setOnClickListener{
            marketViewModel.BuyStack(1000)
        }
        //물약 구매
        cl_buy_potion.setOnClickListener{
            marketViewModel.BuyPotion()
            val dialogUnsuccess = mContext?.let { Dialog_negotiation_unsuccess(it) }
            marketViewModel.getPotion().value?.let { it1 -> dialogUnsuccess.start(6, it1) }
        }

        return v
    }

    override fun onResume() {
        loadad()
        super.onResume()
    }


    fun dbupdate() {
        profileDbManager!!.setMoney(money)
        profileDbManager!!.setValue1(value1)
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
        money = profileDbManager!!.getMoney()!!.toInt()+moneyreward
        value1 = profileDbManager!!.getValue1()!!.toInt()
        if (value1 >= value1reward) {
            value1 -= value1reward
        } else {
            value1 = 0
        }
        Toast.makeText(mContext, "보상지급: +100만 스택, -5000 피로도\n현재 보유 스택: " + money + "\n현재 피로도: " + value1, Toast.LENGTH_LONG).show()
        Toast.makeText(mContext, "보상지급: +100만 스택, -5000 피로도\n현재 보유 스택: "+money+"\n현재 피로도: "+value1, Toast.LENGTH_LONG).show()
        // 서버에 업데이트
        update(getHash(profileDbManager!!.getLoginId()!!).trim(),
                getHash(profileDbManager!!.getLoginPw()!!).trim(),
                money, value1,
                profileDbManager!!.getNickname()!!,
                profileDbManager!!.getProfitRate()!!,
                profileDbManager!!.getRelativeProfit()!!,
                profileDbManager!!.getRoundCount()!!,
                profileDbManager!!.getHistory()!!,
                profileDbManager!!.getLevel()!!
        )
        // profiledb에 업데이트
        val marketViewModel = MarketViewModel(_MainActivity!!)
        marketViewModel.BuyStack(moneyreward)
        tv_mountOfStack.text = dec.format(profileDbManager!!.getMoney()).toString()
    }

    override fun onRewardedVideoStarted() {
        Log.d("SSS", "onRewardedVideoStarted()")
    }

    override fun onRewardedVideoAdFailedToLoad(p0: Int) {
        Log.d("SSS", "onRewardedVideoAdFailedToLoad($p0)")
    }


}