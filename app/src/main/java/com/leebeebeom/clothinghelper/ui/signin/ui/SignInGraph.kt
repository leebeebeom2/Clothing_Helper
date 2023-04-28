package com.leebeebeom.clothinghelper.ui.signin.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leebeebeom.clothinghelper.ui.MainNavRoute
import com.leebeebeom.clothinghelper.ui.signin.ui.resetpassword.ResetPasswordScreen
import com.leebeebeom.clothinghelper.ui.signin.ui.signin.SignInScreen
import com.leebeebeom.clothinghelper.ui.signin.ui.signup.SignUpScreen

sealed class SignInGraphRoute {
    companion object {
        const val SignInScreen = "sign in"
        const val SignUpScreen = "sign up"
        const val ResetPasswordScreen = "reset password"
    }
}

fun NavGraphBuilder.signInGraph(
    navigateToResetPassword: () -> Unit,
    navigateToSignUp: () -> Unit,
    popBackStack: () -> Unit
) {
    navigation(
        startDestination = SignInGraphRoute.SignInScreen,
        route = MainNavRoute.SignInGraph,
    ) {
        composable(route = SignInGraphRoute.SignInScreen) {
            SignInScreen(
                onForgotPasswordClick = navigateToResetPassword,
                onSignUpWithEmailClick = navigateToSignUp
            )
        }
        composable(route = SignInGraphRoute.SignUpScreen) { SignUpScreen() }
        composable(route = SignInGraphRoute.ResetPasswordScreen) {
            ResetPasswordScreen(onEmailSendSuccess = popBackStack)
        }
    }
}