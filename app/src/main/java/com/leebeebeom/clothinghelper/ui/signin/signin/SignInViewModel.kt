package com.leebeebeom.clothinghelper.ui.signin.signin

import com.leebeebeom.clothinghelper.data.TextFieldAttr
import com.leebeebeom.clothinghelper.data.TextFieldManager
import com.leebeebeom.clothinghelper.ui.signin.FirebaseExecution
import com.leebeebeom.clothinghelper.ui.signin.CHGoogleSignIn
import com.leebeebeom.clothinghelper.ui.signin.SignInBaseViewModel

class SignInViewModel : SignInBaseViewModel(), CHGoogleSignIn {
    override val emailTextFieldAttr = TextFieldAttr.signInEmail(textFieldManager)
    override val passwordTextFieldAttr = TextFieldAttr.signInPassword(textFieldManager)

    override fun firebaseTask(firebaseExecution: FirebaseExecution) =
        firebaseExecution.signInWithEmailAndPassword(
            emailTextFieldAttr.text,
            passwordTextFieldAttr.text
        )
}