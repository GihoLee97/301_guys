package com.guys_from_301.stock_game

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guys_from_301.stock_game.data.GameNormalDB
import com.guys_from_301.stock_game.data.GameSet
import com.guys_from_301.stock_game.data.GameSetDB
import com.guys_from_301.stock_game.data.ProfileDB

class MarketViewModel(_marketActivity : Context): ViewModel() {
    //변수 선언
    private var marketActivity = _marketActivity
    private var profileDb: ProfileDB? = null
    private var gameSetDb: GameSetDB? = null
    private var gameset: GameSet? = null
    private var gameNormalDb: GameNormalDB? = null

    private var initialId: Int = 0
    private var _stack = MutableLiveData<Int>()
    private var _initialAsset = MutableLiveData<Int>()
    private var _initialMonthly = MutableLiveData<Int>()
    private var _initialSalaryRaise = MutableLiveData<Int>()

    init{
        profileDb = ProfileDB.getInstace(marketActivity)
        gameSetDb = GameSetDB.getInstace(marketActivity)
        gameNormalDb = GameNormalDB.getInstace(marketActivity)
        _stack.value = profileDb?.profileDao()?.getMoney()
        gameset = gameSetDb?.gameSetDao()?.getSetWithId(initialId)
        _initialAsset.value =  gameset?.setcash
        _initialMonthly.value = gameset?.setmonthly
        _initialSalaryRaise.value = gameset?.setsalaryraise
    }

    fun refresh(){
        profileDb = ProfileDB.getInstace(marketActivity)
        gameSetDb = GameSetDB.getInstace(marketActivity)
        gameNormalDb = GameNormalDB.getInstace(marketActivity)
        _stack.value = profileDb?.profileDao()?.getMoney()
        gameset = gameSetDb?.gameSetDao()?.getSetWithId(initialId)
        _initialAsset.value = gameset?.setcash
        _initialMonthly.value = gameset?.setmonthly
        _initialSalaryRaise.value = gameset?.setsalaryraise
    }

    fun writeDataBase(){
        var write = Runnable {
            profileDb = ProfileDB.getInstace(marketActivity)
            var newProfile = profileDb?.profileDao()?.getAll()?.get(0)
            if (newProfile != null) {
                newProfile.money = _stack.value!!
                profileDb?.profileDao()?.update(newProfile)
            }
            gameSetDb = GameSetDB.getInstace(marketActivity)
            var newGameSet = gameSetDb?.gameSetDao()?.getSetWithId(initialId)
            if (newGameSet != null) {
                newGameSet.setcash = _initialAsset.value!!
                newGameSet.setmonthly = _initialMonthly.value!!
                newGameSet.setsalaryraise = _initialSalaryRaise.value!!
                gameSetDb?.gameSetDao()?.insert(newGameSet)
            }
        }
        var writeThread = Thread(write)
        writeThread.start()
    }

    fun getStack():LiveData<Int>{return _stack}
    fun getInitialAsset(): LiveData<Int>{return _initialAsset}
    fun getInitialMonthly(): LiveData<Int>{return _initialMonthly}
    fun getInitialSalaryRaise(): LiveData<Int>{return _initialSalaryRaise}

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
        writeDataBase()
    }

    override fun onCleared() {
        writeDataBase()
        super.onCleared()
    }
}