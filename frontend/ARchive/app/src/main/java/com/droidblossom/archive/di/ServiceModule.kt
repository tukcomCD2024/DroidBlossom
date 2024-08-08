package com.droidblossom.archive.di

import com.droidblossom.archive.data.source.remote.api.AuthService
import com.droidblossom.archive.data.source.remote.api.CapsuleService
import com.droidblossom.archive.data.source.remote.api.CapsuleSkinService
import com.droidblossom.archive.data.source.remote.api.FriendService
import com.droidblossom.archive.data.source.remote.api.GroupCapsuleService
import com.droidblossom.archive.data.source.remote.api.GroupService
import com.droidblossom.archive.data.source.remote.api.MemberService
import com.droidblossom.archive.data.source.remote.api.PublicService
import com.droidblossom.archive.data.source.remote.api.S3Service
import com.droidblossom.archive.data.source.remote.api.SecretService
import com.droidblossom.archive.data.source.remote.api.TreasureService
import com.droidblossom.archive.data.source.remote.api.UnAuthenticatedService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Singleton
    @Provides
    fun providesAuthService(@RetrofitModule.AuthenticatedRetrofit retrofit: Retrofit) : AuthService = retrofit.create(AuthService::class.java)

    @Singleton
    @Provides
    fun providesMemberService(@RetrofitModule.AuthenticatedRetrofit retrofit: Retrofit) : MemberService = retrofit.create(MemberService::class.java)

    @Singleton
    @Provides
    fun providesSecretService(@RetrofitModule.AuthenticatedRetrofit retrofit: Retrofit) : SecretService = retrofit.create(SecretService::class.java)

    @Singleton
    @Provides
    fun providesS3Service(@RetrofitModule.AuthenticatedRetrofit retrofit: Retrofit) : S3Service = retrofit.create(S3Service::class.java)

    @Singleton
    @Provides
    fun providesCapsuleService(@RetrofitModule.AuthenticatedRetrofit retrofit: Retrofit) : CapsuleService = retrofit.create(CapsuleService::class.java)

    @Singleton
    @Provides
    fun providesCapsuleSkinService(@RetrofitModule.AuthenticatedRetrofit retrofit: Retrofit) : CapsuleSkinService = retrofit.create(CapsuleSkinService::class.java)

    @Singleton
    @Provides
    fun providesFriendService(@RetrofitModule.AuthenticatedRetrofit retrofit: Retrofit) : FriendService = retrofit.create(FriendService::class.java)

    @Singleton
    @Provides
    fun providesPublicService(@RetrofitModule.AuthenticatedRetrofit retrofit: Retrofit) : PublicService = retrofit.create(PublicService::class.java)

    @Singleton
    @Provides
    fun providesGroupService(@RetrofitModule.AuthenticatedRetrofit retrofit: Retrofit) : GroupService = retrofit.create(GroupService::class.java)

    @Singleton
    @Provides
    fun providesGroupCapsuleService(@RetrofitModule.AuthenticatedRetrofit retrofit: Retrofit) : GroupCapsuleService = retrofit.create(GroupCapsuleService::class.java)

    @Singleton
    @Provides
    fun providesTreasureService(@RetrofitModule.AuthenticatedRetrofit retrofit: Retrofit) : TreasureService = retrofit.create(TreasureService::class.java)

    @Singleton
    @Provides
    fun providesUnAuthenticatedService(@RetrofitModule.UnAuthenticatedRetrofit retrofit: Retrofit) : UnAuthenticatedService = retrofit.create(UnAuthenticatedService::class.java)
}