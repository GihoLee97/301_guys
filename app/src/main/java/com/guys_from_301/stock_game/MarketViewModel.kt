package com.guys_from_301.stock_game

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.test.core.app.ApplicationProvider
import com.android.billingclient.api.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.guys_from_301.stock_game.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.ArrayList

class MarketViewModel(_marketActivity : Context, _activity: Activity): ViewModel() {
    //변수 선언
    private var marketActivity = _marketActivity
//    private var profileDb: ProfileDB? = null
    private var gameSetDb: GameSetDB? = null
    private var gameset: GameSet? = null
    private var gameNormalDb: GameNormalDB? = null
    private var itemDb: ItemDB? = null
    private var itemList: Item? = null

    private var activity = _activity

    private var initialId: Int = 0
    private var _stack = MutableLiveData<Int>()
    private var _initialAsset = MutableLiveData<Int>()
    private var _initialMonthly = MutableLiveData<Int>()
    private var _initialSalaryRaise = MutableLiveData<Int>()
    private var _potion = MutableLiveData<Int>()

    init{
        gameSetDb = GameSetDB.getInstace(marketActivity)
        gameNormalDb = GameNormalDB.getInstace(marketActivity)
        itemDb = ItemDB.getInstace(marketActivity)
        _stack.value = profileDbManager!!.getMoney()
        gameset = gameSetDb?.gameSetDao()?.getSetWithId(accountID+initialId, accountID!!)
        itemList = itemDb?.itemDao()?.getAll()?.get(0)
        _initialAsset.value =  gameset?.setcash
        _initialMonthly.value = gameset?.setmonthly
        _initialSalaryRaise.value = gameset?.setsalaryraise
        _potion.value = itemList?.potion
    }

    fun refresh(){
        profileDbManager!!.refresh(marketActivity)
//        profileDb = ProfileDB.getInstace(marketActivity)
        gameSetDb = GameSetDB.getInstace(marketActivity)
        gameNormalDb = GameNormalDB.getInstace(marketActivity)
        itemDb = ItemDB.getInstace(marketActivity)
        _stack.value = profileDbManager!!.getMoney()
        gameset = gameSetDb?.gameSetDao()?.getSetWithId(accountID+initialId, accountID!!)
        itemList = itemDb?.itemDao()?.getAll()?.get(0)
        _initialAsset.value = gameset?.setcash
        _initialMonthly.value = gameset?.setmonthly
        _initialSalaryRaise.value = gameset?.setsalaryraise
        _potion.value = itemList?.potion
    }

    fun writeDataBase(){
        if (!profileDbManager!!.isEmpty(marketActivity)) {
            profileDbManager!!.setnWriteMoney(_stack.value!!)
        }
        var write = Runnable {
            gameSetDb = GameSetDB.getInstace(marketActivity)
            var newGameSet = gameSetDb?.gameSetDao()?.getSetWithId(accountID+initialId, accountID!!)
            if (newGameSet != null) {
                newGameSet.setcash = _initialAsset.value!!
                newGameSet.setmonthly = _initialMonthly.value!!
                newGameSet.setsalaryraise = _initialSalaryRaise.value!!
                gameSetDb?.gameSetDao()?.insert(newGameSet)
            }
            itemDb = ItemDB.getInstace(marketActivity)
            var newItem = itemDb?.itemDao()?.getAll()?.get(0)
            if (newItem != null) {
                newItem.potion = _potion.value!!
                itemDb?.itemDao()?.update(newItem)
            }
        }
        var writeThread = Thread(write)
        writeThread.start()
    }

    fun getStack():LiveData<Int>{return _stack}
    fun getInitialAsset(): LiveData<Int>{return _initialAsset}
    fun getInitialMonthly(): LiveData<Int>{return _initialMonthly}
    fun getInitialSalaryRaise(): LiveData<Int>{return _initialSalaryRaise}
    fun getPotion():LiveData<Int>{return _potion}

    fun applyItemRaiseSetCash(){
        _initialAsset.value = _initialAsset.value?.plus(1)
        _stack.value = _stack.value?.minus(ITEM_COST)
        writeDataBase()
    }

    fun applyItemFailtoSetCash(){
        _initialAsset.value = 0
        _stack.value = _stack.value?.minus(ITEM_COST)
        writeDataBase()
    }

    fun applyItemRaiseSetMonthly(){
        _initialMonthly.value = _initialMonthly.value?.plus(1)
        _stack.value = _stack.value?.minus(ITEM_COST)
        writeDataBase()
    }

    fun applyItemFailtoSetMonthly(){
        _initialMonthly.value = 0
        _stack.value = _stack.value?.minus(ITEM_COST)
        writeDataBase()
    }

    fun applyItemRaiseSetSalaryRaise(){
        _initialSalaryRaise.value = _initialSalaryRaise.value?.plus(1)
        _stack.value = _stack.value?.minus(ITEM_COST)
        writeDataBase()
    }

    fun applyItemFailtoSetSalaryRaise(){
        _initialSalaryRaise.value = 0
        _stack.value = _stack.value?.minus(ITEM_COST)
        writeDataBase()
    }

    fun BuyStack(payment: Int){
        Log.d("Giho", "BuyStack"+_stack.value.toString() + payment.toString())
//        _stack.value = _stack.value?.plus(payment)
//        _stack.value = _stack.value!! + payment

        //TODO: 결제 함수가 생기면 결제가 확인되어야 buymoney 실행하게 변경해야 함
        buymoney(getHash(profileDbManager!!.getLoginId()!!), _stack.value!!,payment)
        Log.d("Giho", "BuyStack+ "+_stack.value.toString())

    }

