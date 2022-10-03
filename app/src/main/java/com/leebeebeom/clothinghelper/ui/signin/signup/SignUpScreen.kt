package com.leebeebeom.clothinghelper.ui.signin.signup

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.*
import com.leebeebeom.clothinghelper.ui.signin.signin.OrDivider
import com.leebeebeom.clothinghelper.ui.signin.signin.SignInColumn

@Composable
fun SignUpScreen(viewModel: SignUpViewModel = viewModel()) {
    SignInColumn(viewModel) {
        SimpleSpacer(dp = 60)
        MaxWidthTextField(attr = viewModel.name)
        MaxWidthTextField(attr = viewModel.email)
        MaxWidthTextField(attr = viewModel.password)
        MaxWidthTextField(attr = viewModel.passwordConfirm)
        FirebaseButton(text = stringResource(id = R.string.sign_up), viewModel)
        OrDivider()
        GoogleSignInBtn(viewModel)
    }
    if (viewModel.isFirebaseTaskSuccessful) {
        Toast.makeText(
            LocalContext.current,
            stringResource(R.string.sign_up_complete),
            Toast.LENGTH_SHORT
        ).show()
        FinishActivity()
    }
}

@Preview
@Composable
fun SignUpPreview() {
    SignUpScreen()
}
