package com.guys_from_301.stock_game

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.util.Log
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
    private lateinit var rg_profileImageSelect : RadioGroup
    private var isRadioGroupChecked = false
    private lateinit var rb_image1 : RadioButton
    private lateinit var rb_image2 : RadioButton
    private lateinit var rb_image3 : RadioButton
    private lateinit var rb_image4 : RadioButton
    private lateinit var rb_image5 : RadioButton
    private lateinit var rb_image6 : RadioButton
    private lateinit var rb_image7 : RadioButton
    private lateinit var rb_image8 : RadioButton
    private lateinit var rb_image9 : RadioButton
    private lateinit var rb_image10 : RadioButton

    var _u_id : String = ""
    var _u_pw : String = ""
    var _u_method : String = "general"
    override fun onCreate(savedInstanceState: Bundle?) {
        mContext = this
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_signup_final)
        window?.statusBarColor = resources.getColor(R.color.themeFragment)
        rb_image1 = findViewById(R.id.rb_icon1)
        rb_image2 = findViewById(R.id.rb_icon2)
        rb_image3 = findViewById(R.id.rb_icon3)
        rb_image4 = findViewById(R.id.rb_icon4)
        rb_image5 = findViewById(R.id.rb_icon5)
        rb_image6 = findViewById(R.id.rb_icon6)
        rb_image7 = findViewById(R.id.rb_icon7)
        rb_image8 = findViewById(R.id.rb_icon8)
        rb_image9 = findViewById(R.id.rb_icon9)
        rb_image10 = findViewById(R.id.rb_icon10)
        val images: List<RadioButton> = listOf(rb_image1,rb_image2,rb_image3,rb_image4,rb_image5,rb_image6,rb_image7,rb_image8,rb_image9,rb_image10)
        var _u_imageNumber = 0

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

        isRadioGroupChecked = false
        rg_profileImageSelect = findViewById(R.id.rg_profileImageSelect)
        rg_profileImageSelect.setOnCheckedChangeListener{ group, checkedId ->
            isRadioGroupChecked = true
            Log.d("Giho","checkedId : "+checkedId.toString())
        }

                rb_image1.setOnCheckedChangeListener{ group, checked ->
            isRadioGroupChecked = true
            if(checked) {
                _u_imageNumber = 0
                for ( i: Int in 0..9){
                    if(i != _u_imageNumber) {
                        images[i].isChecked = false
                        images[i].alpha = 0.5F
                    }
                    else images[i].alpha = 1F
                }
            }
        }
        rb_image2.setOnCheckedChangeListener{ group, checked ->
            isRadioGroupChecked = true
            if(checked) {
                _u_imageNumber = 1
                for ( i: Int in 0..9){
                    if(i != _u_imageNumber) {
                        images[i].isChecked = false
                        images[i].alpha = 0.5F
                    }
                    else images[i].alpha = 1F
                }
            }
        }
        rb_image3.setOnCheckedChangeListener{ group, checked ->
            isRadioGroupChecked = true
            if(checked) {
                _u_imageNumber = 2
                for ( i: Int in 0..9){
                    if(i != _u_imageNumber) {
                        images[i].isChecked = false
                        images[i].alpha = 0.5F
                    }
                    else images[i].alpha = 1F
                }
            }
        }
        rb_image4.setOnCheckedChangeListener{ group, checked ->
            isRadioGroupChecked = true
            if(checked) {
                _u_imageNumber = 3
                for ( i: Int in 0..9){
                    if(i != _u_imageNumber) {
                        images[i].isChecked = false
                        images[i].alpha = 0.5F
                    }
                    else images[i].alpha = 1F
                }
            }
        }
        rb_image5.setOnCheckedChangeListener{ group, checked ->
            isRadioGroupChecked = true
            if(checked) {
                _u_imageNumber = 4
                for ( i: Int in 0..9){
                    if(i != _u_imageNumber) {
                        images[i].isChecked = false
                        images[i].alpha = 0.5F
                    }
                    else images[i].alpha = 1F
                }
            }
       }
        rb_image6.setOnCheckedChangeListener{ group, checked ->
            isRadioGroupChecked = true
            if(checked) {
                _u_imageNumber = 5
                for ( i: Int in 0..9){
                    if(i != _u_imageNumber) {
                        images[i].isChecked = false
                        images[i].alpha = 0.5F
                    }
                    else images[i].alpha = 1F
                }
            }
        }
        rb_image7.setOnCheckedChangeListener{ group, checked ->
            isRadioGroupChecked = true
            if(checked) {
                _u_imageNumber = 6
                for ( i: Int in 0..9){
                    if(i != _u_imageNumber) {
                        images[i].isChecked = false
                        images[i].alpha = 0.5F
                    }
                    else images[i].alpha = 1F
                }
            }
        }
        rb_image8.setOnCheckedChangeListener{ group, checked ->
            isRadioGroupChecked = true
            if(checked) {
                _u_imageNumber = 7
                for ( i: Int in 0..9){
                    if(i != _u_imageNumber) {
                        images[i].isChecked = false
                        images[i].alpha = 0.5F
                    }
                    else images[i].alpha = 1F
                }
            }
        }
        rb_image9.setOnCheckedChangeListener{ group, checked ->
            isRadioGroupChecked = true
            if(checked) {
                _u_imageNumber = 8
                for ( i: Int in 0..9){
                    if(i != _u_imageNumber) {
                        images[i].isChecked = false
                        images[i].alpha = 0.5F
                    }
                    else images[i].alpha = 1F
                }
            }
        }
        rb_image10.setOnCheckedChangeListener{ group, checked ->
            isRadioGroupChecked = true
            if(checked) {
                _u_imageNumber = 9
                for ( i: Int in 0..9){
                    if(i != _u_imageNumber) {
                        images[i].isChecked = false
                        images[i].alpha = 0.5F
                    }
                    else images[i].alpha = 1F
                }
            }
        }


        findViewById<Button>(R.id.btn_go_on).setOnClickListener{
            if(isRadioGroupChecked) {
                when(rg_profileImageSelect.checkedRadioButtonId){
                    R.id.rb_icon1 -> _u_imageNumber=0
                    R.id.rb_icon2 -> _u_imageNumber=1
                    R.id.rb_icon3 -> _u_imageNumber=2
                    R.id.rb_icon4 -> _u_imageNumber=3
                    R.id.rb_icon5 -> _u_imageNumber=4
                    R.id.rb_icon6 -> _u_imageNumber=5
                    R.id.rb_icon7 -> _u_imageNumber=6
                    R.id.rb_icon8 -> _u_imageNumber=7
                    R.id.rb_icon9 -> _u_imageNumber=8
                    R.id.rb_icon10 -> _u_imageNumber=9
                }
                val time1: LocalDateTime = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val formatted = time1.format(formatter)
                val SignupDate: String = formatted.toString().trim()
                signupnickcheck(getHash(_u_id).toString().trim(),
                        getHash(_u_pw).toString().trim(),
                        SignupDate, et_signup_nick.text.toString(),
                        _u_imageNumber, _u_method)
//            Toast.makeText(this, "아이디: "+ u_id +"\n비밀번호: "+u_pw, Toast.LENGTH_LONG).show()
            } else{
                findViewById<TextView>(R.id.tv_signup_final_ment_2).visibility = View.VISIBLE
            }
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

    fun signupnickcheck(u_id: String, u_pw: String, u_date : String,u_nickname: String, u_imageNumber : Int ,method : String) {
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
        funsignupnickcheck.signupnickcheck(u_id, u_pw, u_date, u_nickname, u_imageNumber).enqueue(object : Callback<String> {
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
                        val intent = Intent(mContext, InitialSignUpCompleteActivity::class.java)
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