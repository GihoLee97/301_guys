package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.view.Window

class Dialog_loading(context : Context){
    var mContext: Context? = context

    val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    fun start() {
        dlg.setContentView(R.layout.dialog_loading)     //다이얼로그에 사용할 xml 파일을 불러옴
        //dlg.supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록
        dlg.show()
    }
    fun finish(){
        dlg.dismiss()
    }

}