package com.guys_from_301.stock_game

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.template.model.*

class KakaoMessageManager() {

    val defaultFeed = FeedTemplate(
            content = Content(
                    title = "실전형 모의 주식게임 \"타디스\"",
                    description = "#S&P500 #주식앱 #주식공부 #동학개미",
                    imageUrl = "http://mud-kage.kakao.co.kr/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png",
                    link = Link(
                            webUrl = "https://developers.kakao.com",
                            mobileWebUrl = "https://developers.kakao.com"
                    )
            ),
            social = Social(
                    likeCount = 286,
                    commentCount = 45,
                    sharedCount = 845
            ),
            buttons = listOf(
                    Button(
                            "앱에서 보기",
                            Link(
                                    webUrl = "https://developers.kakao.com",
                                    mobileWebUrl = "https://developers.kakao.com"
                            )
                    )
            )
    )

    fun sendMessageMyself() {
        TalkApiClient.instance.sendDefaultMemo(defaultFeed) { error ->
            if (error != null) {
                Log.e(TAG, "나에게 보내기 실패", error)
            } else {
                Log.i(TAG, "나에게 보내기 성공")
            }
        }
    }

    fun sendMessage() {
        // 카카오톡 친구 목록 가져오기
        TalkApiClient.instance.friends { friends, error ->
            if (error != null) {
                Log.e(TAG, "카카오톡 친구 목록 가져오기 실패", error)
            } else {
                Log.d(TAG, "카카오톡 친구 목록 가져오기 성공 \n${friends!!.elements.joinToString("\n")}")

                if (friends.elements.isEmpty()) {
                    Log.e(TAG, "메시지 보낼 친구가 하나도 없어요 ㅠㅠ")
                } else {
                    Log.d("Giho", ("카카오톡 친구 수 : " + friends.elements.size.toString()))
                    var receiverUuids = List(friends.elements.size, { i -> friends.elements[i].uuid })
                    // Feed 메시지
                    val template = defaultFeed
                    // 메시지 보내기
                    TalkApiClient.instance.sendDefaultMessage(receiverUuids, template) { result, error ->
                        if (error != null) {
                            Log.e(TAG, "메시지 보내기 실패", error)
                        } else if (result != null) {
                            Log.i(TAG, "메시지 보내기 성공 ${result.successfulReceiverUuids}")
                            if (result.failureInfos != null) {
                                Log.d(TAG, "메시지 보내기에 일부 성공했으나, 일부 대상에게는 실패 \n${result.failureInfos}")
                            }
                        }
                    }
                }
            }
        }
    }
}