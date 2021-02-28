package com.guys_from_301.stock_game

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.guys_from_301.stock_game.data.*
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

const val IN_GAME_PROFIT = 10
const val RELATIVE_PROFIT = IN_GAME_PROFIT+5
const val CUMULATIVE_TRADING_DAY = RELATIVE_PROFIT+5
const val CONTINUOUS_SURPLUS = CUMULATIVE_TRADING_DAY+5
const val PLAY_GAME = CONTINUOUS_SURPLUS+5
const val INVITE = PLAY_GAME+1

class InitialLoginActivity : AppCompatActivity() {

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
    private lateinit var tv_goToSignUp : TextView
    private lateinit var tv_notice : TextView
    private lateinit var et_id : EditText
    private lateinit var et_pw : EditText
    private lateinit var view_idUnderBar : View
    private lateinit var view_pwUnderBar : View
    private lateinit var tv_idTitle : TextView
    private lateinit var tv_pwTitle : TextView
    private lateinit var iv_visibility : ImageView
    private lateinit var ll_id : LinearLayout
    private lateinit var cl_pw : ConstraintLayout

    private var visibility = false
    private var id_length = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_login)

        btn_googleSignIn = findViewById(R.id.btn_googleSignIn)
        btn_generalLogin = findViewById(R.id.btn_generalLogin)
        btn_kakaoLogin = findViewById(R.id.btn_kakaoLogin)
        tv_goToSignUp = findViewById(R.id.tv_goToSignUp)
        tv_notice = findViewById(R.id.tv_notice)
        et_id = findViewById(R.id.et_id)
        et_pw = findViewById(R.id.et_pw)
        view_idUnderBar = findViewById(R.id.view_idUnderBar)
        view_pwUnderBar = findViewById(R.id.view_pwUnderBar)
        tv_idTitle = findViewById(R.id.tv_idTitle)
        tv_pwTitle = findViewById(R.id.tv_pwTitle)
        iv_visibility = findViewById(R.id.iv_visibility)
        ll_id = findViewById(R.id.ll_id)
        cl_pw = findViewById(R.id.cl_pw)

        // password visibility
        visibility = false

        onlyAlphabetFilterToEnglishET(et_id)
        onlyAlphabetFilterToEnglishET(et_pw)
        tv_notice.visibility = View.INVISIBLE
        attention("no")


        // attention to id or pw
        et_id.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                Log.d("Giho","onClickIdET")
                attention("id")
            }
            else{
                attention("no")
            }
        }
        et_pw.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                Log.d("Giho","onClickIdET")
                attention("pw")
            }
            else{
                attention("no")
            }
        }

        et_id.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                id_length = p0?.length!!
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0?.length!! !in 6..20){
                    notice("아이디는 6~20자입니다.")
                }
                else{
                    notice("")
                }
            }
        })

        et_pw.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0?.length!!>0&&(id_length in 6..20)){
                    btn_generalLogin.setBackgroundResource(R.drawable.initial_box_go_on_orange)
                }
                else{
                    btn_generalLogin.setBackgroundResource(R.drawable.initial_box_go_on_gray)
                    if(id_length !in 6..20)
                        notice("아이디는 6~20자입니다.")
                    else
                        notice("비밀번호를 입력하세요.")
                }
            }
        })

        et_pw.transformationMethod = HideReturnsTransformationMethod.getInstance()
        iv_visibility.setOnClickListener {
            visibility = !visibility
            if(visibility) {
                iv_visibility.setImageResource(R.drawable.ic_visible)
                et_pw.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            else {
                iv_visibility.setImageResource(R.drawable.ic_invisible)
                et_pw.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
        }


        // 로그인 & onClickListner
        btn_generalLogin.setOnClickListener{
            val et_id: TextView = findViewById(R.id.et_id)
            val et_pw: TextView = findViewById(R.id.et_pw)
            saveid = et_id.text.toString()
            savepw = et_pw.text.toString()
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
            val dialog = Dialog_loading(this)
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
                        dialog.dismiss()
                        Log.i(TAG, "로그인 성공 ${token.accessToken}")
                        GoogleKakaoSignup(getHash(id).trim(), getHash(pw).trim(),"KAKAO",id, pw)
                        println("---kakao"+id)
                        println("---kakaopw"+pw)
//                        loginSuccess("KAKAO", id, pw) // memorize login method and move to MainActivity
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

        tv_goToSignUp.setOnClickListener {
            val intent = Intent(this,InitialSetIdActivity::class.java)
            startActivity(intent)
        }

        // 피로도: 로그인 화면에서 마지막 저장시간 초기화
        var itemDb: ItemDB? = null
        itemDb = ItemDB?.getInstace(this)
        val newItem = Item()
        var nowtime = System.currentTimeMillis() // 현재 시간

        val addRunnable = Runnable {
            newItem.lasttime = nowtime
            itemDb?.itemDao()?.insert(newItem)
        }
        val addThread = Thread(addRunnable)
        addThread.start()
        //


        // 도전과제: 초기 도전과제 저장
        questDb = QuestDB.getInstance(this)
        // if profile DB is empty insert dummy
        if (questDb?.questDao()?.getAll().isNullOrEmpty()) {
            for (i in 1..questList.size){
                val newQuest = Quest(i, "" ,questList[i-1],rewardList[i-1], 0)
                if(i<= IN_GAME_PROFIT) newQuest.theme = "수익률"
                else if(i<= RELATIVE_PROFIT) newQuest.theme = "시장대비 수익률"
                else if(i<= CUMULATIVE_TRADING_DAY) newQuest.theme = "누적 거래일"
                else if(i<= CONTINUOUS_SURPLUS) newQuest.theme = "연속 흑자"
                else if(i<= PLAY_GAME) newQuest.theme = "투자 경험"
                else if(i== INVITE) newQuest.theme = "초대하기"
                questDb?.questDao()?.insert(newQuest)
            }
        }

        //초기 게임 setting 저장
        gameSetDb = GameSetDB.getInstace(this)
        val r = Runnable {
            val newGameSet = GameSet()
            setId = 1
            newGameSet.id = 1
            newGameSet.setcash = 0
            newGameSet.setgamelength = START_GAME_LENGTH
            newGameSet.setgamespeed = START_GAME_SPEED
            newGameSet.setmonthly = 0
            newGameSet.setsalaryraise = 0
            gameSetDb?.gameSetDao()?.insert(newGameSet)
            newGameSet.id = 0
            gameSetDb?.gameSetDao()?.insert(newGameSet)
        }
        val t = Thread(r)
        t.start()
    }

    // general login
    fun generalLoginCheck(u_id: String, u_pw: String, u_date: String) {
        val dialog = Dialog_loading(this@InitialLoginActivity)
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
                notice("아이디나 비밀번호가 일치하지 않거나 존재하지 않는 아이디 입니다.")
//                Toast.makeText(this@InitialLoginActivity, "아이디나 비밀번호가 맞지 않습니다.", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
            override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    val okcode: String = response.body()!!
                    if (okcode == "7774"){
//                        Toast.makeText(this@InitialLoginActivity, "로그인 성공!", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                        loginSuccess("GENERAL", u_id, u_pw) // memorize login method and move to MainActivity
                    }
                    if (okcode == "4"){
                        notice("아이디나 비밀번호가 일치하지 않거나 존재하지 않는 아이디 입니다.")
                        dialog.dismiss()
                    }
                }

            }
        })
    }

    fun GoogleKakaoSignup(u_id: String, u_pw: String, method: String, id : String, pw : String) {
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
                Toast.makeText(this@InitialLoginActivity, t.message, Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                val code: String = response.body()!!
                if(code == "444"){
                    Toast.makeText(this@InitialLoginActivity, "오류가 발생했습니다.\n잠시 후 다시 시도해주세요", Toast.LENGTH_LONG).show()
                }
                if(code == "555"){
                    Log.d("Gihoe","555???????")
                    loginSuccess(method,id, pw)
                    //첫번째 로그인 아님
                }
                if(code == "666"){
                    //첫번째 로그인 -> 회원가입 해야함
                    Toast.makeText(this@InitialLoginActivity, "먼저 회원가입해야 합니다. 회원가입 페이지로 이동합니다.", Toast.LENGTH_LONG).show()
                    goToSignUp()
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
        val dialog = Dialog_loading(this@InitialLoginActivity)
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
                        dialog.dismiss()
                        GoogleKakaoSignup(getHash(id).trim(), getHash(pw).trim(),"GOOGLE",id, pw)
//                        loginSuccess("GOOGLE", id, pw) // memorize login method and move to MainActivity
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
            profileDb = ProfileDB.getInstace(this@InitialLoginActivity)
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
        profileDb = ProfileDB?.getInstace(this)
        // if profile DB is empty insert dummy
        if (profileDb?.profileDao()?.getAll().isNullOrEmpty()) {
            if (method == "GENERAL") temp = 1
            else if (method == "GOOGLE") temp = 2
            else if (method == "KAKAO") temp = 4
            // save which login method user have used // 절대 지우면 안됨!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            val newProfile = Profile(1, "#########first_login##########",0,0, 0F,0F, 0, 0, 1,0,0, temp, "a", "")
            profileDb?.profileDao()?.insert(newProfile)
        } else {
            profileDb = ProfileDB?.getInstace(this)
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
            newProfile.value1 = profileDb?.profileDao()?.getValue1()!!
            newProfile.relativeprofitrate = profileDb?.profileDao()?.getRelativeProfitRate()!!
            newProfile.login_id = profileDb?.profileDao()?.getLoginid()!!
            newProfile.login_pw = profileDb?.profileDao()?.getLoginpw()!!
            profileDb?.profileDao()?.update(newProfile)
        }
    }
//
//    // 두번 누르면 종료되는 코드
//    var time3: Long = 0
//    override fun onBackPressed() {
//        val time1 = System.currentTimeMillis()
//        val time2 = time1 - time3
//        if (time2 in 0..2000) {
//            // 이거 3줄 다 써야 안전하게 종료
//            moveTaskToBack(true)
//            finish()
//            android.os.Process.killProcess(android.os.Process.myPid())
//        }
//        else {
//            time3 = time1
//            Toast.makeText(applicationContext, "한번 더 누르시면 종료됩니다.",Toast.LENGTH_SHORT).show()
//        }
//    }

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
                    profileDb = ProfileDB?.getInstace(this@InitialLoginActivity)
                    val newProfile = Profile()
                    newProfile.id = profileDb?.profileDao()?.getId()?.toLong()
                    newProfile.nickname = data?.NICKNAME!!
                    newProfile.history = data?.HISTORY!!
                    newProfile.level = data?.LEVEL!!
                    newProfile.exp = data?.EXP!!
                    newProfile.login = profileDb?.profileDao()?.getLogin()!!
                    newProfile.money = data?.MONEY!!
                    newProfile.value1 = data?.VALUE1!!
                    newProfile.relativeprofitrate = data?.RELATIVEPROFITRATE!!
                    newProfile.roundcount = data?.ROUNDCOUNT!!
                    newProfile.login_id = u_id
                    newProfile.login_pw = u_pw
                    //TODO: 업적저장
                    questdecode(data?.QUEST!!)
                    profileDb?.profileDao()?.update(newProfile)
                    profileDb = ProfileDB?.getInstace(this@InitialLoginActivity)
                }
            }
        })
    }

    //업적 디코딩
    private fun questdecode(questcode : Int){
        var cnt = 0
        var tmp = 0
        questDb = QuestDB.getInstance(this@InitialLoginActivity)
        var stringquest = Integer.toBinaryString(questcode)

        //가져온 string은 숫자앞에서부터 저장된다. 13=1101(2) 1, 1, 0, 1 순서
        // '0' 은 48 '1'은 49

        for(i in 0..stringquest.length - 1){
            val newQuest = Quest()
            newQuest.id = stringquest.length -i
            newQuest.achievement = stringquest[i].toInt()-48
            newQuest.theme = questDb?.questDao()?.getAll()!![stringquest.length -i -1].theme
            newQuest.questcontents = questDb?.questDao()?.getAll()!![stringquest.length -i -1].questcontents
            questDb?.questDao()?.update(newQuest)
        }
    }



    private fun onlyAlphabetFilterToEnglishET(IdorPw : EditText) {
        IdorPw.setFilters(arrayOf(
                InputFilter { src, start, end, dst, dstart, dend ->
                    if (src == " ") { // for space
                        notice("공백은 입력할 수 없습니다.")
                        return@InputFilter ""
                    }
                    if (src == "") { // for backspace
                        println("---back")
                        return@InputFilter ""
                    }
                    if (src.matches(Regex("[a-zA-Z0-9!\"#\$%&'()*+,./:;<=>?@\\^_`{|}~-]+"))) {
                        println("---정상")
                        notice("")
                        return@InputFilter src
                    }
//                    if (src.matches(Regex("[ㄱ-ㅎㅏ-ㅣ가-힣]+"))) {
//                        println("---1")
//                        return@InputFilter ""
//                    }
                    else{
                        notice("영어 대소문자, 숫자, 특수문자만을 입력해주세요")
                        return@InputFilter ""
                    }
//                    Toast.makeText(this, "영단어를 입력해주세요", Toast.LENGTH_LONG).show()
//                    IdorPw.setText("")
//                    return@InputFilter ""
                }
        ))
    }

    private fun goToSignUp(){
        val intent = Intent(this@InitialLoginActivity,InitialSetIdActivity::class.java)
        startActivity(intent)
    }

    private fun notice(notice : String){
        tv_notice.visibility = View.VISIBLE
        tv_notice.text = notice
    }

    private fun attention(obj : String){
        val attentionColor = R.color.attention
        val ignoreColor = R.color.ignore

        if(obj=="no"){
            view_idUnderBar.setBackgroundResource(R.drawable.ic_bottom_line_gray)
            view_pwUnderBar.setBackgroundResource(R.drawable.ic_bottom_line_gray)
            tv_idTitle.setTextColor(getColor(ignoreColor))
            tv_pwTitle.setTextColor(getColor(ignoreColor))
        }
        else if(obj=="id"){
            view_idUnderBar.setBackgroundResource(R.drawable.ic_bottom_line_orange)
            view_pwUnderBar.setBackgroundResource(R.drawable.ic_bottom_line_gray)
            tv_idTitle.setTextColor(getColor(attentionColor))
            tv_pwTitle.setTextColor(getColor(ignoreColor))
        }

        else if(obj=="pw"){
            view_idUnderBar.setBackgroundResource(R.drawable.ic_bottom_line_gray)
            view_pwUnderBar.setBackgroundResource(R.drawable.ic_bottom_line_orange)
            tv_idTitle.setTextColor(getColor(ignoreColor))
            tv_pwTitle.setTextColor(getColor(attentionColor))
        }
    }
}