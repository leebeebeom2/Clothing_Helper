package com.leebeebeom.clothinghelper.ui.signin.signin

import com.leebeebeom.clothinghelper.data.OutlinedTextFieldAttr
import com.leebeebeom.clothinghelper.ui.signin.CHFirebase
import com.leebeebeom.clothinghelper.ui.signin.CHGoogleSignIn
import com.leebeebeom.clothinghelper.ui.signin.SignInBaseViewModel

class SignInViewModel : SignInBaseViewModel(), CHGoogleSignIn {
    override val emailTextFieldAttr = OutlinedTextFieldAttr.email()
    val passwordTextFieldAttr = OutlinedTextFieldAttr.password()

    override val isTextFieldEmpty get() = emailTextFieldAttr.isEmpty || passwordTextFieldAttr.isEmpty
    override val isErrorEnable get() = emailTextFieldAttr.isErrorEnabled || passwordTextFieldAttr.isErrorEnabled

    override fun firebaseTask(chFirebase: CHFirebase) =
        chFirebase.signInWithEmailAndPassword(
            emailTextFieldAttr.text,
            passwordTextFieldAttr.text
        )
}