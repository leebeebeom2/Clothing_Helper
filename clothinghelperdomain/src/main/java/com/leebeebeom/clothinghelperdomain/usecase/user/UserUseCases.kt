package com.leebeebeom.clothinghelperdomain.usecase.user

import com.leebeebeom.clothinghelperdomain.model.AuthResult
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.model.User
import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.PushInitialSubCategoriesUseCase
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@ViewModelScoped
class GetSignInLoadingStateUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(): StateFlow<Boolean> {
        return userRepository.isLoading
    }
}

@ViewModelScoped
class GetSignInStateUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(): StateFlow<Boolean> {
        return userRepository.isSignIn
    }
}

@ViewModelScoped
class GoogleSignInUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val pushInitialSubCategoriesUseCase: PushInitialSubCategoriesUseCase
) {
    suspend operator fun invoke(
        credential: Any?,
        onUpdateSubCategoriesFail: (FirebaseResult) -> Unit
    ): AuthResult {
        val authResult = userRepository.googleSignIn(credential = credential)
        if (authResult is AuthResult.Success && authResult.isNewer) {
            pushInitialSubCategoriesUseCase(authResult.user.uid, onUpdateSubCategoriesFail)
        }
        return authResult
    }
}

@ViewModelScoped
class SignInUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(email: String, password: String): AuthResult {
        return userRepository.signIn(email, password)
    }
}

@ViewModelScoped
class SignUpUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val pushInitialSubCategoriesUseCase: PushInitialSubCategoriesUseCase
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        name: String,
        onUpdateSubCategoriesFail: (FirebaseResult) -> Unit
    ): AuthResult {
        val authResult =
            userRepository.signUp(email, password, name)
        if (authResult is AuthResult.Success) {
            pushInitialSubCategoriesUseCase(authResult.user.uid, onUpdateSubCategoriesFail)
        }
        return authResult
    }
}

@ViewModelScoped
class ResetPasswordUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(email: String): AuthResult {
        return userRepository.resetPasswordEmail(email)
    }
}

@ViewModelScoped
class GetUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(): StateFlow<User?> {
        return userRepository.user
    }
}

@ViewModelScoped
class SignOutUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke() {
        userRepository.signOut()
    }
}