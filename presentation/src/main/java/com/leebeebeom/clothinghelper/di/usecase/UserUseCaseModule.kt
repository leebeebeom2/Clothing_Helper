package com.leebeebeom.clothinghelper.di.usecase

import com.leebeebeom.clothinghelperdata.repository.UserRepositoryImpl
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.WriteInitialSubCategoriesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.user.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class UserUseCaseModule {
    @Provides
    fun provideGoogleSignInUseCase(
        userRepository: UserRepositoryImpl,
        writeInitialSubCategoriesUseCase: WriteInitialSubCategoriesUseCase
    ) = GoogleSignInUseCase(userRepository, writeInitialSubCategoriesUseCase)

    @Provides
    fun provideResetPasswordUseCase(userRepository: UserRepositoryImpl) =
        ResetPasswordUseCase(userRepository)

    @Provides
    fun provideSignUpUseCase(
        userRepository: UserRepositoryImpl,
        writeInitialSubCategoriesUseCase: WriteInitialSubCategoriesUseCase
    ) = SignUpUseCase(userRepository, writeInitialSubCategoriesUseCase)

    @Provides
    fun provideSignInUseCase(userRepository: UserRepositoryImpl) = SignInUseCase(userRepository)

    @Provides
    fun provideGetSignInStateUseCase(userRepository: UserRepositoryImpl) =
        GetSignInStateUseCase(userRepository)

    @Provides
    fun provideGetUserUseCase(userRepository: UserRepositoryImpl) = GetUserUseCase(userRepository)
}