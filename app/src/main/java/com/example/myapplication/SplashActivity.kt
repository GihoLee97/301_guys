package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.common.KakaoSdk

class SplashActivity : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth
    val SPLASH_VIEW_TIME: Long = 2000 //2초간 스플래시 화면을 보여줌 (ms)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        KakaoSdk.init(this, "0c9ac0ead6e3f965c35fa7c9d0973b7f")

        Handler().postDelayed({ //delay를 위한 handler
            if(!isGoogleAuthChecked()&&!isKakaoAuthChecked()) {   // No access authority
                val intent = Intent(this, InitialActivity::class.java) //Initial으로 이동
                startActivity(intent)
            }
            finish()
        }, SPLASH_VIEW_TIME)
    }

    private fun isGoogleAuthChecked() : Boolean{
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        if(user!=null){ // available access authority
            val intent = Intent(this, MainActivity::class.java) //Main으로 이동
            startActivity(intent)
            return true
        }
        return false
    }

    private fun isKakaoAuthChecked(): Boolean{
        var alreadyKakaoAuth = false
        if(alreadyKakaoAuth) {// available access authority
            val intent = Intent(this, MainActivity::class.java) //Main으로 이동
            startActivity(intent)
            return true
        }
        return false
    }
}