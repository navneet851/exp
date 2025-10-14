package org.example.project.platform

import platform.Foundation.NSUUID
import platform.UserNotifications.*
import platform.darwin.NSObject

actual class NotificationManager {
    actual fun showNotification(
        title: String,
        description: String
    ) {
        showNotification(title, description, NotificationVariant.Basic)
    }

    actual fun showNotification(
        title: String,
        description: String,
        variant: NotificationVariant
    ) {
        val content = UNMutableNotificationContent().apply {
            setTitle(title)
            setBody(description)
        }

        val center = UNUserNotificationCenter.currentNotificationCenter()
        center.delegate = object : NSObject(), UNUserNotificationCenterDelegateProtocol {
            override fun userNotificationCenter(
                center: UNUserNotificationCenter,
                willPresentNotification: UNNotification,
                withCompletionHandler: (UNNotificationPresentationOptions) -> Unit
            ) {
                // Present alert and sound in foreground when sound is set
                withCompletionHandler(UNNotificationPresentationOptionAlert or UNNotificationPresentationOptionSound)
            }

            override fun userNotificationCenter(
                center: UNUserNotificationCenter,
                didReceiveNotificationResponse: UNNotificationResponse,
                withCompletionHandler: () -> Unit
            ) { withCompletionHandler() }
        }

        var trigger: UNNotificationTrigger? = null

        when (variant) {
            NotificationVariant.Basic -> {
                content.setSound(UNNotificationSound.defaultSound)
            }
            NotificationVariant.Silent -> {
                content.setSound(null)
            }
            NotificationVariant.BigText -> {
                // iOS doesn't have BigText; use basic
                content.setSound(UNNotificationSound.defaultSound)
            }
            NotificationVariant.Progress -> {
                // No native progress in local notification; map to basic
                content.setSound(UNNotificationSound.defaultSound)
            }
            NotificationVariant.Action -> {
                val action = UNNotificationAction.actionWithIdentifier("OPEN_ACTION", "Open", UNNotificationActionOptionForeground)
                val categoryId = "ACTION_CATEGORY"
                val category = UNNotificationCategory.categoryWithIdentifier(categoryId, listOf(action), listOf<String>(), 0u)
                center.setNotificationCategories(setOf(category))
                content.setCategoryIdentifier(categoryId)
                content.setSound(UNNotificationSound.defaultSound)
            }
            NotificationVariant.Scheduled -> {
                content.setSound(UNNotificationSound.defaultSound)
                trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(5.0, false)
            }
            NotificationVariant.ImageRich -> {
                // Attachments require file URLs in the bundle. Mapping to basic due to resource constraints.
                content.setSound(UNNotificationSound.defaultSound)
                content.setSubtitle("Image preview available")
            }
            NotificationVariant.Template -> {
                // Use subtitle and thread grouping to emulate a templated structure
                content.setSound(UNNotificationSound.defaultSound)
                content.setSubtitle("Template • 3 new items")
                content.setThreadIdentifier("TEMPLATE_THREAD")
                content.setSummaryArgument("3 items")
            }
            NotificationVariant.MediaPlayer -> {
                val play = UNNotificationAction.actionWithIdentifier("PLAY_ACTION", "Play", 0u)
                val pause = UNNotificationAction.actionWithIdentifier("PAUSE_ACTION", "Pause", 0u)
                val next = UNNotificationAction.actionWithIdentifier("NEXT_ACTION", "Next", 0u)
                val categoryId = "MEDIA_CATEGORY"
                val category = UNNotificationCategory.categoryWithIdentifier(categoryId, listOf(play, pause, next), listOf<String>(), 0u)
                center.setNotificationCategories(setOf(category))
                content.setCategoryIdentifier(categoryId)
                content.setSound(UNNotificationSound.defaultSound)
                content.setSubtitle("Now Playing • Fancy Design")
            }
        }

        val uuid = NSUUID.UUID().UUIDString()
        val request = UNNotificationRequest.requestWithIdentifier(uuid, content, trigger)

        center.addNotificationRequest(request) { error ->
            if (error != null) println("Error -> $error") else println("Notification sent")
        }
    }
}