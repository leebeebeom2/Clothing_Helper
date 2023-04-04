package com.leebeebeom.clothinghelper.ui.signin.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leebeebeom.clothinghelper.ui.MainNavRoute
import com.leebeebeom.clothinghelper.ui.signin.ui.resetpassword.ResetPasswordScreen
import com.leebeebeom.clothinghelper.ui.signin.ui.signin.SignInScreen
import com.leebeebeom.clothinghelper.ui.signin.ui.signup.SignUpScreen

enum class SignInGraphRoute {
    SignInScreen, SignUpScreen, ResetPasswordScreen
}

fun NavGraphBuilder.signInGraph(navController: NavHostController) {
    navigation(
        startDestination = SignInGraphRoute.SignInScreen.name,
        route = MainNavRoute.SignInGraph,
    ) {
        composable(route = SignInGraphRoute.SignInScreen.name) {
            SignInScreen(
                navigateToResetPassword = navController::navigateToResetPassword,
                navigateToSignUp = navController::navigateToSignUp
            )
        }
        composable(route = SignInGraphRoute.SignUpScreen.name) { SignUpScreen() }
        composable(route = SignInGraphRoute.ResetPasswordScreen.name) {
            ResetPasswordScreen(
                popBackStack = navController::popBackStack
            )
        }
    }
}

private fun NavHostController.navigateToSignUp() = navigate(SignInGraphRoute.SignUpScreen.name)
private fun NavHostController.navigateToResetPassword() =
    navigate(SignInGraphRoute.ResetPasswordScreen.name)