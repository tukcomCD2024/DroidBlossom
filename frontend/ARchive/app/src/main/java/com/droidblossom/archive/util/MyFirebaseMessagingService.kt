package com.droidblossom.archive.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.droidblossom.archive.R
import com.droidblossom.archive.presentation.snack.CallSnackBar
import com.droidblossom.archive.presentation.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "FirebaseService"

    @Inject
    lateinit var dataStoreUtils: DataStoreUtils

    override fun onNewToken(token: String) {
        Log.d(TAG, "new Token: $token")
    }

    /** 메시지 수신 메서드(포그라운드) */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.from)

        Log.d(TAG, "Message data : ${remoteMessage.data}")
        Log.d(TAG, "Message noti : ${remoteMessage.notification}")

        when (remoteMessage.from) {
            // 스킨 생성 알림 - 서버에서 아직 안나옴 : topic1
            "894030886016" -> {
                handle894030886016Notification(remoteMessage)
            }
            else -> {
                handleDefaultNotification(remoteMessage)
            }
        }
    }

    private fun handle894030886016Notification(remoteMessage: RemoteMessage) {
        sendNotification(remoteMessage, "ARchiveChannelId1", "894030886016")
    }

    private fun handleDefaultNotification(remoteMessage: RemoteMessage) {
        sendNotification(remoteMessage, "ARchiveChannelId", "ARchive")
    }

    private fun createMainActivityIntent(
        data: Map<String, String>,
        destination: FragmentDestination? = null
    ): Intent {
        val intent = Intent(this, MainActivity::class.java)
        data.forEach { (key, value) ->
            intent.putExtra(key, value)
        }
        destination?.let {
            intent.putExtra("fragmentDestination", it.name)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        return intent
    }

    private fun sendNotification(
        remoteMessage: RemoteMessage,
        channelId: String,
        channelName: String
    ) {
        // channel 설정
        val channelDescription = "$channelName FCM 채널"
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }
            notificationManager.createNotificationChannel(channel)
        }

        val uniId: Int = (System.currentTimeMillis() / 7).toInt()

        // 인텐트 설정
        val intent = when (channelId) {
            "894030886016" -> createMainActivityIntent(
                remoteMessage.data,
                FragmentDestination.SKIN_FRAGMENT
            )

            else -> createMainActivityIntent(remoteMessage.data, FragmentDestination.SKIN_FRAGMENT)
        }


        // PendingIntent 설정
        val pendingIntent = PendingIntent.getActivity(
            this,
            uniId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId).apply {
            priority = NotificationCompat.PRIORITY_HIGH // 중요도 (HIGH: 상단바 표시 가능)
            setSmallIcon(R.drawable.app_symbol)
            setContentTitle(remoteMessage.notification?.title ?: "Unknown title") // 제목
            setContentText(remoteMessage.notification?.body ?: "Unknown body")
            setAutoCancel(true) // 알람클릭시 삭제여부
            setSound(soundUri)  // 알림 소리
            setContentIntent(pendingIntent) // 알림 실행 시 Intent
        }

        // Android 최신버전 대응 (Android O 이상)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 전송
        notificationManager.notify(uniId, notificationBuilder.build())

        // 이벤트 버스를 통해 알림을 전달
        EventBus.getDefault()
            .post(CallSnackBar(remoteMessage.notification?.title ?: "Unknown title"))
    }

    suspend fun getFirebaseToken(): String {
        val tokenTask = withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            FirebaseMessaging.getInstance().token.addOnSuccessListener {
                Log.d("FCM", "token=${it}")
            }
        }
        return tokenTask.await()
    }

    enum class FragmentDestination {
        SKIN_FRAGMENT,
    }
}

/**
"notificationBuilder" 알림 생성시 여러가지 옵션을 이용해 커스텀 가능.
setSmallIcon : 작은 아이콘 (필수)
setContentTitle : 제목 (필수)
setContentText : 내용 (필수)
setColor : 알림내 앱 이름 색
setWhen : 받은 시간 커스텀 ( 기본 시스템에서 제공합니다 )
setShowWhen : 알림 수신 시간 ( default 값은 true, false시 숨길 수 있습니다 )
setOnlyAlertOnce : 알림 1회 수신 ( 동일 아이디의 알림을 처음 받았을때만 알린다, 상태바에 알림이 잔존하면 무음 )
setContentTitle : 제목
setContentText : 내용
setFullScreenIntent : 긴급 알림 ( 자세한 설명은 아래에서 설명합니다 )
setTimeoutAfter : 알림 자동 사라지기 ( 지정한 시간 후 수신된 알림이 사라집니다 )
setContentIntent : 알림 클릭시 이벤트 ( 지정하지 않으면 클릭했을때 아무 반응이 없고 setAutoCancel 또한 작동하지 않는다 )
setLargeIcon : 큰 아이콘 ( mipmap 에 있는 아이콘이 아닌 drawable 폴더에 있는 아이콘을 사용해야 합니다. )
setAutoCancel : 알림 클릭시 삭제 여부 ( true = 클릭시 삭제 , false = 클릭시 미삭제 )
setPriority : 알림의 중요도를 설정 ( 중요도에 따라 head up 알림으로 설정할 수 있는데 자세한 내용은 밑에서 설명하겠습니다. )
setVisibility : 잠금 화면내 알림 노출 여부
Notification.VISIBILITY_PRIVATE : 알림의 기본 정보만 노출 (제목, 타이틀 등등)
Notification.VISIBILITY_PUBLIC : 알림의 모든 정보 노출
Notification.VISIBILITY_SECRET : 알림의 모든 정보 비노출
 */