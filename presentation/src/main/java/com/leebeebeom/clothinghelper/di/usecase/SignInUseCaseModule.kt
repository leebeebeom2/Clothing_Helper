package com.leebeebeom.clothinghelper.di.usecase

import com.leebeebeom.clothinghelperdata.repository.UserRepositoryImpl
import com.leebeebeom.clothinghelperdomain.usecase.signin.*
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.PushInitialSubCategoriesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class SignInUseCaseModule {
    @Provides
    fun googleSignInUseCase(
        userRepository: UserRepositoryImpl,
        pushInitialSubCategoriesUseCase: PushInitialSubCategoriesUseCase
    ) = GoogleSignInUseCase(userRepository, pushInitialSubCategoriesUseCase)

    @Provides
    fun resetPasswordUseCase(userRepository: UserRepositoryImpl) =
        ResetPasswordUseCase(userRepository)

    @Provides
    fun signUpUseCase(
        userRepository: UserRepositoryImpl,
        pushInitialSubCategoriesUseCase: PushInitialSubCategoriesUseCase
    ) = SignUpUseCase(userRepository, pushInitialSubCategoriesUseCase)

    @Provides
    fun signInUseCase(userRepository: UserRepositoryImpl) = SignInUseCase(userRepository)

    @Provides
    fun getSignInStateUseCase(userRepository: UserRepositoryImpl) =
        GetSignInStateUseCase(userRepository)

    @Provides
    fun getUserUseCase(userRepository: UserRepositoryImpl) = GetUserUseCase(userRepository)

    @Provides
    fun signOutUseCase(userRepository: UserRepositoryImpl) = SignOutUseCase(userRepository)

    @Provides
    fun getSignInLoadingStateUseCase(userRepository: UserRepositoryImpl) =
        GetSignInLoadingStateUseCase(userRepository)
}