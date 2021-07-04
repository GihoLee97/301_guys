package com.guys_from_301.stock_game

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.guys_from_301.stock_game.retrofit.RetrofitDelete
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.guys_from_301.stock_game.data.GameNormalDB
import com.guys_from_301.stock_game.data.GameSetDB
import com.guys_from_301.stock_game.data.QuestDB
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Dialog_DeleteKakaoGoogle(context: Context, method : String) {
    var mContext: Context = context
    var loginMethod : String = method
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var btnOK: Button
    private lateinit var btnCancel: ImageButton
    private lateinit var et_nickname: EditText
    private lateinit var tv_nick_change_ment_1 : TextView
    private var gameNormalDb: GameNormalDB? = null
    private var gameSetDb: GameSetDB? = null
    private var questDb: QuestDB? = null

    fun start() {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE) //타이틀바 제거
        dlg.setContentView(R.layout.dialog_deletekakaogoogle) //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false) //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        dlg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btnOK = dlg.findViewById(R.id.btn_deleteok)
        btnCancel = dlg.findViewById(R.id.btn_deletecancel)
        et_nickname = dlg.findViewById(R.id.et_nickname)
        tv_nick_change_ment_1 = dlg.findViewById(R.id.tv_pwChangeNotice)

        gameNormalDb = GameNormalDB.getInstace(dlg.context)
        gameSetDb = GameSetDB.getInstace(dlg.context)
        questDb = QuestDB.getInstance(dlg.context)

        et_nickname.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.isNullOrEmpty()){
                    btnOK.setBackgroundResource(R.drawable.nickname_change_ok_box_gray)
                }
                else{
                    btnOK.setBackgroundResource(R.drawable.nickname_change_ok_box)
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        btnOK.setOnClickListener {
            if(et_nickname.text.toString()==profileDbManager!!.getNickname()) {
                accountdelete(
                    getHash(profileDbManager!!.getLoginId()!!).trim(),
                    getHash(profileDbManager!!.getLoginPw()!!).trim()
                )
                gameSetDb?.gameSetDao()?.deleteSignOut()
                gameNormalDb?.gameNormalDao()?.deleteSignOut()
                questDb?.questDao()?.deleteSignOut()
                signOut(loginMethod)
                dlg.dismiss()
            }
            else{
                tv_nick_change_ment_1.visibility = View.VISIBLE
                tv_nick_change_ment_1.text = "닉네임이 틀렸습니다."
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
                        val intent = Intent(mContext, NewInitialActivity::class.java)
                        mContext.startActivity(intent)
                    }
                    else{
                        Toast.makeText(mContext, "잠시 후 다시 시도해 주세요.", Toast.LENGTH_LONG).show()
                        tv_nick_change_ment_1.visibility = View.VISIBLE
                        tv_nick_change_ment_1.text = "잠시 후 다시 시도해 주세요"
                    }
                }
            }
        })
    }

    private fun signOut(method: String){
        if(method=="KAKAO"){
            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    Log.e("KAKAO REVOKE ACCESS", "연결 끊기 실패", error)
                }
                else {
                    Log.i("KAKAO REVOKE ACCESS", "연결 끊기 성공. SDK에서 토큰 삭제 됨")
                }
            }
        }
        else if(method=="GOOGLE"){
            var googleAuth : FirebaseAuth
            googleAuth = FirebaseAuth.getInstance()
            googleAuth.signOut()
        }
    }
}