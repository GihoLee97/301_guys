package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.kakao.sdk.common.KakaoSdk

class LoadnAutoauthonizeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_loadn_autoauthonize)

//        KakaoSdk.init(this, "0c9ac0ead6e3f965c35fa7c9d0973b7f")
//
        var enterAccess = ""

        if(enterAccess==""){ // No access authority
            val intent = Intent(this, InitialActivity::class.java) //Initial으로 이동
            startActivity(intent)
        }
        else{ // available access authority
            val intent = Intent(this, MainActivity::class.java) //Main으로 이동
            startActivity(intent)
        }
    }
}