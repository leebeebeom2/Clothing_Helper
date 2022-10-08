package com.leebeebeom.clothinghelper.ui.signin.signin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.leebeebeom.clothinghelper.data.TextFieldState
import com.leebeebeom.clothinghelper.ui.signin.FirebaseUseCase
import com.leebeebeom.clothinghelper.ui.signin.GoogleSignInImpl
import com.leebeebeom.clothinghelper.ui.signin.SignInBaseViewModel

class SignInViewModel : SignInBaseViewModel(), GoogleSignInImpl {
    override val passwordTextFieldState by mutableStateOf(
        TextFieldState(essentialTextFields, true)
    )

    override fun firebaseTask(firebaseUseCase: FirebaseUseCase) =
        firebaseUseCase.signInWithEmailAndPassword(
            emailTextFieldState.text,
            passwordTextFieldState.text
        )
}