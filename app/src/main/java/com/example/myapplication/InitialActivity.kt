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
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.Profile
import com.example.myapplication.data.ProflieDB
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.experimental.and


class InitialActivity : AppCompatActivity() {
    // profileDb
    private var profileDb : ProflieDB? = null
    var saveid :String = ""
    var savepw :String = ""

    // google signin
    var auth: FirebaseAuth? = null
    val GOOGLE_REQUEST_CODE = 99
    val TAG = "googleLogin"
    private lateinit var googleSignInClient: GoogleSignInClient

    @RequiresApi(Build.VERSION_CODES.O)
    private lateinit var btn_googleSignIn : SignInButton
    private lateinit var btn_generalSignup : Button
    private lateinit var btn_generalLogin : Button
    private lateinit var btn_kakaoLogin : ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)

        btn_googleSignIn = findViewById(R.id.btn_googleSignIn)
        btn_generalSignup = findViewById(R.id.btn_generalsignup)
        btn_generalLogin = findViewById(R.id.btn_generalLogin)
        btn_kakaoLogin = findViewById(R.id.btn_kakaoLogin)

        // 회원가입 & onClickListner
        btn_generalSignup.setOnClickListener{
            val id1: TextView = findViewById(R.id.et_id)
            val pw1: TextView = findViewById(R.id.et_pw)

            val time1: LocalDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formatted = time1.format(formatter)
            val loginID = getHash(id1.text.toString().trim()).trim()
            val loginPW = getHash(pw1.text.toString().trim()).trim()
            Log.d("Giho","ID is hashed to : "+loginID)
            Log.d("Giho","PW is hashed to : "+loginPW)
            val loginDate : String = formatted.toString().trim()
            generalSignup(loginID, loginPW, loginDate)
        }

        // 로그인 & onClickListner
        btn_generalLogin.setOnClickListener{
            val id1: TextView = findViewById(R.id.et_id)
            val pw1: TextView = findViewById(R.id.et_pw)
            saveid = id1.text.toString()
            savepw = pw1.text.toString()
            val time1: LocalDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formatted = time1.format(formatter)
            val loginID = getHash(id1.text.toString().trim()).trim()
            val loginPW = getHash(pw1.text.toString().trim()).trim()
            Log.d("Giho","ID is hashed to : "+loginID)
            Log.d("Giho","PW is hashed to : "+loginPW)
            val loginDate : String = formatted.toString().trim()

            generalLoginCheck(loginID, loginPW, loginDate)
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

        // kakao login & onClickListner
        btn_kakaoLogin.setOnClickListener(View.OnClickListener {
            // 로그인 공통 callback 구성
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e(TAG, "로그인 실패", error)
                }
                else if (token != null) {
                    Log.i(TAG, "로그인 성공 ${token.accessToken}")
                    loginSuccess("KAKAO") // memorize login method and move to MainActivity
                }
            }
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (LoginClient.instance.isKakaoTalkLoginAvailable(this)) {
                LoginClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                LoginClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
            showLoadingDialog()
        })
    }

    // general signup
    fun generalSignup(u_id: String, u_pw: String, u_date : String) {
        var api_signup: Retrofitsignup? = null
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

    // general login
    fun generalLoginCheck(u_id: String, u_pw: String, u_date: String) {
        var funlogincheck: Retrofitlogincheck? = null
        val url = "http://stockgame.dothome.co.kr/test/logincheck.php/"
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
        funlogincheck= retrofit.create(Retrofitlogincheck::class.java)
        funlogincheck.post_logincheck(u_id, u_pw, u_date).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@InitialActivity, "아이디나 비밀번호가 맞지 않습니다.", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    val okcode: String = response.body()!!
                    if (okcode == "7774"){
                        Toast.makeText(this@InitialActivity, "로그인 성공!", Toast.LENGTH_LONG).show()
                        loginSuccess("GENERAL") // memorize login method and move to MainActivity
                    }
                    if (okcode == "4"){
                        Toast.makeText(this@InitialActivity, "아이디나 비밀번호가 틀렸습니다.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
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
        showLoadingDialog()
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "로그인 성공")
                    val user = auth!!.currentUser
                    loginSuccess("GOOGLE") // memorize login method and move to MainActivity
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun loginSuccess(method: String){
        memorizeLogMethod(method)
        delLoadingDialog()
        if(profileDb?.profileDao()?.getNickname()=="#########first_login##########"){
            saveidpw(saveid.trim(), savepw.trim())

            val intent = Intent(this, WelcomeActivity::class.java)

            startActivity(intent)
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        finish()
    }

    // remember which login method did user used with DB
    private fun memorizeLogMethod(method : String){
        var temp = 0
        profileDb = ProflieDB?.getInstace(this)
        // if profile DB is empty insert dummy
        if (profileDb?.profileDao()?.getAll().isNullOrEmpty()) {
            if (method == "GENERAL") temp = 1
            else if (method == "GOOGLE") temp = 2
            else if (method == "KAKAO") temp = 4
            // save which login method user have used
            val newProfile = Profile(1, "#########first_login##########", 0, "", 1, temp, "a", "")
            profileDb?.profileDao()?.insert(newProfile)
        } else {
            profileDb = ProflieDB?.getInstace(this)
            temp = profileDb?.profileDao()?.getLogin()!!
            if (method == "GENERAL") temp = temp?.or(1)
            else if (method == "GOOGLE") temp = temp?.or(2)
            else if (method == "KAKAO") temp = temp?.or(4)
            // update the login method to DB
            val newProfile = Profile()
            newProfile.id = profileDb?.profileDao()?.getId()?.toLong()
            newProfile.nickname = profileDb?.profileDao()?.getNickname()!!
            newProfile.history = profileDb?.profileDao()?.getHistory()!!
            newProfile.level = profileDb?.profileDao()?.getLevel()!!
            newProfile.login = temp
            newProfile.profit = profileDb?.profileDao()?.getProfit()!!
            newProfile.login_id = profileDb?.profileDao()?.getLoginid()!!
            newProfile.login_pw = profileDb?.profileDao()?.getLoginpw()!!
            profileDb?.profileDao()?.update(newProfile)
        }
    }

    private fun getHash(input : String):String{
        val salt1 = "We-are-301-guys"
        val salt2 = "We-are-gonna-be-rich"
        var adjusted_input_1 = input+salt1
        var adjusted_input_2 = input+salt2
        var messagedigest = MessageDigest.getInstance("SHA-256")
        var result1 = String(messagedigest.digest(adjusted_input_1.hashCode().toString().toByteArray()))
        var result2 = String(messagedigest.digest(adjusted_input_2.hashCode().toString().toByteArray()))
        var adjusted_input_3 = result1+result2
        var result3 = messagedigest.digest(adjusted_input_3.hashCode().toString().toByteArray())

        var sb: StringBuilder = StringBuilder()

        var i = 0
        while (i < result3.count()) {
            sb.append(((result3[i].and(0xff.toByte())) + 0x100).toString(16).substring(0, 1))
            i++
        }

        var final_result = sb.toString()
        return final_result
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
    // 로딩 창 띄우는 코드
    private fun showLoadingDialog() {
        val dialog = Dialog_loading(this@InitialActivity)
        CoroutineScope(Dispatchers.Unconfined).launch {
            dialog.start()
        }
    }
    private fun delLoadingDialog() {
        val dialog = Dialog_loading(this@InitialActivity)
        CoroutineScope(Dispatchers.Unconfined).launch {
            dialog.finish()
        }
    }

    fun saveidpw(id : String, pw: String){
        profileDb = ProflieDB?.getInstace(this)
        var tmp : String = id
        val setRunnable = Runnable {
            val newProfile = Profile()
            newProfile.id = profileDb?.profileDao()?.getId()?.toLong()
            newProfile.nickname = profileDb?.profileDao()?.getNickname()!!
            newProfile.history = profileDb?.profileDao()?.getHistory()!!
            newProfile.level = profileDb?.profileDao()?.getLevel()!!
            newProfile.login = profileDb?.profileDao()?.getLogin()!!
            newProfile.profit = profileDb?.profileDao()?.getProfit()!!
            newProfile.login_id = id
            newProfile.login_pw = pw
            tmp = newProfile.login_id

            profileDb?.profileDao()?.update(newProfile)
        }
            var setThread = Thread(setRunnable)
            setThread.start()
    }


//    private fun updatelogOutInFo2DB(method : String){
//        var mask : Int = 0
//        var tmp : Int = 0
//
//        profileDb = ProflieDB?.getInstace(this)
//        if(!profileDb?.profileDao()?.getAll().isNullOrEmpty()) {
//            val setRunnable = Runnable {
//                val newProfile = Profile()
//                newProfile.id = profileDb?.profileDao()?.getId()?.toLong()
//                newProfile.nickname = profileDb?.profileDao()?.getNickname()!!
//                newProfile.history = profileDb?.profileDao()?.getHistory()!!
//                newProfile.level = profileDb?.profileDao()?.getLevel()!!
//                newProfile.login = profileDb?.profileDao()?.getLogin()!!-mask
//                newProfile.profit = profileDb?.profileDao()?.getProfit()!!
//                tmp = newProfile.profit
//            }
//            Toast.makeText(this@InitialActivity, tmp, Toast.LENGTH_LONG).show()
//            var setThread = Thread(setRunnable)
//            setThread.start()
//        }
//    }
}

