package com.example.themechangevideo

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { DataStore(androidContext()) }
    viewModelOf(::ExampleViewModel)
}