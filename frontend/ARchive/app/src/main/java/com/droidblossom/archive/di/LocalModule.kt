package com.droidblossom.archive.di

import android.content.Context
import com.droidblossom.archive.data.repository.MemberRepositoryImpl
import com.droidblossom.archive.domain.repository.MemberRepository
import com.droidblossom.archive.domain.usecase.member.FcmTokenUseCase
import com.droidblossom.archive.util.DataStoreUtils
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
    fun providesDataStoreUtils(@ApplicationContext context: Context) : DataStoreUtils{
        return DataStoreUtils(context)
    }

    @Provides
    @Singleton
    fun providesFcmUseCase(repository: MemberRepositoryImpl) : FcmTokenUseCase{
        return FcmTokenUseCase(repository)
    }
}