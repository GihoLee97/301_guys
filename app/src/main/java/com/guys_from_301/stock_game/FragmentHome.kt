package com.guys_from_301.stock_game

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.guys_from_301.stock_game.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timer

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentHome : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//    var profileDb : ProfileDB? = null
    var itemDb : ItemDB? = null
    private var item: Item? = null
    private var questDb: QuestDB? = null
    private var questList = listOf<Quest>()
    private var achievementList = listOf<Int>()
    private var questInGameProfitRateList = listOf<Quest>()
    private var questRelativeProfitRateList = listOf<Quest>()
    private lateinit var tv_questName1 : TextView
    private lateinit var tv_questName2 : TextView
    private lateinit var tv_questDescription1: TextView
    private lateinit var tv_questDescription2: TextView
    private lateinit var pb_quest1: ProgressBar
    private lateinit var pb_quest2: ProgressBar
    private lateinit var tv_profitRate: TextView
    private lateinit var tv_profitRate_kr: TextView
    private lateinit var rv_games: ViewPager
    private lateinit var ll_title: LinearLayout
    //게임 데이터 불러올 변수
    private var gameDb: GameSetDB? = null
    private var gameNormalDB: GameNormalDB? = null
    private var game = ArrayList<GameSet>()
    private var title_kr = arrayListOf<String>("누적 수익률", "시장대비수익률", "누적투자일", "레벨", "보유스택")
    lateinit var mAdapter: MyGameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var v : View = inflater.inflate(R.layout.fragment_home, container, false)
        activity?.window?.statusBarColor = resources.getColor(R.color.white)


        itemDb = ItemDB.getInstace(_MainActivity!!)
        tv_profitRate = v.findViewById(R.id.tv_profitRate)
        tv_profitRate_kr = v.findViewById(R.id.tv_profitRate_kr)
        tv_questName1 = v.findViewById(R.id.tv_questName1)
        tv_questName2 = v.findViewById(R.id.tv_questName2)
        tv_questDescription1 = v.findViewById(R.id.tv_questDescription1)
        tv_questDescription2 = v.findViewById(R.id.tv_questDescription2)
        pb_quest1 = v.findViewById(R.id.pb_quest1)
        pb_quest2 = v.findViewById(R.id.pb_quest2)
        ll_title = v.findViewById(R.id.ll_title)
        val random = Random()
        var num = random.nextInt(title_kr.size)
        tv_profitRate_kr.text = title_kr[num]
        when(num){
            0->tv_profitRate.text = per.format(profileDbManager!!.getProfitRate()!!).toString()+"%"
            1->tv_profitRate.text = per.format(profileDbManager!!.getRelativeProfit()!!).toString()+"%"
            2->tv_profitRate.text = profileDbManager!!.getHistory()!!.toString()+"일"
            3->tv_profitRate.text = profileDbManager!!.getLevel()!!.toString()
            4->tv_profitRate.text = dec.format(profileDbManager!!.getMoney()!!).toString()
        }

