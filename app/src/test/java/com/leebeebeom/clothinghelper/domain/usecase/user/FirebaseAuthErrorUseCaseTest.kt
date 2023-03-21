package com.leebeebeom.clothinghelper.domain.usecase.user

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthException
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_EMAIL_ALREADY_IN_USE
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_INVALID_EMAIL
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_USER_NOT_FOUND
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_WRONG_PASSWORD
import org.junit.Test

class FirebaseAuthErrorUseCaseTest {
    private val firebaseAuthErrorUseCase = FirebaseAuthErrorUseCase()

    @Test
    fun errorTest() {
        firebaseAuthErrorUseCase.firebaseAuthError( // invalidEmail
            throwable = FirebaseAuthException(ERROR_INVALID_EMAIL, ""),
            setEmailError = { assert(it == R.string.error_invalid_email) },
            setPasswordError = { assert(false) },
            showToast = { assert(false) })

        firebaseAuthErrorUseCase.firebaseAuthError( // emailAlreadyInUse
            throwable = FirebaseAuthException(ERROR_EMAIL_ALREADY_IN_USE, ""),
            setEmailError = { assert(it == R.string.error_email_already_in_use) },
            setPasswordError = { assert(false) },
            showToast = { assert(false) })

        firebaseAuthErrorUseCase.firebaseAuthError( // userNotFound
            throwable = FirebaseAuthException(ERROR_USER_NOT_FOUND, ""),
            setEmailError = { assert(it == R.string.error_user_not_found) },
            setPasswordError = { assert(false) },
            showToast = { assert(false) })

        firebaseAuthErrorUseCase.firebaseAuthError( // wrongPassword
            throwable = FirebaseAuthException(ERROR_WRONG_PASSWORD, ""),
            setEmailError = { assert(false) },
            setPasswordError = { assert(it == R.string.error_wrong_password) },
            showToast = { assert(false) })

        firebaseAuthErrorUseCase.firebaseAuthError( // TooManyRequest
            throwable = FirebaseTooManyRequestsException(""),
            setEmailError = { assert(false) },
            setPasswordError = { assert(false) },
            showToast = { assert(it == R.string.too_many_requst_error) }
        )

        firebaseAuthErrorUseCase.firebaseAuthError( // TooManyRequest
            throwable = FirebaseNetworkException(""),
            setEmailError = { assert(false) },
            setPasswordError = { assert(false) },
            showToast = { assert(it == R.string.network_error) }
        )

        firebaseAuthErrorUseCase.firebaseAuthError( // unknown
            throwable = Exception(),
            setEmailError = { assert(false) },
            setPasswordError = { assert(false) },
            showToast = { assert(it == R.string.unknown_error) }
        )
    }
}