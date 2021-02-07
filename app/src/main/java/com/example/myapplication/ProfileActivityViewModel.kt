package com.example.myapplication

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.Profile
import com.example.myapplication.data.ProflieDB

class ProfileActivityViewModel(_profileActivity : Context): ViewModel() {
    //변수 선언
    private var profileActivity = _profileActivity
    private var profileDb: ProflieDB? = null

    private var id = MutableLiveData<Long>()
    private var nickname = MutableLiveData<String>()
    private var profit = MutableLiveData<Int>()
    private var history = MutableLiveData<String>()
    private var level = MutableLiveData<Int>()
    private var rank = MutableLiveData<Int>()
    private var login = MutableLiveData<Int>()
    private var login_id = MutableLiveData<String>()
    private var login_pw = MutableLiveData<String>()

    init {
        profileDb = ProflieDB.getInstace(profileActivity)
        id.value = profileDb?.profileDao()?.getId()
        nickname.value = profileDb?.profileDao()?.getNickname()
        profit.value = profileDb?.profileDao()?.getProfit()
        history.value = profileDb?.profileDao()?.getHistory()
        level.value = profileDb?.profileDao()?.getLevel()
        rank.value = profileDb?.profileDao()?.getRank()
        login.value = profileDb?.profileDao()?.getLogin()
        login_id.value = profileDb?.profileDao()?.getLoginid()
        login_pw.value = profileDb?.profileDao()?.getLoginpw()
    }

    fun refresh(){
        profileDb = ProflieDB.getInstace(profileActivity)
        id.value = profileDb?.profileDao()?.getId()
        nickname.value = profileDb?.profileDao()?.getNickname()
        profit.value = profileDb?.profileDao()?.getProfit()
        history.value = profileDb?.profileDao()?.getHistory()
        level.value = profileDb?.profileDao()?.getLevel()
        rank.value = profileDb?.profileDao()?.getRank()
        login.value = profileDb?.profileDao()?.getLogin()
        login_id.value = profileDb?.profileDao()?.getLoginid()
        login_pw.value = profileDb?.profileDao()?.getLoginpw()
    }

    fun write2database(){
        var write = Runnable {
            profileDb = ProflieDB.getInstace(profileActivity)
            var newProfile = Profile(id.value!!,nickname.value!!,profit.value!!,history.value!!,
                    level.value!!,rank.value!!,login.value!!,login_id.value!!,login_pw.value!!)
            profileDb?.profileDao()?.update(newProfile)
        }
        var writeThread = Thread(write)
        writeThread.start()
    }

    // setter
    fun setId(newId : Long){ id.value = newId }
    fun setNickname(newNickname : String){ nickname.value = newNickname }
    fun setProfit(newProfit : Int){ profit.value = newProfit }
    fun setHistory(newHistory : String){ history.value = newHistory }
    fun setLevel(newLevel : Int){ level.value = newLevel }
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
    fun setnWriteProfit(newProfit : Int){
        profit.value = newProfit
        write2database() }
    fun setnWriteHistory(newHistory : String){
        history.value = newHistory
        write2database() }
    fun setnWriteLevel(newLevel : Int){
        level.value = newLevel
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
    fun getProfit(): LiveData<Int> { return profit }
    fun getHistory(): LiveData<String> { return history }
    fun setLevel(): LiveData<Int> { return level }
    fun getRank(): LiveData<Int> { return rank }
    fun getLogin(): LiveData<Int> { return login }
    fun getLoginId(): LiveData<String> { return login_id }
    fun getLoginPw(): LiveData<String> { return login_pw }

    override fun onCleared() {
        write2database()
        super.onCleared()
    }
}