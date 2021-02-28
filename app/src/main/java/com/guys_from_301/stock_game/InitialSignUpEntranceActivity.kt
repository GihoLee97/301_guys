package com.guys_from_301.stock_game

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.guys_from_301.stock_game.data.GameSetDB
import com.guys_from_301.stock_game.data.ProfileDB
import com.guys_from_301.stock_game.data.QuestDB
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class InitialSignUpEntranceActivity : AppCompatActivity() {
    val mContext : Context = this
    // profileDb
    private var profileDb : ProfileDB? = null
    var saveid :String = ""
    var savepw :String = ""
    // questDb
    private var questDb: QuestDB? = null
    private var questList = arrayListOf<String>(
        "게임 내 수익률 10% 달성",
        "게임 내 수익률 20% 달성",
        "게임 내 수익률 30% 달성",
        "게임 내 수익률 50% 달성",
        "게임 내 수익률 100% 달성",
        "게임 내 수익률 200% 달성",
        "게임 내 수익률 300% 달성",
        "게임 내 수익률 400% 달성",
        "게임 내 수익률 500% 달성",
        "게임 내 수익률 1,000% 달성",
        "시장대비 평균수익률 10% 달성",
        "시장대비 평균수익률 20% 달성",
        "시장대비 평균수익률 50% 달성",
        "시장대비 평균수익률 100% 달성",
        "시장대비 평균수익률 200% 달성",
        "게임 누적 거래일 10,000일 달성",
        "게임 누적 거래일 20,000일 달성",
        "게임 누적 거래일 30,000일 달성",
        "게임 누적 거래일 50,000일 달성",
        "게임 누적 거래일 100,000일 달성",
        "10 거래일 연속 흑자",
        "20 거래일 연속 흑자",
        "30 거래일 연속 흑자",
        "40 거래일 연속 흑자",
        "50 거래일 연속 흑자",
        "게임 1번 플레이하기",
        "게임 5번 플레이하기",
        "게임 10번 플레이하기",
        "게임 50번 플레이하기",
        "게임 100번 플레이하기",
        "친구 초대하기"
    )
    private var rewardList = arrayListOf<String>("10,000스택", "20,000스택", "30,000스택", "50,000스택", "100,000스택", "200,000스택", "300,000스택", "400,000스택", "500,000스택", "1,000,000스택",
        "10,000스택", "20,000스택", "50,000스택", "100,000스택", "200,000스택",
        "100,000스택", "200,000스택", "300,000스택", "500,000스택", "1,000,000스택",
        "10,000스택", "20,000스택", "30,000스택", "40,000스택", "50,000스택",
        "10,000스택", "20,000스택", "30,000스택", "40,000스택", "50,000스택",
        "100,000스택")
    //gameSetDb
    private var gameSetDb: GameSetDB? = null

    // google signin
    var auth: FirebaseAuth? = null
    val GOOGLE_REQUEST_CODE = 99
    val TAG = "googleLogin"
    private lateinit var googleSignInClient: GoogleSignInClient

    @RequiresApi(Build.VERSION_CODES.O)
    private lateinit var btn_googleSignIn : SignInButton
    private lateinit var btn_generalLogin : Button
    private lateinit var btn_kakaoLogin : ImageButton
    private lateinit var ll_backBtn : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_sign_up_entrance)

        btn_googleSignIn = findViewById(R.id.btn_googleSignIn)
        btn_generalLogin = findViewById(R.id.btn_generalLogin)
        btn_kakaoLogin = findViewById(R.id.btn_kakaoLogin)
        ll_backBtn = findViewById(R.id.ll_backBtn)

        btn_generalLogin.setOnClickListener {
            val intent = Intent(this,InitialSetIdActivity::class.java)
            startActivity(intent)
        }

        // google Sign in & onClickListner
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)

        btn_googleSignIn.setOnClickListener {
            googleSignIn()
        }

        btn_kakaoLogin.setOnClickListener(View.OnClickListener {
            val dialog = Dialog_loading(this@InitialSignUpEntranceActivity)
            dialog.show()
            // 로그인 공통 callback 구성

            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e(TAG, "로그인 실패", error)
                }
                else if (token != null) {
                    UserApiClient.instance.me { user, error ->
                        var id : String = ""
                        var pw : String = ""
                        id = user?.id.toString()
                        pw = user?.kakaoAccount?.email.toString()
                        saveid = id
                        savepw = pw
                        dialog.dismiss()
                        Log.i(TAG, "로그인 성공 ${token.accessToken}")
                        GoogleKakaoAccountAlreadyExistCheck(getHash(id).trim(), getHash(pw).trim())
                        println("---kakao"+id)
                        println("---kakaopw"+pw)
                    }
                }
            }
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (LoginClient.instance.isKakaoTalkLoginAvailable(this)) {
                LoginClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                LoginClient.instance.loginWithKakaoAccount(this, callback = callback)
            }

        })

        ll_backBtn.setOnClickListener {
            onBackPressed()
        }

    }

    // google Sign In
    private fun googleSignIn() {
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
        val dialog = Dialog_loading(this@InitialSignUpEntranceActivity)
        dialog.show()
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    var googleAuth : FirebaseAuth
                    googleAuth = FirebaseAuth.getInstance()
                    val currUser = googleAuth.currentUser
                    val user = auth!!.currentUser
                    Log.d(TAG, "로그인 성공")
                    val id = currUser?.email.toString()
                    val pw =currUser?.uid.toString()
                    saveid = id
                    savepw = pw
                    dialog.dismiss()
                    GoogleKakaoAccountAlreadyExistCheck(getHash(id).trim(), getHash(pw).trim())
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }


    fun GoogleKakaoAccountAlreadyExistCheck(u_id: String, u_pw: String) {
        var alreadyExistCheck = true

        var api_signup: Retrofitsignup? = null
        val time1: LocalDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatted = time1.format(formatter)
        val u_date : String = formatted.toString().trim()
        val url = "http://stockgame.dothome.co.kr/test/Signup.php/"
        var gson: Gson = GsonBuilder()
            .setLenient()
            .create()
        //creating retrofit object
        var retrofit =
            Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        //creating our api
        api_signup = retrofit.create(Retrofitsignup::class.java)
        api_signup.retro_signup(u_id, u_pw, u_date).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@InitialSignUpEntranceActivity, t.message, Toast.LENGTH_LONG).show()
                Log.d("Giho","onFailure_GoogleKakaoAccountAlreadyExist")
            }
            override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                val code: String = response.body()!!
                if(code == "444"){
                    Toast.makeText(this@InitialSignUpEntranceActivity, "오류가 발생했습니다.\n잠시 후 다시 시도해주세요", Toast.LENGTH_LONG).show()
                }
                if(code == "555"){
                    //첫번째 로그인 아님
                    Toast.makeText(this@InitialSignUpEntranceActivity, "이미 계정이 존재합니다.", Toast.LENGTH_LONG).show()
                }
                if(code == "666"){
                    //첫번째 로그인
                    var method = "notgeneral"
                    val intent = Intent(this@InitialSignUpEntranceActivity, InitialSignupFinalActivity::class.java)
                    intent.putExtra("u_id", saveid)
                    intent.putExtra("u_pw", savepw)
                    intent.putExtra("u_method", method)
                    startActivity(intent)
                    alreadyExistCheck = false
                }
            }
        })
    }

}