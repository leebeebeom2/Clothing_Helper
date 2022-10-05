package com.leebeebeom.clothinghelper.ui.signin.signup

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.data.TextFieldAttr
import com.leebeebeom.clothinghelper.data.TextFieldManager
import com.leebeebeom.clothinghelper.ui.signin.FirebaseExecution
import com.leebeebeom.clothinghelper.ui.signin.CHGoogleSignIn
import com.leebeebeom.clothinghelper.ui.signin.SignInBaseViewModel

class SignUpViewModel : SignInBaseViewModel(), CHGoogleSignIn {
    override val emailTextFieldAttr = TextFieldAttr.signInEmail(textFieldManager)
    val nameTextFieldAttr = TextFieldAttr.signUpName(textFieldManager)

    override val passwordTextFieldAttr =
        TextFieldAttr.signUpPassword(textFieldManager).apply {
            onValueChange = {
                onValueChange(it)
                if (!passwordConfirmTextField.isEmpty)
                    if (it != passwordConfirmTextField.text) setPasswordNotSameError()
                    else if (isErrorEnable) passwordConfirmTextField.errorDisable()
                if (it.length < 6) errorEnable(R.string.error_weak_password)
            }
        }

    val passwordConfirmTextField: TextFieldAttr =
        TextFieldAttr.signUpPasswordConfirm(textFieldManager).apply {
            onValueChange = {
                onValueChange(it)
                if (it.isNotEmpty())
                    if (it != passwordTextFieldAttr.text) errorEnable(R.string.error_password_confirm_not_same)
                    else if (isErrorEnable) errorDisable()
            }
        }

    private fun setPasswordNotSameError() =
        passwordConfirmTextField.errorEnable(R.string.error_password_confirm_not_same)

    override fun firebaseTask(firebaseExecution: FirebaseExecution) =
        firebaseExecution.createUserWithEmailAndPassword(emailTextFieldAttr.text, passwordTextFieldAttr.text)

    override val onCompleteListener: (Task<*>) -> Unit = {
        if (it.isSuccessful) {
            val userProfileChangeRequest =
                userProfileChangeRequest { displayName = nameTextFieldAttr.text }

            FirebaseAuth.getInstance().currentUser?.
                updateProfile(userProfileChangeRequest)?.addOnCompleteListener(super.onCompleteListener)
        } else taskFailed(it)
        progressionOn = false
    }
}