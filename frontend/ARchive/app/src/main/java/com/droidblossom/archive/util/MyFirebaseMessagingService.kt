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
import com.droidblossom.archive.presentation.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "FirebaseService"

    override fun onNewToken(token: String) {
        Log.d(TAG, "new Token: $token")
    }

    /** 메시지 수신 메서드(포그라운드) */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.from)

        //받은 remoteMessage의 값 출력해보기. 데이터메세지 / 알림메세지
        Log.d(TAG, "Message data : ${remoteMessage.data}")
        Log.d(TAG, "Message noti : ${remoteMessage.notification}")

        if (remoteMessage.data.isNotEmpty()) {
            sendNotification(remoteMessage)
        } else {
            Log.e(TAG, "data가 비어있습니다. 메시지를 수신하지 못했습니다.")
        }
    }

    /** 알림 생성 메서드 */
    private fun sendNotification(remoteMessage: RemoteMessage) {

        //channel 설정
        val channelId = "ARchiveChannelId"
        val channelName = "ARchive"
        val channelDescription = "ARchive FCM 채널"
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

        // RequestCode, Id를 고유값으로 지정하여 알림이 개별 표시
        val uniId: Int = (System.currentTimeMillis() / 7).toInt()

        // 일회용 PendingIntent : Intent 의 실행 권한을 외부의 어플리케이션에게 위임
        val intent = Intent(this, MainActivity::class.java)

        //각 key, value 추가
        for (key in remoteMessage.data.keys) {
            intent.putExtra(key, remoteMessage.data.getValue(key))
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Activity Stack 을 경로만 남김(A-B-C-D-B => A-B)

        //23.05.22 Android 최신버전 대응 (FLAG_MUTABLE, FLAG_IMMUTABLE)
        //PendingIntent.FLAG_MUTABLE은 PendingIntent의 내용을 변경할 수 있도록 허용, PendingIntent.FLAG_IMMUTABLE은 PendingIntent의 내용을 변경할 수 없음
        //val pendingIntent = PendingIntent.getActivity(this, uniId, intent, PendingIntent.FLAG_ONE_SHOT)
        val pendingIntent = PendingIntent.getActivity(
            this,
            uniId,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE
        )

        // 서버에서 보낸 아이콘 이름 추출
        val iconName = remoteMessage.data["icon"] // 서버에서 "icon" 키로 아이콘 이름을 보냈다고 가정
        // 리소스 ID 찾기
        val iconResId = resources.getIdentifier(iconName, "drawable", packageName)

        // 알림에 대한 UI 정보, 작업
        val notificationBuilder = NotificationCompat.Builder(this, channelId).apply {
            setPriority(NotificationCompat.PRIORITY_HIGH) // 중요도 (HIGH: 상단바 표시 가능)
            if (iconResId != 0) {
                setSmallIcon(iconResId) // 서버에서 받은 아이콘 이름으로 리소스 ID 찾아 설정
            } else {
                setSmallIcon(R.drawable.app_symbol) // 기본 아이콘
            }
            setContentTitle(remoteMessage.data["title"].toString()) // 제목
            setContentText(remoteMessage.data["body"].toString()) // 메시지 내용
            setAutoCancel(true) // 알람클릭시 삭제여부
            setSound(soundUri)  // 알림 소리
            setContentIntent(pendingIntent) // 알림 실행 시 Intent
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "Notice", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(uniId, notificationBuilder.build())
    }

    suspend fun getFirebaseToken(): String {
        val tokenTask = withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                FirebaseMessaging.getInstance().token.addOnSuccessListener {
                    Log.d("FCM", "token=${it}")
                }
            }
        return tokenTask.await()
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