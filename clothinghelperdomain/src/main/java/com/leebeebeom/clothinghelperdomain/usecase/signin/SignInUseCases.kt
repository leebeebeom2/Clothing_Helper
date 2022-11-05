package com.leebeebeom.clothinghelperdomain.usecase.signin

import com.leebeebeom.clothinghelperdomain.model.AuthResult
import com.leebeebeom.clothinghelperdomain.model.SignIn
import com.leebeebeom.clothinghelperdomain.model.SignUp
import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.PushInitialSubCategoriesUseCase

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
    suspend operator fun invoke(credential: Any?): AuthResult {
        val authResult = userRepository.googleSignIn(credential = credential)
        pushInitialSubCategoriesUseCase.push(authResult) // 실패 성공 상관 없이 진행
        return authResult
    }
}

class SignInUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(email: String, password: String) =
        userRepository.signIn(signIn = SignIn(email = email, password = password))
}

class SignUpUseCase(
    private val userRepository: UserRepository,
    private val pushInitialSubCategoriesUseCase: PushInitialSubCategoriesUseCase
) {
    suspend operator fun invoke(email: String, password: String, name: String): AuthResult {
        val authResult =
            userRepository.signUp(signUp = SignUp(email = email, password = password, name = name))
        pushInitialSubCategoriesUseCase.push(authResult) // 실패 성공 상관 없이 진행
        return authResult
    }
}

class ResetPasswordUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(email: String) = userRepository.resetPasswordEmail(email = email)
}

class GetUserUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.user
}

class SignOutUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke() = userRepository.signOut()
}

suspend fun PushInitialSubCategoriesUseCase.push(authResult: AuthResult) {
    if (authResult is AuthResult.Success && authResult.isNewer) invoke(authResult.user.uid)
}