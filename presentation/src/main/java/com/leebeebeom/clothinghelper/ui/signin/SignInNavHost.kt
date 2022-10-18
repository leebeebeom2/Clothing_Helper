package com.leebeebeom.clothinghelper.ui.signin

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.ui.signin.resetpassword.ResetPasswordScreen
import com.leebeebeom.clothinghelper.ui.signin.signin.SignInScreen
import com.leebeebeom.clothinghelper.ui.signin.signup.SignUpScreen
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme

object SignInDestinations {
    const val SIGN_IN = "signIn"
    const val SIGN_UP = "signUp"
    const val RESET_PASSWORD = "resetPassword"
}

@Composable
fun SignInNavHost() {
    val navController = rememberNavController()

    ClothingHelperTheme {
        Scaffold {
            NavHost(
                navController = navController,
                startDestination = SignInDestinations.SIGN_IN,
                modifier = Modifier.padding(it)
            ) {
                composable(SignInDestinations.SIGN_IN) {
                    SignInScreen(
                        onForgotPasswordClick = { navController.signNavigate(SignInDestinations.RESET_PASSWORD) },
                        onEmailSignUpClick = { navController.signNavigate(SignInDestinations.SIGN_UP) }
                    )
                }
                composable(SignInDestinations.SIGN_UP) { SignUpScreen() }
                composable(SignInDestinations.RESET_PASSWORD) {
                    ResetPasswordScreen()
                }
            }
        }

    }
}

fun NavController.signNavigate(destination: String) =
    navigate(destination) {
        launchSingleTop = true
        popUpTo(SignInDestinations.SIGN_IN)
    }