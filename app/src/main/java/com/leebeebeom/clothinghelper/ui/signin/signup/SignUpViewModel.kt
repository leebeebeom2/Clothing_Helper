package com.leebeebeom.clothinghelper.ui.signin.signup

import com.google.android.gms.tasks.Task
import com.leebeebeom.clothinghelper.data.TextFieldState
import com.leebeebeom.clothinghelper.ui.signin.FirebaseUseCase
import com.leebeebeom.clothinghelper.ui.signin.GoogleSignInImpl
import com.leebeebeom.clothinghelper.ui.signin.SignInBaseViewModel

class SignUpViewModel : SignInBaseViewModel(), GoogleSignInImpl {
    val nameTextFieldAttr = TextFieldState(essentialTextFields)

    override val passwordTextFieldState =
        TextFieldState(essentialTextFields, true).apply {
            onValueChange = {
                onValueChange(it)
                if (!passwordConfirmTextField.isBlank)
                    if (it != passwordConfirmTextField.text) setPasswordNotSameError()
                    else if (isErrorEnabled) passwordConfirmTextField.errorDisable()
                if (it.length < 6) errorEnable(TextFieldState.TextFieldError.ERROR_WEAK_PASSWORD)
                if (it.isBlank()) errorDisable()
            }
        }

    val passwordConfirmTextField: TextFieldState =
        TextFieldState(essentialTextFields, true).apply {
            onValueChange = {
                onValueChange(it)
                if (it.isNotEmpty())
                    if (it != passwordTextFieldState.text) errorEnable(TextFieldState.TextFieldError.ERROR_PASSWORD_CONFIRM_NOT_SAME)
                    else if (isErrorEnabled) errorDisable()
                if (it.isBlank()) errorDisable()
            }
        }

    private fun setPasswordNotSameError() =
        passwordConfirmTextField.errorEnable(TextFieldState.TextFieldError.ERROR_PASSWORD_CONFIRM_NOT_SAME)

    override fun firebaseTask(firebaseUseCase: FirebaseUseCase) =
        firebaseUseCase.createUserWithEmailAndPassword(
            emailTextFieldState.text,
            passwordTextFieldState.text
        )

    override val onCompleteListener: (Task<*>) -> Unit = {
        if (it.isSuccessful)
            FirebaseUseCase.updateName(
                nameTextFieldAttr.text,
                super.onCompleteListener
            )
        else taskFailed(it)

        progressOn = false
    }
}