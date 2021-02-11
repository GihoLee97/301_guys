package com._301_guys.stock_game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WelcomViewModel : ViewModel() {

    // Create a LiveData with a String
    val currClick: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    fun initialize(){
        currClick.value = 0
    }

    fun click(click : Int){
        currClick.value = click
    }

    // Rest of the ViewModel...
}