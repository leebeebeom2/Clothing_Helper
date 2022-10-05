package com.leebeebeom.clothinghelper.ui.signin.resetpassword

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
        Box(
            modifier = Modifier
                .padding(start = 4.dp, top = 356.dp)
                .align(Alignment.TopStart)
        ) {
            Text(
                text = stringResource(id = R.string.reset_password_text),
                style = MaterialTheme.typography.body2
            )
        }
        MaxWidthTextField(attr = viewModel.emailTextFieldAttr)
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            FirebaseButton(R.string.check, viewModel)
            SimpleHeightSpacer(dp = 380)
        }
    }
    if (viewModel.isFirebaseTaskSuccessful) {
        SimpleToast(resId = R.string.email_send_complete)
        navController.popBackStack()
    }
}