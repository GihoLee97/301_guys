package com.guys_from_301.stock_game

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.guys_from_301.stock_game.data.Quest

class Dialog_new_quest(context: Context) {
    var mContext: Context? = context
    val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var tv_reward: TextView
    private lateinit var tv_quest_contents: TextView
    private lateinit var btn_close: Button

    fun start(quest: Quest){
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.dialog_new_quest)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        dlg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        tv_reward = dlg.findViewById(R.id.tv_reward)
        tv_quest_contents = dlg.findViewById(R.id.tv_quest_contents)
        btn_close = dlg.findViewById(R.id.btn_close)

        tv_reward.text = "보상: "+ quest.reward
        tv_quest_contents.text = "'"+quest.questcontents+"' 도전과제를 달성하여 보상이 바로 지급되었습니다."

        btn_close.setOnClickListener {
            dlg.dismiss()
        }
        dlg.show()
    }
}