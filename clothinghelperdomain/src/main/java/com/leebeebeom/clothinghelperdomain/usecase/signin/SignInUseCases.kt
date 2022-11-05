package com.leebeebeom.clothinghelperdomain.usecase.signin

import com.leebeebeom.clothinghelperdomain.model.AuthResult
import com.leebeebeom.clothinghelperdomain.model.SignIn
import com.leebeebeom.clothinghelperdomain.model.SignUp
import com.leebeebeom.clothinghelperdomain.model.SubCategoryPushResult
import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.LoadSubCategoriesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.PushInitialSubCategoriesUseCase

class GetSignInLoadingStateUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.isLoading
}

class GetSignInStateUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.isSignIn
}

class GoogleSignInUseCase(
    private val userRepository: UserRepository,
    private val pushInitialSubCategoriesUseCase: PushInitialSubCategoriesUseCase,
    private val loadSubCategoriesUseCase: LoadSubCategoriesUseCase
) {
    suspend operator fun invoke(credential: Any?): AuthResult {
        var authResult = userRepository.googleSignIn(credential = credential)
        authResult = pushInitialSubCategoriesUseCase.push(authResult, loadSubCategoriesUseCase)
        return authResult
    }
}

class SignInUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(email: String, password: String) =
        userRepository.signIn(signIn = SignIn(email = email, password = password))
}

class SignUpUseCase(
    private val userRepository: UserRepository,
    private val pushInitialSubCategoriesUseCase: PushInitialSubCategoriesUseCase,
    private val loadSubCategoriesUseCase: LoadSubCategoriesUseCase
) {
    suspend operator fun invoke(email: String, password: String, name: String): AuthResult {
        var authResult =
            userRepository.signUp(signUp = SignUp(email = email, password = password, name = name))
        authResult = pushInitialSubCategoriesUseCase.push(authResult, loadSubCategoriesUseCase)
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

suspend fun PushInitialSubCategoriesUseCase.push(
    authResult: AuthResult,
    loadSubCategoriesUseCase: LoadSubCategoriesUseCase
): AuthResult {
    if (authResult is AuthResult.Success && authResult.isNewer) {
        val pushResult = invoke(authResult.user.uid)
        loadSubCategoriesUseCase(authResult.user)
        if (pushResult is SubCategoryPushResult.Fail)
            return AuthResult.Fail(Exception(PushInitialSubCategoriesFailed))
    }
    return authResult
}

const val PushInitialSubCategoriesFailed = "pushInitialSubCategoriesFailed"