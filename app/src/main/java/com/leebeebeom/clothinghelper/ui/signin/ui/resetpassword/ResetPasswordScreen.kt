package com.leebeebeom.clothinghelper.ui.signin.ui.resetpassword

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.components.CenterDotProgressIndicator
import com.leebeebeom.clothinghelper.ui.components.HeightSpacer
import com.leebeebeom.clothinghelper.ui.components.MaxWidthButton
import com.leebeebeom.clothinghelper.ui.main.composables.ToastWrapper
import com.leebeebeom.clothinghelper.ui.signin.components.SignInBaseColumn
import com.leebeebeom.clothinghelper.ui.signin.components.textfield.EmailTextField

const val ResetPasswordScreenTag = "reset password screen"

@Composable // skippable
fun ResetPasswordScreen(
    popBackStack: () -> Unit,
    viewModel: ResetPasswordViewModel = hiltViewModel()
) {
    val state = viewModel.resetPasswordState
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SignInBaseColumn(modifier = Modifier.testTag(ResetPasswordScreenTag)) {
        Text(
            text = stringResource(id = R.string.reset_password_text),
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )

        EmailTextField(
            initialEmail = state.email.savedValue,
            error = { uiState.emailError },
            imeAction = ImeAction.Done,
            onEmailChange = state::setEmail
        )

        val onResetPasswordButtonClick: () -> Unit = remember {
            {
                viewModel.sendResetPasswordEmail(popBackStack = popBackStack)
            }
        }
        HeightSpacer(dp = 12)
        MaxWidthButton(
            text = R.string.send,
            enabled = { uiState.buttonEnabled },
            onClick = onResetPasswordButtonClick
        )
    }
    CenterDotProgressIndicator(show = { uiState.isLoading })
    ToastWrapper(toastTexts = { uiState.toastTexts }, toastShown = viewModel::removeFirstToastText)
}