package com.guys_from_301.stock_game

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.guys_from_301.stock_game.data.Profile
import com.guys_from_301.stock_game.data.ProfileDB
import com.guys_from_301.stock_game.retrofit.RetrofitDelete
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Dialog_DeleteAlert(context: Context) {
    var mContext: Context = context
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var btnOK: Button
    private lateinit var btnCancel: Button
    private lateinit var pw_present: EditText
    private lateinit var pw_check: EditText
    fun start() {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE) //타이틀바 제거
        dlg.setContentView(R.layout.dialog_deletealert) //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false) //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        btnOK = dlg.findViewById(R.id.btn_deleteok)
        btnCancel = dlg.findViewById(R.id.btn_deletecancel)
        pw_present = dlg.findViewById(R.id.pw_present)
        pw_check = dlg.findViewById(R.id.pw_check)
        var profileDb: ProfileDB? = null

        btnOK.setOnClickListener {
            profileDb = ProfileDB?.getInstace(mContext)
            if ((pw_present.text.toString().trim() == pw_check.text.toString().trim()) && (profileDb?.profileDao()?.getLoginpw()!! == pw_present.text.toString().trim())) {
                accountdelete(getHash(profileDb?.profileDao()?.getLoginid()!!).trim(),
                        getHash(pw_present.text.toString().trim()).trim())
                dlg.dismiss()
            }
            println("---db:"+profileDb?.profileDao()?.getLoginpw()!!)
            println("---현재:"+pw_present.text.toString().trim())
            println("---화긴: "+pw_check.text.toString().trim())
            if(profileDb?.profileDao()?.getLoginpw()!! != pw_present.text.toString().trim()) {
                Toast.makeText(mContext, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show()
            }
            if(pw_present.text.toString().trim() != pw_check.text.toString().trim()) {
                Toast.makeText(mContext, "비밀번호와 비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_LONG).show()
            }
        }
        btnCancel.setOnClickListener {
            dlg.dismiss()
        }
        dlg.show()
    }

    // 회원탈퇴 코드
    fun accountdelete(u_id: String, u_pw: String) {
        var fundelete: RetrofitDelete? = null
        val url = "http://stockgame.dothome.co.kr/test/delete.php/"
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
        fundelete = retrofit.create(RetrofitDelete::class.java)
        fundelete.retro_delete(u_id, u_pw).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(mContext, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    println("---"+response.body()!!)
                    if(response.body()!! == "444"){
                        Toast.makeText(mContext, "회원탈퇴가 완료되었습니다.", Toast.LENGTH_LONG).show()
                        generallogout()
                        val intent = Intent(mContext, InitialActivity::class.java)
                        mContext.startActivity(intent)
                    }
                    else{
                        Toast.makeText(mContext, "잠시 후 다시 시도해 주세요.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
    private fun generallogout(){
        var profileDb: ProfileDB? = null
        profileDb = ProfileDB?.getInstace(mContext)

        val newProfile = Profile()
        newProfile.id = profileDb?.profileDao()?.getId()?.toLong()
        newProfile.nickname = profileDb?.profileDao()?.getNickname()!!
        newProfile.history = profileDb?.profileDao()?.getHistory()!!
        newProfile.level = profileDb?.profileDao()?.getLevel()!!
        newProfile.login = profileDb?.profileDao()?.getLogin()!!
        newProfile.profit = profileDb?.profileDao()?.getProfit()!!
        newProfile.login_id = profileDb?.profileDao()?.getLoginid()!!
        newProfile.login_pw = profileDb?.profileDao()?.getLoginpw()!!
        profileDb?.profileDao()?.delete(newProfile)
    }
}