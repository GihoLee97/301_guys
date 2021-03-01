package com.guys_from_301.stock_game

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import android.widget.TextView

class Dialog_negotiation_unsuccess(context: Context) {
    var mContext: Context? = context
    val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var btn_close: Button
    private lateinit var tv_fail_message: TextView

    fun start(theme: Int, potion: Int){
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.dialog_negotiation_unsuccess)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        dlg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btn_close = dlg.findViewById(R.id.btn_close)
        tv_fail_message = dlg.findViewById(R.id.tv_negotiation_fail_message)

        when(theme){
            1->tv_fail_message.text = "협상에 실패하여 초기자산이 $"+ dec.format(SET_CASH_STEP[0])+"가 되었습니다"
            2->tv_fail_message.text = "협상에 실패하여 월급이 $"+ dec.format(SET_MONTHLY_STEP[0])+"가 되었습니다"
            3->tv_fail_message.text = "협상에 실패하여 연봉 인상률이 "+ SET_SALARY_RAISE_STEP[0]+"%가 되었습니다"
            4->tv_fail_message.text = "현재 스택이 부족합니다"
            5->tv_fail_message.text = "더 이상 협상을 진행할 수 없습니다"
            6->tv_fail_message.text = "피로도 물약 구매 성공 ! 현재 개수 " + potion +"개"
        }
        btn_close.setOnClickListener{
            dlg.dismiss()
        }

        dlg.show()
    }
}