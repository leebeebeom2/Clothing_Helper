package com.leebeebeom.clothinghelper.ui.signin.signin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.*
import com.leebeebeom.clothinghelper.ui.signin.RESET_PASSWORD
import com.leebeebeom.clothinghelper.ui.signin.SIGN_IN
import com.leebeebeom.clothinghelper.ui.signin.SIGN_UP
import com.leebeebeom.clothinghelper.ui.signin.SignInBaseViewModel
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import com.leebeebeom.clothinghelper.ui.theme.PrimaryVariant

@Composable
fun SignInScreen(navController: NavController, viewModel: SignInViewModel = viewModel()) {
    SignInColumn(viewModel) {
        SimpleSpacer(dp = 100)
        MaxWidthTextField(attr = viewModel.email)
        MaxWidthTextField(attr = viewModel.password)
        SimpleSpacer(dp = 2)
        Box(modifier = Modifier.fillMaxWidth()) {
            ClickableText(
                text = AnnotatedString(stringResource(R.string.forget_password)),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0XFF6A707C)
                ),
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                onClick = { navigate(navController, RESET_PASSWORD) }
            )
        }
        SimpleSpacer(dp = 12)
        FirebaseButton(stringResource(id = R.string.login), viewModel)
        OrDivider()
        GoogleSignInBtn(viewModel)
    }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 20.dp)
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
                style = TextStyle(
                    color = PrimaryVariant,
                    fontSize = 15.sp
                )
            )
            Spacer(modifier = Modifier.width(6.dp))
            ClickableText(
                text = AnnotatedString(stringResource(id = R.string.sign_up_with_email)),
                style = TextStyle(
                    color = Color(0xFF35C2C1),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                ),
                onClick = { navController.navigate(SIGN_UP) }
            )
        }
    }
    if (viewModel.isFirebaseTaskSuccessful) {
        SimpleToast(resId = R.string.login_complete)
        FinishActivity()
    }
}

@Composable
fun SignInColumn(viewModel: SignInBaseViewModel, content: @Composable () -> Unit) {
    ClothingHelperTheme {
        Scaffold {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
            ) { content() }
            if (viewModel.isProgressionOn) CenterCircularProgressIndicator()
        }
    }
}

@Composable
fun OrDivider() {
    SimpleSpacer(dp = 12)
    Row(verticalAlignment = Alignment.CenterVertically) {
        Divider(modifier = Modifier.weight(1f), color = Color(0xFFE8ECF4))
        Text(
            text = stringResource(id = R.string.or),
            modifier = Modifier
                .padding(horizontal = 14.dp)
                .align(Alignment.CenterVertically),
            style = TextStyle(
                color = Color(0xFF6A707C),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        )
        Divider(modifier = Modifier.weight(1f), color = Color(0xFFE8ECF4))
    }
}

@Preview
@Composable
fun SignInScreePreview() {
    val navController = rememberNavController()
    SignInScreen(navController = navController)
}

fun navigate(navController: NavController, destination: String) {
    navController.navigate(destination) {
        popUpTo(SIGN_IN)
    }
}