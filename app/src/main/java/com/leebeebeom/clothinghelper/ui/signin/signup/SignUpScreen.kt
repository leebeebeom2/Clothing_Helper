package com.leebeebeom.clothinghelper.ui.signin.signup

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.*
import com.leebeebeom.clothinghelper.ui.signin.signin.OrDivider
import com.leebeebeom.clothinghelper.ui.signin.signin.SignInColumn
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme

@Composable
fun SignUpScreen(viewModel: SignUpViewModel = viewModel()) {
    SignInColumn(viewModel) {
        SimpleHeightSpacer(dp = 60)
        MaxWidthTextField(attr = viewModel.nameTextFieldAttr)
        MaxWidthTextField(attr = viewModel.emailTextFieldAttr)
        MaxWidthTextField(attr = viewModel.passwordTextFieldAttr)
        MaxWidthTextField(attr = viewModel.passwordConfirmTextField)
        FirebaseButton(R.string.sign_up, viewModel)
        OrDivider()
        GoogleSignInBtn(viewModel)
    }
    if (viewModel.isFirebaseTaskSuccessful) {
        SimpleToast(resId =R.string.sign_up_complete )
        FinishActivity()
    }
}

@Preview
@Composable
fun SignUpPreview() {
    ClothingHelperTheme {
        Scaffold {
            SignUpScreen()
        }
    }
}
