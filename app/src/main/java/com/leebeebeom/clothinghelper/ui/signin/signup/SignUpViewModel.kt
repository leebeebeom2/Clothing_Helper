package com.leebeebeom.clothinghelper.ui.signin.signup

import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.data.OutlinedTextFieldAttrFactory
import com.leebeebeom.clothinghelper.ui.signin.CHFirebase
import com.leebeebeom.clothinghelper.ui.signin.CHGoogleSignIn
import com.leebeebeom.clothinghelper.ui.signin.SignInBaseViewModel

class SignUpViewModel : SignInBaseViewModel(), CHGoogleSignIn {
    override val emailTextFieldAttr by lazy { OutlinedTextFieldAttrFactory.email() }
    val nameTextFieldAttr by lazy { OutlinedTextFieldAttrFactory.signUpName() }

    private var passwordText = ""
    val passwordTextFieldAttr by lazy {
        OutlinedTextFieldAttrFactory.signUpPassword().apply {
            onValueChange = {
                text = it
                if (isErrorEnabled) errorDisable()
                passwordText = it
                if (passwordConfirmText.isNotEmpty())
                    if (it != passwordConfirmText) setPasswordNotSameError()
                    else if (isErrorEnabled) passwordConfirmTextField.errorDisable()
                if (it.length < 6) errorEnable(R.string.error_weak_password)
            }
        }
    }

    private var passwordConfirmText = ""
    val passwordConfirmTextField by lazy {
        OutlinedTextFieldAttrFactory.signUpPasswordConfirm().apply {
            onValueChange = {
                text = it
                if (isErrorEnabled) errorDisable()
                passwordConfirmText = it
                if (it.isNotEmpty())
                    if (it != passwordText) errorEnable(R.string.error_password_confirm_not_same)
                    else if (isErrorEnabled) errorDisable()
            }
        }
    }

    override val isTextFieldEmpty get() = emailTextFieldAttr.isEmpty || passwordTextFieldAttr.isEmpty || passwordConfirmTextField.isEmpty || nameTextFieldAttr.isEmpty
    override val isErrorEnable
        get() = emailTextFieldAttr.isErrorEnabled || passwordTextFieldAttr.isErrorEnabled ||
                passwordConfirmTextField.isErrorEnabled || nameTextFieldAttr.isErrorEnabled

    private fun setPasswordNotSameError() =
        passwordConfirmTextField.errorEnable(R.string.error_password_confirm_not_same)

    override fun firebaseTask(chFirebase: CHFirebase)=
        chFirebase.createUserWithEmailAndPassword(emailTextFieldAttr.text, passwordText)

}