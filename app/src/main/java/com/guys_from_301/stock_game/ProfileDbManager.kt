package com.guys_from_301.stock_game

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.guys_from_301.stock_game.data.Profile
import com.guys_from_301.stock_game.data.ProfileDB
import java.util.logging.Handler

class ProfileDbManager(initialActivity : Context){
    private lateinit var mActivity : Context
    private var profileDb: ProfileDB? = null

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

    private var change = false

    init {
        mActivity = initialActivity
        profileDb = ProfileDB.getInstace(mActivity)
        if(!isEmpty(mActivity)) {
            id.value = profileDb?.profileDao()?.getId()
            nickname.value = profileDb?.profileDao()?.getNickname()
            money.value = profileDb?.profileDao()?.getMoney()
            value1.value = profileDb?.profileDao()?.getValue1()
            profitrate.value = profileDb?.profileDao()?.getProfitRate()
            relativeprofitrate.value = profileDb?.profileDao()?.getRelativeProfitRate()
            roundcount.value = profileDb?.profileDao()?.getRoundCount()
            history.value = profileDb?.profileDao()?.getHistory()
            level.value = profileDb?.profileDao()?.getLevel()
            exp.value = profileDb?.profileDao()?.getExp()
            rank.value = profileDb?.profileDao()?.getRank()
            login.value = profileDb?.profileDao()?.getLogin()
            login_id.value = profileDb?.profileDao()?.getLoginid()
            login_pw.value = profileDb?.profileDao()?.getLoginpw()
            hash.value = profileDb?.profileDao()?.getHash()
        }
    }

    fun refresh(currentActivity : Context){
        if(getHash()==getHashRespectFromDbManager())
            write2database()
        mActivity = currentActivity
//        profileDb = ProfileDB.getInstace(mActivity)
//        id.value = profileDb?.profileDao()?.getId()
//        nickname.value = profileDb?.profileDao()?.getNickname()
//        money.value = profileDb?.profileDao()?.getMoney()
//        value1.value = profileDb?.profileDao()?.getValue1()
//        profitrate.value = profileDb?.profileDao()?.getProfitRate()
//        relativeprofitrate.value = profileDb?.profileDao()?.getRelativeProfitRate()
//        roundcount.value = profileDb?.profileDao()?.getRoundCount()
//        history.value = profileDb?.profileDao()?.getHistory()
//        level.value = profileDb?.profileDao()?.getLevel()
//        exp.value = profileDb?.profileDao()?.getExp()
//        rank.value = profileDb?.profileDao()?.getRank()
//        login.value = profileDb?.profileDao()?.getLogin()
//        login_id.value = profileDb?.profileDao()?.getLoginid()
//        login_pw.value = profileDb?.profileDao()?.getLoginpw()
//        hash.value = profileDb?.profileDao()?.getHash()
//        change = false
    }

    fun resetWithNoWrite2Db(currentActivity : Context){
        mActivity = currentActivity
        profileDb = ProfileDB.getInstace(mActivity)
        id.value = profileDb?.profileDao()?.getId()
        nickname.value = profileDb?.profileDao()?.getNickname()
        money.value = profileDb?.profileDao()?.getMoney()
        value1.value = profileDb?.profileDao()?.getValue1()
        profitrate.value = profileDb?.profileDao()?.getProfitRate()
        relativeprofitrate.value = profileDb?.profileDao()?.getRelativeProfitRate()
        roundcount.value = profileDb?.profileDao()?.getRoundCount()
        history.value = profileDb?.profileDao()?.getHistory()
        level.value = profileDb?.profileDao()?.getLevel()
        exp.value = profileDb?.profileDao()?.getExp()
        rank.value = profileDb?.profileDao()?.getRank()
        login.value = profileDb?.profileDao()?.getLogin()
        login_id.value = profileDb?.profileDao()?.getLoginid()
        login_pw.value = profileDb?.profileDao()?.getLoginpw()
        hash.value = profileDb?.profileDao()?.getHash()
        change = false
    }


