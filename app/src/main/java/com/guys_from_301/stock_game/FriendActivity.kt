package com.guys_from_301.stock_game

import android.content.ContentValues
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

// 친구 정보
var friendid = mutableListOf<String>()
var friendmoney = mutableListOf<Int>()
var friendlevel = mutableListOf<Int>()
var friendnick = mutableListOf<String>()

class FriendActivity  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend)

        TalkApiClient.instance.profile { profile, error ->
            if (error != null) {
                println("카카오톡 실패??"+error)
            }
            else if (profile != null) {
                println("카카오톡 성공"+profile.nickname+" "+profile.countryISO)
                //println("---"+pro)
//                Log.i(TAG, "카카오톡 프로필 가져오기 성공" +
//                        "\n닉네임: ${profile.nickname}" +
//                        "\n프로필사진: ${profile.thumbnailUrl}" +
//                        "\n국가코드: ${profile.countryISO}")
            }
        }
        TalkApiClient.instance.friends { friends, error ->
            if (error != null) {
                println("카카오톡 친구 목록 실패"+error)
            }
            else if (friends != null) {
                val friendscount = friends.totalCount
                var count = 0
                while(count<friendscount){
                    addlayout(friends, count)
                    friendid.add(getHash((friends.elements[count].id).toString()))
                    count++
                }
                println("---몇일까"+friendscount)
                friendInfo(friendid, friendscount)

            }
        }
        findViewById<Button>(R.id.btn_goback).setOnClickListener{
            onBackPressed()
        }

    }

    fun addlayout(friends: Friends<Friend>, count : Int){
        var textView = TextView(this)
        var layout = LinearLayout(this)
        var button_info = Button(this)
        var button_invite = Button(this)
        textView.text = "이름: " + friends.elements[count].profileNickname
        textView.textSize = 30f
        button_info.text = "정보보기"
        button_invite.text = "초대보내기"
        button_invite.setOnClickListener{
            if(friendlevel[count]==-1){
                kakaoMessageManager.sendMessageonlyone(friends.elements[count].uuid)
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

    // 친구 정보 받아오는 코드

    fun friendInfo(u_id: MutableList<String>, u_number :Int) {
        var funfriend: RetrofitFriend? = null
        val url = "http://stockgame.dothome.co.kr/test/friendrank.php/"
        var gson: Gson = GsonBuilder()
                .setLenient()
                .create()
        //creating retrofit object
        var retrofit =
                Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
        //creating our api
        funfriend = retrofit.create(RetrofitFriend::class.java)
        funfriend.funfriend(u_id, u_number).enqueue(object : Callback<MutableList<DATACLASS>> {
            override fun onFailure(call: Call<MutableList<DATACLASS>>, t: Throwable) {
                println("---실패")
                //Toast.makeText(this@InitialActivity, "아이디나 비밀번호가 맞지 않습니다.", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<MutableList<DATACLASS>>, response: retrofit2.Response<MutableList<DATACLASS>>) {
                if (response.isSuccessful && response.body() != null) {
                    val data: MutableList<DATACLASS> = response.body()!!
                    println("---성공")
                    for(i in 0..u_number-1){
                        if(response.body()!![i]==null){
                            println("---그치")
                            friendmoney.add(-1)
                            friendlevel.add(-1)
                            friendnick.add("존재하지 않는 아이디")
                        }
                        else{
                            friendmoney.add(data[i].MONEY)
                            friendlevel.add(data[i].LEVEL)
                            friendnick.add(data[i].NICKNAME)
                        }
                    }
 //                   while(response.body()!![])
                    println("---"+response.body()!!)
//                    println("---돈"+response.body()!![2].MONEY)
//                    println("---성분"+response.body()!![2].LEVEL)
//                    println("---닉넴"+response.body()!![2].NICKNAME)
                }
            }
        })
    }

}



