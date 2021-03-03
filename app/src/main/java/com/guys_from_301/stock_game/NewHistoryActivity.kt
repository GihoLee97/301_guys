package com.guys_from_301.stock_game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guys_from_301.stock_game.data.GameNormal
import com.guys_from_301.stock_game.data.GameNormalDB


class NewHistoryActivity : AppCompatActivity() {
    private lateinit var tv_nickname: TextView
    private lateinit var tv_tradeday: TextView
    private lateinit var tv_best_profitrate: TextView
    private lateinit var tv_cumulative_profitrate: TextView
    private lateinit var iv_share : ImageView
    private lateinit var ib_back : ImageButton

    private var gameNormalDB : GameNormalDB? = null
    private var game = listOf<GameNormal>()
    lateinit var mAdaper: MyHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        tv_nickname = findViewById(R.id.tv_nickname)
        tv_tradeday = findViewById(R.id.tv_tradeday)
        tv_best_profitrate = findViewById(R.id.tv_best_profitrate)
        tv_cumulative_profitrate = findViewById(R.id.tv_cumulative_profitrate)
        iv_share = findViewById(R.id.iv_share)
        ib_back = findViewById(R.id.ib_go_back)

        gameNormalDB = GameNormalDB.getInstace(this)
        game = gameNormalDB?.gameNormalDao()?.getLast(accountID!!)!!

        tv_nickname.text = profileDbManager!!.getNickname()
        tv_tradeday.text = "투자한지 "+ dec.format(profileDbManager!!.getHistory())+"일 째"
        tv_cumulative_profitrate.text = per.format(profileDbManager!!.getProfitRate())+"%"
        tv_best_profitrate.text = per.format(gameNormalDB?.gameNormalDao()?.getBestProfit(accountID!!))+"%"
        if(tv_best_profitrate.text.isNullOrBlank()) tv_best_profitrate.text ="00.00%"



        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        val hRecyclerView = findViewById<RecyclerView>(R.id.hRecyclerView)
        hRecyclerView.layoutManager = layoutManager
        mAdaper = MyHistoryAdapter(this, game){
            gameUnit ->
            isHistory = true
            profitrate = gameUnit.profitrate
            relativeprofitrate = gameUnit.relativeprofitrate
            taxtot = gameUnit.taxtot
            dividendtot = gameUnit.dividendtot
            tradecomtot = gameUnit.tradecomtot
            profit = gameUnit.profit
            totaltradeday = gameUnit.item1able
            val intent = Intent(this@NewHistoryActivity, NewResultNormalActivity::class.java)
            startActivity(intent)
        }

        hRecyclerView.adapter = mAdaper
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        hRecyclerView.setHasFixedSize(true)
        manager.reverseLayout = true
        manager.stackFromEnd = true
        hRecyclerView.layoutManager = manager

        ib_back.setOnClickListener {
            onBackPressed()
        }

    }
}