package com.guys_from_301.stock_game

import com.kakao.sdk.talk.TalkApiClient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView


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
                    var textView = TextView(this)
                    var layout = LinearLayout(this)
                    textView.text = friends.elements[count].profileNickname
                    println("---2"+friends.elements[count].profileNickname)
                    layout.addView(textView)
                    findViewById<LinearLayout>(R.id.layout_admin).addView(layout)
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

}



