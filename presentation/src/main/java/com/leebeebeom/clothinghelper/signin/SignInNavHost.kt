package com.leebeebeom.clothinghelper.signin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.signin.resetpassword.ResetPasswordScreen
import com.leebeebeom.clothinghelper.signin.signin.SignInScreen
import com.leebeebeom.clothinghelper.signin.signup.SignUpScreen
import com.leebeebeom.clothinghelper.util.navigateSingleTop

object SignInDestinations {
    const val SIGN_IN = "signIn"
    const val SIGN_UP = "signUp"
    const val RESET_PASSWORD = "resetPassword"
}

@Composable
fun SignInNavHost(state: SignInNavHostState = rememberSignInNavHostState()) {
    SignInRoot {
        NavHost(
            navController = state.navController,
            startDestination = SignInDestinations.SIGN_IN
        ) {
            composable(route = SignInDestinations.SIGN_IN) {
                SignInScreen(
                    onForgotPasswordClick = state::navigateToResetPassword,
                    onEmailSignUpClick = state::navigateToSignUp
                )
            }
            composable(route = SignInDestinations.SIGN_UP) { SignUpScreen() }
            composable(route = SignInDestinations.RESET_PASSWORD) { ResetPasswordScreen() }
        }
    }
}

data class SignInNavHostState(val navController: NavHostController) {
    fun navigateToSignUp() {
        navController.navigateSingleTop(SignInDestinations.SIGN_UP)
    }

    fun navigateToResetPassword() {
        navController.navigateSingleTop(SignInDestinations.RESET_PASSWORD)
    }
}

@Composable
fun rememberSignInNavHostState(navController: NavHostController = rememberNavController()): SignInNavHostState {
    return remember { SignInNavHostState(navController) }
}