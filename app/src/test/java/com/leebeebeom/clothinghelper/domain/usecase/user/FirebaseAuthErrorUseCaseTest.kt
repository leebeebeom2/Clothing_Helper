package com.leebeebeom.clothinghelper.domain.usecase.user

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthException
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_EMAIL_ALREADY_IN_USE
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_INVALID_EMAIL
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_USER_NOT_FOUND
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_WRONG_PASSWORD
import org.junit.Before
import org.junit.Test

class FirebaseAuthErrorUseCaseTest {
    private lateinit var firebaseAuthErrorUseCase: FirebaseAuthErrorUseCase

    @Before
    fun init() {
        firebaseAuthErrorUseCase = FirebaseAuthErrorUseCase()
    }

    @Test
    fun test() {

        firebaseAuthErrorUseCase.firebaseAuthError( // invalidEmail
            exception = FirebaseAuthException(ERROR_INVALID_EMAIL, ""),
            updateEmailError = { assert(it == R.string.error_invalid_email) },
            updatePasswordError = { assert(false) },
            showToast = { assert(false) })

        firebaseAuthErrorUseCase.firebaseAuthError( // emailAlreadyInUse
            exception = FirebaseAuthException(ERROR_EMAIL_ALREADY_IN_USE, ""),
            updateEmailError = { assert(it == R.string.error_email_already_in_use) },
            updatePasswordError = { assert(false) },
            showToast = { assert(false) })

        firebaseAuthErrorUseCase.firebaseAuthError( // userNotFound
            exception = FirebaseAuthException(ERROR_USER_NOT_FOUND, ""),
            updateEmailError = { assert(it == R.string.error_user_not_found) },
            updatePasswordError = { assert(false) },
            showToast = { assert(false) })

        firebaseAuthErrorUseCase.firebaseAuthError( // wrongPassword
            exception = FirebaseAuthException(ERROR_WRONG_PASSWORD, ""),
            updateEmailError = { assert(false) },
            updatePasswordError = { assert(it == R.string.error_wrong_password) },
            showToast = { assert(false) })

        firebaseAuthErrorUseCase.firebaseAuthError( // TooManyRequest
            exception = FirebaseTooManyRequestsException(""),
            updateEmailError = { assert(false) },
            updatePasswordError = { assert(false) },
            showToast = { assert(it == R.string.too_many_requst_error) }
        )

        firebaseAuthErrorUseCase.firebaseAuthError( // TooManyRequest
            exception = FirebaseNetworkException(""),
            updateEmailError = { assert(false) },
            updatePasswordError = { assert(false) },
            showToast = { assert(it == R.string.network_error) }
        )

        firebaseAuthErrorUseCase.firebaseAuthError( // unknown
            exception = Exception(),
            updateEmailError = { assert(false) },
            updatePasswordError = { assert(false) },
            showToast = { assert(it == R.string.unknown_error) }
        )
    }
}