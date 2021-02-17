package com.guys_from_301.stock_game

import com.kakao.sdk.talk.TalkApiClient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


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
                println("카카오톡 친구 목록 성공\n" +"${friends.elements.joinToString("\n")}")
                // 친구의 UUID 로 메시지 보내기 가능
            }
        }


    }

}



