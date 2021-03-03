package com.guys_from_301.stock_game

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guys_from_301.stock_game.data.GameNormal
import com.guys_from_301.stock_game.data.GameNormalDB
import com.guys_from_301.stock_game.data.GameSetDB

class GameNormalActivityVeiwModel(_gameNormalActivity : Context): ViewModel() {
    //변수 선언
    private var gameNormalActivity = _gameNormalActivity
    private var gamenormalDb: GameNormalDB? = null
    private var currentGame: GameNormal? = null
    private var gameSetDb: GameSetDB? = null

    private var _setId = MutableLiveData<String>()
    private var _asset = MutableLiveData<Float>()
    private var _cash = MutableLiveData<Float>()
    private var _input = MutableLiveData<Float>()
    private var _bought = MutableLiveData<Float>()
    private var _sold = MutableLiveData<Float>()
    private var _evaluation = MutableLiveData<Float>()
    private var _profit = MutableLiveData<Float>()
    private var _profitrate = MutableLiveData<Float>()
    private var _profittot = MutableLiveData<Float>()
    private var _profityear = MutableLiveData<Float>()
    private var _buyorsell = MutableLiveData<String>()
    private var _volume = MutableLiveData<Float>()
    private var _price = MutableLiveData<Float>()
    private var _select = MutableLiveData<Int>()
    private var _quant = MutableLiveData<Int>()
    private var _quant1x = MutableLiveData<Int>()
    private var _quant3x = MutableLiveData<Int>()
    private var _quantinv1x = MutableLiveData<Int>()
    private var _quantinv3x = MutableLiveData<Int>()
    private var _bought1x = MutableLiveData<Float>()
    private var _bought3x = MutableLiveData<Float>()
    private var _boughtinv1x = MutableLiveData<Float>()
    private var _boughtinv3x = MutableLiveData<Float>()
    private var _aver1x = MutableLiveData<Float>()
    private var _aver3x = MutableLiveData<Float>()
    private var _averinv1x = MutableLiveData<Float>()
    private var _averinv3x = MutableLiveData<Float>()
    private var _buylim1x = MutableLiveData<Float>()
    private var _buylim3x = MutableLiveData<Float>()
    private var _buyliminv1x = MutableLiveData<Float>()
    private var _buyliminv3x = MutableLiveData<Float>()
    private var _price1x = MutableLiveData<Float>()
    private var _price3x = MutableLiveData<Float>()
    private var _priceinv1x = MutableLiveData<Float>()
    private var _priceinv3x = MutableLiveData<Float>()
    private var _val1x = MutableLiveData<Float>()
    private var _val3x = MutableLiveData<Float>()
    private var _valinv1x = MutableLiveData<Float>()
    private var _valinv3x = MutableLiveData<Float>()
    private var _pr1x = MutableLiveData<Float>()
    private var _pr3x = MutableLiveData<Float>()
    private var _prinv1x = MutableLiveData<Float>()
    private var _prinv3x = MutableLiveData<Float>()
    private var _monthly = MutableLiveData<Float>()
    private var _monthtoggle = MutableLiveData<Int>()
    private var _tradecomtot = MutableLiveData<Float>()
    private var _tradecom = MutableLiveData<Float>()
    private var _dividendtot = MutableLiveData<Float>()
    private var _taxtot = MutableLiveData<Float>()
    private var _item = MutableLiveData<String>()
    private var _item1active = MutableLiveData<Boolean>()
    private var _item1length = MutableLiveData<Int>()
    private var _item1able = MutableLiveData<Int>()
    private var _item2active = MutableLiveData<Boolean>()
    private var _item3active = MutableLiveData<Boolean>()
    private var _item4active = MutableLiveData<Boolean>()
    private var _autobuy = MutableLiveData<Boolean>()
    private var _autoratio = MutableLiveData<Int>()
    private var _auto1x = MutableLiveData<Int>()
    private var _endpoint = MutableLiveData<Int>()
    private var _countyear = MutableLiveData<Int>()
    private var _countmonth = MutableLiveData<Int>()
    private var _snpnowdays = MutableLiveData<Int>()
    private var _snpnowval = MutableLiveData<Float>()
    private var _snpdiff = MutableLiveData<Float>()
    private var _relativeprofitrate = MutableLiveData<Float>()
    private var _endtime = MutableLiveData<String>()

