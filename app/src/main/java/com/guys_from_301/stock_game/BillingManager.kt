package com.guys_from_301.stock_game

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.SkuType.INAPP
import com.guys_from_301.stock_game.Security.verifyPurchase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException


class BillingManager(_activity: Activity, _viewModel: MarketViewModel) : PurchasesUpdatedListener {
    private val activity = _activity
    private var PRODUCT_ID = ""
    private val PREF_FILE = "MyPref"
    private val PURCHASE_KEY = "purchase"
    private var viewModel = _viewModel
    private var added_money = 0

    val billingClient = BillingClient.newBuilder(activity).enablePendingPurchases()
            .setListener(this).build()

    fun purchase(_PRODUCT_ID : String, _money : Int){
        PRODUCT_ID = _PRODUCT_ID
        added_money = _money
        if (billingClient.isReady)
            initiatePurchase(PRODUCT_ID)
        else{
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                        // The BillingClient is ready. You can query purchases here.
                        Log.d("Giho","service connected")
                        CoroutineScope(Dispatchers.Default).launch{
                            initiatePurchase(PRODUCT_ID)
//                        querySkuDetails()
                        }
                    }
                    else
                        Toast.makeText(activity,"Billing response is not okay!",Toast.LENGTH_LONG).show()
                }
                override fun onBillingServiceDisconnected() {
                    Toast.makeText(activity,"Billing service disconnected!",Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun initiatePurchase(PRODUCT_ID : String){
        val skuList: MutableList<String> = ArrayList()
        skuList.add(PRODUCT_ID)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(INAPP)
        billingClient.querySkuDetailsAsync(
            params.build()
        ) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                if (skuDetailsList != null && skuDetailsList.size > 0) {
                    val flowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetailsList[0])
                        .build()
                    billingClient.launchBillingFlow(activity, flowParams)
                } else {
                    //try to add item/product id "purchase" inside managed product in google play console
                    Toast.makeText(
                        getApplicationContext(),
                        "Purchase Item not Found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    getApplicationContext(),
                    " Error " + billingResult.debugMessage, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            Log.d("Giho","onPurchasesUpdated okay")
            handlePurchases(purchases)
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            Log.d("Giho","onPurchasesUpdated user canceled")
        } else {
            // Handle any other error codes.
            Toast.makeText(activity,"onPurchasesUpdated error",Toast.LENGTH_LONG).show()
        }
    }

    fun handlePurchases(purchases: List<Purchase>) {

        for (purchase in purchases) {
            //if item is purchase
            Log.d("giholee", PRODUCT_ID.equals(purchase.skus[0]).toString())
            Log.d("giholee", (purchase.purchaseState == Purchase.PurchaseState.PURCHASED).toString())
            Log.d("giholee", purchase.isAcknowledged.toString())
            Log.d("giholee", (purchase.purchaseState == Purchase.PurchaseState.PENDING).toString())
            Log.d("giholee", (purchase.purchaseState == Purchase.PurchaseState.UNSPECIFIED_STATE).toString())

//            var ackPurchase =
//                AcknowledgePurchaseResponseListener { billingResult ->
//                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
//                        //if purchase is acknowledged
//                        // Grant entitlement to the user. and restart activity
//                        savePurchaseValueToPref(true)
//                        Toast.makeText(getApplicationContext(), "Item Purchased", Toast.LENGTH_SHORT).show()
//                        viewModel.BuyStack(money)
//                        consume(purchase)
//                        activity.recreate()
//                    }
//                    else
//                        Log.d("giholee", "fail to acknowledge")
//                }

            if (PRODUCT_ID.equals(purchase.skus[0]) && purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                if (!verifyValidSignature(purchase.originalJson, purchase.signature)) {
                    // Invalid purchase
                    // show error to user
                    Toast.makeText(
                        getApplicationContext(),
                        "Error : Invalid Purchase",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                // else purchase is valid
                //if item is purchased and not acknowledged
                if (!purchase.isAcknowledged) {
                    val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()
//                    billingClient.acknowledgePurchase(acknowledgePurchaseParams, ackPurchase)
                    consume(purchase)
                } else {
                    // Grant entitlement to the user on item purchase
                    // restart activity
                    if (!getPurchaseValueFromPref()) {
                        savePurchaseValueToPref(true)
                        Toast.makeText(
                            getApplicationContext(),
                            "Item Purchased",
                            Toast.LENGTH_SHORT
                        ).show()
                        consume(purchase)
                        activity.recreate()
                    }
                }
            } else if (PRODUCT_ID.equals(purchase.skus[0]) && purchase.purchaseState == Purchase.PurchaseState.PENDING) {
                Toast.makeText(
                    getApplicationContext(),
                    "Purchase is Pending. Please complete Transaction", Toast.LENGTH_SHORT
                ).show()
            } else if (PRODUCT_ID.equals(purchase.skus[0]) && purchase.purchaseState == Purchase.PurchaseState.UNSPECIFIED_STATE) {
                savePurchaseValueToPref(false)
                Toast.makeText(
                    getApplicationContext(),
                    "Purchase Status Unknown",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getPreferenceObject(): SharedPreferences {
        return getApplicationContext<Context>().getSharedPreferences(PREF_FILE, 0)
    }

    private fun getPreferenceEditObject(): SharedPreferences.Editor {
        val pref = getApplicationContext<Context>().getSharedPreferences(PREF_FILE, 0)
        return pref.edit()
    }

    private fun getPurchaseValueFromPref(): Boolean {
        return getPreferenceObject().getBoolean(PURCHASE_KEY, false)
    }

    private fun savePurchaseValueToPref(value: Boolean) {
//        Log.d("Giho", "savePurchaseValueToPref : before")
        getPreferenceEditObject().putBoolean(PURCHASE_KEY, value).commit()
//        Log.d("Giho", "savePurchaseValueToPref : after")
    }



    /**
     * Verifies that the purchase was signed correctly for this developer's public key.
     * <p>Note: It's strongly recommended to perform such check on your backend since hackers can
     * replace this method with "constant true" if they decompile/rebuild your app.
     * </p>
     */
    private fun verifyValidSignature(signedData :String,signature: String) : Boolean  {
        try {
            // To get key go to Developer Console > Select your app > Development Tools > Services & APIs.
            val base64Key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgcRBtmUvGMswOJosjkwkxzY9ijETtXjhpdh89eGwKVgv9MxbI0F70xlcszmkaFquQLZkt/rKnHqbwsjLxwuxDxyaMkHWdcMdaeL5LZIRyT+JsJbTn/Xb4mdlXTucdqzOctqw8QbTzBymVjMswFRYCipv3FJHBcgliBnvT2zzWFMqhTFyllfZUE9zK+tMTb4qgscuaMyqHMdkWjMTqhmmT15rtknRWGBaFg3QTegxM8FBispJgl7GlSKvPPa3bQxjJDjKlpnqN0nQQlFyTBOFiXar1hQ1z2ZsxQZYRxJZP5SrZ5xQIB11k/CEw3MPkjmIaQv+D++ZZQkAnBeiUyePEQIDAQAB"
            return verifyPurchase(base64Key, signedData, signature)
        } catch (e : IOException) {
            return false;
        }
    }

    private fun consume(purchase: Purchase){
        val consumeParams = ConsumeParams
            .newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        billingClient.consumeAsync(consumeParams, ConsumeResponseListener { billingResult, s ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                //if purchase is acknowledged
                // Grant entitlement to the user. and restart activity
                savePurchaseValueToPref(true)
                Toast.makeText(getApplicationContext(), "Item Purchased", Toast.LENGTH_SHORT).show()
                viewModel.BuyStack(added_money)
                activity.recreate()
            }
            else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_NOT_OWNED){
                viewModel.BuyStack(added_money)
                activity.recreate()
            }
            else{
                Log.d("giholee", "fail to acknowledge : "+billingResult.responseCode.toString())
                billingClient.startConnection(object : BillingClientStateListener {
                    override fun onBillingSetupFinished(billingResult: BillingResult) {
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            // The BillingClient is ready. You can query purchases here.
                            Log.d("Giho", "service connected / comsumeAsync")
                            //if purchase is acknowledged
                            // Grant entitlement to the user. and restart activity
                            savePurchaseValueToPref(true)
                            Toast.makeText(getApplicationContext(), "Item Purchased", Toast.LENGTH_SHORT).show()
//                            Log.d("Giho", "service connected / comsumeAsync2")
                            viewModel.BuyStack(added_money)
//                            Log.d("Giho", "service connected / comsumeAsync3")

                            activity.recreate()
                        } else {
                            Toast.makeText(
                                activity,
                                "Billing response is not okay!",
                                Toast.LENGTH_LONG
                            ).show()
                            consume(purchase)
                        }
                    }
                    override fun onBillingServiceDisconnected() {
                        Toast.makeText(activity,"Billing service disconnected! / comsumeAsync",Toast.LENGTH_LONG).show()
                        consume(purchase)
                    }
                })
            }

        })
    }

}