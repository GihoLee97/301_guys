package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.GameNormal
import com.example.myapplication.data.GameNormalDB
import kotlinx.coroutines.launch

class GameNormalActivityVeiwModel: ViewModel() {
    //변수 선언
    var finished = MutableLiveData<Boolean>()
    val fees: Float = 0.001F
    private var _assets =MutableLiveData<Float>()
    private var _purchase =MutableLiveData<Float>()
    private var _cash = MutableLiveData<Float>()
    private var _evaluation = MutableLiveData<Float>()
    private var _profit = MutableLiveData<Float>()
    private var _item1 = MutableLiveData<Int>()
    private var _item2 = MutableLiveData<Int>()
    private var _item3 = MutableLiveData<Int>()
    private var _priceBuy = MutableLiveData<Float>()
    private var _priceNow = MutableLiveData<Float>()
    private var _priceBefore = MutableLiveData<Float>()
    //게임 초기화
    fun initialize(startcash:Float,startpurchase:Float,startprice: Float, startevaluation:Float, startprofit: Float, startitem1 : Int, startitem2 : Int, startitem3 : Int){
        _cash.value = startcash
        _evaluation.value = startevaluation
        _purchase.value = startpurchase
        _profit.value = startprofit
        _priceBuy.value = startprice
        finished.value = true
        _item1.value = startitem1
        _item2.value = startitem2
        _item3.value = startitem3
        _assets.value = _cash.value?.plus(_evaluation.value!!)
    }

    init {
        viewModelScope.launch {

        }
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
    //평단가 반환
    fun priceBuy(): LiveData<Float>{
        return _priceBuy
    }
    //매입금액 반환
    fun purchase(): LiveData<Float>{
        return _purchase
    }
    //시세 현재가
    fun priceNow(): LiveData<Float>{
        return _priceNow
    }
    fun priceBefore(): LiveData<Float>{
        return _priceBefore
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
        _evaluation.value = _purchase.value?.times((1+fluctuation))
        update()
    }
    //시세 받아오기
    fun priceUpdate(priceNow: Float, priceBefore:Float){
        _priceBefore.value = priceBefore
        _priceNow.value = priceNow
        var fluctuation = 0F
        if(_priceBuy.value==0F){ }
        else {fluctuation = (_priceNow.value!! - _priceBuy.value!!)/ _priceBuy.value!!}
        fluctuate(fluctuation)
    }
    //매수
    fun buyStock(volume:Float, fees:Float){
        _cash.value = (_cash.value?.minus(volume) ?: 0F) -fees
        _priceBuy.value = ((_priceBuy.value?.times(_evaluation.value!!) ?: 1F) + (_priceNow.value?.times(volume)
                ?: 1F))/(_evaluation.value?.plus(volume)!!)
        _evaluation.value = _evaluation.value?.plus(volume)
        _purchase.value = _purchase.value?.plus(volume)
        update()
    }
    //매도
    fun sellStock(volume:Float, fees:Float){
        _cash.value = (_cash.value?.plus(volume) ?: 0F) -fees
        _purchase.value = _purchase.value?.minus(volume/(1F+((_evaluation.value?.minus(_purchase.value!!))?.div(_purchase.value!!)!!)))
        _evaluation.value = _evaluation.value?.minus(volume)
        update()
    }
    //세금 징수
    fun payTax(tax:Float){
        _cash.value = _cash.value?.minus(tax)
        _assets.value = _cash.value?.plus(_evaluation.value!!)//세금 내는 방법 논의 필요(현금으로? 현금이 없을때는 주식으로?)
        update()
    }
    //총자산, 수익률(수수료를 제외한 수익) 업데이트
    fun update(){
        _assets.value = _cash.value?.plus(_evaluation.value!!)
        if(_purchase.value == 0F){_profit.value=0F}
        else{_profit.value = ((_evaluation.value?.times((1-fees).toFloat()) ?:0F ) - _purchase.value!!)?.div(_purchase.value!!)?.times(100)}
    }
    //게임 종료
    fun result(): List<Float?> {
        return listOf(_assets.value, _cash.value, _purchase.value ,_evaluation.value, _profit.value)
    }

    override fun onCleared() {
        super.onCleared()
    }
}