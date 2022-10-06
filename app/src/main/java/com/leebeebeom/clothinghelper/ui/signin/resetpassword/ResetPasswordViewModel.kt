package com.leebeebeom.clothinghelper.ui.signin.resetpassword

import com.leebeebeom.clothinghelper.data.TextFieldState
import com.leebeebeom.clothinghelper.ui.signin.FirebaseExecutor
import com.leebeebeom.clothinghelper.ui.signin.SignInBaseViewModel

class ResetPasswordViewModel : SignInBaseViewModel() {
    override val passwordTextFieldState: TextFieldState? = null

    override fun firebaseTask(firebaseExecutor: FirebaseExecutor) =
        firebaseExecutor.sendResetPasswordEmail(emailTextFieldState.text)
}