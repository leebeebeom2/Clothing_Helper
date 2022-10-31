package com.leebeebeom.clothinghelper.signin.signup

import android.util.Log
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.TAG
import com.leebeebeom.clothinghelper.base.MaxWidthTextFieldState
import com.leebeebeom.clothinghelper.signin.base.GoogleSignInUpViewModel
import com.leebeebeom.clothinghelper.signin.base.GoogleSignInViewModelState
import com.leebeebeom.clothinghelper.signin.base.setFireBaseError
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.usecase.signin.GoogleSignInUseCase
import com.leebeebeom.clothinghelperdomain.usecase.signin.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase, googleSignInUseCase: GoogleSignInUseCase
) : GoogleSignInUpViewModel(googleSignInUseCase) {
    override val viewModelState = SignUpViewModelState()

    fun signUpWithEmailAndPassword() {
        signUpUseCase(
            email = viewModelState.email,
            password = viewModelState.password,
            name = viewModelState.name,
            onSignUpDone = {
                when (it) {
                    is FirebaseResult.Success -> viewModelState.showToast(R.string.sign_up_complete)
                    is FirebaseResult.Fail -> {
                        setFireBaseError(
                            exception = it.exception,
                            updateEmailError = viewModelState.emailState::updateError,
                            updatePasswordError = {},
                            showToast = viewModelState::showToast
                        )
                    }
                }
            }
        ) {
            if (it is FirebaseResult.Fail) {
                viewModelState.showToast(R.string.name_update_failed)
                Log.d(TAG, "taskFailed: $it.exception")
            }
        }
    }
}

class SignUpViewModelState : GoogleSignInViewModelState() {
    val emailState = MaxWidthTextFieldState.email(ImeAction.Next)
    val nameState = MaxWidthTextFieldState(
        label = R.string.name, keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
    )
    val passwordState = SignUpPasswordState()
    val passwordConfirmState = SignUpPasswordConfirmState(passwordState = passwordState)

    val email get() = emailState.textFiled.text
    val name get() = nameState.textFiled.text
    val password get() = passwordState.textFiled.text
    val passwordConfirm get() = passwordConfirmState.textFiled.text

    val submitButtonEnable
        get() = email.isNotBlank() && name.isNotBlank() && password.isNotBlank() && passwordConfirm.isNotBlank() &&
                !emailState.isError && !passwordState.isError && !passwordConfirmState.isError
}

class SignUpPasswordState :
    MaxWidthTextFieldState(
        label = R.string.password,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        )
    ) {

    override fun onValueChange(newText: TextFieldValue) {
        super.onValueChange(newText)
        if (newText.text.isNotBlank() && newText.text.length < 6) updateError(R.string.error_weak_password)
    }
}

class SignUpPasswordConfirmState(private val passwordState: SignUpPasswordState) :
    MaxWidthTextFieldState(
        label = R.string.password_confirm,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        )
    ) {
    override fun onValueChange(newText: TextFieldValue) {
        super.onValueChange(newText)
        if (newText.text.isNotBlank() && passwordState.textFiled.text.isNotBlank() && newText.text != passwordState.textFiled.text)
            updateError(R.string.error_password_confirm_not_same)
    }
}