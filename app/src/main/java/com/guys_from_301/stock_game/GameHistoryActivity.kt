package com.guys_from_301.stock_game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guys_from_301.stock_game.data.GameNormal
import com.guys_from_301.stock_game.data.GameNormalDB

class GameHistoryActivity : AppCompatActivity() {

    //변수선언
    private var historyDb: GameNormalDB? = null
    private var history = listOf<GameNormal>()
    lateinit var mAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_history)
        //기본설정(data 불러오기 및 리사이클러뷰 바인딩)
        historyDb = GameNormalDB.getInstace(this)
        mAdapter = HistoryAdapter(this, history)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        val hRecyclerView = findViewById<RecyclerView>(R.id.hRecyclerView)
        hRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        hRecyclerView.layoutManager = layoutManager
        val r= Runnable {
            try{
                history = historyDb?.gameNormalDao()?.getHistory(accountID!!)!!
                mAdapter = HistoryAdapter(this, history)
                mAdapter.notifyDataSetChanged()

                hRecyclerView.adapter = mAdapter
                val manager = LinearLayoutManager(this)
                hRecyclerView.setHasFixedSize(true)
                manager.reverseLayout = true
                manager.stackFromEnd = true
                hRecyclerView.layoutManager = manager
            }catch (e: Exception){
                Log.d("tag", "Error - $e")
            }
        }
        val thread = Thread(r)
        thread.start()

    }

    override fun onDestroy() {
        GameNormalDB.destroyINSTANCE()
        historyDb = null
        super.onDestroy()
    }
}