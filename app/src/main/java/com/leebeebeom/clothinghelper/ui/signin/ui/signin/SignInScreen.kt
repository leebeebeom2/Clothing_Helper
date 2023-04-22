package com.leebeebeom.clothinghelper.ui.signin.ui.signin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.component.CenterDotProgressIndicator
import com.leebeebeom.clothinghelper.ui.component.HeightSpacer
import com.leebeebeom.clothinghelper.ui.component.IconWrapper
import com.leebeebeom.clothinghelper.ui.component.MaxWidthButton
import com.leebeebeom.clothinghelper.ui.component.SingleLineText
import com.leebeebeom.clothinghelper.ui.main.component.ToastWrapper
import com.leebeebeom.clothinghelper.ui.signin.component.GoogleSignInButton
import com.leebeebeom.clothinghelper.ui.signin.component.Logo
import com.leebeebeom.clothinghelper.ui.signin.component.OrDivider
import com.leebeebeom.clothinghelper.ui.signin.component.SignInBaseColumn
import com.leebeebeom.clothinghelper.ui.signin.component.textfield.EmailTextField
import com.leebeebeom.clothinghelper.ui.signin.component.textfield.PasswordTextField
import com.leebeebeom.clothinghelper.ui.signin.state.PasswordState

const val SignInScreenTag = "sign in screen"

@Composable // skippable
fun SignInScreen(
    navigateToResetPassword: () -> Unit,
    navigateToSignUp: () -> Unit,
) {
    val viewModel = hiltViewModel<SignInViewModel>()
    val state = rememberSignInScreenState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SignInBaseColumn(modifier = Modifier.testTag(SignInScreenTag)) {
        Logo()
        EmailTextField(
            error = { state.emailError }, onEmailChange = state::onEmailChange
        )
        PasswordTextField(
            error = { state.passwordError },
            onInputChange = state::onPasswordChange,
            imeAction = ImeAction.Done
        )

        ForgotPasswordText(onClick = navigateToResetPassword)

        val signInButtonClick = remember {
            {
                viewModel.signInWithEmailAndPassword(
                    email = state.email,
                    password = state.password,
                    setEmailError = state::setEmailError,
                    setPasswordError = state::setPasswordError
                )
            }
        }
        MaxWidthButton(
            text = R.string.sign_in,
            enabled = { state.buttonEnabled },
            onClick = signInButtonClick,
        )
        OrDivider()
        MaxWidthButton(text = R.string.sign_up_with_email,
            onClick = navigateToSignUp,
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface),
            icon = { IconWrapper(drawable = R.drawable.ic_email) })
        HeightSpacer(dp = 12)
        GoogleSignInButton(onResult = viewModel::signInWithGoogleEmail)
    }
    CenterDotProgressIndicator(show = { uiState.isLoading })
    ToastWrapper(toastTexts = { uiState.toastTexts }, toastShown = viewModel::removeFirstToastText)
}

@Composable // skippable
private fun ColumnScope.ForgotPasswordText(onClick: () -> Unit) {
    SingleLineText(
        modifier = Modifier
            .padding(3.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(3.dp)
            .align(Alignment.End),
        text = R.string.forgot_password,
        style = MaterialTheme.typography.caption.copy(textDecoration = TextDecoration.Underline)
    )
}

// stable
class SignInScreenState(
    initialEmail: String = "",
    initialEmailError: Int? = null,
    initialPassword: String = "",
    initialPasswordError: Int? = null,
) : PasswordState(
    initialEmail = initialEmail,
    initialEmailError = initialEmailError,
    initialPassword = initialPassword,
    initialPasswordError = initialPasswordError
) {
    companion object {
        val Saver = listSaver<SignInScreenState, Any?>(save = {
            arrayListOf(it.email, it.emailError, it.password, it.passwordError)
        }, restore = {
            SignInScreenState(
                it[0] as String,
                it[1] as Int?,
                it[2] as String,
                it[3] as Int?,
            )
        })
    }
}

@Composable
fun rememberSignInScreenState() =
    rememberSaveable(saver = SignInScreenState.Saver) { SignInScreenState() }