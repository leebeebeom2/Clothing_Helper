package com.leebeebeom.clothinghelperdomain.usecase.user

import com.leebeebeom.clothinghelperdomain.repository.FireBaseListeners
import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.WriteInitialSubCategoriesUseCase

class GetSignInStateUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.isSignIn()
}

class GoogleSignInUseCase(
    private val userRepository: UserRepository,
    private val writeInitialSubCategoriesUseCase: WriteInitialSubCategoriesUseCase
) {
    suspend operator fun invoke(
        googleCredential: Any?, googleSignInListener: FireBaseListeners.GoogleSignInListener
    ) {
        val isFirstUser = userRepository.googleSignIn(googleCredential, googleSignInListener)
        if (isFirstUser) writeInitialSubCategoriesUseCase()
    }
}

class SignInUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(
        email: String, password: String, signInListener: FireBaseListeners.SignInListener
    ) = userRepository.signIn(email, password, signInListener)
}

class SignUpUseCase(
    private val userRepository: UserRepository,
    private val writeInitialSubCategoriesUseCase: WriteInitialSubCategoriesUseCase
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        name: String,
        signUpListener: FireBaseListeners.SignUpListener,
        updateNameListener: FireBaseListeners.UpdateNameListener
    ) {
        userRepository.signUp(email, password, name, signUpListener, updateNameListener)
        writeInitialSubCategoriesUseCase()
    }
}

class ResetPasswordUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(
        email: String, resetPasswordListener: FireBaseListeners.ResetPasswordListener
    ) = userRepository.resetPasswordEmail(email, resetPasswordListener)
}

class GetUserUseCase(private val userRepository: UserRepository) {
    fun getUser() = userRepository.getUser()
}