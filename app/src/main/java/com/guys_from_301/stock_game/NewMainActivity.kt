package com.guys_from_301.stock_game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guys_from_301.stock_game.data.GameNormalDB
import com.guys_from_301.stock_game.data.GameSet
import com.guys_from_301.stock_game.data.GameSetDB

class NewMainActivity : AppCompatActivity() {

    //게임 데이터 불러올 변수
    private var gameDb: GameSetDB? = null
    private var gameNormalDB: GameNormalDB? = null
    private var game = listOf<GameSet>()
    lateinit var mAdapter: MyGameSetAdapter


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_main)
        println("---1: "+ tip_title[0] + " , " + tip_information[0])

        //기본설정(data 불러오기 및 리사이클러뷰 바인딩)
        gameDb = GameSetDB.getInstace(this)
        gameNormalDB = GameNormalDB.getInstace(this)
        mAdapter = gameNormalDB?.let {
            MyGameSetAdapter(this, game, it){ game -> setId = game.id }
        }!!
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        val pRecyclerView = findViewById<RecyclerView>(R.id.rv_games)
        val dialog = Dialog_loading(this@NewMainActivity)
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
                mAdapter = MyGameSetAdapter(this, game,gameNormalDB!!){
                    gameUnit -> setId = gameUnit.id
                    val intent = Intent(this, GameNormalActivity::class.java)
                    startActivity(intent)
                }
                mAdapter.notifyDataSetChanged()

                pRecyclerView.adapter = mAdapter
                val manager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
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

    }
}