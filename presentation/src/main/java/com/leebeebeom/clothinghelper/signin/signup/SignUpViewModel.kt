package com.leebeebeom.clothinghelper.signin.signup

import android.util.Log
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.TAG
import com.leebeebeom.clothinghelper.signin.base.GoogleSignInUpViewModel
import com.leebeebeom.clothinghelper.signin.base.GoogleSignInViewModelState
import com.leebeebeom.clothinghelper.signin.base.setFireBaseError
import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener2
import com.leebeebeom.clothinghelperdomain.usecase.user.GoogleSignInUseCase
import com.leebeebeom.clothinghelperdomain.usecase.user.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase, googleSignInUseCase: GoogleSignInUseCase
) : GoogleSignInUpViewModel(googleSignInUseCase) {
    override val viewModelState = GoogleSignInViewModelState()

    fun signUpWithEmailAndPassword(email: String, password: String, name: String) {
        viewModelState.loadingOn()
        signUpUseCase(
            email = email,
            password = password,
            name = name,
            signUpListener = signUpListener,
            updateNameListener = updateNameListener
        )
    }

    private val signUpListener = object : FirebaseListener2 {
        override fun taskSuccess() = viewModelState.showToast(R.string.sign_up_complete)
        override fun taskFailed(exception: Exception?) = setFireBaseError(
            exception = exception,
            showEmailError = viewModelState.showEmailError,
            showPasswordError = {},
            showToast = viewModelState::showToast
        )

        override fun taskFinish() = viewModelState.loadingOff()
    }

    private val updateNameListener = object : FirebaseListener2 {
        override fun taskFailed(exception: Exception?) {
            viewModelState.showToast(R.string.name_update_failed)
            Log.d(TAG, "taskFailed: $exception")
        }

        override fun taskFinish() {}
        override fun taskSuccess() {}
    }
}