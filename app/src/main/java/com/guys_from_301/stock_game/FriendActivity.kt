package com.guys_from_301.stock_game

import com.kakao.sdk.talk.TalkApiClient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
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

// 친구 정보

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

                    friendInfo(getHash(friends.elements[count].uuid).toString().trim(), count)
                    count ++
                }


                println("---"+friends)

                println("---" +friends.elements[0].profileNickname)
                println("---" +friends.elements[1].profileNickname)
                // 친구의 UUID 로 메시지 보내기 가능
            }
        }
        findViewById<Button>(R.id.btn_goback).setOnClickListener{
            onBackPressed()
        }

    }

    fun addlayout(friends: Friends<Friend>, count : Int){
        var textView = TextView(this)
        var layout = LinearLayout(this)
        textView.text = friends.elements[count].profileNickname
        textView.textSize = 30f
        layout.gravity = Gravity.CENTER
        layout.setOnClickListener{
            //TODO: 친구 정보들 띄우자
//            val dialog = Dialog_friend(this, count)
//            dialog.start(count)
            println("---"+friendnick[0])
        }
        layout.addView(textView)
        findViewById<LinearLayout>(R.id.layout_admin).addView(layout)
    }

    // 친구 정보 받아오는 코드

    fun friendInfo(u_id: String, count: Int) {
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
        funfriend.funfriend(u_id).enqueue(object : Callback<DATACLASS> {
            override fun onFailure(call: Call<DATACLASS>, t: Throwable) {
                println("---실패")
                //Toast.makeText(this@InitialActivity, "아이디나 비밀번호가 맞지 않습니다.", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<DATACLASS>, response: retrofit2.Response<DATACLASS>) {
                println("---머지")
                if (response.isSuccessful && response.body() != null) {
                    println("---성공")
                    var data: DATACLASS = response.body()!!
                    friendmoney[count] = data?.MONEY
                    friendlevel[count] = data?.LEVEL
                    friendnick[count] = data?.NICKNAME
                }
            }
        })
    }
}



