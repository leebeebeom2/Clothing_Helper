package com.leebeebeom.clothinghelper.ui.signin.ui.signup

import androidx.activity.result.ActivityResult
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.leebeebeom.clothinghelper.ui.signin.components.GoogleSignInButton
import com.leebeebeom.clothinghelper.ui.signin.components.Logo
import com.leebeebeom.clothinghelper.ui.signin.components.OrDivider
import com.leebeebeom.clothinghelper.ui.signin.components.SignInBaseColumn
import com.leebeebeom.clothinghelper.ui.signin.components.textfield.EmailTextField
import com.leebeebeom.clothinghelper.ui.signin.components.textfield.PasswordTextField
import com.leebeebeom.clothinghelper.ui.util.ShowToast

const val SignUpScreenTag = "sign up screen"

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(), showToast: ShowToast
) {
    val state = viewModel.signUpState
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SignInBaseColumn(modifier = Modifier.testTag(SignUpScreenTag)) {
        Logo()
        EmailTextField(
            initialEmail = state.email.savedValue,
            error = { uiState.emailError },
            onEmailChange = state::setEmail
        )

        StatefulMaxWidthTestFieldWithCancelIcon(
            initialText = state.name.savedValue, keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
            ), onInputChange = state::setName, blockBlank = false, label = R.string.nickname
        )

        PasswordTextField(
            initialPassword = state.password.savedValue,
            error = { uiState.passwordError },
            imeAction = ImeAction.Next,
            onInputChange = state::setPassword
        )

        PasswordTextField(
            label = R.string.password_confirm,
            initialPassword = state.passwordConfirm.savedValue,
            error = { uiState.passwordConfirmError },
            imeAction = ImeAction.Done,
            onInputChange = state::setPasswordConfirm
        )

        val onSignUpButtonClick: () -> Unit = remember {
            {
                viewModel.signUpWithEmailAndPassword(
                    showToast = showToast, setLoading = state::setLoading
                )
            }
        }
        HeightSpacer(dp = 12)
        MaxWidthButton(
            text = R.string.sign_up,
            enabled = { uiState.buttonEnabled },
            onClick = onSignUpButtonClick,
        )
        OrDivider()

        val onActivityResult: (ActivityResult) -> Unit = remember {
            { viewModel.signInWithGoogleEmail(activityResult = it, showToast = showToast) }
        }
        GoogleSignInButton(
            enabled = { uiState.googleButtonEnabled },
            onActivityResult = onActivityResult,
            disable = state::googleButtonDisable
        )
    }
    CenterDotProgressIndicator(show = { uiState.isLoading })
}