    fun BuyPotion(){
        _potion.value = _potion.value?.plus(1)
        _stack.value = _stack.value?.minus(POTION_COST)
        writeDataBase()
    }

    override fun onCleared() {
        writeDataBase()
        super.onCleared()
    }

    fun buymoney(u_id: String, u_money : Int, payment: Int) {
        Log.d("Giho", "Buymoney")
        var funbuymoney: RetrofitBuyMoney? = null
        val url = "http://stockgame.dothome.co.kr/test/buymoney.php/"
        var gson: Gson = GsonBuilder()
                .setLenient()
                .create()
        //creating retrofit object
        var retrofit =
                Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
        //creating our api
        funbuymoney= retrofit.create(RetrofitBuyMoney::class.java)
        funbuymoney.setasset(u_id, u_money + payment).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(_MainActivity, t.message, Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    if(response.body()!! == "555"){
                        _stack.value = _stack.value?.plus(payment)
                        Toast.makeText(_MainActivity, "서버에 올라감", Toast.LENGTH_LONG).show()
                        writeDataBase()

                    }
                    else if(response.body()!! == "666"){
                        Toast.makeText(_MainActivity, "오류 발생", Toast.LENGTH_LONG).show()
                    }
                    else {
                        Toast.makeText(_MainActivity, "앱을 종료 후 다시 실행시켜 주세요", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    //// purchase
    private var PRODUCT_ID = ""
    private val PREF_FILE = "MyPref"
    private val PURCHASE_KEY = "purchase"
    private var added_money = 0


    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            Log.d("Giho","onPurchasesUpdated okay")
            handlePurchases(purchases)
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            Log.d("Giho","onPurchasesUpdated user canceled")
        } else {
            // Handle any other error codes.
            Toast.makeText(marketActivity,"onPurchasesUpdated error",Toast.LENGTH_LONG).show()
        }
    }

    val billingClient = BillingClient.newBuilder(marketActivity).enablePendingPurchases()
        .setListener(purchasesUpdatedListener).build()

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
                        Toast.makeText(marketActivity,"Billing response is not okay!",Toast.LENGTH_LONG).show()
                }
                override fun onBillingServiceDisconnected() {
                    Toast.makeText(marketActivity,"Billing service disconnected!",Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun initiatePurchase(PRODUCT_ID : String){
        val skuList: MutableList<String> = ArrayList()
        skuList.add(PRODUCT_ID)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
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
                        ApplicationProvider.getApplicationContext(),
                        "Purchase Item not Found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    ApplicationProvider.getApplicationContext(),
                    " Error " + billingResult.debugMessage, Toast.LENGTH_SHORT
                ).show()
            }
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
                        ApplicationProvider.getApplicationContext(),
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
                            ApplicationProvider.getApplicationContext(),
                            "Item Purchased",
                            Toast.LENGTH_SHORT
                        ).show()
                        consume(purchase)
//                        marketActivity.recreate() TODO
                    }
                }
            } else if (PRODUCT_ID.equals(purchase.skus[0]) && purchase.purchaseState == Purchase.PurchaseState.PENDING) {
                Toast.makeText(
                    ApplicationProvider.getApplicationContext(),
                    "Purchase is Pending. Please complete Transaction", Toast.LENGTH_SHORT
                ).show()
            } else if (PRODUCT_ID.equals(purchase.skus[0]) && purchase.purchaseState == Purchase.PurchaseState.UNSPECIFIED_STATE) {
                savePurchaseValueToPref(false)
                Toast.makeText(
                    ApplicationProvider.getApplicationContext(),
                    "Purchase Status Unknown",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getPreferenceObject(): SharedPreferences {
        return ApplicationProvider.getApplicationContext<Context>()
            .getSharedPreferences(PREF_FILE, 0)
    }

    private fun getPreferenceEditObject(): SharedPreferences.Editor {
        val pref = ApplicationProvider.getApplicationContext<Context>()
            .getSharedPreferences(PREF_FILE, 0)
        return pref.edit()
    }

    private fun getPurchaseValueFromPref(): Boolean {
        return getPreferenceObject().getBoolean(PURCHASE_KEY, false)
    }

    private fun savePurchaseValueToPref(value: Boolean) {
        getPreferenceEditObject().putBoolean(PURCHASE_KEY, value).commit()
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
            return Security.verifyPurchase(base64Key, signedData, signature)
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
                Toast.makeText(ApplicationProvider.getApplicationContext(), "Item Purchased", Toast.LENGTH_SHORT).show()
                BuyStack(added_money)
            }
            else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_NOT_OWNED){
                BuyStack(added_money)
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
//                            savePurchaseValueToPref(true)
//                            Toast.makeText(ApplicationProvider.getApplicationContext(), "Item Purchased", Toast.LENGTH_SHORT).show()
                            BuyStack(added_money)
                        } else {
                            Toast.makeText(
                                marketActivity,
                                "Billing response is not okay!",
                                Toast.LENGTH_LONG
                            ).show()
                            consume(purchase)
                        }
                    }
                    override fun onBillingServiceDisconnected() {
                        Toast.makeText(marketActivity,"Billing service disconnected! / comsumeAsync",Toast.LENGTH_LONG).show()
                        consume(purchase)
                    }
                })
            }

        })
    }

}