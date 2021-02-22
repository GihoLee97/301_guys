package com.guys_from_301.stock_game

import android.app.Dialog
import android.app.ProgressDialog.show
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.Window
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guys_from_301.stock_game.data.*
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
        questDb = QuestDB.getInstance(dlg.context)
        var mAdapter = QuestAdapter(dlg.context, questAchieved)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(dlg.context)
        val qRecyclerView = dlg.findViewById<RecyclerView>(R.id.qRecyclerView)
        val r = Runnable{
            try{
                mAdapter = QuestAdapter(dlg.context, questAchieved)
                mAdapter.notifyDataSetChanged()

                qRecyclerView.adapter = mAdapter
                val manager = LinearLayoutManager(dlg.context, LinearLayoutManager.HORIZONTAL, false)
                qRecyclerView.setHasFixedSize(true)
                manager.reverseLayout = true
                manager.stackFromEnd = true
                qRecyclerView.layoutManager = manager
            }catch (e: Exception){
                Log.d("tag", "Error - $e")
            }
        }
        val thread = Thread(r)
        thread.start()
        btnOk = dlg.findViewById(R.id.btn_ok)
        btnQuest = dlg.findViewById(R.id.btn_quest)
        btnOk.setOnClickListener{
            quest(getHash(profileDb?.profileDao()?.getLoginid()!!).toString().trim(),
                    getHash(profileDb?.profileDao()?.getLoginpw()!!).toString().trim(),
                    sum())
            dlg.dismiss()
            questAchieved = arrayListOf()
        }

        btnQuest.setOnClickListener{
            //TODO: 업적 창 띄우기
            quest(getHash(profileDb?.profileDao()?.getLoginid()!!).toString().trim(),
                    getHash(profileDb?.profileDao()?.getLoginpw()!!).toString().trim(),
                    sum())
            dlg.dismiss()
            val intent = Intent(mContext, QuestActivity::class.java)
            questAchieved = arrayListOf()
            mContext?.startActivity(intent)
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
