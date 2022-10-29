package com.leebeebeom.clothinghelperdomain.usecase.user

import com.leebeebeom.clothinghelperdomain.model.SignIn
import com.leebeebeom.clothinghelperdomain.model.SignUp
import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener
import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener2
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
        credential: Any?, listener: FirebaseListener2
    ) = userRepository.googleSignIn(
        credential,
        listener,
        subCategoryRepository::pushInitialSubCategories
    )
}

class SignInUseCase(private val userRepository: UserRepository) {
    operator fun invoke(
        email: String,
        password: String,
        listener: FirebaseListener2,
    ) = userRepository.signIn(SignIn(email = email, password = password), listener)
}

class SignUpUseCase(
    private val userRepository: UserRepository,
    private val subCategoryRepository: SubCategoryRepository
) {
    operator fun invoke(
        email: String,
        password: String,
        name: String,
        signUpListener: FirebaseListener2,
        updateNameListener: FirebaseListener2,
    ) = userRepository.signUp(
        signUp = SignUp(email = email, password = password, name = name),
        signUpListener = signUpListener,
        updateNameListener = updateNameListener,
        pushInitialSubCategories = subCategoryRepository::pushInitialSubCategories,
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