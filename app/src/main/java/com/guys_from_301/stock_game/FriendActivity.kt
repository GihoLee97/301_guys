package com.guys_from_301.stock_game

import com.kakao.sdk.talk.TalkApiClient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.guys_from_301.stock_game.retrofit.FRIENDCLASS
import com.guys_from_301.stock_game.retrofit.RetrofitFriend
import com.guys_from_301.stock_game.retrofit.RetrofitRanking
import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.talk.model.Friends
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 친구 정보

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
                    count ++
                }
//                findViewById<LinearLayout>(R.id.layout_admin).addView(textView)

//                findViewById<LinearLayout>(R.id.layout_user1)

                //myLinearLayout.addView(textView)

//                while(count < friendscount){
//                    findViewById<LinearLayout>(2131231057+count).visibility = View.VISIBLE
//                    findViewById<TextView>(2131231371+3*count).text = friends.elements[count].profileNickname
//                    count += 1
//                }

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
//        println("---2"+friends.elements[count].profileNickname)
        layout.addView(textView)
        findViewById<LinearLayout>(R.id.layout_admin).addView(layout)
    }

    // 친구 정보 받아오는 코드

    fun friendInfo(u_id: String) {
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
        funfriend.funfriend(u_id).enqueue(object : Callback<FRIENDCLASS> {
            override fun onFailure(call: Call<FRIENDCLASS>, t: Throwable) {
                //Toast.makeText(this@InitialActivity, "아이디나 비밀번호가 맞지 않습니다.", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<FRIENDCLASS>, response: retrofit2.Response<FRIENDCLASS>) {
                if (response.isSuccessful && response.body() != null) {
                    var data: FRIENDCLASS = response.body()!!

                }
            }
        })
    }
}



