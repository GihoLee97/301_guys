package com.guys_from_301.stock_game

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.guys_from_301.stock_game.data.*
import com.guys_from_301.stock_game.retrofit.quest
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt

class Dialog_quest(context: Context) {
    var mContext: Context? = context
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감\

    private lateinit var btnOk: Button
    private lateinit var btnQuest : Button
    private var profileDb : ProfileDB? = null
    private var questDb : QuestDB? = null

    fun start() {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.dialog_quest)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        profileDb = ProfileDB.getInstace(mContext!!)
        questDb = QuestDB.getInstance(mContext!!)

        questDb?.questDao()?.getAll()!![0].questcontents
        btnOk = dlg.findViewById(R.id.btn_ok)
        btnQuest = dlg.findViewById(R.id.btn_quest)
        btnOk.setOnClickListener{
            quest(getHash(profileDb?.profileDao()?.getLoginid()!!).toString().trim(),
                    getHash(profileDb?.profileDao()?.getLoginpw()!!).toString().trim(),
                    sum())
            dlg.dismiss()
        }

        btnQuest.setOnClickListener{
            //TODO: 업적 창 띄우기
            quest(getHash(profileDb?.profileDao()?.getLoginid()!!).toString().trim(),
                    getHash(profileDb?.profileDao()?.getLoginpw()!!).toString().trim(),
                    sum())
            dlg.dismiss()
        }
        dlg.show()
    }

    fun sum():Int{
        var tmp = 0
        var sum : Int = 0
        questDb = QuestDB.getInstance(mContext!!)
        val numberofquest = questDb?.questDao()?.getAll()!!.size
        questDb?.questDao()?.getAll()!![0].questcontents
        tmp = tmp + questDb?.questDao()?.getAll()!![0].achievement

        while(tmp < numberofquest){
            sum += questDb?.questDao()?.getAll()!![tmp].achievement!! * ((2.0).pow(tmp)).toInt()
            tmp++
        }
        return sum+1
    }
}
