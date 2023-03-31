package com.leebeebeom.clothinghelper.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {
    @Singleton
    @Provides
    @AppScope
    fun appScope() = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @Provides
    @DispatcherIO
    fun dispatcherIO() = Dispatchers.IO

    @Provides
    @DispatcherDefault
    fun dispatcherDefault() = Dispatchers.Default
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AppScope

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DispatcherIO

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DispatcherDefault