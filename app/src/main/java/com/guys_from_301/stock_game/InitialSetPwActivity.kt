package com.guys_from_301.stock_game

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class InitialSetPwActivity : AppCompatActivity() {
    var visible_pw : Boolean = false
    var visible_pw_check : Boolean = false
    var u_id : String = ""
    private lateinit var et_signup_pw : EditText
    private lateinit var et_signup_pw_check : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_set_pw)
        et_signup_pw = findViewById(R.id.et_signup_pw)
        et_signup_pw_check = findViewById(R.id.et_signup_pw_check)
        onlyAlphabetFilterToEnglishET(et_signup_pw)
        onlyAlphabetFilterToEnglishET(et_signup_pw_check)

        if (intent.hasExtra("u_id")) {
             u_id = intent.getStringExtra("u_id")!!
        } else {
            Toast.makeText(this, "다시 시도해주세요", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }

        findViewById<ImageButton>(R.id.ib_go_setid).setOnClickListener{
            onBackPressed()
        }
        findViewById<Button>(R.id.btn_go_on).setOnClickListener{
            if(et_signup_pw.text.toString().length < 6){
                findViewById<TextView>(R.id.tv_signup_pw_ment_1).visibility = View.VISIBLE
            }
            else{
                findViewById<TextView>(R.id.tv_signup_pw_ment_1).visibility = View.INVISIBLE
            }
            if(et_signup_pw.text.toString() != et_signup_pw_check.text.toString()){
                findViewById<TextView>(R.id.tv_signup_pw_ment_2).visibility = View.VISIBLE
            }
            else{
                findViewById<TextView>(R.id.tv_signup_pw_ment_2).visibility = View.INVISIBLE
            }
            if(et_signup_pw.text.toString() == et_signup_pw_check.text.toString() && et_signup_pw.text.toString().length >= 6){
                val intent = Intent(this, InitialSignupFinalActivity::class.java)
                intent.putExtra("u_id", u_id)
                intent.putExtra("u_pw", findViewById<EditText>(R.id.et_signup_pw).text.toString())
                startActivity(intent)
            }
        }

        findViewById<ImageButton>(R.id.ib_initial_pw).setOnClickListener{
            if(visible_pw){
                // 안 보이게 함
                findViewById<EditText>(R.id.et_signup_pw).setTransformationMethod(PasswordTransformationMethod.getInstance())
                findViewById<ImageButton>(R.id.ib_initial_pw).setBackgroundResource(R.drawable.initial_pw_visibility_off)
            }
            else{
                // 보이게 함
                findViewById<EditText>(R.id.et_signup_pw).setTransformationMethod(HideReturnsTransformationMethod.getInstance())
                findViewById<ImageButton>(R.id.ib_initial_pw).setBackgroundResource(R.drawable.initial_pw_visibility_on)
            }
            visible_pw = !visible_pw
        }
        findViewById<ImageButton>(R.id.ib_initial_pw_check).setOnClickListener{
            if(visible_pw_check){
                // 안 보이게 함
                findViewById<EditText>(R.id.et_signup_pw_check).setTransformationMethod(PasswordTransformationMethod.getInstance())
                findViewById<ImageButton>(R.id.ib_initial_pw_check).setBackgroundResource(R.drawable.initial_pw_visibility_off)
            }
            else{
                // 보이게 함
                findViewById<EditText>(R.id.et_signup_pw_check).setTransformationMethod(HideReturnsTransformationMethod.getInstance())
                findViewById<ImageButton>(R.id.ib_initial_pw_check).setBackgroundResource(R.drawable.initial_pw_visibility_on)
            }
            visible_pw_check = !visible_pw_check
        }
    }
    private fun onlyAlphabetFilterToEnglishET(IdorPw : EditText) {
        IdorPw.setFilters(arrayOf(InputFilter.LengthFilter(20),
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

}