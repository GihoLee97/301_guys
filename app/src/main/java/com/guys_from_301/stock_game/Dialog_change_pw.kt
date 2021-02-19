package com.guys_from_301.stock_game

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.guys_from_301.stock_game.data.Profile
import com.guys_from_301.stock_game.data.ProfileDB
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Dialog_change_pw(context: Context) {
    var mContext: Context = context
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var btnOK : Button
    private lateinit var btnCancel : Button
    private lateinit var pw_present: EditText
    private lateinit var pw_future : EditText
    private var profileDb: ProfileDB? = null
    fun start(){
        profileDb = ProfileDB.getInstace(mContext)

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE) //타이틀바 제거
        dlg.setContentView(R.layout.dialog_change_pw) //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false) //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        btnOK = dlg.findViewById(R.id.okButton)
        btnCancel = dlg.findViewById(R.id.cancelButton)
        pw_present = dlg.findViewById(R.id.pw_present)
        pw_future = dlg.findViewById(R.id.pw_future)


        btnOK.setOnClickListener {
            val hashid =getHash(profileDb?.profileDao()?.getLoginid().toString().trim()).trim()
            println("---")
            val hashpw =getHash(pw_present.text.toString().trim()).trim()
            val hashnewpw =getHash(pw_future.text.toString().trim()).trim()
            if(profileDb?.profileDao()?.getLoginpw().toString().trim() == pw_present.text.toString().trim())
            {
                changepw(hashid,hashpw, hashnewpw)
                dlg.dismiss()
            }
            else{
                Toast.makeText(mContext, "비밀번호가 틀렸습니다.", Toast.LENGTH_LONG).show()
            }

        }
        btnCancel.setOnClickListener {
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
            }
            override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    //val okcode: String = response.body()!!
                    //TODO
                    println("---"+response.body()!!)
                    Toast.makeText(mContext, "비밀번호 변경이 완료되었습니다.", Toast.LENGTH_LONG).show()
                    updateInFo(pw_future.text.toString().trim())
                }
            }
        })
    }
    private fun updateInFo(pw : String){
        profileDb = ProfileDB?.getInstace(mContext)
        val setRunnable = Runnable {
            val newProfile = Profile()
            newProfile.id = profileDb?.profileDao()?.getId()?.toLong()
            newProfile.nickname = profileDb?.profileDao()?.getNickname()!!
            newProfile.money = profileDb?.profileDao()?.getMoney()!!
            newProfile.history = profileDb?.profileDao()?.getHistory()!!
            newProfile.level = profileDb?.profileDao()?.getLevel()!!
            newProfile.exp = profileDb?.profileDao()?.getExp()!!
            newProfile.rank = profileDb?.profileDao()?.getRank()!!
            newProfile.login = profileDb?.profileDao()?.getLogin()!!
            newProfile.profit = profileDb?.profileDao()?.getProfit()!!
            newProfile.login_id = profileDb?.profileDao()?.getLoginid()!!
            newProfile.login_pw = pw
            profileDb?.profileDao()?.update(newProfile)
        }
        var setThread = Thread(setRunnable)
        setThread.start()
    }
}