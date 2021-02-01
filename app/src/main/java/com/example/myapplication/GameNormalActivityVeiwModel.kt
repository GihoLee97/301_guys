package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameNormalActivityVeiwModel: ViewModel() {
    //변수 선언
    var finished = MutableLiveData<Boolean>()
    private var _cash = MutableLiveData<Float>()
    private var _evaluation = MutableLiveData<Float>()
    private var _profit = MutableLiveData<Float>()

    //게임 초기화
    fun initialize(startcash:Float, startevaluation:Float, startprofit: Float){
        _cash.value = startcash
        _evaluation.value = startevaluation
        _profit.value = startprofit
        finished.value = true
    }

    //현금 반환
    fun cash(): LiveData<Float>{
        return _cash
    }
    //원화평가금액 반환
    fun evaluation(): LiveData<Float>{
        return _evaluation
    }
    //수익률 반환
    fun profit(): LiveData<Float>{
        return _profit
    }
    //게임 진행시 실시간 데이터 반영
    fun playing(price:Int){

    }
    //매수
    fun buyStock(price:Float, fees:Float){
        _cash.value = (_cash.value?.minus(price) ?: 0F) -fees
    }
    //매도
    fun sellStock(price:Float, fees:Float){
        _cash.value = (_cash.value?.plus(price) ?: 0F) -fees
    }

    override fun onCleared() {
        super.onCleared()
    }
}