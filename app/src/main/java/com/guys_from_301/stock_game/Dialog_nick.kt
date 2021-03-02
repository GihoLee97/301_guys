package com.guys_from_301.stock_game


import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.*
import com.guys_from_301.stock_game.data.ProfileDB
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Dialog_nick(context: Context, first_login: Boolean) {
    var mContext: Context? = context
    private val dlg = Dialog(context) //부모 액티비티의 context 가 들어감
    private lateinit var et_nickname_change: EditText
    private lateinit var tv_nickname_number: TextView
    private lateinit var tv_set_nick_underline: TextView
    private lateinit var tv_nick_change_ment_1: TextView
    private lateinit var btn_nickname_change: Button
    private lateinit var ib_cancel: ImageButton

    private lateinit var listenter: Dialog_nick.NicknameDialogClickedListener
//    private val viewModel = _viewModel


    fun start() {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE) //타이틀바 제거
        dlg.setContentView(R.layout.dialog_nickname) //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false) //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        dlg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //
        et_nickname_change = dlg.findViewById(R.id.et_nickname_change)
        tv_nickname_number = dlg.findViewById(R.id.tv_nickname_number)
        tv_set_nick_underline = dlg.findViewById(R.id.tv_set_nick_underline)
        tv_nick_change_ment_1 = dlg.findViewById(R.id.tv_nick_change_ment_1)
        btn_nickname_change = dlg.findViewById(R.id.btn_nickname_change)
        ib_cancel = dlg.findViewById(R.id.ib_cancel)
        nickfilter(et_nickname_change)
        et_nickname_change.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length!! >= 0) {
                    if (p0?.length!! == 0) {
                        btn_nickname_change.setBackgroundResource(R.drawable.nickname_change_ok_box_gray)
                        tv_set_nick_underline.setBackgroundResource(R.drawable.initial_set_id_line_red)
                    } else {
                        btn_nickname_change.setBackgroundResource(R.drawable.nickname_change_ok_box)
                        tv_set_nick_underline.setBackgroundResource(R.drawable.initial_set_id_line)
                    }
                    tv_nickname_number.setText(p0?.length!!.toString() + "/10")
                } else {
                }
            }
        })

        ib_cancel.setOnClickListener {
            dlg.dismiss()
        }

        btn_nickname_change.setOnClickListener {
            var login_id: String = profileDbManager.getLoginId()!!

            var nickname: String = et_nickname_change.text.toString().trim()
            var funnickcheck: RetrofitNickcheck? = null
            val url = "http://stockgame.dothome.co.kr/test/nickcheck.php/"
            if (et_nickname_change.text.toString().trim() == "" || et_nickname_change.text.toString().trim() == null) {
                Toast.makeText(mContext, "공백 닉네임은 불가합니다.", Toast.LENGTH_LONG).show()
            } else {
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
                                tv_nick_change_ment_1.visibility = View.VISIBLE
                                tv_set_nick_underline.setBackgroundResource(R.drawable.initial_set_id_line_red)
                            }
                            if (okcode == "555") {
                                Toast.makeText(mContext, "닉네임이 변경되었습니다.", Toast.LENGTH_LONG).show()
                                profileDbManager.setNickname(et_nickname_change.text.toString())
//                                viewModel.setnWriteNickname(et_nickname_change.text.toString())
                                tv_nick_change_ment_1.visibility = View.INVISIBLE
                                update(getHash(profileDbManager.getLoginId()!!).trim(),
                                        getHash(profileDbManager.getLoginPw()!!).trim(),
                                        profileDbManager.getMoney()!!,
                                        profileDbManager.getValue1()!!,
                                        profileDbManager.getNickname()!!,
                                        profileDbManager.getProfitRate()!!,
                                        profileDbManager.getRelativeProfit()!!,
                                        profileDbManager.getRoundCount()!!,
                                        profileDbManager.getHistory()!!,
                                        profileDbManager.getLevel()!!
                                )
                                dlg.dismiss()
                            }
                            if (okcode == "666") {
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

    private fun nickfilter(IdorPw: EditText) {
        IdorPw.setFilters(arrayOf(InputFilter.LengthFilter(10),
                InputFilter { src, start, end, dst, dstart, dend ->
                    if (src == " ") { // for space
                        return@InputFilter ""
                    }
                    if (src == "") { // for backspace
                        btn_nickname_change.setBackgroundResource(R.drawable.nickname_change_ok_box_gray)
                        tv_set_nick_underline.setBackgroundResource(R.drawable.initial_set_id_line)
                        return@InputFilter ""
                    }
                    if (src != "" && src != null) {
                    }
                    if (src.matches(Regex("[a-zA-Z0-9ㄱ-ㅎ가-힣]+"))) {
                        return@InputFilter src
                    } else {
                        return@InputFilter ""
                    }
                }
        ))
    }

    fun setOnNicknameClickedListener(listener: (String) -> Unit) {
        this.listenter = object : NicknameDialogClickedListener {
            override fun onNicknameClicked(content: String) {
                listener(content)
            }
        }
    }

    interface NicknameDialogClickedListener {
        fun onNicknameClicked(content: String)
    }

}