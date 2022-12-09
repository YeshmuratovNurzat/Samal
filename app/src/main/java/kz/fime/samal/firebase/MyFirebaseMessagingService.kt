package kz.fime.samal.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.se.omapi.Session
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kz.fime.samal.R
import kz.fime.samal.data.SessionManager
import kz.fime.samal.data.repositories.FcmRepository
import kz.fime.samal.ui.MainActivity
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MyFirebaseMessagingService : FirebaseMessagingService(), CoroutineScope {

    val TAG = "MyFirebaseMsgService"

    @Inject
    lateinit var fcmRepository: FcmRepository

    override fun onMessageReceived(message: RemoteMessage) {

        if (SessionManager.token.isNotEmpty()) {

            message.notification?.let {
                Log.d(TAG, "From: " + message.from)
                Log.d(TAG, "Message data payload: " + message.data)

                sendNotification(it.title, it.body)
            }
        }
    }

    private fun sendNotification(title: String?, body: String?) {
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder =
            NotificationCompat.Builder(this, channelId).setSmallIcon(R.drawable.ic_logo_116)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        }

        notificationBuilder
            .setContentText(body)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setSound(defaultSoundUri)

        if (title != null) {
            notificationBuilder.setContentTitle(title)
        }
        if (body != null) {
            notificationBuilder.setContentText(body)
        }


        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val contentIntent = PendingIntent.getActivity(this, 0, intent, 0)


        notificationBuilder.setContentIntent(contentIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                getString(R.string.default_notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        if (SessionManager.token.isNotEmpty()) {
            launch {
                if (SessionManager.getPushToken().isEmpty())
                    fcmRepository.addPushToken(token)
                else fcmRepository.editPushToken(token)
            }
//            val pushTokenBody = PushTokenBody(token, "2")
//            if (prefs.getPushToken().isNotEmpty())
//                repository.updatePushToken(pushTokenBody)
//            else repository.addPushToken(pushTokenBody)
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + SupervisorJob()

}