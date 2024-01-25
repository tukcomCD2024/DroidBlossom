package com.droidblossom.archive.di

import com.droidblossom.archive.data.repository.AuthRepositoryImpl
import com.droidblossom.archive.data.repository.MemberRepositoryImpl
import com.droidblossom.archive.data.source.remote.api.AuthService
import com.droidblossom.archive.data.source.remote.api.MemberService
import com.droidblossom.archive.domain.repository.AuthRepository
import com.droidblossom.archive.domain.repository.MemberRepository
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
    fun providesAuthRepository(api: AuthService): AuthRepository =
        AuthRepositoryImpl(api)

    @Provides
    @ViewModelScoped
    fun providesMemberRepository(api: MemberService): MemberRepository =
        MemberRepositoryImpl(api)
}