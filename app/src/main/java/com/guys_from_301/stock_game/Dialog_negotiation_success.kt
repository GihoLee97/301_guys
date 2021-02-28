package com.guys_from_301.stock_game

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import android.widget.TextView

class Dialog_negotiation_success(context: Context) {
    var mContext: Context? = context
    val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var tv_before: TextView
    private lateinit var tv_after: TextView
    private lateinit var btn_close: Button

    fun start(theme:Int, step:Int){
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.dialog_negotiation_success)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        dlg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        tv_before = dlg.findViewById(R.id.tv_before)
        tv_after = dlg.findViewById(R.id.tv_after)
        btn_close = dlg.findViewById(R.id.btn_close)

        when(theme){
            1->{
                tv_before.text = "$"+dec.format(SET_CASH_STEP[step]).toString()
                tv_after.text = "$"+ dec.format(SET_CASH_STEP[step+1]).toString()
            }
            2->{
                tv_before.text = "$"+ dec.format(SET_MONTHLY_STEP[step]).toString()
                tv_after.text = "$"+ dec.format(SET_MONTHLY_STEP[step+1]).toString()
            }
            3->{
                tv_before.text = "    "+SET_SALARY_RAISE_STEP[step].toString() +"%"
                tv_after.text = SET_SALARY_RAISE_STEP[step+1].toString()+ "%  "
            }
        }

        btn_close.setOnClickListener{
            dlg.dismiss()
        }

        dlg.show()
    }
}