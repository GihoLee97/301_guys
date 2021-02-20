package com.guys_from_301.stock_game

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity

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

//    fun shareBinary(activity: Activity, uriToImage: String){
//        val shareIntent: Intent = Intent().apply {
//            action = Intent.ACTION_SEND
//            putExtra(Intent.EXTRA_STREAM, uriToImage)
//            type = "image/jpeg"
//        }
//        activity.startActivity(Intent.createChooser(shareIntent, resources.getText(R.string.send_to)))
//    }
}