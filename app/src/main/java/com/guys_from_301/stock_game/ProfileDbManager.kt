package com.guys_from_301.stock_game

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.guys_from_301.stock_game.data.Profile
import com.guys_from_301.stock_game.data.ProfileDB
import java.util.logging.Handler

//class ProfileDbManager(initialActivity : Context, profile: Profile){
class ProfileDbManager(initialActivity : Context){
    private lateinit var mActivity : Context
    private var profileDb: ProfileDB? = null

    private var id : Long = 0
    private var nickname : String = ""
    private var money : Int = 0
    private var value1 : Int = 0
    private var relativeprofitrate : Float = 0F
    private var profitrate : Float = 0F
    private var roundcount : Int = 0
    private var history : Int = 0
    private var level : Int = 0
    private var exp : Int = 0
    private var rank : Int = 0
    private var login : Int = 0
    private var login_id : String = ""
    private var login_pw : String = ""
    private var hash : String = ""

    private var change = false

    init {
        mActivity = initialActivity
        profileDb = ProfileDB.getInstace(mActivity)
        if(!isEmpty(mActivity)) {
//            profileDb = ProfileDB.getInstace(mActivity)
//            id = profile.id
//            nickname = profile.nickname
//            money = profile.money
//            value1 = profile1
//            profitrate = profile.profitrate
//            relativeprofitrate = profile.relativeprofitrate
//            roundcount = profile.roundcount
//            history = profile.history
//            level = profile.level
//            exp = profile.exp
//            rank = profile.rank
//            login = profile.login
//            login_id = profile.login_id
//            login_pw = profile.login_pw
//            hash = profile.hash
            id = profileDb?.profileDao()?.getId()!!
            nickname = profileDb?.profileDao()?.getNickname()!!
            money = profileDb?.profileDao()?.getMoney()!!
            value1 = profileDb?.profileDao()?.getValue1()!!
            profitrate = profileDb?.profileDao()?.getProfitRate()!!
            relativeprofitrate = profileDb?.profileDao()?.getRelativeProfitRate()!!
            roundcount = profileDb?.profileDao()?.getRoundCount()!!
            history = profileDb?.profileDao()?.getHistory()!!
            level = profileDb?.profileDao()?.getLevel()!!
            exp = profileDb?.profileDao()?.getExp()!!
            rank = profileDb?.profileDao()?.getRank()!!
            login = profileDb?.profileDao()?.getLogin()!!
            login_id = profileDb?.profileDao()?.getLoginid()!!
            login_pw = profileDb?.profileDao()?.getLoginpw()!!
            hash = profileDb?.profileDao()?.getHash()!!
        }
    }

    fun refresh(currentActivity : Context){
        if(getHash()==getHashRespectFromDbManager())
            write2database()
        mActivity = currentActivity
//        profileDb = ProfileDB.getInstace(mActivity)
//        id = profileDb?.profileDao()?.getId()
//        nickname = profileDb?.profileDao()?.getNickname()
//        money = profileDb?.profileDao()?.getMoney()
//        value1 = profileDb?.profileDao()?.getValue1()
//        profitrate = profileDb?.profileDao()?.getProfitRate()
//        relativeprofitrate = profileDb?.profileDao()?.getRelativeProfitRate()
//        roundcount = profileDb?.profileDao()?.getRoundCount()
//        history = profileDb?.profileDao()?.getHistory()
//        level = profileDb?.profileDao()?.getLevel()
//        exp = profileDb?.profileDao()?.getExp()
//        rank = profileDb?.profileDao()?.getRank()
//        login = profileDb?.profileDao()?.getLogin()
//        login_id = profileDb?.profileDao()?.getLoginid()
//        login_pw = profileDb?.profileDao()?.getLoginpw()
//        hash = profileDb?.profileDao()?.getHash()
//        change = false
    }

