package com.leebeebeom.clothinghelperdomain.usecase.user

import com.leebeebeom.clothinghelperdomain.model.*
import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.PushInitialSubCategoriesUseCase
import kotlinx.coroutines.flow.StateFlow

class GetSignInLoadingStateUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.isLoading
}

class GetSignInStateUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.isSignIn
}

class GoogleSignInUseCase(
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

class SignInUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(email: String, password: String): AuthResult {
        return userRepository.signIn(signIn = SignIn(email = email, password = password))
    }
}

class SignUpUseCase(
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
            userRepository.signUp(signUp = SignUp(email = email, password = password, name = name))
        if (authResult is AuthResult.Success) {
            pushInitialSubCategoriesUseCase(authResult.user.uid, onUpdateSubCategoriesFail)
        }
        return authResult
    }
}

class ResetPasswordUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(email: String): AuthResult {
        return userRepository.resetPasswordEmail(email = email)
    }
}

class GetUserUseCase(private val userRepository: UserRepository) {
    operator fun invoke(): StateFlow<User?> {
        return userRepository.user
    }
}

class SignOutUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke() {
        userRepository.signOut()
    }
}