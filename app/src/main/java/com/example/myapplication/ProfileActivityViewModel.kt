package com.example.myapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileActivityViewModel: ViewModel() {
    //변수 선언
    private var _nickname =  MutableLiveData<String>()
    var profit = 0
    var history = ""
    var level = 1
    var rank = 1

    fun nicknameChange(newnickname:String){
        _nickname.value = newnickname
    }
}