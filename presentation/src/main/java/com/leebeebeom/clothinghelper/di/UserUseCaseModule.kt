package com.leebeebeom.clothinghelper.di

import com.leebeebeom.clothinghelper.data.datasource.ContentRemoteDataSource
import com.leebeebeom.clothinghelper.data.repository.UserRepositoryImpl
import com.leebeebeom.clothinghelper.domain.usecase.user.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class UserUseCaseModule {
    @Singleton
    @Provides
    fun provideGoogleSignInUseCase(userRepository: UserRepositoryImpl) =
        GoogleSignInUseCase(userRepository)

    @Singleton
    @Provides
    fun provideResetPasswordUseCase(userRepository: UserRepositoryImpl) =
        ResetPasswordUseCase(userRepository)

    @Singleton
    @Provides
    fun provideSignUpUseCase(userRepository: UserRepositoryImpl) =
        SignUpUseCase(userRepository)

    @Singleton
    @Provides
    fun provideSignInUseCase(userRepository: UserRepositoryImpl) =
        SignInUseCase(userRepository)

    @Singleton
    @Provides
    fun provideUserInfoUseCase(userRepository: UserRepositoryImpl) =
        UserInfoUserCase(userRepository)

    @Singleton
    @Provides
    fun provideContentRemoteDataSource() =
        ContentRemoteDataSource() // TODO 삭제

    @Singleton
    @Provides
    fun provideUserRepositoryImpl() = UserRepositoryImpl()
}