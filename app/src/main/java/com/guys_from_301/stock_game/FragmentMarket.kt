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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
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

    //gamesetdB
    private var gameSetDB: GameSetDB? = null
    private lateinit var tv_marketReceipt : TextView

    private var profileDb: ProfileDB? = null

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
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v : View = inflater.inflate(R.layout.fragment_market, container, false)
        profileDb = ProfileDB.getInstace(mContext)
        gameSetDB = GameSetDB.getInstace(mContext)
        var gameset = gameSetDB?.gameSetDao()?.getAll()?.get(0)
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
        //btn
        cl_todayStack = v.findViewById(R.id.cl_todayStack)
//        btn_purchase = v.findViewById(R.id.btn_purchase)
        tv_marketReceipt = v.findViewById(R.id.tv_marketReceipt)

        //tv
        tv_mountOfStack = v.findViewById(R.id.tv_mountOfStack)

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(mContext)
        mRewardedVideoAd.rewardedVideoAdListener = FragmentMarket()!!
        tv_initial_asset = v.findViewById(R.id.tv_initial_asset)
        tv_initial_monthly = v.findViewById(R.id.tv_initial_monthly)
        tv_initial_salary_raise = v.findViewById(R.id.tv_initial_salary_raise)

        //viewmodel 적용
        marketViewModel.getStack().observe(viewLifecycleOwner, Observer { tv_mountOfStack.text = it.toString() })
        marketViewModel.getInitialAsset().observe(viewLifecycleOwner, Observer { tv_initial_asset.text = "$" + SET_CASH_STEP[it] })
        marketViewModel.getInitialMonthly().observe(viewLifecycleOwner, Observer { tv_initial_monthly.text = "$" + SET_MONTHLY_STEP[it] })
        marketViewModel.getInitialSalaryRaise().observe(viewLifecycleOwner, Observer { tv_initial_salary_raise.text = SET_SALARY_RAISE_STEP[it].toString() + "%" })

        tv_mountOfStack = v.findViewById(R.id.tv_mountOfStack)
        tv_mountOfStack.text = dec.format(profileDb?.profileDao()?.getMoney()!!).toString()
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(mContext)
        mRewardedVideoAd.rewardedVideoAdListener = this

        loadad()
        initLayout(marketViewModel)

        // billing Manager for purchase process
        billingManager = BillingManager(mContext as Activity)


        //초기자산 아이템 구매
        cl_upgrade_asset.setOnClickListener {
            //billingManager.startConnection()
            if (marketViewModel.getStack().value!! >= ITEM_COST){
                if(marketViewModel.getInitialAsset().value == 4) Toast.makeText(mContext, "더 이상 협상을 진행할 수 없습니다", Toast.LENGTH_LONG).show()
                else {
                    val dialog = Dialog_loading(mContext)
                    dialog.show()
                    if(negotiation(marketViewModel.getInitialAsset().value!!)) {
                        marketViewModel.applyItemRaiseSetCash()
                        Toast.makeText(mContext, "협상에 성공했습니다!", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        marketViewModel.applyItmeFailtoSetCash()
                        Toast.makeText(mContext, "협상에 실패했습니다", Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }
            }
            else Toast.makeText(mContext, "스택이 부족합니다", Toast.LENGTH_SHORT).show()
        }
//        btn_purchase.setOnClickListener {
//            billingManager.startConnection()
//        }
        tv_marketReceipt.setOnClickListener{
            Toast.makeText(mContext, "구매 내역!", Toast.LENGTH_LONG).show()

        }

        //초기월금 아이템 구매
        cl_upgrade_monthly.setOnClickListener {
            if (marketViewModel.getStack().value!! >= ITEM_COST){
                if(marketViewModel.getInitialMonthly().value == 4) Toast.makeText(mContext, "더 이상 협상을 진행할 수 없습니다", Toast.LENGTH_LONG).show()
                else {
                    val dialog = Dialog_loading(mContext)
                    dialog.show()
                    if(negotiation(marketViewModel.getInitialMonthly().value!!)) {
                        marketViewModel.applyItemRaiseSetMonthly()
                        Toast.makeText(mContext, "협상에 성공했습니다!", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        marketViewModel.applyItmeFailtoSetMonthly()
                        Toast.makeText(mContext, "협상에 실패했습니다", Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }
            }
            else Toast.makeText(mContext, "스택이 부족합니다", Toast.LENGTH_SHORT).show()
        }

        return v
    }


    override fun onResume() {
        tv_mountOfStack.text = dec.format(profileDb?.profileDao()?.getMoney()!!).toString()
        loadad()
        super.onResume()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentMarket().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun initLayout(marketViewModel: MarketViewModel) {
        cl_todayStack.setOnClickListener {
            if (mRewardedVideoAd.isLoaded) {
                mRewardedVideoAd.show()
                marketViewModel.BuyStack(moneyreward)
            }

        }
    }

    fun dbupdate() {
        var profileDb: ProfileDB? = null
        profileDb = ProfileDB.getInstace(mContext)
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
        profileDb = ProfileDB.getInstace(mContext)
        profileDb = ProfileDB.getInstace(mContext)
        money = profileDb?.profileDao()?.getMoney()!!.toInt()+moneyreward
        value1 = profileDb?.profileDao()?.getValue1()!!.toInt()

        if (value1 >= value1reward) {
            value1 -= value1reward
        } else {
            value1 = 0
        }

        Toast.makeText(mContext, "보상지급: +100만 스택, -5000 피로도\n현재 보유 스택: " + money + "\n현재 피로도: " + value1, Toast.LENGTH_LONG).show()
        Toast.makeText(mContext, "보상지급: +100만 스택, -5000 피로도\n현재 보유 스택: "+money+"\n현재 피로도: "+value1, Toast.LENGTH_LONG).show()
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

    fun negotiation(level: Int): Boolean {
        var percentage = 0
        val random = Random()
        val num = random.nextInt(99)
        when (level) {
            0 -> percentage = 80
            1 -> percentage = 50
            2 -> percentage = 25
            3 -> percentage = 10
        }
        return num < percentage
    }

}