package com.leebeebeom.clothinghelper.ui.signin.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leebeebeom.clothinghelper.ui.MainNavRoute
import com.leebeebeom.clothinghelper.ui.signin.ui.resetpassword.ResetPasswordScreen
import com.leebeebeom.clothinghelper.ui.signin.ui.signin.SignInScreen
import com.leebeebeom.clothinghelper.ui.signin.ui.signup.SignUpScreen

object SignInGraphRoute {
    const val SignInScreen = "sign in"
    const val SignUpScreen = "sign up"
    const val ResetPasswordScreen = "reset password"
}

fun NavGraphBuilder.signInGraph(navController: NavHostController) {
    navigation(
        startDestination = SignInGraphRoute.SignInScreen,
        route = MainNavRoute.SignInGraph,
    ) {
        composable(route = SignInGraphRoute.SignInScreen) {
            SignInScreen(
                navigateToResetPassword = navController::navigateToResetPassword,
                navigateToSignUp = navController::navigateToSignUp
            )
        }
        composable(route = SignInGraphRoute.SignUpScreen) { SignUpScreen() }
        composable(route = SignInGraphRoute.ResetPasswordScreen) {
            ResetPasswordScreen(
                popBackStack = navController::popBackStack
            )
        }
    }
}

private fun NavHostController.navigateToSignUp() = navigate(SignInGraphRoute.SignUpScreen)
private fun NavHostController.navigateToResetPassword() =
    navigate(SignInGraphRoute.ResetPasswordScreen)