package com.guys_from_301.stock_game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guys_from_301.stock_game.data.GameNormalDB
import com.guys_from_301.stock_game.data.GameSet
import com.guys_from_301.stock_game.data.GameSetDB

class PickGameActivity : AppCompatActivity() {

    //게임 데이터 불러올 변수
    private var gameDb: GameSetDB? = null
    private var game = listOf<GameSet>()
    lateinit var mAdapter: PickAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_pick)
        //기본설정(data 불러오기 및 리사이클러뷰 바인딩)
        gameDb = GameSetDB.getInstace(this)
        mAdapter = PickAdapter(this, game){
            game -> setId = game.id
        }
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        val pRecyclerView = findViewById<RecyclerView>(R.id.pRecyclerView)
        val btnadd = findViewById<Button>(R.id.btnadd)
        pRecyclerView.layoutManager = layoutManager
        val r = Runnable {
            try{
                game = gameDb?.gameSetDao()?.getAll()!!
                mAdapter = PickAdapter(this, game){
                        game -> setId = game.id
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
            }catch (e: Exception){
                Log.d("tag", "Error - $e")
            }
        }
        val thread = Thread(r)
        thread.start()

        //새 게임 추가 버튼
        btnadd.setOnClickListener {
            if(mAdapter.itemCount>=3){ Toast.makeText(this, "투자는 동시에 3개까지 진행할 수 있습니다.", Toast.LENGTH_LONG).show() }
            else{
                val intent = Intent(this, GameNormalActivity::class.java)
                val addRunnable = Runnable {
                    val newGameSetDB = GameSet()
                    if(mAdapter.itemCount == 1){
                        if(game.last().id==1) newGameSetDB.id = 2
                        else if(game.last().id==2) newGameSetDB.id = 3
                        else if(game.last().id==3) newGameSetDB.id = 1
                    }
                    else if(mAdapter.itemCount == 2 ){
                        if(game[0].id+game[1].id == 3) newGameSetDB.id = 3
                        if(game[0].id+game[1].id == 4) newGameSetDB.id = 2
                        if(game[0].id+game[1].id == 5) newGameSetDB.id = 1
                    }
                    setId = newGameSetDB.id
                    newGameSetDB.setcash = START_CASH
                    newGameSetDB.setgamelength = START_GAME_LENGTH
                    newGameSetDB.setgamespeed = START_GAME_SPEED
                    newGameSetDB.setmonthly = START_MONTHLY
                    newGameSetDB.setsalaryraise = START_SALARY_RAISE
                    gameDb?.gameSetDao()?.insert(newGameSetDB)
                }
                val addThread = Thread(addRunnable)
                addThread.start()
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        GameNormalDB.destroyINSTANCE()
        gameDb = null
        super.onDestroy()
    }

}