package com.example.myapplication.data

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import com.example.myapplication.R

class Dialog_nick(context : Context) {
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var btnOK : Button
    fun start() {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.nickname_dialog)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        //
        btnOK = dlg.findViewById(R.id.nicknameokbtn)

        btnOK.setOnClickListener {
            //TODO: 부모 액티비티로 내용을 돌려주기 위해 작성할 코드
            dlg.dismiss()
        }


        dlg.show()
    }
}