package com.guys_from_301.stock_game

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class PushAlarmManager(activity: Activity) {
    private var mActivity = activity
    private lateinit var builder: NotificationCompat.Builder

    // push alarm 생성
    fun generateAlarm() {
        builder = NotificationCompat.Builder(mActivity, CHANNEL_ID)
                .setSmallIcon(R.drawable.splash)
                .setContentTitle("타디스에서 알립니다~")
                .setContentText("플레이한지 너무 오랜 시간이 지났습니다. 다시 들어와서 플레이하세요~")
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText("플레이한지 너무 오랜 시간이 지났습니다. 다시 들어와서 플레이하세요~..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

    // push alarm 띄우기
    fun push() {
        Log.d("Giho", "pushAlarm push success")
        NotificationManagerCompat.from(mActivity).notify(1, builder.build())
    }

    fun openSetting() {
        // 알람 채널 설정 열기
        val myNotificationChannel = notificationManager.getNotificationChannel(CHANNEL_ID)
        val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, "com.guys_from_301.stock_game")
            putExtra(Settings.EXTRA_CHANNEL_ID, myNotificationChannel.getId())
        }
        mActivity.startActivity(intent)
    }
}