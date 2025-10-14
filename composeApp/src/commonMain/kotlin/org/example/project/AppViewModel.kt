package org.example.project

import androidx.lifecycle.ViewModel
import org.example.project.platform.NotificationManager
import org.example.project.platform.NotificationVariant

class AppViewModel(
    private val notificationManager: NotificationManager
): ViewModel() {
    fun showNotification() { // kept for backward compatibility
        notificationManager.showNotification("Exp test", "This is a test notification")
    }

    fun showBasic() = notificationManager.showNotification("Basic", "High importance with sound & vibration", NotificationVariant.Basic)

    fun showSilent() = notificationManager.showNotification("Silent", "Low importance without sound", NotificationVariant.Silent)

    fun showBigText() = notificationManager.showNotification("Big Text", "This demo shows an expanded message.", NotificationVariant.BigText)

    fun showProgress() = notificationManager.showNotification("Progress", "Working...", NotificationVariant.Progress)

    fun showAction() = notificationManager.showNotification("Action", "Tap action to open app", NotificationVariant.Action)

    fun showScheduled() = notificationManager.showNotification("Scheduled", "This will appear in 5 seconds", NotificationVariant.Scheduled)

    fun showImageRich() = notificationManager.showNotification("Image", "With big picture and large icon", NotificationVariant.ImageRich)

    fun showTemplate() = notificationManager.showNotification("Template", "Structured items below", NotificationVariant.Template)

    fun showMediaPlayer() = notificationManager.showNotification("Now Playing", "Artist • Track", NotificationVariant.MediaPlayer)
}