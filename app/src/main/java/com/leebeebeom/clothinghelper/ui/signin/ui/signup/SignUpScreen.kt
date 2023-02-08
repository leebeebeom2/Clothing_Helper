package com.leebeebeom.clothinghelper.ui.signin.ui.signup

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.ActivityViewModel
import com.leebeebeom.clothinghelper.ui.activityViewModel
import com.leebeebeom.clothinghelper.ui.components.*
import com.leebeebeom.clothinghelper.ui.signin.components.GoogleSignInButton
import com.leebeebeom.clothinghelper.ui.signin.components.Logo
import com.leebeebeom.clothinghelper.ui.signin.components.OrDivider
import com.leebeebeom.clothinghelper.ui.signin.components.SignInBaseColumn
import com.leebeebeom.clothinghelper.ui.signin.components.textfield.EmailTextField
import com.leebeebeom.clothinghelper.ui.signin.components.textfield.INVISIBLE_ICON
import com.leebeebeom.clothinghelper.ui.signin.components.textfield.PasswordTextField
import com.leebeebeom.clothinghelper.ui.signin.components.textfield.VISIBLE_ICON
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInNavUiState
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInNavViewModel

const val SIGN_UP_SCREEN_TAG = "sign up screen"
const val PASSWORD_VISIBLE_ICON = "password $VISIBLE_ICON"
const val PASSWORD_CONFIRM_VISIBLE_ICON = "password confirm $VISIBLE_ICON"
const val PASSWORD_INVISIBLE_ICON = "password $INVISIBLE_ICON"
const val PASSWORD_CONFIRM_INVISIBLE_ICON = "password confirm $INVISIBLE_ICON"

@Composable
fun SignUpScreen(
    signInNavViewModel: SignInNavViewModel,
    signInNavUiState: SignInNavUiState = signInNavViewModel.uiState,
    viewModel: SignUpViewModel = hiltViewModel(),
    uiState: SignUpUiState = viewModel.uiState,
    activityViewModel: ActivityViewModel = activityViewModel()
) {
    SignInBaseColumn(modifier = Modifier.testTag(SIGN_UP_SCREEN_TAG)) {
        Logo()
        EmailTextField(error = { uiState.emailError }, onInputChange = viewModel::onEmailChange)

        val nickNameTextFieldState = rememberMaxWidthTextFieldState(
            label = R.string.nickname,
            imeActionRoute = ImeActionRoute.NEXT
        )

        MaxWidthTextFieldWithError(
            state = nickNameTextFieldState,
            onValueChange = nickNameTextFieldState::onValueChange,
            onFocusChanged = nickNameTextFieldState::onFocusChanged,
            onInputChange = viewModel::onNickNameChange
        )

        PasswordTextField(
            error = { uiState.passwordError },
            onInputChange = viewModel::onPasswordChange,
            imeActionRoute = ImeActionRoute.NEXT,
            visibleIconDescription = PASSWORD_VISIBLE_ICON,
            invisibleIconDescription = PASSWORD_INVISIBLE_ICON
        )

        PasswordTextField(
            label = R.string.password_confirm,
            error = { uiState.passwordConfirmError },
            onInputChange = viewModel::onPasswordConfirmChange,
            visibleIconDescription = PASSWORD_CONFIRM_VISIBLE_ICON,
            invisibleIconDescription = PASSWORD_CONFIRM_INVISIBLE_ICON,
        )

        HeightSpacer(dp = 12)
        MaxWidthButton(
            text = R.string.sign_up,
            enabled = { uiState.buttonEnabled },
            onClick = { viewModel.signUpWithEmailAndPassword(activityViewModel::showToast) },
        )
        OrDivider()
        GoogleSignInButton(enabled = { signInNavUiState.googleButtonEnabled },
            onActivityResult = {
                signInNavViewModel.signInWithGoogleEmail(
                    showToast = activityViewModel::showToast,
                    activityResult = it
                )
            },
            disable = { signInNavViewModel.setGoogleButtonEnable(false) })
    }
}