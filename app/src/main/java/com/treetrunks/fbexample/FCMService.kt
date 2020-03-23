package com.treetrunks.fbexample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {
    private val LOG_TAG = javaClass.simpleName

    override fun onNewToken(token: String) {
        /* 토큰 새로 생성시 호출되는 메소드
         토큰은 주로 아래 상황에서 자동으로 생성됨
          - 앱 설치후 최초실행될때 (삭제후 재설치하여 실행했을때도 포함)
          - 앱데이터를 삭제후 다시 실행할때
         */
        super.onNewToken(token)

        Log.d(LOG_TAG, "got a new token: $token")

        // register token to app server
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        Log.d(LOG_TAG, "send token to app server.....")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d(LOG_TAG, "remote message received.......................")

        Log.d(LOG_TAG, "Message from : ${remoteMessage.from}")

        if (remoteMessage.data.isNotEmpty()) {
            Log.d(LOG_TAG, "Message data payload: ${remoteMessage.data}")

            if (false) {
                // 받은 데이터를 처리하는데 긴 시간이 걸릴경우 아래 메소드로 처리
                scheduleJob()
            } else {
                handleNow(remoteMessage.data)
            }
        }

        if (remoteMessage.notification != null) {

            val body = remoteMessage.notification?.body.orEmpty()

            Log.d(LOG_TAG, "Message notification body: $body")
            sendNotification(body)
        }

    }

    private fun scheduleJob() {
        /*
         시간이 오래걸리는 작업을 처리하기위한 메소드.
         WorkManager를 이용하여 처리를 권장
         */
        Log.d(LOG_TAG, "do some scheduleJob.....")
    }

    private fun handleNow(data: Map<String, String>) {
        val message = data.get("message") ?:"Default body"
        sendNotification(message)
    }

    //android p 이상에서 isBackgroundRestricted() 이용해서 앱에 백그라운드 제한이 적용되었는지 확인하기
    private fun sendNotification(body: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this,
            0, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager
            = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()

        Log.d(LOG_TAG, "user deleted messaged....")
    }
}