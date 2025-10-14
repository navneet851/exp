package org.example.project.platform

enum class NotificationVariant {
    Basic,
    Silent,
    BigText,
    Progress,
    Action,
    Scheduled,
    ImageRich,
    Template,
    MediaPlayer
}

expect class NotificationManager {
    fun showNotification(
        title: String,
        description: String
    )

    fun showNotification(
        title: String,
        description: String,
        variant: NotificationVariant
    )
}