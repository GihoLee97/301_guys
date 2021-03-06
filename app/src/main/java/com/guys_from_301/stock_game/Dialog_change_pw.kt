package com.guys_from_301.stock_game

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Dialog_change_pw(context: Context) {
    var mContext: Context = context
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var btn_changeOk : Button
    private lateinit var ib_changeCancle : ImageButton
    private lateinit var et_presentPW: EditText
    private lateinit var et_futurePw : EditText
    private lateinit var et_futurePwCheck : EditText
    private lateinit var tv_pwChangeNotice : TextView

    private var textLen1 = 0
    private var textLen2 = 0
    private var textLen3 = 0


    fun start(){
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE) //타이틀바 제거
        dlg.setContentView(R.layout.dialog_change_pw) //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false) //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        dlg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btn_changeOk = dlg.findViewById(R.id.btn_changeOk)
        ib_changeCancle = dlg.findViewById(R.id.ib_changeCancle)
        et_presentPW = dlg.findViewById(R.id.et_presentPW)
        et_futurePw = dlg.findViewById(R.id.et_futurePw)
        et_futurePwCheck = dlg.findViewById(R.id.et_futurePwCheck)
        tv_pwChangeNotice = dlg.findViewById(R.id.tv_pwChangeNotice)

        et_presentPW.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(textLen1*textLen2*textLen3==0){
                    btn_changeOk.setBackgroundResource(R.drawable.nickname_change_ok_box_gray)
                } else{
                    btn_changeOk.setBackgroundResource(R.drawable.nickname_change_ok_box)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                textLen1 = s!!.length
            }
        })

        et_futurePw.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(textLen1*textLen2*textLen3==0){
                    btn_changeOk.setBackgroundResource(R.drawable.nickname_change_ok_box_gray)
                } else{
                    btn_changeOk.setBackgroundResource(R.drawable.nickname_change_ok_box)
                }
            }
            override fun afterTextChanged(s: Editable?) {
                textLen2 = s!!.length
            }
        })

        et_futurePwCheck.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(textLen1*textLen2*textLen3==0){
                    btn_changeOk.setBackgroundResource(R.drawable.nickname_change_ok_box_gray)
                } else{
                    btn_changeOk.setBackgroundResource(R.drawable.nickname_change_ok_box)
                }
            }
            override fun afterTextChanged(s: Editable?) {
                textLen3 = s!!.length
            }
        })

        btn_changeOk.setOnClickListener {
            val hashid = getHash(profileDbManager!!.getLoginId().toString().trim()).trim()
            val hashPresentPw = getHash(et_presentPW.text.toString().trim()).trim()
            val hashNewPw = getHash(et_futurePw.text.toString().trim()).trim()
            val hashNewPwCheck = getHash(et_futurePwCheck.text.toString().trim()).trim()
            if(profileDbManager!!.getLoginPw().toString().trim() == et_presentPW.text.toString().trim()) {
                if (textLen1*textLen2*textLen3!=0) {
                    if (hashNewPw == hashNewPwCheck) {
                        changepw(hashid, hashPresentPw, hashNewPw)
                        dlg.dismiss()
                    } else {
                        tv_pwChangeNotice.visibility = View.VISIBLE
                        tv_pwChangeNotice.text = "새로운 비밀번호가 확인과 일치하지 않습니다."
                    }
                } else {
                    tv_pwChangeNotice.visibility = View.VISIBLE
                    tv_pwChangeNotice.text = "모두 입력하세요."
                }
            } else {
                tv_pwChangeNotice.visibility = View.VISIBLE
                tv_pwChangeNotice.text = "현재 비밀번호가 틀렸습니다."
            }
        }
        ib_changeCancle.setOnClickListener {
            dlg.dismiss()
        }

        dlg.show()
    }

    // change password
    fun changepw(u_id: String, u_pw: String, new_pw :String) {
        var funchangepw: RetrofitChange? = null
        val url = "http://stockgame.dothome.co.kr/test/changepw.php/"
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
        funchangepw= retrofit.create(RetrofitChange::class.java)
        funchangepw.retro_changepw(u_id, u_pw, new_pw).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                //TODO
                Toast.makeText(mContext, t.message, Toast.LENGTH_LONG).show()
                tv_pwChangeNotice.text = "잠시 후 다시 시도해 주세요."
            }
            override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                if(response.isSuccessful && response.body() != null) {
                    //val okcode: String = response.body()!!
                    //TODO
                    println("---"+response.body()!!)
                    Toast.makeText(mContext, "비밀번호 변경이 완료되었습니다.", Toast.LENGTH_LONG).show()
                    updateInFo(et_futurePw.text.toString().trim())
                }
            }
        })
    }
    private fun updateInFo(pw : String){
        profileDbManager!!.setLoginPw(pw)
    }
}