    fun updateManager(newProfile : Profile){
        id.value = newProfile.id
        nickname.value = newProfile.nickname
        money.value = newProfile.money
        value1.value = newProfile.value1
        profitrate.value = newProfile.profitrate
        relativeprofitrate.value = newProfile.relativeprofitrate
        roundcount.value = newProfile.roundcount
        history.value = newProfile.history
        level.value = newProfile.level
        exp.value = newProfile.exp
        rank.value = newProfile.rank
        login.value = newProfile.login
        login_id.value = newProfile.login_id
        login_pw.value = newProfile.login_pw
        change = true
        updateHashValue()
    }

    fun write2database(){
        if(change&&getHash()==getHashRespectFromDbManager()) {
            updateHashValue()
            var write = Runnable {
                profileDb = ProfileDB.getInstace(mActivity)
                var newProfile = Profile(id.value!!, nickname.value!!, money.value!!, value1.value!!, profitrate.value!!, relativeprofitrate.value!!, roundcount.value!!, history.value!!,
                        level.value!!, exp.value!!, rank.value!!, login.value!!, login_id.value!!, login_pw.value!!, hash.value!!)
                if(profileDb?.profileDao()?.getAll().isNullOrEmpty())
                    profileDb?.profileDao()?.insert(newProfile)
                else
                    profileDb?.profileDao()?.update(newProfile)
            }
            var writeThread = Thread(write)
            writeThread.start()
            change = false
        }
    }

    // setter
    fun setId(newId : Long){
        id.value = newId
        change = true
        updateHashValue()
    }
    fun setNickname(newNickname : String){
        nickname.value = newNickname
        change = true
        updateHashValue()
    }
    fun setMoney(newMoney : Int){
        money.value = newMoney
        change = true
        updateHashValue()
    }
    fun setValue1(newMoney : Int){
        value1.value = newMoney
        change = true
        updateHashValue()
    }
    fun setProfitRate(newProfitRate: Float){
        profitrate.value = newProfitRate
        change = true
        updateHashValue()
    }
    fun setRelativeProfit(newRelativeProfit : Float){
        relativeprofitrate.value = newRelativeProfit
        change = true
        updateHashValue()
    }
    fun setRoundCount(newRoundCount : Int){
        roundcount.value = newRoundCount
        change = true
        updateHashValue()
    }
    fun setHistory(newHistory : Int){
        history.value = newHistory
        change = true
        updateHashValue()
    }
    fun setLevel(newLevel : Int){
        level.value = newLevel
        change = true
        updateHashValue()
    }
    fun setExp(newExp: Int){
        exp.value = newExp
        change = true
        updateHashValue()
    }
    fun setRank(newRank : Int){
        rank.value = profileDb?.profileDao()?.getRank()
        change = true
        updateHashValue()
    }
    fun setLogin(newLogin : Int){
        login.value = newLogin
        change = true
        updateHashValue()
    }
    fun setLoginId(newLoginId : String){
        change = true
        login_id.value = newLoginId
        updateHashValue()
    }
    fun setLoginPw(newLoginPw: String){
        login_pw.value = newLoginPw
        change = true
        updateHashValue()
    }

