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
import com.opencsv.CSVReader
import com.opencsv.CSVReaderBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

////////////////////////////////////////////////////////////////////////////////////////////////////
// CSV to 차트 데이터 ArrayList 생성
val snp_date: ArrayList<String> = ArrayList() // S&P500 날짜
val snp_val: ArrayList<String> = ArrayList() // S&P500 지수 값
val snp_vol: ArrayList<String> = ArrayList() // S&P500 거래량
val fund_date: ArrayList<String> = ArrayList() // Fed Fund Rate 날짜
val fund_val: ArrayList<String> = ArrayList() // Fed Fund Rate 값
val bond_date: ArrayList<String> = ArrayList() // 10 Year Treasury Bond Rate 날짜
val bond_val: ArrayList<String> = ArrayList() // 10 Year Treasury Bond Rate 값
val indpro_date: ArrayList<String> = ArrayList() // 미국 산업 생산량 날짜
val indpro_val: ArrayList<String> = ArrayList() // 미국 산성 생산량 값
val unem_date: ArrayList<String> = ArrayList() // 미국 실업률 날짜
val unem_val: ArrayList<String> = ArrayList() // 미국 실업률 값
val inf_date: ArrayList<String> = ArrayList() // Inflation rate 날짜
val inf_val: ArrayList<String> = ArrayList() // Inflation rate 값
var loadcomp: Int = 0 // 데이터 로드 완료 여부(미완료:0, 완료:1)
////////////////////////////////////////////////////////////////////////////////////////////////////

