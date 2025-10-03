package org.example.project.di

import org.example.project.AppViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModules : Module

val sharedModules : Module = module{
    viewModelOf(::AppViewModel)
}