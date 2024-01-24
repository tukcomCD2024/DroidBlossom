package com.droidblossom.archive.di

import android.content.Context
import com.droidblossom.archive.util.SharedPreferencesUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun providesTokenUtils(@ApplicationContext context: Context) : SharedPreferencesUtils{
        return SharedPreferencesUtils(context)
    }
}