//        CoroutineScope(Dispatchers.Main).launch {
//            val job1 = launch {
//                while (true) {
//                    num = num+1
//                    num = num % 5
//                    ll_title.animate().apply {
//                        duration = 250
//                        rotationXBy(180.00000f).withEndAction{
//                        }.start()
//                        delay(260L)
//                        tv_profitRate_kr.text = title_kr[num]
//                        when(num){
//                            0->tv_profitRate.text = per.format(profileDbManager!!.getProfitRate()!!).toString()+"%"
//                            1->tv_profitRate.text = per.format(profileDbManager!!.getRelativeProfit()!!).toString()+"%"
//                            2->tv_profitRate.text = profileDbManager!!.getHistory()!!.toString()+"일"
//                            3->tv_profitRate.text = profileDbManager!!.getLevel()!!.toString()
//                            4->tv_profitRate.text = dec.format(profileDbManager!!.getMoney()!!).toString()
//                        }
//                        duration = 250
//                        rotationXBy(180.00000f).withEndAction{
//                        }.start()
//                        delay(5000L)
//                    }
//
//
//                }
//            }
//        }

        CoroutineScope(Dispatchers.Main).launch {
            val job1 = launch {
                while (true) {
                    num = num+1
                    num = num % 5
                    tv_profitRate_kr.text = title_kr[num]
                    when(num){
                        0->tv_profitRate.text = per.format(profileDbManager!!.getProfitRate()!!).toString()+"%"
                        1->tv_profitRate.text = per.format(profileDbManager!!.getRelativeProfit()!!).toString()+"%"
                        2->tv_profitRate.text = profileDbManager!!.getHistory()!!.toString()+"일"
                        3->tv_profitRate.text = profileDbManager!!.getLevel()!!.toString()
                        4->tv_profitRate.text = dec.format(profileDbManager!!.getMoney()!!).toString()
                    }

                    ll_title.animate().apply {
                        duration = 500
                        rotationXBy(360.00000f).withEndAction{
                        }.start()
                        delay(5000L)
                    }


                }
            }
        }




        //도전과제 실시간 반영
        var profitAchievement = 0
        var relativeProfitAchievement = 0
        questDb = QuestDB.getInstance(_MainActivity!!)
        questInGameProfitRateList = questDb?.questDao()?.getQuestToAdapterByTheme("수익률")!!
        questRelativeProfitRateList = questDb?.questDao()?.getQuestToAdapterByTheme("시장대비 수익률")!!
        profitAchievement = 100 - questInGameProfitRateList.size * 10
        relativeProfitAchievement = 100 - questRelativeProfitRateList.size * 20
        if (questInGameProfitRateList.isEmpty()) {
            questInGameProfitRateList = listOf(Quest(0, "수익률", "투자왕","모든 도전과제 달성", 1))
            profitAchievement = 100
        }
        if (questRelativeProfitRateList.isEmpty()) {
            questRelativeProfitRateList = listOf(Quest(0, "시장대비 수익률", "이기는 투자자","모든 도전과제 달성", 1))
            relativeProfitAchievement = 100
        }
        questList = listOf(questInGameProfitRateList.first(), questRelativeProfitRateList.first())
        achievementList = listOf(profitAchievement,relativeProfitAchievement)
        tv_questName1.text = questList[0].theme
        tv_questName2.text = questList[1].theme
        if(questList[0].questcontents.length>13)  tv_questDescription1.text = questList[0].questcontents.slice(IntRange(9,questList[0].questcontents.length-1))
        else tv_questDescription1.text = questList[0].questcontents
        if(questList[1].questcontents.length>15)  tv_questDescription2.text = questList[1].questcontents.slice(IntRange(11,questList[1].questcontents.length-1))
        else tv_questDescription2.text = questList[1].questcontents
        pb_quest1.setProgress(achievementList[0])
        pb_quest2.setProgress(achievementList[1])

        //기본설정(data 불러오기 및 게임 선택창 리사이클러뷰 바인딩)
        gameDb = GameSetDB.getInstace(_MainActivity!!)
        gameNormalDB = GameNormalDB.getInstace(_MainActivity!!)
        Log.d("hongz", "사용자 계정: "+ accountID!!)
        mAdapter = gameNormalDB?.let {
            MyGameAdapter(_MainActivity!!, game){ game -> setId = game.id }
        }!!
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(_MainActivity!!)
        val gamePager = v.findViewById<ViewPager>(R.id.rv_games)
        gamePager.setPadding(50,0,50,0)
        gamePager.pageMargin = 0

        game = ArrayList(gameDb?.gameSetDao()?.getPick(accountID!!,accountID!!+1,accountID!!+2,accountID!!+3)!!)
        //초기자산값 변수
        game.reverse()
        val initialgameset = gameDb?.gameSetDao()?.getSetWithId(accountID+0, accountID!!)
        if (initialgameset != null) {
            setCash = SET_CASH_STEP[initialgameset.setcash]
            setMonthly = SET_MONTHLY_STEP[initialgameset.setmonthly]
            setSalaryraise = SET_SALARY_RAISE_STEP[initialgameset.setsalaryraise]
            setGamelength = initialgameset.setgamelength
            setGamespeed = initialgameset.setgamespeed
        }