    fun resetWithNoWrite2Db(currentActivity : Context){
        mActivity = currentActivity
        profileDb = ProfileDB.getInstace(mActivity)
//        var profile = profileDb?.profileDao()?.getAll()!![0]
//        id = profile.id
//        nickname = profile.nickname
//        money = profile.money
//        value1 = profile1
//        profitrate = profile.profitrate
//        relativeprofitrate = profile.relativeprofitrate
//        roundcount = profile.roundcount
//        history = profile.history
//        level = profile.level
//        exp = profile.exp
//        rank = profile.rank
//        login = profile.login
//        login_id = profile.login_id
//        login_pw = profile.login_pw
//        hash = profile.hash
        id = profileDb?.profileDao()?.getId()!!
        nickname = profileDb?.profileDao()?.getNickname()!!
        money = profileDb?.profileDao()?.getMoney()!!
        value1 = profileDb?.profileDao()?.getValue1()!!
        profitrate = profileDb?.profileDao()?.getProfitRate()!!
        relativeprofitrate = profileDb?.profileDao()?.getRelativeProfitRate()!!
        roundcount = profileDb?.profileDao()?.getRoundCount()!!
        history = profileDb?.profileDao()?.getHistory()!!
        level = profileDb?.profileDao()?.getLevel()!!
        exp = profileDb?.profileDao()?.getExp()!!
        rank = profileDb?.profileDao()?.getRank()!!
        login = profileDb?.profileDao()?.getLogin()!!
        login_id = profileDb?.profileDao()?.getLoginid()!!
        login_pw = profileDb?.profileDao()?.getLoginpw()!!
        hash = profileDb?.profileDao()?.getHash()!!
        change = false
    }


    fun updateManager(newProfile : Profile){
        id = newProfile.id!!
        nickname = newProfile.nickname
        money = newProfile.money
        value1 = newProfile.value1
        profitrate = newProfile.profitrate
        relativeprofitrate = newProfile.relativeprofitrate
        roundcount = newProfile.roundcount
        history = newProfile.history
        level = newProfile.level
        exp = newProfile.exp
        rank = newProfile.rank
        login = newProfile.login
        login_id = newProfile.login_id
        login_pw = newProfile.login_pw
        // hash는 가져오는 것이 아니라 다시 계산 해야함!
        change = false
        updateHashValue()
    }

