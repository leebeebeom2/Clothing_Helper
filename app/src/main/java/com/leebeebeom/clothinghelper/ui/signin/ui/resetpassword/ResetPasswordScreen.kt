package com.leebeebeom.clothinghelper.ui.signin.ui.resetpassword

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.ActivityViewModel
import com.leebeebeom.clothinghelper.ui.activityViewModel
import com.leebeebeom.clothinghelper.ui.components.HeightSpacer
import com.leebeebeom.clothinghelper.ui.components.ImeActionRoute
import com.leebeebeom.clothinghelper.ui.components.MaxWidthButton
import com.leebeebeom.clothinghelper.ui.signin.components.SignInBaseColumn
import com.leebeebeom.clothinghelper.ui.signin.components.textfield.EmailTextField

@Composable
fun ResetPasswordScreen(
    popBackStack: () -> Unit,
    viewModel: ResetPasswordViewModel = hiltViewModel(),
    uiState: ResetPasswordUiState = viewModel.uiState,
    activityViewModel: ActivityViewModel = activityViewModel()
) {
    PopBackStack(
        taskSuccess = { uiState.isTaskSuccess },
        popBackStack = popBackStack,
        consumeTaskSuccess = viewModel::consumeTaskSuccess
    )

    SignInBaseColumn {
        Text(
            text = stringResource(id = R.string.reset_password_text),
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )

        EmailTextField(
            error = { uiState.emailError },
            imeActionRoute = ImeActionRoute.DONE,
            onInputChange = viewModel::onEmailChange
        )

        HeightSpacer(dp = 12)
        MaxWidthButton(text = R.string.send,
            enabled = { uiState.buttonEnabled },
            onClick = { viewModel.sendResetPasswordEmail(showToast = activityViewModel::showToast) })
    }
}

@Composable
private fun PopBackStack(
    taskSuccess: () -> Boolean,
    popBackStack: () -> Unit,
    consumeTaskSuccess: () -> Unit
) {
    if (taskSuccess()) {
        popBackStack()
        consumeTaskSuccess()
    }
}