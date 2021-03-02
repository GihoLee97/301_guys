package com.guys_from_301.stock_game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guys_from_301.stock_game.data.GameNormalDB
import com.guys_from_301.stock_game.data.Notice
import com.guys_from_301.stock_game.data.NoticeDB

var noticeId : Int = 0
class NoticeActivity : AppCompatActivity() {
    private var noticeDb: NoticeDB? = null
    private var notice = listOf<Notice>()
    private lateinit var btn_back: Button
    lateinit var mAdapter: NoticeAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)
        noticeDb = NoticeDB.getInstace(this)
        mAdapter = NoticeAdapter(this, notice, {notice ->  noticeId=notice.id})
        btn_back = findViewById(R.id.btn_back)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        val nRecyclerView = findViewById<RecyclerView>(R.id.nRecyclerView)
        nRecyclerView.layoutManager = layoutManager

        val r = Runnable {
            try{
                notice = noticeDb?.noticeDao()?.getAll()!!
                mAdapter = NoticeAdapter(this, notice,{notice ->  noticeId=notice.id})

                mAdapter.notifyDataSetChanged()

                nRecyclerView.adapter = mAdapter
                val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                nRecyclerView.setHasFixedSize(true)
                manager.reverseLayout = true
                manager.stackFromEnd = true
                nRecyclerView.layoutManager = manager
                nRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
            }catch (e: Exception){
                Log.d("tag", "Error - $e")
            }
        }
        val thread = Thread(r)
        thread.start()

        btn_back.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onDestroy() {
        GameNormalDB.destroyINSTANCE()
        noticeDb = null
        super.onDestroy()
    }

}