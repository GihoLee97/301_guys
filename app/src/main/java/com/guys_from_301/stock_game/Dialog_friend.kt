package com.guys_from_301.stock_game

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.guys_from_301.stock_game.data.ProfileDB

class Dialog_friend(context: Context, count : Int) {
    var mContext: Context = context
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var btnOK : Button
    private lateinit var textmoney : TextView
    private lateinit var textlevel : TextView
    private lateinit var textnick : TextView

    private var profileDb: ProfileDB? = null
    fun start(count: Int){
        profileDb = ProfileDB.getInstace(mContext)
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE) //타이틀바 제거
        dlg.setContentView(R.layout.dialog_friend) //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false) //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        btnOK = dlg.findViewById(R.id.btn_friend_ok)
        textlevel = dlg.findViewById(R.id.friendlevel_text)
        textmoney = dlg.findViewById(R.id.friendmoney_text)
        textnick = dlg.findViewById(R.id.friendnick_text)


        textnick.text = friendnick[count].toString()
        textmoney.text = friendmoney[count].toString()
        textlevel.text = friendlevel[count].toString()

        btnOK.setOnClickListener {
            dlg.dismiss()
        }
        dlg.show()
    }
}