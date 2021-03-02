package com.guys_from_301.stock_game

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guys_from_301.stock_game.data.Profile
import com.guys_from_301.stock_game.data.ProfileDB
import kotlin.math.round

class ProfileActivityViewModel(_profileActivity : Context): ViewModel() {
    //변수 선언
    private var profileActivity = _profileActivity
//    private var profileDb: ProfileDB? = null

    private var id = MutableLiveData<Long>()
    private var nickname = MutableLiveData<String>()
    private var money = MutableLiveData<Int>()
    private var value1 = MutableLiveData<Int>()
    private var relativeprofitrate = MutableLiveData<Float>()
    private var profitrate = MutableLiveData<Float>()
    private var roundcount = MutableLiveData<Int>()
    private var history = MutableLiveData<Int>()
    private var level = MutableLiveData<Int>()
    private var exp = MutableLiveData<Int>()
    private var rank = MutableLiveData<Int>()
    private var login = MutableLiveData<Int>()
    private var login_id = MutableLiveData<String>()
    private var login_pw = MutableLiveData<String>()
    private var hash = MutableLiveData<String>()

    init {
        profileDbManager.refresh(profileActivity)
        if(!profileDbManager.isEmpty(profileActivity)) {
            id.value = profileDbManager.getId()
            nickname.value = profileDbManager.getNickname()
            money.value = profileDbManager.getMoney()
            value1.value = profileDbManager.getValue1()
            profitrate.value = profileDbManager.getProfitRate()
            relativeprofitrate.value = profileDbManager.getRelativeProfit()
            roundcount.value = profileDbManager.getRoundCount()
            history.value = profileDbManager.getHistory()
            level.value = profileDbManager.getLevel()
            exp.value = profileDbManager.getExp()
            rank.value = profileDbManager.getRank()
            login.value = profileDbManager.getLogin()
            login_id.value = profileDbManager.getLoginId()
            login_pw.value = profileDbManager.getLoginPw()
            hash.value = profileDbManager.getHash()
        }
    }

    fun refresh(){
        if(profileDbManager.getHash()== profileDbManager.getHashRespectFromDbManager())
            profileDbManager.write2database()
    }

    fun write2database(){
        profileDbManager.write2database()
//        var write = Runnable {
//            profileDb = ProfileDB.getInstace(profileActivity)
//            var newProfile = Profile(id.value!!,nickname.value!!, money.value!!,value1.value!!,profitrate.value!!,relativeprofitrate.value!!,roundcount.value!!,history.value!!,
//                    level.value!!,exp.value!!,rank.value!!,login.value!!,login_id.value!!,login_pw.value!!,hash.value!!)
//            profileDbManager.update(newProfile)
//        }
//        var writeThread = Thread(write)
//        writeThread.start()
    }

    // setter
    fun setId(newId : Long){
        id.value = newId
        profileDbManager.setId(newId)
    }
    fun setNickname(newNickname : String){
        nickname.value = newNickname
        profileDbManager.setNickname(newNickname)
    }
    fun setMoney(newMoney : Int){
        money.value = newMoney
        profileDbManager.setMoney(newMoney)
    }
    fun setValue1(newValue1 : Int){
        value1.value = newValue1
        profileDbManager.setValue1(newValue1)
    }
    fun setProfitRate(newProfitRate: Float){
        profitrate.value = newProfitRate
        profileDbManager.setProfitRate(newProfitRate)
    }
    fun setRelativeProfit(newRelativeProfit : Float){
        relativeprofitrate.value = newRelativeProfit
        profileDbManager.setRelativeProfit(newRelativeProfit)
    }
    fun setRoundCount(newRoundCount : Int){
        roundcount.value = newRoundCount
        profileDbManager.setRoundCount(newRoundCount)
    }
    fun setHistory(newHistory : Int){
        history.value = newHistory
        profileDbManager.setHistory(newHistory)
    }
    fun setLevel(newLevel : Int){
        level.value = newLevel
        profileDbManager.setLevel(newLevel)
    }
    fun setExp(newExp: Int){
        exp.value = newExp
        profileDbManager.setExp(newExp)
    }
    fun setRank(newRank : Int){
        rank.value = profileDbManager.getRank()
        profileDbManager.setRank(newRank)
    }
    fun setLogin(newLogin : Int){
        login.value = newLogin
        profileDbManager.setLogin(newLogin)
    }
    fun setLoginId(newLoginId : String){
        login_id.value = newLoginId
        profileDbManager.setLoginId(newLoginId)
    }
    fun setLoginPw(newLoginPw: String){
        login_pw.value = newLoginPw
        profileDbManager.setLoginPw(newLoginPw)
    }

    // setter
    fun setnWriteId(newId : Long){
        setId(newId)
        write2database() }
    fun setnWriteNickname(newNickname : String){
        setNickname(newNickname)
        write2database() }
    fun setnWriteMoney(newMoney: Int){
        setMoney(newMoney)
        write2database() }
    fun setnWriteValue1(newValue1: Int){
        setValue1(newValue1)
        write2database() }
    fun setWriteProfit(newProfitRate: Float){
        setProfitRate(newProfitRate)
        write2database()
    }
    fun setnWriteRelativeProfit(newRelativeProfit: Float){
        setRelativeProfit(newRelativeProfit)
        write2database() }
    fun setnWriteRoundCount(newRoundCount: Int ){
        setRoundCount(newRoundCount)
        write2database() }
    fun setnWriteHistory(newHistory : Int){
        setHistory(newHistory)
        write2database() }
    fun setnWriteLevel(newLevel : Int){
        setLevel(newLevel)
        write2database() }
    fun setnWriteExp(newExp: Int){
        setExp(newExp)
        write2database() }
    fun setnWriteRank(newRank : Int){
        setRank(newRank)
        write2database() }
    fun setnWriteLogin(newLogin : Int){
        setLogin(newLogin)
        write2database() }
    fun setnWriteLoginId(newLoginId : String){
        setLoginId(newLoginId)
        write2database() }
    fun setnWriteLoginPw(newLoginPw: String){
        setLoginPw(newLoginPw)
        write2database() }

    // getter function
    fun getId(): LiveData<Long> { return id }
    fun getNickname(): LiveData<String> { return nickname }
    fun getMoney(): LiveData<Int> {return money}
    fun getValue1(): LiveData<Int> {return value1}
    fun getProfitRate(): LiveData<Float> {return profitrate}
    fun getRelativeProfit(): LiveData<Float> { return relativeprofitrate }
    fun getRoundCount(): LiveData<Int> {return roundcount}
    fun getHistory(): LiveData<Int> { return history }
    fun getLevel(): LiveData<Int> { return level }
    fun getExp(): LiveData<Int> { return exp }
    fun getRank(): LiveData<Int> { return rank }
    fun getLogin(): LiveData<Int> { return login }
    fun getLoginId(): LiveData<String> { return login_id }
    fun getLoginPw(): LiveData<String> { return login_pw }
    fun getHash(): LiveData<String>{ return hash}

    override fun onCleared() {
        write2database()
        super.onCleared()
    }
}