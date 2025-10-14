package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import exp.composeapp.generated.resources.Res
import exp.composeapp.generated.resources.compose_multiplatform
import org.example.project.platform.NotificationManager
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(appViewModel: AppViewModel = koinViewModel()) {
    MaterialTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = { appViewModel.showBasic() }) { Text("Basic") }
            Button(onClick = { appViewModel.showSilent() }) { Text("Silent") }
            Button(onClick = { appViewModel.showBigText() }) { Text("Big Text") }
            Button(onClick = { appViewModel.showProgress() }) { Text("Progress") }
            Button(onClick = { appViewModel.showAction() }) { Text("Action") }
            Button(onClick = { appViewModel.showScheduled() }) { Text("Scheduled (5s)") }
            Button(onClick = { appViewModel.showImageRich() }) { Text("Image Rich") }
            Button(onClick = { appViewModel.showTemplate() }) { Text("Template") }
            Button(onClick = { appViewModel.showMediaPlayer() }) { Text("Media Player") }
        }
    }
}