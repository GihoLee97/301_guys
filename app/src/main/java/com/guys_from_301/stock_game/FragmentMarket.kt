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
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.guys_from_301.stock_game.data.Profile
import com.guys_from_301.stock_game.data.ProfileDB

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
    private lateinit var billingManager : BillingManager
    // 보상형 광고 관련 코드
    private lateinit var mRewardedVideoAd: RewardedVideoAd
    // Button
    private lateinit var btn_ad : Button
    private lateinit var btn_purchase : Button
    private lateinit var btn_receipt : Button

    private var profileDb: ProfileDB? = null
    //TextView
    private lateinit var tv_stack : TextView
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

        //btn
        btn_ad = v.findViewById(R.id.btn_ad)
        btn_purchase = v.findViewById(R.id.btn_purchase)
        btn_receipt = v.findViewById(R.id.btn_receipt)

        //tv
        tv_stack = v.findViewById(R.id.tv_stack)

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(mContext)
        mRewardedVideoAd.rewardedVideoAdListener = FragmentMarket()!!

        loadad()
        initLayout()

        // billing Manager for purchase process
        billingManager = BillingManager(mContext as Activity)

        btn_purchase.setOnClickListener {
            billingManager.startConnection()
        }
        btn_receipt.setOnClickListener{
            Toast.makeText(mContext, "구매 내역!", Toast.LENGTH_LONG).show()

        }

        return v
    }

    override fun onResume() {
        tv_stack.text = dec.format(profileDb?.profileDao()?.getMoney()!!).toString()
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
        profileDb = ProfileDB.getInstace(mContext)
        money = profileDb?.profileDao()?.getMoney()!!.toInt()+moneyreward
        value1 = profileDb?.profileDao()?.getValue1()!!.toInt()

        if (value1 >= value1reward) {
            value1 -= value1reward
        } else {
            value1 = 0
        }

        Toast.makeText(mContext, "보상지급: +100만 스택, -5000 피로도\n현재 보유 스택: "+money+"\n현재 피로도: "+value1, Toast.LENGTH_LONG).show()
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
        profileDb = ProfileDB.getInstace(mContext)
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