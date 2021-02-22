package com.guys_from_301.stock_game

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.guys_from_301.stock_game.data.GameNormal
import com.guys_from_301.stock_game.data.GameNormalDB
import com.guys_from_301.stock_game.data.Profile
import com.guys_from_301.stock_game.data.ProfileDB
import com.guys_from_301.stock_game.retrofit.postquest
import kotlin.math.abs
import kotlin.math.roundToInt

class Dialog_quest(context: Context) {
    var mContext: Context? = context
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감\

    private lateinit var btnOk: Button
    private lateinit var btnQuest : Button
    private var profileDb : ProfileDB? = null


    fun start() {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.dialog_quest)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        btnOk = dlg.findViewById(R.id.btn_ok)
        btnQuest = dlg.findViewById(R.id.btn_quest)
        btnOk.setOnClickListener{
            //TODO: 서버로 올리기
//            postquest(getHash(profileDb?.profileDao()?.getLoginid()),
//                    getHash(profileDb?.profileDao()?.getLoginpw()),
//                    )
            dlg.dismiss()
        }

        btnQuest.setOnClickListener{
            //TODO: 업적 창 띄우기
            dlg.dismiss()
        }
    }


}
