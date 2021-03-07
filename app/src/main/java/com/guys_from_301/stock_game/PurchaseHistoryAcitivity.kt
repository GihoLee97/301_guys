package com.guys_from_301.stock_game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PurchaseHistoryAcitivity : AppCompatActivity() {
    private lateinit var cl_donot_purchase: ConstraintLayout
    private var purchaseList = ArrayList<MyPost>()
    lateinit var pAdapter: PurchaseHistoryAdapter
    private lateinit var ib_back: ImageButton
    private lateinit var tv_donot_purchase: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_history_acitivity)

        ib_back = findViewById(R.id.ib_back)
        cl_donot_purchase = findViewById(R.id.cl_donot_purchase)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        val pRecyclerView = findViewById<RecyclerView>(R.id.pRecyclerView)
        pRecyclerView.layoutManager = layoutManager

        val r = Runnable {
            try{
                purchaseList.add(MyPost("100,000스택","1000원","2021년 3월 7일"))
                pAdapter = PurchaseHistoryAdapter(this, purchaseList)
                pAdapter.notifyDataSetChanged()

                pRecyclerView.adapter = pAdapter
                val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                pRecyclerView.setHasFixedSize(true)
                manager.reverseLayout = true
                manager.stackFromEnd = true
                pRecyclerView.layoutManager = manager
                pRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
                if(purchaseList.isEmpty() == false) cl_donot_purchase.visibility = View.INVISIBLE

            }catch (e: Exception){
                Log.d("tag", "Error - $e")
            }
        }
        val thread = Thread(r)
        thread.start()

        ib_back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}