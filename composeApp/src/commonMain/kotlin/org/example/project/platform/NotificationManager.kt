package org.example.project.platform

expect class NotificationManager {
    fun showNotification(
        title: String,
        description: String
    )
}