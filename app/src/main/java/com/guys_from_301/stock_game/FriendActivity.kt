package com.guys_from_301.stock_game

import android.content.ContentValues
import android.graphics.Color
import com.kakao.sdk.talk.TalkApiClient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.guys_from_301.stock_game.retrofit.RetrofitFriend
import com.guys_from_301.stock_game.retrofit.RetrofitRanking
import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.talk.model.Friends
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.guys_from_301.stock_game.kakaoMessageManager
import com.guys_from_301.stock_game.MainActivity
import com.guys_from_301.stock_game.data.Quest
import com.guys_from_301.stock_game.data.QuestDB

// 친구 정보
var friendid = mutableListOf<String>()
var friendmoney = mutableListOf<Int>()
var friendlevel = mutableListOf<Int>()
var friendnick = mutableListOf<String>()
var friendname = mutableListOf<String>()
var frienduuid = mutableListOf<String>()
var friendimage = mutableListOf<String>()

class FriendActivity  : AppCompatActivity() {
    val coloroff = "#FF808080"
    val coloron = "#FF7070E7"
    private var questDb: QuestDB? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend)
        var cnt = 0
        questDb = QuestDB.getInstance(this)
        var friendquest = questDb?.questDao()?.getQuestByTheme("초대하기")?.get(0)
        while(cnt < friendlevel.size){
            if (friendquest != null) {
                addlayout(cnt, friendquest)
            }
            cnt++
        }

        findViewById<Button>(R.id.btn_goback).setOnClickListener{
            onBackPressed()
        }

    }

    fun addlayout( count : Int, friendquest: Quest){
        var textView = TextView(this)
        var layout = LinearLayout(this)
        var button_info = Button(this)
        var button_invite = Button(this)
        textView.text = "이름: " + friendname[count]
        textView.textSize = 30f
        button_info.text = "정보보기"
        button_invite.text = "초대보내기"

        if(friendlevel[count]==-1){
            button_info.setBackgroundColor(Color.parseColor(coloroff))
            button_invite.setBackgroundColor(Color.parseColor(coloron))
        }
        else{
            button_info.setBackgroundColor(Color.parseColor(coloron))
            button_invite.setBackgroundColor(Color.parseColor(coloroff))
        }

        button_invite.setOnClickListener{
            if(friendlevel[count]==-1){
                kakaoMessageManager.sendMessageonlyone(frienduuid[count], kakaoMessageManager.dummy)
                friendquest.achievement = friendquest.achievement+1
                val addRunnable = kotlinx.coroutines.Runnable {
                    questDb?.questDao()?.insert(friendquest)
                }
                val addThread = Thread(addRunnable)
                addThread.start()
                questAchieved.add(friendquest)
                Toast.makeText(this, "초대를 보냈습니다.", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this, "이미 가입한 사용자입니다.", Toast.LENGTH_LONG).show()
            }

        }
        layout.gravity = Gravity.CENTER
        button_info.setOnClickListener{
            if(friendlevel[count] == -1)
            {
                Toast.makeText(this, "가입하지 않는 유저입니다. 초대를 보내보세요!", Toast.LENGTH_LONG).show()
            }
            else{
                val dialog = Dialog_friend(this, count)
                dialog.start(count)
            }
        }
        layout.addView(textView)
        layout.addView(button_info)
        layout.addView(button_invite)
        findViewById<LinearLayout>(R.id.layout_admin).addView(layout)
    }



}



