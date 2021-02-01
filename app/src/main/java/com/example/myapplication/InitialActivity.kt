package com.example.myapplication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import retrofit2.Converter

class InitialActivity : AppCompatActivity() {
    // google signin
    var auth: FirebaseAuth? = null
    val GOOGLE_REQUEST_CODE = 99
    val TAG = "googleLogin"
    private lateinit var googleSignInClient: GoogleSignInClient

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)

        // KakaoSdk.init(this, "0c9ac0ead6e3f965c35fa7c9d0973b7f")
        // 회원가입
        val btn_signup = findViewById<Button>(R.id.btn_signup)
        btn_signup.setOnClickListener{
            //val intent = Intent(this, MainActivity::class.java) //Main으로 이동
            val url: String = "http://stockgame.dothome.co.kr/test/Signup.php/"
            val id1: TextView = findViewById(R.id.et_id)
            val pw1: TextView = findViewById(R.id.et_pw)
            val time1: LocalDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formatted = time1.format(formatter)
            val loginID: String = id1.text.toString().trim()
            val loginPW: String = pw1.text.toString().trim()
            val loginDate : String = formatted.toString().trim()
            signup(loginID, loginPW, loginDate)
            //Toast.makeText(this@InitialActivity, loginDate, Toast.LENGTH_LONG).show()

            //startActivity(Intent(this, MainActivity::class.java))
        }
        // 로그인
        val btn_login = findViewById<Button>(R.id.btn_login)
        btn_login.setOnClickListener{
            //val intent = Intent(this, MainActivity::class.java) //Main으로 이동
            val url: String = "http://stockgame.dothome.co.kr/test/logincheck.php/"
            val id1: TextView = findViewById(R.id.et_id)
            val pw1: TextView = findViewById(R.id.et_pw)
            val time1: LocalDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formatted = time1.format(formatter)
            val loginID: String = id1.text.toString().trim()
            val loginPW: String = pw1.text.toString().trim()
            val loginDate : String = formatted.toString().trim()
            login_check(loginID, loginPW, loginDate)
            //startActivity(Intent(this, MainActivity::class.java))
        }


        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)

        val btn_googleSignIn = findViewById<SignInButton>(R.id.btn_googleSignIn)
        btn_googleSignIn.setOnClickListener {
            signIn()
        }

        val btn_kakaoLogin = findViewById<ImageButton>(R.id.btn_kakaoLogin)
        btn_kakaoLogin.setOnClickListener(View.OnClickListener {

            // 로그인 공통 callback 구성
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e(TAG, "로그인 실패", error)
                }
                else if (token != null) {
                    Log.i(TAG, "로그인 성공 ${token.accessToken}")
                    val intent = Intent(this, MainActivity::class.java) //Main으로 이동
                    startActivity(intent)
                }
            }

            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (LoginClient.instance.isKakaoTalkLoginAvailable(this)) {
                LoginClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                LoginClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        })

    }
    // 회원가입
    fun signup(u_id: String, u_pw: String, u_date : String) {
        var api_signup: Retrofitsignup? = null
        val url: String = "http://stockgame.dothome.co.kr/test/Signup.php/"
        var gson: Gson = GsonBuilder()
            .setLenient()
            .create()
//        //creating retrofit object
        var retrofit =
            Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        //creating our api
        api_signup = retrofit.create(Retrofitsignup::class.java)
        api_signup.retro_signup(u_id, u_pw, u_date).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
//                Toast.makeText(this@InitialActivity, "회원가입 실패...", Toast.LENGTH_LONG).show()
                Toast.makeText(this@InitialActivity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                val code: String = response.body()!!
                if(code == "444"){
                    Toast.makeText(this@InitialActivity, "공백 아이디는 불가합니다.", Toast.LENGTH_LONG).show()
                }
                if(code == "555"){
                    Toast.makeText(this@InitialActivity, "이미 등록된 아이디입니다.", Toast.LENGTH_LONG).show()
                }
                if(code == "666"){
                    Toast.makeText(this@InitialActivity, "회원가입이 완료되었습니다!", Toast.LENGTH_LONG).show()
                }
            }
        })
    }
    //

    //
    ///로그인
    fun login_check(u_id: String, u_pw: String, u_date: String) {
        var funlogincheck: Retrofitlogincheck? = null
        val url: String = "http://stockgame.dothome.co.kr/test/logincheck.php/"
        var gson: Gson = GsonBuilder()
            .setLenient()
            .create()
//        //creating retrofit object
        var retrofit =
            Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        //creating our api
        funlogincheck= retrofit.create(Retrofitlogincheck::class.java)
        funlogincheck.post_logincheck(u_id, u_pw, u_date).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@InitialActivity, "아이디나 비밀번호가 맞지 않습니다.", Toast.LENGTH_LONG).show()
                //Toast.makeText(this@InitialActivity, t.message, Toast.LENGTH_LONG).show()

            }

            override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    val okcode: String = response.body()!!
                    if (okcode == "777"){
                        Toast.makeText(this@InitialActivity, "로그인 성공!", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@InitialActivity ,MainActivity::class.java)
                        startActivity(intent)
                    }

                }
            }
        })
    }


    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "로그인 성공")
                    val user = auth!!.currentUser
                    loginSuccess()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }
    private fun loginSuccess(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    // 두번 누르면 종료되는 코드
    var time3: Long = 0
    override fun onBackPressed() {
        val time1 = System.currentTimeMillis()
        val time2 = time1 - time3
        if (time2 in 0..2000) {
            // 이거 3줄 다 써야 안전하게 종료
            moveTaskToBack(true)
            finish()
            android.os.Process.killProcess(android.os.Process.myPid())
        }
        else {
            time3 = time1
            Toast.makeText(applicationContext, "한번 더 누르시면 종료됩니다.",Toast.LENGTH_SHORT).show()
        }
    }
}

