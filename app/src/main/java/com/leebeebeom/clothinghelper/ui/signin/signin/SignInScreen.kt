package com.leebeebeom.clothinghelper.ui.signin.signin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.*
import com.leebeebeom.clothinghelper.ui.signin.SignInBaseViewModel
import com.leebeebeom.clothinghelper.ui.signin.SignInNavigationRoute

@Composable
fun SignInScreen(
    navController: NavController = rememberNavController(),
    viewModel: SignInViewModel = viewModel()
) {
    SignInColumn(viewModel) {
        SimpleHeightSpacer(dp = 100)
        MaxWidthTextField(attr = viewModel.emailTextFieldAttr)
        MaxWidthTextField(attr = viewModel.passwordTextFieldAttr)
        SimpleHeightSpacer(dp = 8)
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = AnnotatedString(stringResource(R.string.forget_password)),
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable { navigate(navController, SignInNavigationRoute.RESET_PASSWORD) }
            )
        }
        SimpleHeightSpacer(dp = 12)
        FirebaseButton(R.string.login, viewModel)
        OrDivider()
        GoogleSignInBtn(viewModel)
    }
    Box(
        modifier = Modifier
            .fillMaxHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center
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
    if (viewModel.isFirebaseTaskSuccessful) {
        SimpleToast(resId = R.string.login_complete)
        FinishActivity()
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
}

@Composable
fun OrDivider() {
    SimpleHeightSpacer(dp = 12)
    Row(verticalAlignment = Alignment.CenterVertically) {
        val dividerModifier = Modifier.weight(1f)
        CHDivider(dividerModifier)
        Text(
            text = stringResource(id = R.string.or),
            modifier = Modifier
                .padding(horizontal = 14.dp)
                .align(Alignment.CenterVertically),
            style = MaterialTheme.typography.body2,
            color = Color(0xFF6A707C)
        )
        CHDivider(dividerModifier)
    }
}

@Preview
@Composable
fun SignInScreePreview() {
    SignInScreen()
}