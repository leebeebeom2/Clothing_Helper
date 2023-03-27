package com.leebeebeom.clothinghelper.ui.signin.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leebeebeom.clothinghelper.ui.MainActivityRoutes
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInDestinations.ResetPasswordRoute
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInDestinations.SignInRoute
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInDestinations.SignUpRoute
import com.leebeebeom.clothinghelper.ui.signin.ui.resetpassword.ResetPasswordScreen
import com.leebeebeom.clothinghelper.ui.signin.ui.signin.SignInScreen
import com.leebeebeom.clothinghelper.ui.signin.ui.signup.SignUpScreen

object SignInDestinations {
    const val SignInRoute = "signIn"
    const val SignUpRoute = "signUp"
    const val ResetPasswordRoute = "resetPassword"
}

fun NavGraphBuilder.signInGraph(navController: NavHostController) {
    navigation(
        startDestination = SignInRoute,
        route = MainActivityRoutes.SignInGraphRoute,
    ) {
        composable(route = SignInRoute) {
            SignInScreen(
                navigateToResetPassword = navController::navigateToResetPassword,
                navigateToSignUp = navController::navigateToSignUp
            )
        }
        composable(route = SignUpRoute) {
            SignUpScreen()
        }
        composable(route = ResetPasswordRoute) {
            ResetPasswordScreen(popBackStack = navController::popBackStack)
        }
    }
}

private fun NavHostController.navigateToSignUp() = navigate(SignUpRoute)
private fun NavHostController.navigateToResetPassword() = navigate(ResetPasswordRoute)