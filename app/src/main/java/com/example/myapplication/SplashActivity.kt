package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.common.KakaoSdk

class SplashActivity : AppCompatActivity() {
    val SPLASH_VIEW_TIME: Long = 2000 //2초간 스플래시 화면을 보여줌 (ms)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({ //delay를 위한 handler
            startActivity(Intent(this, InitialActivity::class.java))
            finish()
        }, SPLASH_VIEW_TIME)
        KakaoSdk.init(this, "0c9ac0ead6e3f965c35fa7c9d0973b7f")

    }
}