    fun write2database(){
        if(change&&getHash()==getHashRespectFromDbManager()) {
            updateHashValue()
            var write = Runnable {
                profileDb = ProfileDB.getInstace(mActivity)
                var newProfile = Profile(id!!, nickname!!, money!!, value1!!, profitrate!!, relativeprofitrate!!, roundcount!!, history!!,
                        level!!, exp!!, rank!!, login!!, login_id!!, login_pw!!, hash!!)
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
        id = newId
        change = true
        updateHashValue()
    }
    fun setNickname(newNickname : String){
        nickname = newNickname
        change = true
        updateHashValue()
    }
    fun setMoney(newMoney : Int){
        money = newMoney
        change = true
        updateHashValue()
    }
    fun setValue1(newMoney : Int){
        value1 = newMoney
        change = true
        updateHashValue()
    }
    fun setProfitRate(newProfitRate: Float){
        profitrate = newProfitRate
        change = true
        updateHashValue()
    }
    fun setRelativeProfit(newRelativeProfit : Float){
        relativeprofitrate = newRelativeProfit
        change = true
        updateHashValue()
    }
    fun setRoundCount(newRoundCount : Int){
        roundcount = newRoundCount
        change = true
        updateHashValue()
    }
    fun setHistory(newHistory : Int){
        history = newHistory
        change = true
        updateHashValue()
    }
    fun setLevel(newLevel : Int){
        level = newLevel
        change = true
        updateHashValue()
    }
    fun setExp(newExp: Int){
        exp = newExp
        change = true
        updateHashValue()
    }
    fun setRank(newRank : Int){
        rank = newRank
        change = true
        updateHashValue()
    }
    fun setLogin(newLogin : Int){
        login = newLogin
        change = true
        updateHashValue()
    }
    fun setLoginId(newLoginId : String){
        change = true
        login_id = newLoginId
        updateHashValue()
    }
    fun setLoginPw(newLoginPw: String){
        login_pw = newLoginPw
        change = true
        updateHashValue()
    }

    // setter
    fun setnWriteId(newId : Long){
        id = newId
        change = true
        updateHashValue()
        write2database() }
    fun setnWriteNickname(newNickname : String){
        nickname = newNickname
        change = true
        updateHashValue()
        write2database() }
    fun setnWriteMoney(newMoney: Int){
        money = newMoney
        change = true
        updateHashValue()
        write2database() }
    fun setnWriteValue1(newValue1: Int){
        value1 = newValue1
        change = true
        updateHashValue()
        write2database() }
    fun setWriteProfit(newProfitRate: Float){
        profitrate = newProfitRate
        change = true
        updateHashValue()
        write2database() }
    fun setnWriteRelativeProfit(newRelativeProfit: Float){
        relativeprofitrate = newRelativeProfit
        change = true
        updateHashValue()
        write2database() }
    fun setnWriteRoundCount(newRoundCount: Int ){
        roundcount = newRoundCount
        change = true
        updateHashValue()
        write2database() }
    fun setnWriteHistory(newHistory : Int){
        history = newHistory
        change = true
        updateHashValue()
        write2database() }
    fun setnWriteLevel(newLevel : Int){
        level = newLevel
        change = true
        updateHashValue()
        write2database() }
    fun setnWriteExp(newExp: Int){
        exp = newExp
        change = true
        updateHashValue()
        write2database() }
    fun setnWriteRank(newRank : Int){
        rank = newRank
        change = true
        updateHashValue()
        write2database() }
    fun setnWriteLogin(newLogin : Int){
        login = newLogin
        change = true
        updateHashValue()
        write2database() }
    fun setnWriteLoginId(newLoginId : String){
        login_id = newLoginId
        change = true
        updateHashValue()
        write2database() }
    fun setnWriteLoginPw(newLoginPw: String){
        login_pw = newLoginPw
        change = true
        updateHashValue()
        write2database() }

    fun isEmpty(currentActivity : Context): Boolean{
        mActivity = currentActivity
        profileDb = ProfileDB.getInstace(mActivity)
        return profileDb?.profileDao()?.getAll().isNullOrEmpty()
    }

    // getter function
    fun getId(): Long? { return id }
    fun getNickname(): String? { return nickname }
    fun getMoney(): Int? {return money}
    fun getValue1(): Int? {return value1}
    fun getProfitRate(): Float? {return profitrate}
    fun getRelativeProfit(): Float? { return relativeprofitrate }
    fun getRoundCount(): Int? {return roundcount}
    fun getHistory(): Int? { return history }
    fun getLevel(): Int? { return level }
    fun getExp(): Int? { return exp }
    fun getRank(): Int? { return rank }
    fun getLogin(): Int? { return login }
    fun getLoginId(): String? { return login_id }
    fun getLoginPw(): String? { return login_pw }
    fun getHash(): String? { return hash}

    fun updateHashValue(){
        change = true
        val hashInput = id.toString()+ nickname + money.toString()+
                value1.toString()+ profitrate.toString() +
                relativeprofitrate.toString() + roundcount.toString()+
                history.toString() + level.toString() +
                exp.toString() + rank.toString() + login.toString()+
                login_id + login_pw
        val hashValue = getHash(hashInput)
        hash = hashValue
        Log.d("Giho","updateHashValue : "+hashValue)
    }

    fun getHashRespectFromDbManager():String{
        val hashInput = id.toString()+ nickname + money.toString()+
                value1.toString()+ profitrate.toString() +
                relativeprofitrate.toString() + roundcount.toString()+
                history.toString() + level.toString() +
                exp.toString() + rank.toString() + login.toString()+
                login_id + login_pw
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