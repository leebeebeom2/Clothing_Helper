package com.leebeebeom.clothinghelper.ui.signin.resetpassword

import com.leebeebeom.clothinghelper.data.TextFieldState
import com.leebeebeom.clothinghelper.ui.signin.FirebaseUseCase
import com.leebeebeom.clothinghelper.ui.signin.SignInBaseViewModel

class ResetPasswordViewModel : SignInBaseViewModel() {
    override val passwordTextFieldState: TextFieldState? = null

    override fun firebaseTask(firebaseUseCase: FirebaseUseCase) =
        firebaseUseCase.sendResetPasswordEmail(emailTextFieldState.text)
}