    // setter
    fun setnWriteId(newId : Long){
        id.value = newId
        change = true
        updateHashValue()
        write2database() }
    fun setnWriteNickname(newNickname : String){
        nickname.value = newNickname
        change = true
        updateHashValue()
        write2database() }
    fun setnWriteMoney(newMoney: Int){
        money.value = newMoney
        change = true
        updateHashValue()
        write2database() }
    fun setnWriteValue1(newValue1: Int){
        value1.value = newValue1
        change = true
        updateHashValue()
        write2database() }
    fun setWriteProfit(newProfitRate: Float){
        profitrate.value = newProfitRate
        change = true
        updateHashValue()
        write2database() }
    fun setnWriteRelativeProfit(newRelativeProfit: Float){
        relativeprofitrate.value = newRelativeProfit
        change = true
        updateHashValue()
        write2database() }
    fun setnWriteRoundCount(newRoundCount: Int ){
        roundcount.value = newRoundCount
        change = true
        updateHashValue()
        write2database() }
    fun setnWriteHistory(newHistory : Int){
        history.value = newHistory
        change = true
        updateHashValue()
        write2database() }
    fun setnWriteLevel(newLevel : Int){
        level.value = newLevel
        change = true
        updateHashValue()
        write2database() }
    fun setnWriteExp(newExp: Int){
        exp.value = newExp
        change = true
        updateHashValue()
        write2database() }
    fun setnWriteRank(newRank : Int){
        rank.value = newRank
        change = true
        updateHashValue()
        write2database() }
    fun setnWriteLogin(newLogin : Int){
        login.value = newLogin
        change = true
        updateHashValue()
        write2database() }
    fun setnWriteLoginId(newLoginId : String){
        login_id.value = newLoginId
        change = true
        updateHashValue()
        write2database() }
    fun setnWriteLoginPw(newLoginPw: String){
        login_pw.value = newLoginPw
        change = true
        updateHashValue()
        write2database() }

    fun isEmpty(currentActivity : Context): Boolean{
        mActivity = currentActivity
        profileDb = ProfileDB.getInstace(mActivity)
        return profileDb?.profileDao()?.getAll().isNullOrEmpty()
    }

    // getter function
    fun getId(): Long? { return id.value }
    fun getNickname(): String? { return nickname.value }
    fun getMoney(): Int? {return money.value}
    fun getValue1(): Int? {return value1.value}
    fun getProfitRate(): Float? {return profitrate.value}
    fun getRelativeProfit(): Float? { return relativeprofitrate.value }
    fun getRoundCount(): Int? {return roundcount.value}
    fun getHistory(): Int? { return history.value }
    fun getLevel(): Int? { return level.value }
    fun getExp(): Int? { return exp.value }
    fun getRank(): Int? { return rank.value }
    fun getLogin(): Int? { return login.value }
    fun getLoginId(): String? { return login_id.value }
    fun getLoginPw(): String? { return login_pw.value }
    fun getHash(): String? { return hash.value}

    fun updateHashValue(){
        change = true
        val hashInput = id.value.toString()+ nickname.value + money.value.toString()+
                value1.value.toString()+ profitrate.value.toString() +
                relativeprofitrate.value.toString() + roundcount.value.toString()+
                history.value.toString() + level.value.toString() +
                exp.value.toString() + rank.value.toString() + login.value.toString()+
                login_id.value + login_pw.value
        val hashValue = getHash(hashInput)
        hash.value = hashValue
        Log.d("Giho","updateHashValue : "+hashValue)
    }

    fun getHashRespectFromDbManager():String{
        val hashInput = id.value.toString()+ nickname.value + money.value.toString()+
                value1.value.toString()+ profitrate.value.toString() +
                relativeprofitrate.value.toString() + roundcount.value.toString()+
                history.value.toString() + level.value.toString() +
                exp.value.toString() + rank.value.toString() + login.value.toString()+
                login_id.value + login_pw.value
        Log.d("Giho","getHashRespectFromDbData : "+getHash(hashInput))
        return getHash(hashInput)
    }

    fun getHashRespectFromInput(inputProfile : Profile):String{
        val hashInput = inputProfile.id.toString()+ inputProfile.nickname + inputProfile.money.toString()+
                inputProfile.value1.toString()+ inputProfile.profitrate.toString() +
                inputProfile.relativeprofitrate.toString() + inputProfile.roundcount.toString()+
                inputProfile.history.toString() + inputProfile.level.toString() +
                inputProfile.exp.toString() + inputProfile.rank.toString() + inputProfile.login.toString()+
                inputProfile.login_id + inputProfile.login_pw
        Log.d("Giho","getHashRespectFromInput : "+getHash(hashInput))
        return getHash(hashInput)
    }
}