//        for (i in 0..gameDb?.gameSetDao()?.getPick(accountID!!,accountID!!+1,accountID!!+2,accountID!!+3)?.size!!-1) {
//            if (gameNormalDB?.gameNormalDao()?.getSetWithNormal(gameDb?.gameSetDao()?.getAll(accountID!!)!![i].id, accountID!!).isNullOrEmpty()) {
//            }
//        }
        mAdapter = MyGameAdapter(_MainActivity!!, game){
            gameUnit -> setId = gameUnit.id
            val intent = Intent(_MainActivity!!, GameNormalActivity::class.java)
            startActivity(intent)
            startGameSet = true
        }
        mAdapter.notifyDataSetChanged()
        gamePager.adapter = mAdapter
        val manager = LinearLayoutManager(_MainActivity!!, LinearLayoutManager.HORIZONTAL, false)
        //gamePager.setHasFixedSize(true)
        manager.reverseLayout = true
        manager.stackFromEnd = true
        //gamePager.layoutManager = manager



        //홈 팁

        v.findViewById<ConstraintLayout>(R.id.cl_dashboard2).setOnClickListener{
            val intent = Intent(_MainActivity!!, TipMainActivity::class.java)
            startActivity(intent)
        }

        v.findViewById<ConstraintLayout>(R.id.cl_dashboard4).setOnClickListener{
            val intent = Intent(_MainActivity!!, HistoryActivity::class.java)
            startActivity(intent)
        }
//        val r = Runnable {
//            try{
//                game = gameDb?.gameSetDao()?.getPick()!!
//                //초기자산값 변수
//                val initialgameset = gameDb?.gameSetDao()?.getSetWithId(0)
//                if (initialgameset != null) {
//                    setCash = SET_CASH_STEP[initialgameset.setcash]
//                    setMonthly = SET_MONTHLY_STEP[initialgameset.setmonthly]
//                    setSalaryraise = SET_SALARY_RAISE_STEP[initialgameset.setsalaryraise]
//                    setGamelength = initialgameset.setgamelength
//                    setGamespeed = initialgameset.setgamespeed
//                }
//                for (i in 0..gameDb?.gameSetDao()?.getPick()?.size!!-1) {
//                    if (gameNormalDB?.gameNormalDao()?.getSetWithNormal(gameDb?.gameSetDao()?.getAll()!![i].id).isNullOrEmpty()) {
//                    }
//                }
//                mAdapter = MyGameSetAdapter(_MainActivity!!, game,gameNormalDB!!){
//                    gameUnit -> setId = gameUnit.id
//                    val intent = Intent(_MainActivity!!, GameNormalActivity::class.java)
//                    startActivity(intent)
//                }
////                mAdapter.notifyDataSetChanged()
//
//                pRecyclerView.adapter = mAdapter
//                val manager = LinearLayoutManager(_MainActivity!!, LinearLayoutManager.HORIZONTAL, false)
//                pRecyclerView.setHasFixedSize(true)
//                manager.reverseLayout = true
//                manager.stackFromEnd = true
//                pRecyclerView.layoutManager = manager
//            }catch (e: Exception){
//                Log.d("tag", "Error - $e")
//            }
//        }
//        val thread = Thread(r)
//        thread.start()


        v.findViewById<ConstraintLayout>(R.id.cl_dashboard3).setOnClickListener{
            val intent = Intent(_MainActivity, QuestActivity::class.java)
            startActivity(intent)
        }

        v.findViewById<ConstraintLayout>(R.id.cl_dashboard4).setOnClickListener {
            val intent = Intent(_MainActivity, NewHistoryActivity::class.java)
            startActivity(intent)
        }

        v.findViewById<TextView>(R.id.tv_use_potion).setOnClickListener {
            val dialogPotion: Dialog_potion = Dialog_potion(_MainActivity!!)
            itemDb?.itemDao()?.getAll()?.get(0)?.let { it1 -> dialogPotion.start(it1.potion) }
        }

        v.findViewById<TextView>(R.id.tv_userName).text = profileDbManager!!.getNickname()+"님"

        // 피로도
        CoroutineScope(Dispatchers.Default).launch {
            val job1 = launch {
                while (true) {
                    v.findViewById<ProgressBar>(R.id.pb_itemfatigue).progress = profileDbManager!!.getValue1()!!
                    delay(200L)
                }
            }
        }

        return v
    }

    override fun onResume() {
        super.onResume()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                FragmentHome().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}