package com.leebeebeom.clothinghelperdomain.usecase.user

import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelperdomain.repository.UserRepository

class GetSignInStateUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.isSignIn
}

class GoogleSignInUseCase(
    private val userRepository: UserRepository,
    private val subCategoryRepository: SubCategoryRepository
) {
    operator fun invoke(
        googleCredential: Any?, googleSignInListener: FirebaseListener
    ) = userRepository.googleSignIn(
        googleCredential,
        googleSignInListener,
        subCategoryRepository::pushInitialSubCategories
    )
}

class SignInUseCase(private val userRepository: UserRepository) {
    operator fun invoke(
        email: String,
        password: String,
        signInListener: FirebaseListener,
        taskFinish: () -> Unit
    ) = userRepository.signIn(email, password, signInListener, taskFinish)
}

class SignUpUseCase(
    private val userRepository: UserRepository,
    private val subCategoryRepository: SubCategoryRepository
) {
    operator fun invoke(
        email: String,
        password: String,
        name: String,
        signUpListener: FirebaseListener,
        updateNameListener: FirebaseListener,
        taskFinish: () -> Unit
    ) = userRepository.signUp(
        email,
        password,
        name,
        signUpListener,
        updateNameListener,
        subCategoryRepository::pushInitialSubCategories,
        taskFinish
    )
}

class ResetPasswordUseCase(private val userRepository: UserRepository) {
    operator fun invoke(
        email: String, resetPasswordListener: FirebaseListener, taskFinish: () -> Unit
    ) = userRepository.resetPasswordEmail(email, resetPasswordListener, taskFinish)
}

class GetUserUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.user
}

class SignOutUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.signOut()
}