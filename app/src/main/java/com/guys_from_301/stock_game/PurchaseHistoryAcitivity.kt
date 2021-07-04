package com.guys_from_301.stock_game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class PurchaseHistoryAcitivity : AppCompatActivity() {
    private lateinit var cl_donot_purchase: ConstraintLayout
    private var purchaseList = ArrayList<MyPost>()
    lateinit var pAdapter: PurchaseHistoryAdapter
    private lateinit var ib_back: ImageButton
    private lateinit var tv_donot_purchase: TextView
    private lateinit var btn_check_purchase_history : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_history_acitivity)

        ib_back = findViewById(R.id.ib_back)
        cl_donot_purchase = findViewById(R.id.cl_donot_purchase)
        btn_check_purchase_history = findViewById(R.id.btn_check_purchase_history)

        cl_donot_purchase.visibility = View.INVISIBLE

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        val pRecyclerView = findViewById<RecyclerView>(R.id.pRecyclerView)
        pRecyclerView.layoutManager = layoutManager

        val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                Log.d("Giho","onPurchasesUpdated okay")
//                handlePurchases(purchases)
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.
                Log.d("Giho","onPurchasesUpdated user canceled")
            } else {
                // Handle any other error codes.
                Toast.makeText(this,"onPurchasesUpdated error",Toast.LENGTH_LONG).show()
            }
        }

        val billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(purchasesUpdatedListener).build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    Log.d("Giho","service connected")
                    billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS) { billingResult, purchaseHistoryRecords ->
                         if (purchaseHistoryRecords != null) {
                             Log.d("Giho", "purchaseHistoryRecords != null")
                             if(purchaseHistoryRecords.isEmpty()){
                                 Log.d("Giho", "but purchaseHistoryRecords is empty")
                             }
                             for (record in purchaseHistoryRecords){
                                 var date = Date(record.purchaseTime)
                                 var textDate = date.year.toString()+"년 "+date.month.toString()+"월 "+date.day.toString()+"일"
                                 if (record.skus[0]=="game_money_1000")
                                     purchaseList.add(MyPost("2,000스택","1000원",textDate))
                                 else if (record.skus[0]=="game_money_2000")
                                     purchaseList.add(MyPost("4,000스택","2000원",textDate))
                                 else if (record.skus[0]=="game_money_10000")
                                     purchaseList.add(MyPost("40,000스택","10000원",textDate))
                             }
                         }
                         else
                             Log.d("Giho", "purchaseHistoryRecords == null")
                    }
                }
                else
                    Toast.makeText(this@PurchaseHistoryAcitivity,"Billing response is not okay!",Toast.LENGTH_LONG).show()
            }
            override fun onBillingServiceDisconnected() {
                Toast.makeText(this@PurchaseHistoryAcitivity,"Billing service disconnected!",Toast.LENGTH_LONG).show()
            }
        })

        ib_back.setOnClickListener {
            onBackPressed()
        }

        btn_check_purchase_history.setOnClickListener {
            this@PurchaseHistoryAcitivity.runOnUiThread {
                if(!purchaseList.isEmpty()) {
                    try{
                        cl_donot_purchase.visibility = View.INVISIBLE
                        pAdapter = PurchaseHistoryAdapter(this@PurchaseHistoryAcitivity, purchaseList)
                        pAdapter.notifyDataSetChanged()
                        pRecyclerView.adapter = pAdapter
                        val manager = LinearLayoutManager(this@PurchaseHistoryAcitivity, LinearLayoutManager.VERTICAL, false)
                        pRecyclerView.setHasFixedSize(true)
                        manager.reverseLayout = true
                        manager.stackFromEnd = true
                        pRecyclerView.layoutManager = manager
                        pRecyclerView.addItemDecoration(DividerItemDecoration(this@PurchaseHistoryAcitivity, LinearLayoutManager.VERTICAL))
                        pAdapter.notifyDataSetChanged()
                    }catch (e: Exception){
                        Log.d("tag", "Error - $e")
                    }
                }
                else
                    cl_donot_purchase.visibility = View.VISIBLE
            }
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}