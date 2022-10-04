package com.leebeebeom.clothinghelper.ui.signin.signin

import com.leebeebeom.clothinghelper.data.OutlinedTextFieldAttrFactory
import com.leebeebeom.clothinghelper.ui.signin.CHGoogleSignIn
import com.leebeebeom.clothinghelper.ui.signin.CHFirebase
import com.leebeebeom.clothinghelper.ui.signin.SignInBaseViewModel

class SignInViewModel : SignInBaseViewModel(), CHGoogleSignIn {
    override val emailTextFieldAttr = OutlinedTextFieldAttrFactory.email()
    val passwordTextFieldAttr = OutlinedTextFieldAttrFactory.password()

    override val isTextFieldEmpty get() = emailTextFieldAttr.isEmpty || passwordTextFieldAttr.isEmpty
    override val isErrorEnable get() = emailTextFieldAttr.isErrorEnabled || passwordTextFieldAttr.isErrorEnabled

    override fun firebaseTask(chFirebase: CHFirebase) =
        chFirebase.signInWithEmailAndPassword(
            emailTextFieldAttr.text,
            passwordTextFieldAttr.text
        )
}