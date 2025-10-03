package org.example.project

import androidx.lifecycle.ViewModel
import org.example.project.platform.NotificationManager

class AppViewModel(
    private val notificationManager: NotificationManager
): ViewModel() {
    fun showNotification() {
        notificationManager.showNotification("Exp test", "This is a test notification")
    }
}