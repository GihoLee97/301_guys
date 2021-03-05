package com.guys_from_301.stock_game

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guys_from_301.stock_game.data.GameNormalDB
import com.guys_from_301.stock_game.data.GameSet
import com.guys_from_301.stock_game.data.GameSetDB
import java.time.LocalDateTime

class GamsetViewModel(context: Context): ViewModel() {
    private var gameSetDb: GameSetDB? = null
    private var gameset: GameSet? = null
    private var gameInProgress = listOf<GameSet>()
    private var gameNormalDb: GameNormalDB? = null

    private var accountId = MutableLiveData<String>()
    private var _initialAsset = MutableLiveData<Int>()
    private var _initialMonthly = MutableLiveData<Int>()
    private var _initialSalaryRaise = MutableLiveData<Int>()
    private var _profitRate = MutableLiveData<Float>()
    private var _endtiem = MutableLiveData<String>()

    init {
        gameSetDb = GameSetDB.getInstace(context)
        gameNormalDb = GameNormalDB.getInstace(context)
        gameInProgress = gameSetDb?.gameSetDao()?.getPick(accountID!!, accountID!!+1, accountID!!+2, accountID!!+3)!!
    }

    fun addEmptyGame(gameInProgres: List<GameSet>){
        var existence = false
        val initialgameset = gameSetDb?.gameSetDao()?.getSetWithId(accountID+0, accountID!!)

        for (game in gameInProgres){
            if(gameNormalDb?.gameNormalDao()?.getSetWithNormal(game.id, accountID!!).isNullOrEmpty()) existence = true
            if(!existence&&gameInProgres.size<3){
                val newGameSet = GameSet()

                if (gameInProgres.size == 1) {
                    if (gameInProgres.last().id.last() == '1') newGameSet.id = initialgameset?.accountId+2
                    else if (gameInProgres.last().id.last() == '2') newGameSet.id = initialgameset?.accountId+3
                    else if (gameInProgres.last().id.last() == '3') newGameSet.id = initialgameset?.accountId+1
                    else newGameSet.id = "예외1"
                } else if (gameInProgres.size == 2) {
                    if (gameInProgres[0].id.last().toInt() + gameInProgres[1].id.last().toInt() == '1'.toInt()+'2'.toInt()) newGameSet.id = initialgameset?.accountId+3
                    else if (gameInProgres[0].id.last().toInt() + gameInProgres[1].id.last().toInt() == '1'.toInt()+'3'.toInt()) newGameSet.id = initialgameset?.accountId+2
                    else if (gameInProgres[0].id.last().toInt() + gameInProgres[1].id.last().toInt() == '3'.toInt()+'2'.toInt()) newGameSet.id = initialgameset?.accountId+1
                    else newGameSet.id = "예외2"
                } else if (gameInProgres.size == 0){
                    newGameSet.id = accountID+1
                }
                //예전거 누적
                if (initialgameset != null) {
                    val id = newGameSet.id
                    newGameSet.setcash = initialgameset.setcash
                    newGameSet.setmonthly = initialgameset.setmonthly
                    newGameSet.setsalaryraise = initialgameset.setsalaryraise
                    newGameSet.setgamespeed = initialgameset.setgamespeed
                    newGameSet.setgamelength = initialgameset.setgamelength
                    newGameSet.accountId = initialgameset.accountId
                    Log.d("hongz", "새gameset 추가 [id]: "+id)
                }
                newGameSet.endtime = ""
                gameSetDb?.gameSetDao()?.insert(newGameSet)
            }
        }
    }


    fun updateGameSet(){
        var localDateTime = LocalDateTime.now()
        var newGameSet = gameSetDb?.gameSetDao()?.getSetWithId(setId, accountID!!)
        if (newGameSet != null) {
            newGameSet.endtime = localDateTime.toString()
            newGameSet.profitrate = profitrate
            gameSetDb?.gameSetDao()?.insert(newGameSet)
            Log.d("hongz", "gameset 업데이트")
        }
        updateGameSet = false
    }

}