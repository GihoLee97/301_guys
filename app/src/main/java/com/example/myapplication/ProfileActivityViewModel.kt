package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileActivityViewModel: ViewModel() {
    //변수 선언
    private var _nickname =  MutableLiveData<String>()
    var profit = 0
    var history = ""
    var level = 1
    var rank = 1

    fun initialize(startnickname: String){
        _nickname.value = startnickname
    }

    fun nickname(): LiveData<String>{
        return _nickname
    }

    fun nicknameChange(newnickname:String){
        _nickname.value = newnickname
    }
}