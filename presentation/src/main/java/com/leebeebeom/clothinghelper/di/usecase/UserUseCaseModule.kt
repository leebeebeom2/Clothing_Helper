package com.leebeebeom.clothinghelper.di.usecase

import com.leebeebeom.clothinghelperdata.repository.SubCategoryRepositoryImpl
import com.leebeebeom.clothinghelperdata.repository.UserRepositoryImpl
import com.leebeebeom.clothinghelperdomain.usecase.user.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class UserUseCaseModule {
    @Provides
    fun googleSignInUseCase(
        userRepository: UserRepositoryImpl,
        subCategoryRepository: SubCategoryRepositoryImpl
    ) = GoogleSignInUseCase(userRepository, subCategoryRepository)

    @Provides
    fun resetPasswordUseCase(userRepository: UserRepositoryImpl) =
        ResetPasswordUseCase(userRepository)

    @Provides
    fun signUpUseCase(
        userRepository: UserRepositoryImpl,
        subCategoryRepository: SubCategoryRepositoryImpl
    ) = SignUpUseCase(userRepository, subCategoryRepository)

    @Provides
    fun signInUseCase(userRepository: UserRepositoryImpl) = SignInUseCase(userRepository)

    @Provides
    fun getSignInStateUseCase(userRepository: UserRepositoryImpl) =
        GetSignInStateUseCase(userRepository)

    @Provides
    fun getUserUseCase(userRepository: UserRepositoryImpl) = GetUserUseCase(userRepository)

    @Provides
    fun signOutUserCase(userRepository: UserRepositoryImpl) = SignOutUseCase(userRepository)
}