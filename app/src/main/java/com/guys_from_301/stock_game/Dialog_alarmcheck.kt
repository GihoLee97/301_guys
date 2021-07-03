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
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Dialog_alarmcheck(context: Context) {
    var mContext: Context? = context
    private val dlg = Dialog(context) //부모 액티비티의 context 가 들어감

    private lateinit var btn_cancel: Button
    private lateinit var btn_ok: Button

    fun start() {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE) //타이틀바 제거
        dlg.setContentView(R.layout.dialog_alarmcheck) //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false) //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        dlg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        btn_ok = dlg.findViewById(R.id.btn_save)
        btn_cancel = dlg.findViewById(R.id.btn_exit)

        btn_ok.setOnClickListener{
            pushAlarmManager.openSetting(mContext as MainActivity)
            dlg.dismiss()
        }

        btn_cancel.setOnClickListener {
            dlg.dismiss()
        }

        dlg.show()
    }

}