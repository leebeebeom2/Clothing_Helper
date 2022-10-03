package com.leebeebeom.clothinghelper.ui.signin.resetpassword

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.FirebaseButton
import com.leebeebeom.clothinghelper.ui.MaxWidthButton
import com.leebeebeom.clothinghelper.ui.MaxWidthTextField
import com.leebeebeom.clothinghelper.ui.SimpleSpacer
import com.leebeebeom.clothinghelper.ui.signin.signin.SignInColumn

@Composable
fun ResetPasswordScreen(
    navController: NavController,
    viewModel: ResetPasswordViewModel = viewModel()
) {
    SignInColumn(viewModel) {
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = stringResource(id = R.string.reset_password_text),
            style = TextStyle(
                fontSize = 15.sp,
                color = MaterialTheme.colors.primary
            )
        )
        SimpleSpacer(dp = 12)
        MaxWidthTextField(attr = viewModel.email)
        FirebaseButton(stringResource(R.string.check), viewModel)
        SimpleSpacer(dp = 260)
        if (viewModel.isFirebaseTaskSuccessful) {
            Toast.makeText(
                LocalContext.current,
                stringResource(R.string.email_send_complete),
                Toast.LENGTH_SHORT
            ).show()
            navController.popBackStack()
        }
    }
}

@Preview
@Composable
fun ResetPasswordPreview() {
    ResetPasswordScreen(navController = rememberNavController())
}