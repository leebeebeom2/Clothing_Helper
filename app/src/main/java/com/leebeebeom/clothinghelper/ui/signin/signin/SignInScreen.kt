package com.leebeebeom.clothinghelper.ui.signin.signin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.*
import com.leebeebeom.clothinghelper.ui.signin.SignInBaseViewModel
import com.leebeebeom.clothinghelper.ui.signin.SignInNavigationRoute
import com.leebeebeom.clothinghelper.ui.theme.DisabledDeep

@Composable
fun SignInScreen(navController: NavController, viewModel: SignInViewModel = viewModel()) {
    SignInColumn(viewModel) {
        MaxWidthTextField(attr = viewModel.emailTextFieldAttr)
        MaxWidthTextField(attr = viewModel.passwordTextFieldAttr)
        ForgotPasswordText(navController)
        FirebaseButton(R.string.login, viewModel)
        OrDivider()
        GoogleSignInBtn(viewModel)
    }
    SignUpText(navController)
    if (viewModel.isFirebaseTaskSuccessful) {
        SimpleToast(resId = R.string.login_complete)
        FinishActivity()
    }
}

@Composable
private fun SignUpText(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 32.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.dont_have_an_account),
                style = MaterialTheme.typography.body2
            )
            SimpleWidthSpacer(dp = 6)
            Text(
                text = AnnotatedString(stringResource(id = R.string.sign_up_with_email)),
                style = MaterialTheme.typography.body2,
                color = Color(0xFF35C2C1),
                modifier =
                Modifier.clickable { navigate(navController, SignInNavigationRoute.SIGN_UP) },
            )
        }
    }
}

@Composable
private fun ForgotPasswordText(navController: NavController) {
    SimpleHeightSpacer(dp = 8)
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.forget_password),
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable {
                    navigate(
                        navController,
                        SignInNavigationRoute.RESET_PASSWORD
                    )
                }
        )
    }
}

@Composable
fun SignInColumn(viewModel: SignInBaseViewModel, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        content = content
    )
    if (viewModel.progressionOn) CenterCircularProgressIndicator()
    if (viewModel.isFirebaseTaskFailed) SimpleToast(resId = R.string.unknown_error)
}

@Composable
fun OrDivider() {
    SimpleHeightSpacer(dp = 12)
    Row(verticalAlignment = Alignment.CenterVertically) {
        val dividerModifier = Modifier.weight(1f)
        Weight1Divider(dividerModifier)
        Text(
            text = stringResource(id = R.string.or),
            modifier = Modifier
                .padding(horizontal = 14.dp)
                .align(Alignment.CenterVertically),
            style = MaterialTheme.typography.body2,
            color = DisabledDeep
        )
        Weight1Divider(dividerModifier)
    }
}

@Composable
fun Weight1Divider(modifier: Modifier = Modifier) = Divider(modifier = modifier)