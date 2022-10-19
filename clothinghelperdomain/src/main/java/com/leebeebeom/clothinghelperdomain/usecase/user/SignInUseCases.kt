package com.leebeebeom.clothinghelperdomain.usecase.user

import androidx.activity.result.ActivityResult
import com.leebeebeom.clothinghelperdomain.repository.FireBaseListeners
import com.leebeebeom.clothinghelperdomain.repository.UserRepository

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

class UserInfoUserCase(userRepository: UserRepository) {
    val isLogin = userRepository.isLogin
    val name = userRepository.name
    val email = userRepository.email
    val uid = userRepository.uid
}