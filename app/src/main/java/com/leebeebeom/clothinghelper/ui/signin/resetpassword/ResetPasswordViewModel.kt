package com.leebeebeom.clothinghelper.ui.signin.resetpassword

import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.data.OutlinedTextFieldAttrFactory
import com.leebeebeom.clothinghelper.ui.signin.SignInBaseViewModel

class ResetPasswordViewModel : SignInBaseViewModel() {
    override val email = OutlinedTextFieldAttrFactory.resetPasswordEmail()
    override val isTextFieldEmpty get() = email.isEmpty
    override val isErrorEnable get() = email.isErrorEnabled

    override fun firebaseTask() {
        FirebaseAuth.getInstance()
            .sendPasswordResetEmail(email.text)
            .addOnCompleteListener(onCompleteListener)
    }
}