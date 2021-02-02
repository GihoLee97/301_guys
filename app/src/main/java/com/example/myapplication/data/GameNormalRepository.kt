package com.example.myapplication.data

import androidx.lifecycle.LiveData

class GameNormalRepository(private val gameNormalDao: GameNormalDao) {

    val readAllData: List<GameNormal> = gameNormalDao.getAll()

    suspend fun addGameNormal(gameNormal: GameNormal){
        gameNormalDao.insert(gameNormal)
    }
}