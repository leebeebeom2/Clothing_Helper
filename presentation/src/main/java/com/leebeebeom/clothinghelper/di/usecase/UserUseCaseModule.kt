package com.leebeebeom.clothinghelper.di.usecase

import com.leebeebeom.clothinghelperdata.repository.UserRepositoryImpl
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.WriteInitialSubCategoriesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.user.*
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
    fun provideGoogleSignInUseCase(
        userRepository: UserRepositoryImpl,
        writeInitialSubCategoriesUseCase: WriteInitialSubCategoriesUseCase
    ) = GoogleSignInUseCase(userRepository, writeInitialSubCategoriesUseCase)

    @Singleton
    @Provides
    fun provideResetPasswordUseCase(userRepository: UserRepositoryImpl) =
        ResetPasswordUseCase(userRepository)

    @Singleton
    @Provides
    fun provideSignUpUseCase(
        userRepository: UserRepositoryImpl,
        writeInitialSubCategoriesUseCase: WriteInitialSubCategoriesUseCase
    ) = SignUpUseCase(userRepository, writeInitialSubCategoriesUseCase)

    @Singleton
    @Provides
    fun provideSignInUseCase(
        userRepository: UserRepositoryImpl,
        writeInitialSubCategoriesUseCase: WriteInitialSubCategoriesUseCase
    ) = SignInUseCase(userRepository, writeInitialSubCategoriesUseCase)

    @Singleton
    @Provides
    fun provideUserInfoUseCase(userRepository: UserRepositoryImpl) =
        UserInfoUserCase(userRepository)

    @Singleton
    @Provides
    fun provideGetLoginStateUseCase(userRepository: UserRepositoryImpl) =
        GetLoginStateUseCase(userRepository)
}