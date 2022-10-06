package com.leebeebeom.clothinghelper.ui.signin.signin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.leebeebeom.clothinghelper.data.TextFieldState
import com.leebeebeom.clothinghelper.ui.signin.FirebaseExecutor
import com.leebeebeom.clothinghelper.ui.signin.GoogleSignInImpl
import com.leebeebeom.clothinghelper.ui.signin.SignInBaseViewModel

class SignInViewModel : SignInBaseViewModel(), GoogleSignInImpl {
    override val passwordTextFieldState by mutableStateOf(
        TextFieldState(essentialTextFields, true)
    )

    override fun firebaseTask(firebaseExecutor: FirebaseExecutor) =
        firebaseExecutor.signInWithEmailAndPassword(
            emailTextFieldState.text,
            passwordTextFieldState.text
        )
}