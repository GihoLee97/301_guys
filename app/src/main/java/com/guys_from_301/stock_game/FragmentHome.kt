package com.guys_from_301.stock_game

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guys_from_301.stock_game.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    private lateinit var tv_profitRate: TextView
    //게임 데이터 불러올 변수
    private var gameDb: GameSetDB? = null
    private var gameNormalDB: GameNormalDB? = null
    private var game = listOf<GameSet>()
    lateinit var mAdapter: MyGameSetAdapter

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
        itemDb = ItemDB.getInstace(_MainActivity!!)

        tv_profitRate = v.findViewById(R.id.tv_profitRate)
        tv_profitRate.text = per.format(profileDbManager!!.getProfitRate()!!).toString()+"%"


        //기본설정(data 불러오기 및 게임 선택창 리사이클러뷰 바인딩)
        gameDb = GameSetDB.getInstace(_MainActivity!!)
        gameNormalDB = GameNormalDB.getInstace(_MainActivity!!)
        Log.d("hongz", "사용자 계정: "+ accountID!!)
        mAdapter = gameNormalDB?.let {
            MyGameSetAdapter(_MainActivity!!, game, it){ game -> setId = game.id }
        }!!
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(_MainActivity!!)
        val pRecyclerView = v.findViewById<RecyclerView>(R.id.rv_games)
        pRecyclerView.layoutManager = layoutManager

        game = gameDb?.gameSetDao()?.getPick(accountID!!,accountID!!+1,accountID!!+2,accountID!!+3)!!
        //초기자산값 변수
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
        mAdapter = MyGameSetAdapter(_MainActivity!!, game,gameNormalDB!!){
            gameUnit -> setId = gameUnit.id
            val intent = Intent(_MainActivity!!, GameNormalActivity::class.java)
            startActivity(intent)
        }
//                mAdapter.notifyDataSetChanged()

        pRecyclerView.adapter = mAdapter
        val manager = LinearLayoutManager(_MainActivity!!, LinearLayoutManager.HORIZONTAL, false)
        pRecyclerView.setHasFixedSize(true)
        manager.reverseLayout = true
        manager.stackFromEnd = true
        pRecyclerView.layoutManager = manager



        //홈 팁

        v.findViewById<ConstraintLayout>(R.id.cl_dashboard2).setOnClickListener{
            val dlg = Dialog_home_tip(_MainActivity!!)
            dlg.show()
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