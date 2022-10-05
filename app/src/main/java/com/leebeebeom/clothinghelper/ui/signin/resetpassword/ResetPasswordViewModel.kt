package com.leebeebeom.clothinghelper.ui.signin.resetpassword

import com.leebeebeom.clothinghelper.data.TextFieldAttr
import com.leebeebeom.clothinghelper.ui.signin.FirebaseExecution
import com.leebeebeom.clothinghelper.ui.signin.SignInBaseViewModel

class ResetPasswordViewModel : SignInBaseViewModel() {
    override val emailTextFieldAttr = TextFieldAttr.resetPasswordEmail(textFieldManager)
    override val passwordTextFieldAttr: TextFieldAttr? = null

    override fun firebaseTask(firebaseExecution: FirebaseExecution) =
        firebaseExecution.sendResetPasswordEmail(emailTextFieldAttr.text)
}