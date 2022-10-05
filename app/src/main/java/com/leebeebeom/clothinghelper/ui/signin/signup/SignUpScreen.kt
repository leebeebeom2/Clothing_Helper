package com.leebeebeom.clothinghelper.ui.signin.signup

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.*
import com.leebeebeom.clothinghelper.ui.signin.signin.OrDivider
import com.leebeebeom.clothinghelper.ui.signin.signin.SignInColumn

@Composable
fun SignUpScreen(viewModel: SignUpViewModel = viewModel()) {
    SignInColumn(viewModel) {
        MaxWidthTextField(attr = viewModel.emailTextFieldAttr)
        MaxWidthTextField(attr = viewModel.nameTextFieldAttr)
        MaxWidthTextField(attr = viewModel.passwordTextFieldAttr)
        MaxWidthTextField(attr = viewModel.passwordConfirmTextField)
        FirebaseButton(R.string.sign_up, viewModel)
        OrDivider()
        GoogleSignInBtn(viewModel)
    }
    if (viewModel.isFirebaseTaskSuccessful) {
        SimpleToast(resId = R.string.sign_up_complete)
        FinishActivity()
    }
}