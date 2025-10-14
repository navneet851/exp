package org.example.project.platform

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.example.project.MainActivity
import org.example.project.R

actual class NotificationManager(
    private val context: Context
) {
    private val notificationManager get() = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @SuppressLint("MissingPermission")
    actual fun showNotification(
        title: String,
        description: String
    ) {
        showNotification(title, description, NotificationVariant.Basic)
    }

    @SuppressLint("MissingPermission")
    actual fun showNotification(
        title: String,
        description: String,
        variant: NotificationVariant
    ) {
        when (variant) {
            NotificationVariant.Basic -> {
                createAlertChannel()
                val builder = baseBuilder(ALERTS_CHANNEL_ID, title, description)
                notify(builder)
            }
            NotificationVariant.Silent -> {
                createSilentChannel()
                val builder = baseBuilder(SILENT_CHANNEL_ID, title, description)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                notify(builder, id = 2)
            }
            NotificationVariant.BigText -> {
                createAlertChannel()
                val builder = baseBuilder(ALERTS_CHANNEL_ID, title, description)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(description + "\n\nThis is a longer content (BigText)."))
                notify(builder, id = 3)
            }
            NotificationVariant.Progress -> {
                createAlertChannel()
                val builder = baseBuilder(ALERTS_CHANNEL_ID, title, "Downloading...")
                    .setOngoing(true)
                    .setProgress(0, 0, true)
                notify(builder, id = 4)
            }
            NotificationVariant.Action -> {
                createAlertChannel()
                val intent = Intent(context, MainActivity::class.java)
                val flags = if (Build.VERSION.SDK_INT >= 23) PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT
                val pendingIntent = PendingIntent.getActivity(context, 100, intent, flags)
                val builder = baseBuilder(ALERTS_CHANNEL_ID, title, description)
                    .addAction(R.drawable.ic_launcher_foreground, "Open", pendingIntent)
                    .setContentIntent(pendingIntent)
                notify(builder, id = 5)
            }
            NotificationVariant.Scheduled -> {
                createAlertChannel()
                val builder = baseBuilder(ALERTS_CHANNEL_ID, title, description)
                Handler(Looper.getMainLooper()).postDelayed({ notify(builder, id = 6) }, 5000)
            }
            NotificationVariant.ImageRich -> {
                createAlertChannel()
                val large = BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_background)
                val builder = baseBuilder(ALERTS_CHANNEL_ID, title, description)
                    .setLargeIcon(large)
                    .setStyle(
                        NotificationCompat.BigPictureStyle()
                            .bigPicture(large)
                            .bigLargeIcon(null as android.graphics.Bitmap?)
                            .setSummaryText("Rich image preview")
                    )
                notify(builder, id = 7)
            }
            NotificationVariant.Template -> {
                createAlertChannel()
                val style = NotificationCompat.InboxStyle()
                    .addLine("Line 1: Item A")
                    .addLine("Line 2: Item B")
                    .addLine("Line 3: Item C")
                    .setSummaryText("3 new items")
                val builder = baseBuilder(ALERTS_CHANNEL_ID, title, description)
                    .setSubText("Template")
                    .setStyle(style)
                notify(builder, id = 8)
            }
            NotificationVariant.MediaPlayer -> {
                createAlertChannel()
                val intent = Intent(context, MainActivity::class.java)
                val flags = if (Build.VERSION.SDK_INT >= 23) PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT
                val piPlay = PendingIntent.getActivity(context, 201, intent, flags)
                val piPause = PendingIntent.getActivity(context, 202, intent, flags)
                val piNext = PendingIntent.getActivity(context, 203, intent, flags)
                val builder = baseBuilder(ALERTS_CHANNEL_ID, title, description)
                    .setOngoing(true)
                    .setCategory(android.app.Notification.CATEGORY_TRANSPORT)
                    .addAction(R.drawable.ic_launcher_foreground, "Play", piPlay)
                    .addAction(R.drawable.ic_launcher_foreground, "Pause", piPause)
                    .addAction(R.drawable.ic_launcher_foreground, "Next", piNext)
                notify(builder, id = 9)
            }
        }
    }

    private fun baseBuilder(channelId: String, title: String, description: String): NotificationCompat.Builder =
        NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(0) // channel controls sound/vibration on 8.0+

    private fun notify(builder: NotificationCompat.Builder, id: Int = 1) {
        if (areNotificationEnabled) {
            try {
                NotificationManagerCompat.from(context).notify(id, builder.build())
            } catch (se: SecurityException) {
                // Ignore if POST_NOTIFICATIONS not granted
            }
        }
    }

    private val areNotificationEnabled get() = NotificationManagerCompat
        .from(context)
        .areNotificationsEnabled()

    private fun createAlertChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ALERTS_CHANNEL_ID,
                ALERTS_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = ALERTS_CHANNEL_DESCRIPTION
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 250, 150, 250)

                val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
                setSound(soundUri, audioAttributes)
                enableLights(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createSilentChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                SILENT_CHANNEL_ID,
                SILENT_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = SILENT_CHANNEL_DESCRIPTION
                enableVibration(false)
                setSound(null, null)
                enableLights(false)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val ALERTS_CHANNEL_DESCRIPTION = "High-importance notifications with sound and vibration"
        private const val ALERTS_CHANNEL_NAME = "Alerts"
        private const val ALERTS_CHANNEL_ID = "alerts_channel"

        private const val SILENT_CHANNEL_DESCRIPTION = "Low-importance notifications without sound"
        private const val SILENT_CHANNEL_NAME = "Silent"
        private const val SILENT_CHANNEL_ID = "silent_channel"
    }
}