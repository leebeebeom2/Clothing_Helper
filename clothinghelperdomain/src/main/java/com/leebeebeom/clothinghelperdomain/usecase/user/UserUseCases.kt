package com.leebeebeom.clothinghelperdomain.usecase.user

import com.leebeebeom.clothinghelperdomain.model.SignIn
import com.leebeebeom.clothinghelperdomain.model.SignUp
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import com.leebeebeom.clothinghelperdomain.repository.onDone

class GetSignInLoadingStateUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.isLoading
}

class GetSignInStateUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.isSignIn
}

class GoogleSignInUseCase(
    private val userRepository: UserRepository,
    private val subCategoryRepository: SubCategoryRepository
) {
    operator fun invoke(
        credential: Any?, onDone: onDone
    ) = userRepository.googleSignIn(
        credential = credential,
        onDone = onDone,
        pushInitialSubCategories = subCategoryRepository::pushInitialSubCategories
    )
}

class SignInUseCase(private val userRepository: UserRepository) {
    operator fun invoke(
        email: String, password: String, onDone: onDone
    ) = userRepository.signIn(signIn = SignIn(email = email, password = password), onDone = onDone)
}

class SignUpUseCase(
    private val userRepository: UserRepository,
    private val subCategoryRepository: SubCategoryRepository
) {
    operator fun invoke(
        email: String,
        password: String,
        name: String,
        onSignUpDone: onDone,
        onNameUpdateDone: onDone
    ) = userRepository.signUp(
        signUp = SignUp(email = email, password = password, name = name),
        onSignUpDone = onSignUpDone,
        onNameUpdateDone = onNameUpdateDone,
        pushInitialSubCategories = subCategoryRepository::pushInitialSubCategories,
    )
}

class ResetPasswordUseCase(private val userRepository: UserRepository) {
    operator fun invoke(
        email: String, onDone: onDone
    ) = userRepository.resetPasswordEmail(email = email, onDone = onDone)
}

class GetUserUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.user
}

class SignOutUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.signOut()
}