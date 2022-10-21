package com.leebeebeom.clothinghelperdomain.usecase.user

import com.leebeebeom.clothinghelperdomain.repository.FireBaseListeners
import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.WriteInitialSubCategoriesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetSignInStateUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke() = withContext(Dispatchers.IO) { userRepository.isSignIn() }
}

class GoogleSignInUseCase(
    private val userRepository: UserRepository,
    private val writeInitialSubCategoriesUseCase: WriteInitialSubCategoriesUseCase
) {
    suspend operator fun invoke(
        googleCredential: Any?, googleSignInListener: FireBaseListeners.GoogleSignInListener
    ): Unit = withContext(Dispatchers.IO) {
        val isFirstUser = userRepository.googleSignIn(googleCredential, googleSignInListener)
        if (isFirstUser) writeInitialSubCategoriesUseCase()
    }
}

class SignInUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(
        email: String, password: String, signInListener: FireBaseListeners.SignInListener
    ) = withContext(Dispatchers.IO) { userRepository.signIn(email, password, signInListener) }
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
    ) = withContext(Dispatchers.IO) {
        userRepository.signUp(email, password, name, signUpListener, updateNameListener)
        writeInitialSubCategoriesUseCase()
    }
}

class ResetPasswordUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(
        email: String, resetPasswordListener: FireBaseListeners.ResetPasswordListener
    ) = withContext(Dispatchers.IO) {
        userRepository.resetPasswordEmail(
            email, resetPasswordListener
        )
    }
}

class GetUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        userRepository.getUser()
    }

}