package org.example.project.di

import org.example.project.platform.NotificationManager
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModules: Module = module {
    single { NotificationManager() }
}