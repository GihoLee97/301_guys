package com.guys_from_301.stock_game

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.guys_from_301.stock_game.data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MarketViewModel(_marketActivity : Context): ViewModel() {
    //변수 선언
    private var marketActivity = _marketActivity
//    private var profileDb: ProfileDB? = null
    private var gameSetDb: GameSetDB? = null
    private var gameset: GameSet? = null
    private var gameNormalDb: GameNormalDB? = null
    private var itemDb: ItemDB? = null
    private var itemList: Item? = null

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
        _stack.value = profileDbManager.getMoney()
        gameset = gameSetDb?.gameSetDao()?.getSetWithId(accountID+initialId, accountID!!)
        itemList = itemDb?.itemDao()?.getAll()?.get(0)
        _initialAsset.value =  gameset?.setcash
        _initialMonthly.value = gameset?.setmonthly
        _initialSalaryRaise.value = gameset?.setsalaryraise
        _potion.value = itemList?.potion
    }

    fun refresh(){
        profileDbManager.refresh(marketActivity)
//        profileDb = ProfileDB.getInstace(marketActivity)
        gameSetDb = GameSetDB.getInstace(marketActivity)
        gameNormalDb = GameNormalDB.getInstace(marketActivity)
        itemDb = ItemDB.getInstace(marketActivity)
        _stack.value = profileDbManager.getMoney()
        gameset = gameSetDb?.gameSetDao()?.getSetWithId(accountID+initialId, accountID!!)
        itemList = itemDb?.itemDao()?.getAll()?.get(0)
        _initialAsset.value = gameset?.setcash
        _initialMonthly.value = gameset?.setmonthly
        _initialSalaryRaise.value = gameset?.setsalaryraise
        _potion.value = itemList?.potion
    }

    fun writeDataBase(){
        if (!profileDbManager.isEmpty(marketActivity)) {
            profileDbManager.setMoney(_stack.value!!)
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
        _stack.value = _stack.value?.plus(payment)
        //TODO: 결제 함수가 생기면 결제가 확인되어야 buymoney 실행하게 변경해야 함
        buymoney(getHash(profileDbManager.getLoginId()!!), _stack.value!!)
        writeDataBase()
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

    fun buymoney(u_id: String, u_money : Int) {
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
        funbuymoney.setasset(u_id, u_money).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(_MainActivity, t.message, Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    if(response.body()!! == "555"){
                        Toast.makeText(_MainActivity, "서버에 올라감", Toast.LENGTH_LONG).show()
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

}