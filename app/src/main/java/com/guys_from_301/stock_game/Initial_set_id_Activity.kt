package com.guys_from_301.stock_game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.AllCaps
import android.text.InputFilter.LengthFilter
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.guys_from_301.stock_game.retrofit.RetrofitIdcheck
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Initial_set_id_Activity : AppCompatActivity() {
    var mContext : Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        mContext = this
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_set_id)
        findViewById<ImageButton>(R.id.ib_go_signup_main).setOnClickListener{
            onBackPressed()
        }
        onlyAlphabetFilterToEnglishET(findViewById<EditText>(R.id.et_signup_id))
        findViewById<Button>(R.id.btn_go_on).setOnClickListener{
            idcheck(getHash(findViewById<EditText>(R.id.et_signup_id).text.toString()))
        }
        findViewById<LinearLayout>(R.id.ll_signup_id_delete).setOnClickListener{
            findViewById<EditText>(R.id.et_signup_id).text = null
        }
    }

    private fun onlyAlphabetFilterToEnglishET(IdorPw : EditText) {
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
                        findViewById<Button>(R.id.btn_go_on).setBackgroundResource(R.drawable.initial_go_on_ok)
                        findViewById<Button>(R.id.btn_go_on).setTextAppearance(R.style.initial_go_on_ok)
                    }
                    if (src.matches(Regex("[a-zA-Z0-9]+"))) {
                        println("---정상")
                        return@InputFilter src
                    }
                    else{
                        return@InputFilter ""
                    }
                }
        ))
    }
    fun idcheck(u_id: String) {
        var funidcheck: RetrofitIdcheck? = null
        val url = "http://stockgame.dothome.co.kr/test/idcheck.php/"
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
        funidcheck= retrofit.create(RetrofitIdcheck::class.java)
        funidcheck.idcheck(u_id).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                println("---서버업데이트실패: "+t.message)
            }
            override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    println("---서버업데이트성공")
                    if(response.body()!! == "444"){
                        Toast.makeText(mContext, "이미 있는 아이디입니다.", Toast.LENGTH_LONG).show()
                    }
                    else if(response.body()!! == "555"){
                        val intent = Intent(mContext, Initial_set_pw_Activity::class.java)
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