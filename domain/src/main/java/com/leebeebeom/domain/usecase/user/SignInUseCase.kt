package com.leebeebeom.domain.usecase.user

import androidx.activity.result.ActivityResult
import com.leebeebeom.domain.repository.FireBaseListeners
import com.leebeebeom.domain.repository.UserRepository

class SignInUseCase(private val userRepository: UserRepository) {
    operator fun invoke(
        email: String, password: String, signInListener: FireBaseListeners.SignInListener
    ) = userRepository.signIn(email, password, signInListener)
}

class SignUpUseCase(private val userRepository: UserRepository) {
    operator fun invoke(
        email: String,
        password: String,
        name: String,
        signUpListener: FireBaseListeners.SignUpListener,
        updateNameListener: FireBaseListeners.UpdateNameListener
    ) = userRepository.signUp(
        email, password, name, signUpListener, updateNameListener
    )
}

class ResetPasswordUseCase(private val userRepository: UserRepository) {
    operator fun invoke(
        email: String, resetPasswordListener: FireBaseListeners.ResetPasswordListener
    ) = userRepository.resetPasswordEmail(email, resetPasswordListener)
}

class GoogleSignInUseCase(private val userRepository: UserRepository) {
    operator fun invoke(
        result: ActivityResult, googleSignInListener: FireBaseListeners.GoogleSignInListener
    ) = userRepository.googleSignIn(result, googleSignInListener)
}