package com.guys_from_301.stock_game

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.guys_from_301.stock_game.retrofit.RetrofitIdcheck
import com.guys_from_301.stock_game.retrofit.RetrofitSignupNickcheck
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class InitialSignupFinalActivity : AppCompatActivity()  {
    var mContext : Context? = null
    private lateinit var et_signup_nick : EditText
    var _u_id : String = ""
    var _u_pw : String = ""
    var _u_method : String = "general"
    override fun onCreate(savedInstanceState: Bundle?) {
        mContext = this
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_signup_final)
        if (intent.hasExtra("u_id") && intent.hasExtra("u_pw")) {
            _u_id = intent.getStringExtra("u_id")!!
            _u_pw = intent.getStringExtra("u_pw")!!
        } else {
            Toast.makeText(this, "다시 시도해주세요", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
        if (intent.hasExtra("u_method")) {
            _u_method = intent.getStringExtra("u_method")!!
        } else {
                println("---일반로그인 아님")
        }

        findViewById<LinearLayout>(R.id.ll_signup_nick_delete).setOnClickListener{
            findViewById<EditText>(R.id.et_signup_nick).text = null
        }
        et_signup_nick = findViewById(R.id.et_signup_nick)
        nickfilter(et_signup_nick)
        findViewById<ImageButton>(R.id.ib_go_setpw).setOnClickListener {
            onBackPressed()
        }
        findViewById<Button>(R.id.btn_go_on).setOnClickListener{
            val time1: LocalDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formatted = time1.format(formatter)
            val SignupDate : String = formatted.toString().trim()
            signupnickcheck(getHash(_u_id).toString().trim(),
                    getHash(_u_pw).toString().trim(),
                    SignupDate, et_signup_nick.text.toString(),
                    _u_method)
//            Toast.makeText(this, "아이디: "+ u_id +"\n비밀번호: "+u_pw, Toast.LENGTH_LONG).show()
        }
    }
    private fun nickfilter(IdorPw : EditText) {
        IdorPw.setFilters(arrayOf(InputFilter.LengthFilter(10),
                InputFilter { src, start, end, dst, dstart, dend ->
                    if (src == " ") { // for space
                        findViewById<Button>(R.id.btn_go_on).setBackgroundResource(R.drawable.initial_box_go_on_gray)
                        findViewById<Button>(R.id.btn_go_on).setTextAppearance(R.style.initial_go_on)
                        return@InputFilter ""
                    }
                    if (src == "") { // for backspace
                        findViewById<Button>(R.id.btn_go_on).setBackgroundResource(R.drawable.initial_box_go_on_gray)
                        findViewById<Button>(R.id.btn_go_on).setTextAppearance(R.style.initial_go_on)
                        return@InputFilter ""
                    }
                    if( src != "" && src != null){
                        findViewById<Button>(R.id.btn_go_on).setBackgroundResource(R.drawable.initial_pw_go_on_ok)
                    }
                    if (src.matches(Regex("[a-zA-Z0-9ㄱ-ㅎ가-힣]+"))) {
                        return@InputFilter src
                    }
                    else{
                        return@InputFilter ""
                    }
                }
        ))
    }

    fun signupnickcheck(u_id: String, u_pw: String, u_date : String,u_nickname: String, method : String) {
        var funsignupnickcheck: RetrofitSignupNickcheck? = null
        val url = "http://stockgame.dothome.co.kr/test/signupnickcheck.php/"
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
        funsignupnickcheck= retrofit.create(RetrofitSignupNickcheck::class.java)
        funsignupnickcheck.signupnickcheck(u_id, u_pw, u_date, u_nickname).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                println("---서버업데이트실패: "+t.message)
            }
            override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    println("---서버업데이트성공")
                    if(response.body()!! == "444"){
                        findViewById<TextView>(R.id.tv_signup_final_ment_1).visibility = View.VISIBLE
                    }
                    else if(response.body()!! == "555"){
                        Toast.makeText(mContext, "아이디가 중복됩니다.", Toast.LENGTH_LONG).show()
                    }
                    else if(response.body()!! == "666"){
                        findViewById<TextView>(R.id.tv_signup_final_ment_1).visibility = View.INVISIBLE
                        val intent = Intent(mContext, InitialLoginActivity::class.java)
                        if(method == "general"){
                            intent.putExtra("u_id", _u_id)
                            intent.putExtra("u_pw", _u_pw)
                        }
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(mContext, "잠시 후 다시 시도해주세요", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })

    }
}