class InitialActivity : AppCompatActivity() {
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
            val loginID: String = id1.text.toString().trim()
            val loginPW: String = pw1.text.toString().trim()
            val loginDate : String = formatted.toString().trim()
            generalSignup(loginID, loginPW, loginDate)
        }

        // 로그인 & onClickListner
        btn_generalLogin.setOnClickListener{
            val id1: TextView = findViewById(R.id.et_id)
            val pw1: TextView = findViewById(R.id.et_pw)
            val time1: LocalDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formatted = time1.format(formatter)
            val loginID: String = id1.text.toString().trim()
            val loginPW: String = pw1.text.toString().trim()
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
        })

        ////////////////////////////////////////////////////////////////////////////////////////////
        // CSV to 차트 데이터 로드 코루틴 시작
        GlobalScope.launch(Dispatchers.Default) {

            var fileReader: BufferedReader? = null
            var csvReader: CSVReader? = null
            var count = 0

            // 모든 금융 데이터 csv 파일들은 \app\src\main\assets 에 저장
            // 금융 데이터 업데이트 시 해당 파일들 만을 최신화, 나머지는 모두 자동화

            ///////////////////////////////////////////////////////////////////////////////////////
            // "^GSPC.csv" 파일로 부터 S&P500 historical data 추출
            try {
                //println("\n--- S&P 500 ---")

                fileReader = BufferedReader(InputStreamReader(getAssets().open("^GSPC.csv")))
                // 1950-01-03 (헤더 포함 5498번쨰 행)부터 거래량 정보 유효(이전은 0, null 아님).
                // 헤더 스킵 및 1962-01-02 (헤더 포함 8511번쨰 행)부터 다른 모든 데이터 유효(bondrate).
                csvReader = CSVReaderBuilder(fileReader).withSkipLines(8510).build()

                val snp_rs = csvReader.readAll()
                count = 0
                for (snp_r in snp_rs) {
                    // snp500에 데이터 추가
                    snp_date.add(count, snp_r[0])
                    snp_val.add(count, snp_r[4])
                    snp_vol.add(count, snp_r[6])
                    count += 1

                    // 입력 확인
                    println("[SNP] 날짜 : " + snp_date[count - 1] + " | " + "값 : " + snp_val[count - 1] + " | " + "거래량 : " + snp_vol[count - 1] + " | " + "COUNT = $count")
                    // [0]: Date, [1]: Open, [2]: High, [3]: Low, [4]: Close, [5]: Adj Close, [6]: Volume
                    // 1950-01-03 (헤더 포함 5498번쨰 행)부터 거래량 정보 유효(이전은 0, null 아님).
                    // Open, High, Low, Close 값은 1967-06-30 까지 동일, 1967-07-03 (헤더 포함 9897번째 행)부터 세분화 되어 각각의 값이 달라짐.
                    // 참고) 2021-01-27 까지의 데이터 수(거래일)는 총 23380-1개
                }
            } catch (e: Exception) {
                println("Reading CSV Error : SNP !") // 에러 메시지 출력
                println("[SNP] size : " + snp_date.size.toString())
                e.printStackTrace()
            } finally {
                try {
                    fileReader!!.close()
                    csvReader!!.close()
                    println("[SNP] size : " + snp_date.size.toString())
                    count = 0
                } catch (e: IOException) {
                    println("Closing fileReader/csvParser Error : SNP !") // 에러 메시지 출력
                    e.printStackTrace()
                    count = 0
                }
            }

            ///////////////////////////////////////////////////////////////////////////////////////
            // "fed-funds-rate-historical-chart.csv" 파일로 부터 FED Fund Rate historical data 추출
            try {
                println("\n--- FED Fund Rate ---")

                fileReader = BufferedReader(InputStreamReader(getAssets().open("fed-funds-rate-historical-chart.csv")))
                // 헤더 스킵 및 1962-01-02 (헤더 포함 2759번쨰 행)부터 다른 모든 데이터 유효(bondrate).
                csvReader = CSVReaderBuilder(fileReader).withSkipLines(2758).build()

                val fundrate_rs = csvReader.readAll()
                for (fundrate_r in fundrate_rs) {
                    fund_date.add(count, fundrate_r[0])
                    fund_val.add(count, fundrate_r[1])
                    count += 1

                    //입력 확인
                    println("[FundRate] 날짜 : " + fund_date[count - 1] + " | " + "값 : " + fund_val[count - 1])
                    // [0]: Date, [1]: value
                    // 1954-07-01 (헤더 포함 17번쨰 행)부터 정보 유효.
                    // 이후 일별 데이터(매일 O, 거래일 X)
                    // 참고) 2021-02-17 까지의 데이터 수는 24048-16개
                    // CSV 파일 끝부분에 날짜는 존재하나 이율 값이 null 인 경우가 있음(# of null 행 < 25)
                }
            } catch (e: Exception) {
                println("Reading CSV Error : Fund Rate !") // 에러 메시지 출력
                println("[FundRate] size : " + fund_date.size.toString())
                e.printStackTrace()
            } finally {
                try {
                    fileReader!!.close()
                    csvReader!!.close()
                    println("[FundRate] size : " + fund_date.size.toString())
                    count = 0
                } catch (e: IOException) {
                    println("Closing fileReader/csvParser Error : !") // 에러 메시지 출력
                    e.printStackTrace()
                    count = 0
                }
            }

            ///////////////////////////////////////////////////////////////////////////////////////
            // "10-year-treasury-bond-rate-yield-chart.csv" 파일로 부터 미 국채 10년물 이율 historical data 추출
            try {
                println("\n--- 미 국채 10년물 이율 ---")

                fileReader = BufferedReader(InputStreamReader(getAssets().open("10-year-treasury-bond-rate-yield-chart.csv")))
                // 헤더 스킵(16행)
                csvReader = CSVReaderBuilder(fileReader).withSkipLines(16).build()

                val bondrate_rs = csvReader.readAll()
                for (bondrate_r in bondrate_rs) {
                    bond_date.add(count, bondrate_r[0])
                    bond_val.add(count, bondrate_r[1])
                    count += 1

                    //입력 확인
                    println("[BondRate] 날짜 : " + bond_date[count - 1] + " | " + "값 : " + bond_val[count - 1])
                    // [0]: Date, [1]: value
                    // 1962-01-02 (헤더 포함 17번쨰 행)부터 정보 유효.
                    // 이후 일별 데이터(거래일)
                    // 참고) 2021-01-27 까지의 데이터 수는 14786-16개
                    // CSV 파일 끝부분에 날짜는 존재하나 이율 값이 null 인 경우가 있음(# of null 행 < 25)
                }
            } catch (e: Exception) {
                println("Reading CSV Error : BondRate !") // 에러 메시지 출력
                println("[Bondrate] size : " + bond_date.size.toString())
                e.printStackTrace()
            } finally {
                try {
                    fileReader!!.close()
                    csvReader!!.close()
                    println("[Bondrate] size : " + bond_date.size.toString())
                    count = 0
                } catch (e: IOException) {
                    println("Closing fileReader/csvParser Error : BondRate !") // 에러 메시지 출력
                    e.printStackTrace()
                    count = 0
                }
            }

            ///////////////////////////////////////////////////////////////////////////////////////
            // "industrial-production-historical-chart.csv" 파일로 부터 Industrial production historical data 추출
            try {
                println("\n--- Industrial production ---")

                fileReader = BufferedReader(InputStreamReader(getAssets().open("industrial-production-historical-chart.csv")))
                // 헤더 스킵 및 1962-01-02 (헤더 포함 521번쨰 행)부터 다른 모든 데이터 유효(bondrate).
                // 521행은 1962-01-01
                csvReader = CSVReaderBuilder(fileReader).withSkipLines(520).build()

                val indpro_rs = csvReader.readAll()
                for (indpro_r in indpro_rs) {
                    indpro_date.add(count, indpro_r[0])
                    indpro_val.add(count, indpro_r[1])
                    count += 1

                    //입력 확인
                    println("[IndPro] 날짜 : " + indpro_date[count - 1] + " | " + "값 : " + indpro_val[count - 1])
                    // [0]: Date, [1]: value
                    // 1919-01-01 (헤더 포함 17번쨰 행)부터 정보 유효.
                    // 이후 월별 데이터(매월 1일)
                    // 참고) 2020-12-01 까지의 데이터 수는 1240-16개
                    // null 없음
                }
            } catch (e: Exception) {
                println("Reading CSV Error : IndPro !") // 에러 메시지 출력
                println("[IndPro] size : " + indpro_date.size.toString())
                e.printStackTrace()
            } finally {
                try {
                    fileReader!!.close()
                    csvReader!!.close()
                    println("[IndPro] size : " + indpro_date.size.toString())
                    count = 0
                } catch (e: IOException) {
                    println("Closing fileReader/csvParser Error : IndPro !") // 에러 메시지 출력
                    e.printStackTrace()
                    count = 0
                }
            }

            ///////////////////////////////////////////////////////////////////////////////////////
            // "us-national-unemployment-rate.csv" 파일로 부터 S&P500 historical data 추출
            try {
                println("\n--- Us National Unemployment Rate ---")

                fileReader = BufferedReader(InputStreamReader(getAssets().open("us-national-unemployment-rate.csv")))
                // 헤더 스킵 및 1962-01-02 (헤더 포함 185번쨰 행)부터 다른 모든 데이터 유효(bondrate).
                // 185행은 1962-01-01
                csvReader = CSVReaderBuilder(fileReader).withSkipLines(184).build()

                val unem_rs = csvReader.readAll()
                for (unem_r in unem_rs) {
                    unem_date.add(count, unem_r[0])
                    unem_val.add(count, unem_r[1])
                    count += 1

                    //입력 확인
                    println("[UnEm] 날짜 : " + unem_date[count - 1] + " | " + "값 : " + unem_val[count - 1])
                    // [0]: Date, [1]: value
                    // 1948-01-01 (헤더 포함 17번쨰 행)부터 정보 유효.
                    // 이후 월별 데이터(표기상 매월 1일)
                    // 참고) 2020-12-01 까지의 데이터 수는 892-16개
                }
            } catch (e: Exception) {
                println("Reading CSV Error : UnEm !") // 에러 메시지 출력
                println("[UnEm] size : " + unem_date.size.toString())
                e.printStackTrace()
            } finally {
                try {
                    fileReader!!.close()
                    csvReader!!.close()
                    println("[UnEm] size : " + unem_date.size.toString())
                    count = 0
                } catch (e: IOException) {
                    println("Closing fileReader/csvParser Error : UnEm !") // 에러 메시지 출력
                    e.printStackTrace()
                    count = 0
                }
            }

            ///////////////////////////////////////////////////////////////////////////////////////
            // "historical-inflation-rate-by-year.csv" 파일로 부터 Inflation rate historical data 추출
            try {
                println("\n--- Inflation rate ---")

                fileReader = BufferedReader(InputStreamReader(getAssets().open("historical-inflation-rate-by-year.csv")))
                // 헤더 스킵 및 1962-01-02 (헤더 포함 63번쨰 행)부터 다른 모든 데이터 유효(bondrate).
                // 521행은 1961-12-01
                csvReader = CSVReaderBuilder(fileReader).withSkipLines(62).build()

                val infrate_rs = csvReader.readAll()
                for (infrate_r in infrate_rs) {
                    inf_date.add(count, infrate_r[0])
                    inf_val.add(count, infrate_r[1])
                    count += 1

                    //입력 확인
                    println("[InfRate] 날짜 : " + inf_date[count - 1] + " | " + "값 : " + inf_val[count - 1])
                    // [0]: Date, [1]: value
                    // 1914-12-01 (헤더 포함 17번쨰 행)부터 정보 유효.
                    // 이후 년별 데이터(표기상 매년 12월 1일)
                    // 참고) 2020-12-01 까지의 데이터 수는 123-16개
                    // CSV 파일 마지막 행은 유효하지 않은 데이터(null 은 아님)
                }
            } catch (e: Exception) {
                println("Reading CSV Error : InfRate !") // 에러 메시지 출력
                println("[InfRate] size : " + inf_date.size.toString())
                e.printStackTrace()
            } finally {
                try {
                    fileReader!!.close()
                    csvReader!!.close()
                    println("[InfRate] size : " + inf_date.size.toString())
                    count = 0
                } catch (e: IOException) {
                    println("Closing fileReader/csvParser Error : InfRate !") // 에러 메시지 출력
                    e.printStackTrace()
                    count = 0
                }
            }
            println("Data Load Complete!") // 데이터 입력 종료

            loadcomp = 1
        }
        ////////////////////////////////////////////////////////////////////////////////////////////
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
                    if (okcode == "777"){
                        Toast.makeText(this@InitialActivity, "로그인 성공!", Toast.LENGTH_LONG).show()
                        loginSuccess("GENERAL") // memorize login method and move to MainActivity
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
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    // remember which login method did user used with DB
    private fun memorizeLogMethod(method : String){
        var profileDb = ProflieDB?.getInstace(this)
        // if profile DB is empty insert dummy
        if(profileDb?.profileDao()?.getAll().isNullOrEmpty()){
            val setRunnable = Runnable {
                val newProfile = Profile(1, "", 0, "", 1, 0)
                profileDb?.profileDao()?.insert(newProfile)
            }
            var setThread = Thread(setRunnable)
            setThread.start()
        }
        // save which login method user have used
        profileDb = ProflieDB?.getInstace(this)
        var temp = profileDb?.profileDao()?.getLogin()
        if(method=="GENERAL")   temp = temp?.or(1)
        else if(method=="GOOGLE")   temp = temp?.or(2)
        else if(method=="KAKAO")    temp = temp?.or(4)
        // update the login method to DB
        val setRunnable = Runnable {
            val newProfile = Profile()
            newProfile.id = profileDb?.profileDao()?.getId()?.toLong()
            newProfile.nickname = profileDb?.profileDao()?.getNickname()!!
            newProfile.history = profileDb?.profileDao()?.getHistory()!!
            newProfile.level = profileDb?.profileDao()?.getLevel()!!
            newProfile.login = temp!!
            newProfile.profit = profileDb?.profileDao()?.getProfit()!!
            profileDb?.profileDao()?.update(newProfile)
        }
        var setThread = Thread(setRunnable)
        setThread.start()
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

