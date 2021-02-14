package com.guys_from_301.stock_game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
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
                val manager = LinearLayoutManager(this)
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
            val intent = Intent(this, GameSettingActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        GameNormalDB.destroyINSTANCE()
        gameDb = null
        super.onDestroy()
    }

}