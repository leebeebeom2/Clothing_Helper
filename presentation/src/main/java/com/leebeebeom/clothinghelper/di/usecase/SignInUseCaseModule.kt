package com.leebeebeom.clothinghelper.di.usecase

import com.leebeebeom.clothinghelperdata.repository.UserRepositoryImpl
import com.leebeebeom.clothinghelperdomain.usecase.signin.*
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.LoadSubCategoriesUseCase
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
        pushInitialSubCategoriesUseCase: PushInitialSubCategoriesUseCase,
        loadSubCategoriesUseCase: LoadSubCategoriesUseCase
    ) = GoogleSignInUseCase(userRepository, pushInitialSubCategoriesUseCase, loadSubCategoriesUseCase)

    @Provides
    fun resetPasswordUseCase(userRepository: UserRepositoryImpl) =
        ResetPasswordUseCase(userRepository)

    @Provides
    fun signUpUseCase(
        userRepository: UserRepositoryImpl,
        pushInitialSubCategoriesUseCase: PushInitialSubCategoriesUseCase,
        loadSubCategoriesUseCase: LoadSubCategoriesUseCase
    ) = SignUpUseCase(userRepository, pushInitialSubCategoriesUseCase, loadSubCategoriesUseCase)

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