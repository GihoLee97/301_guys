package com.guys_from_301.stock_game.data

import com.guys_from_301.stock_game.accountID

class GameNormalRepository(private val gameNormalDao: GameNormalDao) {

    val readAllData: List<GameNormal> = gameNormalDao.getAll(accountID!!)

    suspend fun addGameNormal(gameNormal: GameNormal){
        gameNormalDao.insert(gameNormal)
    }
}