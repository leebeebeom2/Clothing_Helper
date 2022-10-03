package com.leebeebeom.clothinghelper.ui.signin.signin

import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.data.OutlinedTextFieldAttrFactory
import com.leebeebeom.clothinghelper.ui.signin.GoogleSignIn
import com.leebeebeom.clothinghelper.ui.signin.SignInBaseViewModel

class SignInViewModel : SignInBaseViewModel(), GoogleSignIn {
    override val email = OutlinedTextFieldAttrFactory.email()
    val password = OutlinedTextFieldAttrFactory.password()

    override val isTextFieldEmpty get() = email.isEmpty || password.isEmpty
    override val isErrorEnable get() = email.isErrorEnabled || password.isErrorEnabled

    override fun firebaseTask() {
        super.onFirebaseButtonClick
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email.text, password.text)
            .addOnCompleteListener(onCompleteListener)
    }
}