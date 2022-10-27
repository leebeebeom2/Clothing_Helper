package com.leebeebeom.clothinghelper.signin

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.signin.resetpassword.ResetPasswordScreen
import com.leebeebeom.clothinghelper.signin.signin.SignInScreen
import com.leebeebeom.clothinghelper.signin.signup.SignUpScreen
import com.leebeebeom.clothinghelper.theme.ClothingHelperTheme

object SignInDestinations {
    const val SIGN_IN = "signIn"
    const val SIGN_UP = "signUp"
    const val RESET_PASSWORD = "resetPassword"
}

@Composable
fun SignInNavHost() {
    val navController = rememberNavController()

    ClothingHelperTheme {
        NavHost(
            navController = navController,
            startDestination = SignInDestinations.SIGN_IN
        ) {
            composable(route = SignInDestinations.SIGN_IN) {
                SignInScreen(
                    onForgotPasswordClick = { navController.signNavigate(SignInDestinations.RESET_PASSWORD) },
                    onEmailSignUpClick = { navController.signNavigate(SignInDestinations.SIGN_UP) }
                )
            }
            composable(route = SignInDestinations.SIGN_UP) { SignUpScreen { navController.popBackStack() } }
            composable(route = SignInDestinations.RESET_PASSWORD) { ResetPasswordScreen { navController.popBackStack() } }
        }
    }
}


fun NavController.signNavigate(destination: String) =
    navigate(destination) {
        launchSingleTop = true
        popUpTo(SignInDestinations.SIGN_IN)
    }