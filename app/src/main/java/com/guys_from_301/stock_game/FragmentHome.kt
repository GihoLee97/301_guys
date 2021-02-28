package com.guys_from_301.stock_game

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guys_from_301.stock_game.data.*

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
    var profileDb : ProfileDB? = null
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
        profileDb = ProfileDB.getInstace(_MainActivity!!)

        //기본설정(data 불러오기 및 리사이클러뷰 바인딩)
        gameDb = GameSetDB.getInstace(_MainActivity!!)
        gameNormalDB = GameNormalDB.getInstace(_MainActivity!!)
        mAdapter = gameNormalDB?.let {
            MyGameSetAdapter(_MainActivity!!, game, it){ game -> setId = game.id }
        }!!
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(_MainActivity!!)
        val pRecyclerView = v.findViewById<RecyclerView>(R.id.rv_games)
        val dialog = Dialog_loading(_MainActivity!!)
        dialog.show()
        pRecyclerView.layoutManager = layoutManager
        val r = Runnable {
            try{
                game = gameDb?.gameSetDao()?.getPick()!!
                //초기자산값 변수
                val initialgameset = gameDb?.gameSetDao()?.getSetWithId(0)
                if (initialgameset != null) {
                    setCash = SET_CASH_STEP[initialgameset.setcash]
                    setMonthly = SET_MONTHLY_STEP[initialgameset.setmonthly]
                    setSalaryraise = SET_SALARY_RAISE_STEP[initialgameset.setsalaryraise]
                    setGamelength = initialgameset.setgamelength
                    setGamespeed = initialgameset.setgamespeed
                }
                for (i in 0..gameDb?.gameSetDao()?.getPick()?.size!!-1) {
                    if (gameNormalDB?.gameNormalDao()?.getSetWithNormal(gameDb?.gameSetDao()?.getAll()!![i].id).isNullOrEmpty()) {
                    }
                }
                mAdapter = MyGameSetAdapter(_MainActivity!!, game,gameNormalDB!!){
                        gameUnit -> setId = gameUnit.id
                    val intent = Intent(_MainActivity!!, GameNormalActivity::class.java)
                    startActivity(intent)
                }
                mAdapter.notifyDataSetChanged()

                pRecyclerView.adapter = mAdapter
                val manager = LinearLayoutManager(_MainActivity!!, LinearLayoutManager.HORIZONTAL, false)
                pRecyclerView.setHasFixedSize(true)
                manager.reverseLayout = true
                manager.stackFromEnd = true
                pRecyclerView.layoutManager = manager
                dialog.dismiss()
            }catch (e: Exception){
                Log.d("tag", "Error - $e")
            }
        }
        val thread = Thread(r)
        thread.start()


        v.findViewById<ConstraintLayout>(R.id.cl_dashboard3).setOnClickListener{
            val intent = Intent(_MainActivity, QuestActivity::class.java)
            startActivity(intent)
        }

        v.findViewById<TextView>(R.id.tv_userName).text = profileDb?.profileDao()?.getNickname()!!+"님"

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