package com.leebeebeom.clothinghelper.ui.signin.ui.signup

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.components.CenterDotProgressIndicator
import com.leebeebeom.clothinghelper.ui.components.HeightSpacer
import com.leebeebeom.clothinghelper.ui.components.MaxWidthButton
import com.leebeebeom.clothinghelper.ui.components.StatefulMaxWidthTestFieldWithCancelIcon
import com.leebeebeom.clothinghelper.ui.main.composables.ToastWrapper
import com.leebeebeom.clothinghelper.ui.signin.components.GoogleSignInButton
import com.leebeebeom.clothinghelper.ui.signin.components.Logo
import com.leebeebeom.clothinghelper.ui.signin.components.OrDivider
import com.leebeebeom.clothinghelper.ui.signin.components.SignInBaseColumn
import com.leebeebeom.clothinghelper.ui.signin.components.textfield.EmailTextField
import com.leebeebeom.clothinghelper.ui.signin.components.textfield.PasswordTextField

const val SignUpScreenTag = "sign up screen"

@Composable // skippable
fun SignUpScreen(viewModel: SignUpViewModel = hiltViewModel()) {
    val state = viewModel.signUpState
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SignInBaseColumn(modifier = Modifier.testTag(SignUpScreenTag)) {
        Logo()
        EmailTextField(
            error = { uiState.emailError }, onEmailChange = state::setEmail
        )

        StatefulMaxWidthTestFieldWithCancelIcon(
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
            ), onInputChange = state::setName, blockBlank = false, label = R.string.nickname
        )

        PasswordTextField(
            error = { uiState.passwordError },
            imeAction = ImeAction.Next,
            onInputChange = state::setPassword
        )

        PasswordTextField(
            label = R.string.password_confirm,
            error = { uiState.passwordConfirmError },
            imeAction = ImeAction.Done,
            onInputChange = state::setPasswordConfirm
        )

        HeightSpacer(dp = 12)
        MaxWidthButton(
            text = R.string.sign_up,
            enabled = { uiState.buttonEnabled },
            onClick = viewModel::signUpWithEmailAndPassword,
        )
        OrDivider()
        GoogleSignInButton(onResult = viewModel::signInWithGoogleEmail)
    }
    CenterDotProgressIndicator(show = { uiState.isLoading })
    ToastWrapper(toastTexts = { uiState.toastTexts }, toastShown = viewModel::removeFirstToastText)
}