package com.guys_from_301.stock_game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager


var tipTitle : String = ""
var tipDescription: String = ""
class TipMainActivity : AppCompatActivity() {
    //actionbar
    private lateinit var myPostList: ArrayList<MyPost>
    private lateinit var myAdapter: MyPostAdapter // 명언 어뎁터
    private lateinit var tipList : ArrayList<MyPost>
    private lateinit var rAdapter: TipRowAdapter // 팁 어뎁터
    private lateinit var viewPager: ViewPager
    private lateinit var btn_back: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tip_main)
        btn_back = findViewById(R.id.btn_back)
        viewPager = findViewById(R.id.vp_tip)

        loadCards()
        loadTip()

        btn_back.setOnClickListener {
            onBackPressed()
        }


    }

    private fun loadCards() {
        myPostList = ArrayList()

        for( i in 14 .. tip_title.size-1){
            myPostList.add(MyPost(tip_title[i], tip_information[i], "0"))
        }

        myAdapter = MyPostAdapter(this, myPostList)

        viewPager.adapter = myAdapter

        viewPager.setPadding(100,0,100,0)
        viewPager.pageMargin = 50
    }

    private fun loadTip(){
        tipList = ArrayList()
        for(i in 0..13){
            tipList.add(MyPost(tip_title[13-i], tip_information[13-i], ""))
        }
        rAdapter=  TipRowAdapter(this, tipList, {tip -> tipTitle = tip.title
        tipDescription = tip.description})
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        val tRecyclerView = findViewById<RecyclerView>(R.id.tRecyclerView)
        tRecyclerView.layoutManager = layoutManager
        val r = Runnable {
            try{
                rAdapter = TipRowAdapter(this, tipList, {tip -> tipTitle = tip.title
                    tipDescription = tip.description
                    val intent = Intent(this,TipDetailActivity::class.java)
                    startActivity(intent)
                })

                rAdapter.notifyDataSetChanged()

                tRecyclerView.adapter = rAdapter
                val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                tRecyclerView.setHasFixedSize(true)
                manager.reverseLayout = true
                manager.stackFromEnd = true
                tRecyclerView.layoutManager = manager
                tRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
            }catch (e: Exception){
                Log.d("tag", "Error - $e")
            }
        }
        val thread = Thread(r)
        thread.start()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}