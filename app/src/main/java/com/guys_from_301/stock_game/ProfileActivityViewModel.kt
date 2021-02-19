package com.guys_from_301.stock_game

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guys_from_301.stock_game.data.Profile
import com.guys_from_301.stock_game.data.ProfileDB

class ProfileActivityViewModel(_profileActivity : Context): ViewModel() {
    //변수 선언
    private var profileActivity = _profileActivity
    private var profileDb: ProfileDB? = null

    private var id = MutableLiveData<Long>()
    private var nickname = MutableLiveData<String>()
    private var money = MutableLiveData<Int>()
    private var value1 = MutableLiveData<Int>()
    private var profit = MutableLiveData<Int>()
    private var history = MutableLiveData<Int>()
    private var level = MutableLiveData<Int>()
    private var exp = MutableLiveData<Int>()
    private var rank = MutableLiveData<Int>()
    private var login = MutableLiveData<Int>()
    private var login_id = MutableLiveData<String>()
    private var login_pw = MutableLiveData<String>()

    init {
        profileDb = ProfileDB.getInstace(profileActivity)
        id.value = profileDb?.profileDao()?.getId()
        nickname.value = profileDb?.profileDao()?.getNickname()
        money.value = profileDb?.profileDao()?.getMoney()
        value1.value = profileDb?.profileDao()?.getValue1()
        profit.value = profileDb?.profileDao()?.getProfit()
        history.value = profileDb?.profileDao()?.getHistory()
        level.value = profileDb?.profileDao()?.getLevel()
        exp.value = profileDb?.profileDao()?.getExp()
        rank.value = profileDb?.profileDao()?.getRank()
        login.value = profileDb?.profileDao()?.getLogin()
        login_id.value = profileDb?.profileDao()?.getLoginid()
        login_pw.value = profileDb?.profileDao()?.getLoginpw()
    }

    fun refresh(){
        profileDb = ProfileDB.getInstace(profileActivity)
        id.value = profileDb?.profileDao()?.getId()
        nickname.value = profileDb?.profileDao()?.getNickname()
        money.value = profileDb?.profileDao()?.getMoney()
        value1.value = profileDb?.profileDao()?.getValue1()
        profit.value = profileDb?.profileDao()?.getProfit()
        history.value = profileDb?.profileDao()?.getHistory()
        level.value = profileDb?.profileDao()?.getLevel()
        exp.value = profileDb?.profileDao()?.getExp()
        rank.value = profileDb?.profileDao()?.getRank()
        login.value = profileDb?.profileDao()?.getLogin()
        login_id.value = profileDb?.profileDao()?.getLoginid()
        login_pw.value = profileDb?.profileDao()?.getLoginpw()
    }

    fun write2database(){
        var write = Runnable {
            profileDb = ProfileDB.getInstace(profileActivity)
            var newProfile = Profile(id.value!!,nickname.value!!, money.value!!,value1.value!!,profit.value!!,history.value!!,
                    level.value!!,exp.value!!,rank.value!!,login.value!!,login_id.value!!,login_pw.value!!)
            profileDb?.profileDao()?.update(newProfile)
        }
        var writeThread = Thread(write)
        writeThread.start()
    }

    // setter
    fun setId(newId : Long){ id.value = newId }
    fun setNickname(newNickname : String){ nickname.value = newNickname }
    fun setMoney(newMoney : Int){ money.value = newMoney}
    fun setValue1(newMoney : Int){ value1.value = newMoney}
    fun setProfit(newProfit : Int){ profit.value = newProfit }
    fun setHistory(newHistory : Int){ history.value = newHistory }
    fun setLevel(newLevel : Int){ level.value = newLevel }
    fun setExp(newExp: Int){ exp.value = newExp }
    fun setRank(newRank : Int){ rank.value = profileDb?.profileDao()?.getRank() }
    fun setLogin(newLogin : Int){ login.value = newLogin }
    fun setLoginId(newLoginId : String){ login_id.value = newLoginId }
    fun setLoginPw(newLoginPw: String){ login_pw.value = newLoginPw }

    // setter
    fun setnWriteId(newId : Long){
        id.value = newId
        write2database() }
    fun setnWriteNickname(newNickname : String){
        nickname.value = newNickname
        write2database() }
    fun setnWriteMoney(newMoney: Int){
        money.value = newMoney
        write2database() }
    fun setnWriteValue1(newValue1: Int){
        value1.value = newValue1
        write2database() }
    fun setnWriteProfit(newProfit : Int){
        profit.value = newProfit
        write2database() }
    fun setnWriteHistory(newHistory : Int){
        history.value = newHistory
        write2database() }
    fun setnWriteLevel(newLevel : Int){
        level.value = newLevel
        write2database() }
    fun setnWriteExp(newExp: Int){
        exp.value = newExp
        write2database() }
    fun setnWriteRank(newRank : Int){
        rank.value = profileDb?.profileDao()?.getRank()
        write2database() }
    fun setnWriteLogin(newLogin : Int){
        login.value = newLogin
        write2database() }
    fun setnWriteLoginId(newLoginId : String){
        login_id.value = newLoginId
        write2database() }
    fun setnWriteLoginPw(newLoginPw: String){
        login_pw.value = newLoginPw
        write2database() }

    // getter function
    fun getId(): LiveData<Long> { return id }
    fun getNickname(): LiveData<String> { return nickname }
    fun getMoney(): LiveData<Int> {return money}
    fun getValue1(): LiveData<Int> {return value1}
    fun getProfit(): LiveData<Int> { return profit }
    fun getHistory(): LiveData<Int> { return history }
    fun setLevel(): LiveData<Int> { return level }
    fun setExp(): LiveData<Int> { return exp }
    fun getRank(): LiveData<Int> { return rank }
    fun getLogin(): LiveData<Int> { return login }
    fun getLoginId(): LiveData<String> { return login_id }
    fun getLoginPw(): LiveData<String> { return login_pw }

    override fun onCleared() {
        write2database()
        super.onCleared()
    }
}