package com.leebeebeom.clothinghelper.ui.signin.ui.resetpassword

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.component.CenterDotProgressIndicator
import com.leebeebeom.clothinghelper.ui.component.HeightSpacer
import com.leebeebeom.clothinghelper.ui.component.MaxWidthButton
import com.leebeebeom.clothinghelper.ui.main.component.ToastWrapper
import com.leebeebeom.clothinghelper.ui.signin.component.SignInBaseColumn
import com.leebeebeom.clothinghelper.ui.signin.component.textfield.EmailTextField
import com.leebeebeom.clothinghelper.ui.signin.state.EmailState

const val ResetPasswordScreenTag = "reset password screen"

@Composable // skippable
fun ResetPasswordScreen(
    popBackStack: () -> Unit,
    viewModel: ResetPasswordViewModel = hiltViewModel()
) {
    val state = rememberResetPasswordScreenState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SignInBaseColumn(modifier = Modifier.testTag(ResetPasswordScreenTag)) {
        Text(
            text = stringResource(id = R.string.reset_password_text),
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )

        EmailTextField(
            error = { state.emailError },
            imeAction = ImeAction.Done,
            onEmailChange = state::onEmailChange
        )

        val onResetPasswordButtonClick: () -> Unit = remember {
            {
                viewModel.sendResetPasswordEmail(
                    email = state.email,
                    setEmailError = state::setEmailError,
                    popBackStack = popBackStack
                )
            }
        }
        HeightSpacer(dp = 12)
        MaxWidthButton(
            text = R.string.send,
            enabled = { state.buttonEnabled },
            onClick = onResetPasswordButtonClick
        )
    }
    CenterDotProgressIndicator(show = { uiState.isLoading })
    ToastWrapper(toastTexts = { uiState.toastTexts }, toastShown = viewModel::removeFirstToastText)
}

// stable
class ResetPasswordScreenState(
    initialEmail: String = "",
    initialEmailError: Int? = null
) : EmailState(initialEmail = initialEmail, initialEmailError = initialEmailError) {
    companion object {
        val Saver = listSaver<ResetPasswordScreenState, Any?>(
            save = { listOf(it.email, it.emailError) },
            restore = { ResetPasswordScreenState(it[0] as String, it[1] as Int?) }
        )
    }
}

@Composable
fun rememberResetPasswordScreenState() =
    rememberSaveable(saver = ResetPasswordScreenState.Saver) { ResetPasswordScreenState() }