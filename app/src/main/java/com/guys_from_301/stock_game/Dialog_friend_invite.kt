package com.guys_from_301.stock_game

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Parcelable
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guys_from_301.stock_game.data.*
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.talk.model.Friends
import java.time.LocalDateTime

class Dialog_friend_invite(context: Context) {

    lateinit var mAdaper: FriendInviteAdapter
    var mContext: Context? = context
    val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    fun start() {
        println("---"+friend_all_array!!.elements[0].profileNickname)
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.dialog_friend_invite)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        dlg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dlg.findViewById<Button>(R.id.btn_friend_invite_cancel).setOnClickListener{
            dlg.dismiss()
        }

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(mContext)
        val hRecyclerView = dlg.findViewById<RecyclerView>(R.id.hRecyclerView)
        hRecyclerView.layoutManager = layoutManager
        mAdaper = FriendInviteAdapter(mContext!!, friend_all_array!!){
//            gameUnit ->
//            isHistory = true
//            profitrate = gameUnit.profitrate
//            relativeprofitrate = gameUnit.relativeprofitrate
//            taxtot = gameUnit.taxtot
//            dividendtot = gameUnit.dividendtot
//            tradecomtot = gameUnit.tradecomtot
//            profit = gameUnit.profit
//            totaltradeday = gameUnit.item1able
//            val intent = Intent(this@NewHistoryActivity, NewResultNormalActivity::class.java)
//            startActivity(intent)
        }
////
        hRecyclerView.adapter = mAdaper
        val manager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        hRecyclerView.setHasFixedSize(true)
        manager.reverseLayout = true
        manager.stackFromEnd = true
        hRecyclerView.layoutManager = manager
//
        dlg.show()
    }

}