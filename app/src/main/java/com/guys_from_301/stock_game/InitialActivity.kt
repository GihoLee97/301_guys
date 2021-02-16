package com.guys_from_301.stock_game

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.guys_from_301.stock_game.data.Profile
import com.guys_from_301.stock_game.data.ProflieDB
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
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.schedule
import android.text.InputFilter

class InitialActivity : AppCompatActivity() {
    val mContext : Context = this
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

        val id1: EditText = findViewById(R.id.et_id)
        val pw1: EditText = findViewById(R.id.et_pw)

        onlyAlphabetFilterToEnglishET(id1)
        onlyAlphabetFilterToEnglishET(pw1)


        // 회원가입 & onClickListner
        btn_generalSignup.setOnClickListener{

            val id1: TextView = findViewById(R.id.et_id)
            val pw1: TextView = findViewById(R.id.et_pw)

            val time1: LocalDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formatted = time1.format(formatter)
            val loginDate : String = formatted.toString().trim()
            val loginID = getHash(id1.text.toString().trim()).trim()
            val loginPW = getHash(pw1.text.toString().trim()).trim()
            Log.d("Giho","ID is hashed to : "+loginID)
            Log.d("Giho","PW is hashed to : "+loginPW)

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
            val loginDate : String = formatted.toString().trim()

            generalLoginCheck(saveid, savepw, loginDate)
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
            val dialog = Dialog_loading(this@InitialActivity)
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
                        id = user?.kakaoAccount?.email.toString()
                        pw = user?.id.toString() + user?.kakaoAccount?.profile?.nickname.toString()
                        dialog.dismiss()
                        Log.i(TAG, "로그인 성공 ${token.accessToken}")
                        GoogleKakaoSignup(getHash(id).trim(), getHash(pw).trim())
                        println("---kakao"+id)
                        println("---kakaopw"+pw)
                        loginSuccess("KAKAO", id, pw) // memorize login method and move to MainActivity
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
        val dialog = Dialog_loading(this@InitialActivity)
        dialog.show()

        var funlogincheck: Retrofitlogincheck? = null
        val loginID = getHash(saveid.trim()).trim()
        val loginPW = getHash(savepw.trim()).trim()

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
        funlogincheck.post_logincheck(loginID, loginPW, u_date).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@InitialActivity, "아이디나 비밀번호가 맞지 않습니다.", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
            override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    val okcode: String = response.body()!!
                    if (okcode == "7774"){
                        Toast.makeText(this@InitialActivity, "로그인 성공!", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                        loginSuccess("GENERAL", u_id, u_pw) // memorize login method and move to MainActivity
                    }
                    if (okcode == "4"){
                        Toast.makeText(this@InitialActivity, "아이디나 비밀번호가 틀렸습니다.", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                }

            }
        })
    }

    fun GoogleKakaoSignup(u_id: String, u_pw: String) {
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
                Toast.makeText(this@InitialActivity, t.message, Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                val code: String = response.body()!!
                if(code == "444"){
                    Toast.makeText(this@InitialActivity, "오류가 발생했습니다.\n잠시 후 다시 시도해주세요", Toast.LENGTH_LONG).show()
                }
                if(code == "555"){
                    //첫번째 로그인 아님
                }
                if(code == "666"){
                    //첫번째 로그인
                    Toast.makeText(this@InitialActivity, "회원가입이 완료되었습니다!", Toast.LENGTH_LONG).show()
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
        val dialog = Dialog_loading(this@InitialActivity)
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
                    GoogleKakaoSignup(getHash(id).trim(), getHash(pw).trim())
                    dialog.dismiss()
                    loginSuccess("GOOGLE", id, pw) // memorize login method and move to MainActivity
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun loginSuccess(method: String, id:String, pw:String){
        memorizeLogMethod(method)
        getuserinformation(id, pw)

        Timer().schedule(500) {
            profileDb = ProflieDB.getInstace(this@InitialActivity)
            if (profileDb?.profileDao()?.getNickname() == "#########first_login##########") {
                val intent = Intent(mContext, WelcomeActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(mContext, MainActivity::class.java)
                startActivity(intent)
            }
            finish()
        }
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
            // save which login method user have used // 절대 지우면 안됨!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            val newProfile = Profile(1, "#########first_login##########",0, 0, "", 1,0,0, temp, "a", "")
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
            newProfile.exp = profileDb?.profileDao()?.getExp()!!
            newProfile.login = temp
            newProfile.money = profileDb?.profileDao()?.getMoney()!!
            newProfile.profit = profileDb?.profileDao()?.getProfit()!!
            newProfile.login_id = profileDb?.profileDao()?.getLoginid()!!
            newProfile.login_pw = profileDb?.profileDao()?.getLoginpw()!!
            profileDb?.profileDao()?.update(newProfile)
        }
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

    fun getuserinformation(u_id: String, u_pw: String) {
        var fungetuserinformation: RetrofitGet? = null
        val url = "http://stockgame.dothome.co.kr/test/getall.php/"
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
        fungetuserinformation = retrofit.create(RetrofitGet::class.java)
        fungetuserinformation.getall(
            com.guys_from_301.stock_game.getHash(u_id).trim(), com.guys_from_301.stock_game.getHash(u_pw).trim()).enqueue(object : Callback<DATACLASS> {
            override fun onFailure(call: Call<DATACLASS>, t: Throwable) {
//                println("---"+t.message)
            }
            override fun onResponse(call: Call<DATACLASS>, response: retrofit2.Response<DATACLASS>) {
                if (response.isSuccessful && response.body() != null) {
                    var data: DATACLASS? = response.body()!!
                    profileDb = ProflieDB?.getInstace(this@InitialActivity)
                    val newProfile = Profile()
                    newProfile.id = profileDb?.profileDao()?.getId()?.toLong()
                    newProfile.nickname = data?.NICKNAME!!
                    newProfile.history = data?.HISTORY!!
                    newProfile.level = data?.LEVEL!!
                    newProfile.exp = data?.EXP!!
                    newProfile.login = profileDb?.profileDao()?.getLogin()!!
                    newProfile.money = data?.MONEY!!
                    newProfile.profit = data?.PROFIT!!
                    newProfile.login_id = u_id
                    newProfile.login_pw = u_pw
                    profileDb?.profileDao()?.update(newProfile)
                    profileDb = ProflieDB?.getInstace(this@InitialActivity)
                }
            }
        })
    }

    private fun onlyAlphabetFilterToEnglishET(IdorPw : EditText) {
        IdorPw.setFilters(arrayOf(
                InputFilter { src, start, end, dst, dstart, dend ->
                    if (src == " ") { // for space
                        println("---sp")
                        return@InputFilter ""
                    }
                    if (src == "") { // for backspace
                        println("---back")
                        return@InputFilter ""
                    }
                    if (src.matches(Regex("[a-zA-Z0-9]+"))) {
                        println("---정상")
                        return@InputFilter src
                    }
//                    if (src.matches(Regex("[ㄱ-ㅎㅏ-ㅣ가-힣]+"))) {
//                        println("---1")
//                        return@InputFilter ""
//                    }
                    else{
                        if(src == "ㄱ")
                        {
                            println("---ok")
                            return@InputFilter ""
                        }
                        println("---why")
                        return@InputFilter ""
                    }
//                    Toast.makeText(this, "영단어를 입력해주세요", Toast.LENGTH_LONG).show()
//                    IdorPw.setText("")
//                    return@InputFilter ""
                }
        ))
    }
}



