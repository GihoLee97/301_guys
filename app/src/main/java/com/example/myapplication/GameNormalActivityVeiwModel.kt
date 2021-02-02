package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameNormalActivityVeiwModel: ViewModel() {
    //변수 선언
    val startassets: Float = 5000000F
    var finished = MutableLiveData<Boolean>()
    private var _cash = MutableLiveData<Float>()
    private var _evaluation = MutableLiveData<Float>()
    private var _profit = MutableLiveData<Float>()
    private var _assets =MutableLiveData<Float>()
    private var _item1 = MutableLiveData<Int>()
    private var _item2 = MutableLiveData<Int>()
    private var _item3 = MutableLiveData<Int>()
    //게임 초기화
    fun initialize(startcash:Float, startevaluation:Float, startprofit: Float, startitem1 : Int, startitem2 : Int, startitem3 : Int){
        _cash.value = startcash
        _evaluation.value = startevaluation
        _profit.value = startprofit
        finished.value = true
        _item1.value = startitem1
        _item2.value = startitem2
        _item3.value = startitem3
        _assets.value = _cash.value?.plus(_evaluation.value!!)
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
    //자산 반환
    fun assets(): LiveData<Float>{
        _assets.value = _cash.value?.plus(_evaluation.value!!)
        return _assets
    }
    //아이템 반환
    fun item1(): LiveData<Int>{
        return _item1
    }
    fun item2(): LiveData<Int>{
        return _item2
    }
    fun item3(): LiveData<Int>{
        return _item3
    }
    // 아이템 값 입력

    fun setitem(item1:Int, item2:Int, item3:Int){
        _item1.value = item1
        _item2.value = item2
        _item3.value = item3

    }
    //게임 진행시 실시간 데이터 반영
    //주식 등락 fluctuation: 등락율
    fun fluctuate(fluctuation: Float){
        _evaluation.value = _evaluation.value?.times(fluctuation)
        update()
    }
    //매수
    fun buyStock(price:Float, fees:Float){
        _cash.value = (_cash.value?.minus(price) ?: 0F) -fees
        _evaluation.value = _evaluation.value?.plus(price)
        update()
    }
    //매도
    fun sellStock(price:Float, fees:Float){
        _cash.value = (_cash.value?.plus(price) ?: 0F) -fees
        _evaluation.value = _evaluation.value?.minus(price)
        update()
    }
    //세금 징수
    fun payTax(tax:Float){
        _cash.value = _cash.value?.minus(tax)
        _assets.value = _cash.value?.plus(_evaluation.value!!)//세금 내는 방법 논의 필요(현금으로? 현금이 없을때는 주식으로?)
        update()
    }
    //자산 수익률 업데이트
    fun update(){
        _assets.value = _cash.value?.plus(_evaluation.value!!)
        _profit.value = (_assets.value?.minus(startassets))?.div(startassets)
    }

    override fun onCleared() {
        super.onCleared()
    }
}