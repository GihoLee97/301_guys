package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.ProflieDB
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.internal.reflect.ReflectionAccessor
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient

class SplashActivity : AppCompatActivity() {
    //receive profile room data
    private var profileDb: ProflieDB? = null

    private lateinit var mAuth : FirebaseAuth
    val SPLASH_VIEW_TIME: Long = 2000 //2초간 스플래시 화면을 보여줌 (ms)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        KakaoSdk.init(this, "0c9ac0ead6e3f965c35fa7c9d0973b7f")

        Handler().postDelayed({ //delay를 위한 handler
            profileDb = ProflieDB?.getInstace(this)
            if(!profileDb?.profileDao()?.getAll().isNullOrEmpty()){
                var loginMethod = profileDb?.profileDao()?.getLogin()
                if(loginMethod?.and(1)==1) go2MainActivity() // general login
                else if(loginMethod?.and(2)==2) { // google login
                    if (isGoogleAuthChecked()) go2MainActivity()
                }
                else if(loginMethod?.and(4)==4){ // kakao login
                    KakaoLogin()
                    go2MainActivity()
                }
                else{
                    go2InitialActivity()
                }
            } else{
                go2InitialActivity()
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

    private fun KakaoLogin(){
        data class Check(var x: Boolean = true)
        // 로그인 공통 callback 구성
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("KAKAO", "로그인 실패", error)
            }
            else if (token != null) {
                Log.i("KAKAO", "로그인 성공 ${token.accessToken}")
            }
        }
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (LoginClient.instance.isKakaoTalkLoginAvailable(this)) {
            LoginClient.instance.loginWithKakaoTalk(this, callback = callback)
        } else {
            LoginClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    private fun go2MainActivity(){
        val intent = Intent(this, MainActivity::class.java) //Main으로 이동
        startActivity(intent)
    }

    private fun go2InitialActivity(){
        val intent = Intent(this, InitialActivity::class.java) //Initial으로 이동
        startActivity(intent)
    }

}