    init{
        gameSetDb = GameSetDB.getInstace(gameNormalActivity)
        gamenormalDb = GameNormalDB.getInstace(gameNormalActivity)
        _setId.value = setId
        currentGame = gamenormalDb?.gameNormalDao()?.getSetWithNormal(_setId.value!!, accountID!!)?.last()
        _asset.value = currentGame?.assets
        _cash.value = currentGame?.cash
        _input.value = currentGame?.input
        _sold.value = currentGame?.sold
        _bought.value = currentGame?.bought
        _evaluation.value = currentGame?.evaluation
        _profit.value = currentGame?.profit
        _profitrate.value = currentGame?.profitrate
        _profittot.value = currentGame?.profittot
        _profityear.value = currentGame?.profityear
        _buyorsell.value = currentGame?.buyorsell
        _volume.value = currentGame?.volume
        _price.value = currentGame?.price
        _select.value = currentGame?.select
        _quant.value = currentGame?.quant
        _quant1x.value = currentGame?.quant1x
        _quant3x.value = currentGame?.quant3x
        _quantinv1x.value = currentGame?.quantinv1x
        _quantinv3x.value = currentGame?.quantinv3x
        _bought1x.value = currentGame?.bought1x
        _bought3x.value = currentGame?.bought3x
        _boughtinv1x.value = currentGame?.boughtinv1x
        _boughtinv3x.value = currentGame?.boughtinv3x
        _aver1x.value = currentGame?.aver1x
        _aver3x.value = currentGame?.aver3x
        _averinv1x.value = currentGame?.averinv1x
        _averinv3x.value = currentGame?.averinv3x
        _buylim1x.value = currentGame?.buylim1x
        _buylim3x.value = currentGame?.buylim3x
        _buyliminv1x.value = currentGame?.buyliminv1x
        _buyliminv3x.value = currentGame?.buyliminv3x
        _price1x.value = currentGame?.price1x
        _price3x.value = currentGame?.price3x
        _priceinv1x.value = currentGame?.priceinv1x
        _priceinv3x.value = currentGame?.priceinv3x
        _val1x.value = currentGame?.val1x
        _val3x.value = currentGame?.val3x
        _valinv1x.value = currentGame?.valinv1x
        _valinv3x.value = currentGame?.valinv3x
        _pr1x.value = currentGame?.pr1x
        _pr3x.value = currentGame?.pr3x
        _prinv1x.value = currentGame?.prinv1x
        _prinv3x.value = currentGame?.prinv3x
        _monthly.value = currentGame?.monthly
        _monthtoggle.value = currentGame?.monthtoggle
        _tradecomtot.value = currentGame?.tradecomtot
        _tradecom.value = currentGame?.tradecom
        _dividendtot.value = currentGame?.dividendtot
        _taxtot.value = currentGame?.taxtot
        _item.value = currentGame?.item
        _item1active.value = currentGame?.item1active
        _item1length.value = currentGame?.item1length
        _item1able.value = currentGame?.item1able
        _item2active.value = currentGame?.item2active
        _item3active.value = currentGame?.item3active
        _item4active.value = currentGame?.item4active
        _autobuy.value = currentGame?.autobuy
        _autoratio.value = currentGame?.autoratio
        _auto1x.value = currentGame?.auto1x
        _endpoint.value = currentGame?.endpoint
        _countyear.value = currentGame?.countyear
        _countmonth.value = currentGame?.countmonth
        _snpnowdays.value = currentGame?.snpnowdays
        _snpnowval.value = currentGame?.snpnowval
        _snpdiff.value = currentGame?.snpdiff
        _relativeprofitrate.value = currentGame?.relativeprofitrate
        _endtime.value = currentGame?.endtime
    }

