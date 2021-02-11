package com._301_guys.stock_game.data

class GameNormalRepository(private val gameNormalDao: GameNormalDao) {

    val readAllData: List<GameNormal> = gameNormalDao.getAll()

    suspend fun addGameNormal(gameNormal: GameNormal){
        gameNormalDao.insert(gameNormal)
    }
}