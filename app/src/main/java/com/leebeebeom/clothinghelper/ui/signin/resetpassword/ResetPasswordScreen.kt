package com.leebeebeom.clothinghelper.ui.signin.resetpassword

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.FirebaseButton
import com.leebeebeom.clothinghelper.ui.MaxWidthTextField
import com.leebeebeom.clothinghelper.ui.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.ui.SimpleToast
import com.leebeebeom.clothinghelper.ui.signin.signin.SignInColumn

@Composable
fun ResetPasswordScreen(navController: NavController, viewModel: ResetPasswordViewModel = viewModel()) {
    SignInColumn(viewModel) {
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = stringResource(id = R.string.reset_password_text),
            style = MaterialTheme.typography.body2
        )
        SimpleHeightSpacer(dp = 12)
        MaxWidthTextField(attr = viewModel.emailTextFieldAttr)
        FirebaseButton(R.string.check, viewModel)
        SimpleHeightSpacer(dp = 260)
        if (viewModel.isFirebaseTaskSuccessful) {
            SimpleToast(resId = R.string.email_send_complete)
            navController.popBackStack()
        }
    }
}

@Preview
@Composable
fun ResetPasswordPreview() {
    ResetPasswordScreen(rememberNavController())
}