package com.leebeebeom.clothinghelper.ui.signin.resetpassword

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.FirebaseButton
import com.leebeebeom.clothinghelper.ui.MaxWidthTextField
import com.leebeebeom.clothinghelper.ui.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.ui.SimpleToast
import com.leebeebeom.clothinghelper.ui.signin.signin.SignInColumn

@Composable
fun ResetPasswordScreen(
    navController: NavController,
    viewModel: ResetPasswordViewModel = viewModel()
) {
    SignInColumn(viewModel) {
        Text(
            text = stringResource(id = R.string.reset_password_text),
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(start = 4.dp)
        )
        SimpleHeightSpacer(dp = 4)
        MaxWidthTextField(attr = viewModel.emailTextFieldAttr)
        FirebaseButton(R.string.check, viewModel)
    }
    if (viewModel.isFirebaseTaskSuccessful) {
        SimpleToast(resId = R.string.email_send_complete)
        navController.popBackStack()
    }
}