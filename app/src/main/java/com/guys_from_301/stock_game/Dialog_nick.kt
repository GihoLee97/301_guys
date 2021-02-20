package com.guys_from_301.stock_game


import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.*
import com.guys_from_301.stock_game.data.ProfileDB
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Dialog_nick(context : Context, first_login : Boolean, _viewModel: ProfileActivityViewModel) {
    var mContext: Context? = context
    private val dlg = Dialog(context) //부모 액티비티의 context 가 들어감
    private lateinit var btn_ok : Button
    private lateinit var nickname_editText : EditText
    private lateinit var listenter: Dialog_nick.NicknameDialogClickedListener
    private var first_login = first_login
    private val viewModel = _viewModel


    fun start(profileDb :ProfileDB?) {

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE) //타이틀바 제거
        dlg.setContentView(R.layout.dialog_nickname) //다이얼로그에 사용할 xml 파일을 불러옴
        if(first_login) dlg.setCancelable(false) //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        else dlg.setCancelable(true)
        //
        btn_ok = dlg.findViewById(R.id.nicknameokbtn)
        nickname_editText = dlg.findViewById(R.id.editNickName)

        btn_ok.setOnClickListener {
            viewModel.setnWriteNickname(nickname_editText.text.toString())
            var login_id: String = profileDb?.profileDao()?.getLoginid()!!

            var nickname: String = nickname_editText.text.toString().trim()
            var history: Int = profileDb?.profileDao()?.getHistory()!!
            var level: Int = profileDb?.profileDao()?.getLevel()!!
            var profit: Float = profileDb?.profileDao()?.getProfit()!!
            var funnickcheck: RetrofitNickcheck? = null
            val url = "http://stockgame.dothome.co.kr/test/nickcheck.php/"
            if (nickname_editText.text.toString().trim() == "" || nickname_editText.text.toString().trim() == null) {
                Toast.makeText(mContext, "공백 닉네임은 불가합니다.", Toast.LENGTH_LONG).show()
            }
            else {
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
                funnickcheck = retrofit.create(RetrofitNickcheck::class.java)
                funnickcheck.nickcheck(getHash(login_id), nickname).enqueue(object :
                    Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        println("---fail")
                    }
                    override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                        if (response.isSuccessful && response.body() != null) {
                            val okcode = response.body()!!
                            if (okcode == "444") {
                                Toast.makeText(mContext, "사용중인 닉네임입니다.", Toast.LENGTH_LONG).show()
                            }
                            if (okcode == "555") {
                                Toast.makeText(mContext, "닉네임이 변경되었습니다.", Toast.LENGTH_LONG).show()
//                                var profileDb: ProflieDB? = null
//                                profileDb = ProflieDB?.getInstace(mContext!!)
//                                val newProfile = Profile()

                                update(getHash(profileDb?.profileDao()?.getLoginid()).trim(),
                                getHash(profileDb?.profileDao()?.getLoginpw()).trim(),
                                    profileDb?.profileDao()?.getMoney()!!,
                                    profileDb?.profileDao()?.getValue1()!!,
                                    profileDb?.profileDao()?.getNickname()!!,
                                    profileDb?.profileDao()?.getProfit()!!,
                                    profileDb?.profileDao()?.getRoundCount()!!,
                                    profileDb?.profileDao()?.getHistory()!!,
                                    profileDb?.profileDao()?.getLevel()!!)
                                dlg.dismiss()
                            }
                            if(okcode =="666"){
                                // 입력한 닉네임이 현재 닉네임과 일치하는 경우
                                dlg.dismiss()
                            }
                        }
                    }
                })

            }
        }
        dlg.show()
    }

    fun setOnNicknameClickedListener(listener: (String)->Unit){
        this.listenter = object : NicknameDialogClickedListener{
            override fun onNicknameClicked(content: String) {
                listener(content)
            }
        }
    }

    interface NicknameDialogClickedListener{
        fun onNicknameClicked(content: String)
    }

}