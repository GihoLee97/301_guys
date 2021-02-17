package com.guys_from_301.stock_game

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.android.billingclient.api.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BillingManager(val _activity: Activity) : PurchasesUpdatedListener {
    private val activity = _activity

    val billingClient = BillingClient.newBuilder(activity).enablePendingPurchases()
            .setListener(this).build()

    fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    Log.d("Giho","service connected")
                    CoroutineScope(Dispatchers.Default).launch{
                        querySkuDetails()
                    }
                }
            }
            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Toast.makeText(activity,"Billing service disconnected!",Toast.LENGTH_LONG).show()
                startConnection()
            }
        })
    }

    private suspend fun querySkuDetails() {
        Log.d("Giho","querySkuDetails")
        val skuList = ArrayList<String>()
        skuList.add("game_money_1000")
        skuList.add("game_money_2000")
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
        withContext(Dispatchers.IO) {
            billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
                // Process the result.
                skuDetailsList?.forEach {
                    Log.d("Giho",it.title)
                }
                Log.d("Giho",billingResult.responseCode.toString())
                val flowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetailsList?.get(0)!!)
                        .build()
                val responseCode = billingClient.launchBillingFlow(activity, flowParams).responseCode
            }
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            Log.d("Giho","onPurchasesUpdated okay")
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            Log.d("Giho","onPurchasesUpdated user canceled")
        } else {
            // Handle any other error codes.
            Toast.makeText(activity,"onPurchasesUpdated error",Toast.LENGTH_LONG).show()
        }
    }

    private fun handlePurchase(_purchase: Purchase) {
        // Purchase retrieved from BillingClient#queryPurchases or your PurchasesUpdatedListener.
        val purchase : Purchase = _purchase;

        // Verify the purchase.
        // Ensure entitlement was not already granted for this purchaseToken.
        // Grant entitlement to the user.

        val consumeParams =
                ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build()

        billingClient.consumeAsync(consumeParams) { billingResult, outToken ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                // Handle the success of the consume operation.
            }
        }
    }
}