    fun refresh(){
        gameSetDb = GameSetDB.getInstace(gameNormalActivity)
        gamenormalDb = GameNormalDB.getInstace(gameNormalActivity)
        _setId.value = setId
        currentGame = gamenormalDb?.gameNormalDao()?.getSetWithNormal(_setId.value!!, accountID!!)?.last()
        _asset.value = currentGame?.assets
        _cash.value = currentGame?.cash
        _input.value = currentGame?.input
        _sold.value = currentGame?.sold
        _bought.value = currentGame?.bought
        _evaluation.value = currentGame?.evaluation
        _profit.value = currentGame?.profit
        _profitrate.value = currentGame?.profitrate
        _profittot.value = currentGame?.profittot
        _profityear.value = currentGame?.profityear
        _buyorsell.value = currentGame?.buyorsell
        _volume.value = currentGame?.volume
        _price.value = currentGame?.price
        _select.value = currentGame?.select
        _quant.value = currentGame?.quant
        _quant1x.value = currentGame?.quant1x
        _quant3x.value = currentGame?.quant3x
        _quantinv1x.value = currentGame?.quantinv1x
        _quantinv3x.value = currentGame?.quantinv3x
        _bought1x.value = currentGame?.bought1x
        _bought3x.value = currentGame?.bought3x
        _boughtinv1x.value = currentGame?.boughtinv1x
        _boughtinv3x.value = currentGame?.boughtinv3x
        _aver1x.value = currentGame?.aver1x
        _aver3x.value = currentGame?.aver3x
        _averinv1x.value = currentGame?.averinv1x
        _averinv3x.value = currentGame?.averinv3x
        _buylim1x.value = currentGame?.buylim1x
        _buylim3x.value = currentGame?.buylim3x
        _buyliminv1x.value = currentGame?.buyliminv1x
        _buyliminv3x.value = currentGame?.buyliminv3x
        _price1x.value = currentGame?.price1x
        _price3x.value = currentGame?.price3x
        _priceinv1x.value = currentGame?.priceinv1x
        _priceinv3x.value = currentGame?.priceinv3x
        _val1x.value = currentGame?.val1x
        _val3x.value = currentGame?.val3x
        _valinv1x.value = currentGame?.valinv1x
        _valinv3x.value = currentGame?.valinv3x
        _pr1x.value = currentGame?.pr1x
        _pr3x.value = currentGame?.pr3x
        _prinv1x.value = currentGame?.prinv1x
        _prinv3x.value = currentGame?.prinv3x
        _monthly.value = currentGame?.monthly
        _monthtoggle.value = currentGame?.monthtoggle
        _tradecomtot.value = currentGame?.tradecomtot
        _tradecom.value = currentGame?.tradecom
        _dividendtot.value = currentGame?.dividendtot
        _taxtot.value = currentGame?.taxtot
        _item.value = currentGame?.item
        _item1active.value = currentGame?.item1active
        _item1length.value = currentGame?.item1length
        _item1able.value = currentGame?.item1able
        _item2active.value = currentGame?.item2active
        _item3active.value = currentGame?.item3active
        _item4active.value = currentGame?.item4active
        _autobuy.value = currentGame?.autobuy
        _autoratio.value = currentGame?.autoratio
        _auto1x.value = currentGame?.auto1x
        _endpoint.value = currentGame?.endpoint
        _countyear.value = currentGame?.countyear
        _countmonth.value = currentGame?.countmonth
        _snpnowdays.value = currentGame?.snpnowdays
        _snpnowval.value = currentGame?.snpnowval
        _snpdiff.value = currentGame?.snpdiff
        _relativeprofitrate.value = currentGame?.relativeprofitrate
        _endtime.value = currentGame?.endtime

    }

    fun writeDataBase(){
        var write = Runnable{
            gamenormalDb
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}