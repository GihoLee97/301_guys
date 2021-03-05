package com.guys_from_301.stock_game

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.core.net.toUri
import java.io.File

class ShareManager {

    fun shareText(activity: Activity, messageToSend: String){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, messageToSend)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        activity.startActivity(shareIntent)
    }

    fun shareBinaryWithOthers(activity: Activity, uriToImage: String){
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uriToImage)
            Log.d("Giho","shareBinary : "+uriToImage)
            type = "image/*"
            //// 카카오로 바로 공유시 선택
            //setPackage("com.kakao.talk")
        }
        activity.startActivity(Intent.createChooser(shareIntent, "shareBinaryTest"))
    }

    // only for kakao
    fun shareBinaryWithKakao(activity: Activity, pathTofile: String){
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, pathTofile.toUri())
            Log.d("Giho","shareBinary : "+pathTofile)
            type = "image/*"
            // 카카오로 바로 공유시 선택
            setPackage("com.kakao.talk")
        }
        activity.startActivity(Intent.createChooser(shareIntent, "shareBinaryTest"))
    }
}