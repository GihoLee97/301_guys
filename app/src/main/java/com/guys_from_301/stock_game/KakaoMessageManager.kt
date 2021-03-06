package com.guys_from_301.stock_game

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.graphics.drawable.toBitmap
import com.kakao.sdk.link.LinkClient
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.template.model.*
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class KakaoMessageManager() {
    private val CAPTURE_PATH = "/Tardis"
    private val strFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath+CAPTURE_PATH

    fun sendMessageMyself(massage : FeedTemplate) {
        TalkApiClient.instance.sendDefaultMemo(massage) { error ->
            if (error != null) {
                Log.e(TAG, "나에게 보내기 실패", error)
            } else {
                Log.i(TAG, "나에게 보내기 성공")
            }
        }
    }

    fun sendMessageToAll(massage : FeedTemplate) {
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
                    val template = massage
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

    // 한명에게만 초대 메세지 보내기

    fun sendMessageonlyone(uuid: String, massage : FeedTemplate) {
        // 카카오톡 친구 목록 가져오기
        TalkApiClient.instance.friends { friends, error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "카카오톡 친구 목록 가져오기 실패", error)
            } else {
                Log.d(ContentValues.TAG, "카카오톡 친구 목록 가져오기 성공 \n${friends!!.elements.joinToString("\n")}")
                Log.d("Giho", ("카카오톡 친구 수 : " + friends.elements.size.toString()))
                var receiverUuids = List(1, { i ->uuid })
                // Feed 메시지
                val template = massage
                // 메시지 보내기
                TalkApiClient.instance.sendDefaultMessage(receiverUuids, template) { result, error ->
                    if (error != null) {
                        Log.e(ContentValues.TAG, "메시지 보내기 실패", error)
                    } else if (result != null) {
                        Log.i(ContentValues.TAG, "메시지 보내기 성공 ${result.successfulReceiverUuids}")
                        if (result.failureInfos != null) {
                            Log.d(ContentValues.TAG, "메시지 보내기에 일부 성공했으나, 일부 대상에게는 실패 \n${result.failureInfos}")
                        }
                    }
                }
            }
        }
    }

    fun fromStorageMakeMassageSendToAll(path : String){
       val file = File(path)

        // 카카오 이미지 서버로 업로드
        LinkClient.instance.uploadImage(file) { imageUploadResult, error ->
            if (error != null) {
                Log.e(TAG, "이미지 업로드 실패", error)
            }
            else if (imageUploadResult != null) {
                Log.i(TAG, "이미지 업로드 성공 \n${imageUploadResult.infos.original}")
                var massage = makeMassage(imageUploadResult.infos.original.url)
                sendMessageToAll(massage)
            }
        }
    }

    fun fromStorageMakeMassageSendToOne(uuid: String ,path : String){
        val file = File(path)

        // 카카오 이미지 서버로 업로드
        LinkClient.instance.uploadImage(file) { imageUploadResult, error ->
            if (error != null) {
                Log.e(TAG, "이미지 업로드 실패", error)
            }
            else if (imageUploadResult != null) {
                Log.i(TAG, "이미지 업로드 성공 \n${imageUploadResult.infos.original}")

                var massage = makeMassage(imageUploadResult.infos.original.url)
                sendMessageonlyone(uuid, massage)
            }
        }
    }

    fun fromStorageMakeMassageSendToMyself(path : String){
        val file = File(path)

        // 카카오 이미지 서버로 업로드
        LinkClient.instance.uploadImage(file) { imageUploadResult, error ->
            if (error != null) {
                Log.e(TAG, "이미지 업로드 실패", error)
            }
            else if (imageUploadResult != null) {
                Log.i(TAG, "이미지 업로드 성공 \n${imageUploadResult.infos.original}")
                var massage = makeMassage(imageUploadResult.infos.original.url)
                sendMessageMyself(massage)
            }
        }
    }

    fun fromResourceMakeMassageNSendToAll(resources: Resources, id : Int, context: Context){
        val path = File(strFolderPath)
        if(!path.exists()){
            path.mkdirs()
            Log.d("Giho","Folder created")
        }
        // 로컬 이미지 파일
        // 이 샘플에서는 프로젝트 리소스로 추가한 이미지 파일을 사용했습니다. 갤러리 등 서비스 니즈에 맞는 사진 파일을 준비하세요.
        val bitmap = getDrawable(context,id)?.toBitmap()
        val file = File(path, "sample1.png")
        val stream = FileOutputStream(file)
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()

        // 카카오 이미지 서버로 업로드
        LinkClient.instance.uploadImage(file) { imageUploadResult, error ->
            if (error != null) {
                Log.e(TAG, "이미지 업로드 실패", error)
            }
            else if (imageUploadResult != null) {
                Log.i(TAG, "이미지 업로드 성공 \n${imageUploadResult.infos.original}")
                var massage = makeMassage(imageUploadResult.infos.original.url)
                sendMessageToAll(massage)
            }
        }
    }

    fun fromResourceMakeMassageNSendToOne(uuid: String ,resources: Resources, id : Int, context: Context){
        val path = File(strFolderPath)
        if(!path.exists()){
            path.mkdirs()
            Log.d("Giho","Folder created")
        }
        // 로컬 이미지 파일
        // 이 샘플에서는 프로젝트 리소스로 추가한 이미지 파일을 사용했습니다. 갤러리 등 서비스 니즈에 맞는 사진 파일을 준비하세요.
        val bitmap = getDrawable(context,id)?.toBitmap()
        val file = File(path, "sample1.png")
        val stream = FileOutputStream(file)
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()

        // 카카오 이미지 서버로 업로드
        LinkClient.instance.uploadImage(file) { imageUploadResult, error ->
            if (error != null) {
                Log.e(TAG, "이미지 업로드 실패", error)
            }
            else if (imageUploadResult != null) {
                Log.i(TAG, "이미지 업로드 성공 \n${imageUploadResult.infos.original}")
                var massage = makeMassage(imageUploadResult.infos.original.url)
                sendMessageonlyone(uuid, massage)
            }
        }
    }

    fun fromResourceMakeMassageNSendToMyself(resources: Resources, id : Int, context: Context){
        val path = File(strFolderPath)
        if(!path.exists()){
            path.mkdirs()
            Log.d("Giho","Folder created")
        }
        // 로컬 이미지 파일
        // 이 샘플에서는 프로젝트 리소스로 추가한 이미지 파일을 사용했습니다. 갤러리 등 서비스 니즈에 맞는 사진 파일을 준비하세요.
        val bitmap = getDrawable(context,id)?.toBitmap()
        val file = File(path, "sample1.png")
        val stream = FileOutputStream(file)
        Log.d("Giho","check1")
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()

        // 카카오 이미지 서버로 업로드
        LinkClient.instance.uploadImage(file) { imageUploadResult, error ->
            if (error != null) {
                Log.e(TAG, "이미지 업로드 실패", error)
            }
            else if (imageUploadResult != null) {
                Log.i(TAG, "이미지 업로드 성공 \n${imageUploadResult.infos.original}")
                var massage = makeMassage(imageUploadResult.infos.original.url)
                sendMessageMyself(massage)
            }
        }
    }

    private fun makeMassage(imageUrl : String) : FeedTemplate{
        val massage = FeedTemplate(
                content = Content(
                        title = "실전형 모의 주식게임 \"타디스\"",
                        description = "#S&P500 #주식앱 #주식공부 #동학개미",
                        imageUrl = imageUrl,
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
        return massage
    }

    val dummy = FeedTemplate(
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


}