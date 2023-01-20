package com.leebeebeom.clothinghelper.ui.signin.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInDestinations.ResetPassword
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInDestinations.SignIN
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInDestinations.SignUp
import com.leebeebeom.clothinghelper.ui.signin.ui.resetpassword.ResetPasswordScreen
import com.leebeebeom.clothinghelper.ui.signin.ui.signin.SignInScreen
import com.leebeebeom.clothinghelper.ui.signin.ui.signup.SignUpScreen
import com.leebeebeom.clothinghelper.util.navigateSingleTop

object SignInDestinations {
    const val SignIN = "signIn"
    const val SignUp = "signUp"
    const val ResetPassword = "resetPassword"
}

@Composable
fun SignInNavHost(navController: NavHostController = rememberNavController()) {
    SignInRoot {
        NavHost(
            navController = navController,
            startDestination = SignIN
        ) {
            composable(route = SignIN) {
                SignInScreen(
                    onForgotPasswordClick = navController::navigateToResetPassword,
                    onSignUpClick = navController::navigateToSignUp
                )
            }
            composable(route = SignUp) { SignUpScreen() }
            composable(route = ResetPassword) { ResetPasswordScreen() }
        }
    }
}

private fun NavHostController.navigateToSignUp() = navigateSingleTop(SignUp)
private fun NavHostController.navigateToResetPassword() = navigateSingleTop(ResetPassword)