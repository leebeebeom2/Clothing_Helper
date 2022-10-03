package com.leebeebeom.clothinghelper.ui.signin.signup

import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.data.OutlinedTextFieldAttrFactory
import com.leebeebeom.clothinghelper.ui.signin.GoogleSignIn
import com.leebeebeom.clothinghelper.ui.signin.SignInBaseViewModel

class SignUpViewModel : SignInBaseViewModel(), GoogleSignIn {
    override val email by lazy { OutlinedTextFieldAttrFactory.email() }
    val name by lazy { OutlinedTextFieldAttrFactory.signUpName() }

    private var passwordText = ""
    val password by lazy {
        OutlinedTextFieldAttrFactory.signUpPassword().apply {
            onValueChange = {
                text = it
                if (isErrorEnabled) errorDisable()
                passwordText = it
                if (passwordConfirmText.isNotEmpty())
                    if (it != passwordConfirmText) setPasswordNotSameError()
                    else passwordConfirm.errorDisable()
                if (it.length < 6) errorEnable(R.string.error_weak_password)
            }
        }
    }

    private var passwordConfirmText = ""
    val passwordConfirm by lazy {
        OutlinedTextFieldAttrFactory.signUpPasswordConfirm().apply {
            onValueChange = {
                text = it
                if (isErrorEnabled) errorDisable()
                passwordConfirmText = it
                if (it.isNotEmpty())
                    if (it != passwordText) errorEnable(R.string.error_password_confirm_not_same)
                    else errorDisable()
            }
        }
    }

    override val isTextFieldEmpty get() = email.isEmpty || password.isEmpty || passwordConfirm.isEmpty || name.isEmpty
    override val isErrorEnable
        get() = email.isErrorEnabled || password.isErrorEnabled ||
                passwordConfirm.isErrorEnabled || name.isErrorEnabled

    private fun setPasswordNotSameError() =
        passwordConfirm.errorEnable(R.string.error_password_confirm_not_same)

    override fun firebaseTask() {
        super.onFirebaseButtonClick
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email.text, passwordText)
            .addOnCompleteListener(onCompleteListener)
    }
}