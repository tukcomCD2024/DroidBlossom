package com.droidblossom.archive.di

import com.droidblossom.archive.data.repository.AuthRepositoryImpl
import com.droidblossom.archive.data.repository.SMSMessageRepositoryImpl
import com.droidblossom.archive.data.source.remote.api.AuthService
import com.droidblossom.archive.data.source.remote.api.SMSMessageService
import com.droidblossom.archive.domain.repository.AuthRepository
import com.droidblossom.archive.domain.repository.SMSMessageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun providesSMSMessageRepository(
        smsMessageService: SMSMessageService
    ) : SMSMessageRepository = SMSMessageRepositoryImpl(smsMessageService)


    @Provides
    @Singleton
    fun providerAuthRepository(api: AuthService): AuthRepository =
        AuthRepositoryImpl(api)
}