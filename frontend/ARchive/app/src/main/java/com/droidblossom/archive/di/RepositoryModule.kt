package com.droidblossom.archive.di

import com.droidblossom.archive.data.repository.AuthRepositoryImpl
import com.droidblossom.archive.data.repository.CapsuleRepositoryImpl
import com.droidblossom.archive.data.repository.CapsuleSkinRepositoryImpl
import com.droidblossom.archive.data.repository.MemberRepositoryImpl
import com.droidblossom.archive.data.repository.KakaoRepositoryImpl
import com.droidblossom.archive.data.repository.S3RepositoryImpl
import com.droidblossom.archive.data.repository.SecretRepositoryImpl
import com.droidblossom.archive.data.source.remote.api.AuthService
import com.droidblossom.archive.data.source.remote.api.CapsuleService
import com.droidblossom.archive.data.source.remote.api.CapsuleSkinService
import com.droidblossom.archive.data.source.remote.api.MemberService
import com.droidblossom.archive.data.source.remote.api.KakaoService
import com.droidblossom.archive.data.source.remote.api.S3Service
import com.droidblossom.archive.data.source.remote.api.SecretService
import com.droidblossom.archive.domain.repository.AuthRepository
import com.droidblossom.archive.domain.repository.CapsuleRepository
import com.droidblossom.archive.domain.repository.CapsuleSkinRepository
import com.droidblossom.archive.domain.repository.MemberRepository
import com.droidblossom.archive.domain.repository.KakaoRepository
import com.droidblossom.archive.domain.repository.S3Repository
import com.droidblossom.archive.domain.repository.SecretRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

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

    @Provides
    @ViewModelScoped
    fun providesSecretRepository(api: SecretService): SecretRepository =
        SecretRepositoryImpl(api)

    @Provides
    @ViewModelScoped
    fun providesS3Repository(api: S3Service): S3Repository =
        S3RepositoryImpl(api)

    @Provides
    @ViewModelScoped
    fun providesKakaoRepository(api: KakaoService): KakaoRepository =
        KakaoRepositoryImpl(api)

    @Provides
    @ViewModelScoped
    fun providesCapsuleRepository(api : CapsuleService) : CapsuleRepository = CapsuleRepositoryImpl(api)

    @Provides
    @ViewModelScoped
    fun providesCapsuleSkinRepository(api : CapsuleSkinService) : CapsuleSkinRepository = CapsuleSkinRepositoryImpl(api)

}