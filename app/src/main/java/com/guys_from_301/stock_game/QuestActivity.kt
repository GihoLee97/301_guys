package com.guys_from_301.stock_game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guys_from_301.stock_game.data.ProfileDB
import com.guys_from_301.stock_game.data.Quest
import com.guys_from_301.stock_game.data.QuestDB

class QuestActivity : AppCompatActivity() {

    private var questDb: QuestDB? = null
    private var profileDb: ProfileDB? = null
    private var questList = listOf<Quest>()
    private lateinit var tv_level : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest)

        questDb = QuestDB.getInstance(this)
        profileDb = ProfileDB.getInstace(this)
        tv_level = findViewById(R.id.tv_level)
        tv_level.text = "레벨 "+ profileDb?.profileDao()?.getLevel()!!
        var mAdapter = QuestAdapter(this, questList)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        val qRecyclerView = findViewById<RecyclerView>(R.id.qRecyclerView)
        val gridLayoutManager = GridLayoutManager(applicationContext, 3)
        qRecyclerView.layoutManager = gridLayoutManager
        val r = Runnable {
            try{
                questList = questDb?.questDao()?.getAll()!!
                mAdapter = QuestAdapter(this, questList)
                mAdapter.notifyDataSetChanged()

                qRecyclerView.adapter = mAdapter
                val manager = GridLayoutManager(applicationContext, 3)
                qRecyclerView.setHasFixedSize(true)
                manager.reverseLayout = true
                manager.stackFromEnd = true
                qRecyclerView.layoutManager = manager
            }catch (e: Exception){
                Log.d("tag", "Error - $e")
            }
        }
        val thread = Thread(r)
        thread.start()

    }
}