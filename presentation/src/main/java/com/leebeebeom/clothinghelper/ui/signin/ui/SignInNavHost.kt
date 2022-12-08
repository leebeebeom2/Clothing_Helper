package com.leebeebeom.clothinghelper.ui.signin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.ui.signin.SignInDestinations.ResetPassword
import com.leebeebeom.clothinghelper.ui.signin.SignInDestinations.SignIN
import com.leebeebeom.clothinghelper.ui.signin.SignInDestinations.SignUp
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInRoot
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
fun SignInNavHost(state: SignInNavHostState = rememberSignInNavHostState()) {
    SignInRoot {
        NavHost(
            navController = state.navController,
            startDestination = SignIN
        ) {
            composable(route = SignIN) {
                SignInScreen(
                    onForgotPasswordClick = state::navigateToResetPassword,
                    onSignUpClick = state::navigateToSignUp
                )
            }
            composable(route = SignUp) { SignUpScreen() }
            composable(route = ResetPassword) { ResetPasswordScreen() }
        }
    }
}

data class SignInNavHostState(val navController: NavHostController) {
    fun navigateToSignUp() = navController.navigateSingleTop(SignUp)
    fun navigateToResetPassword() = navController.navigateSingleTop(ResetPassword)
}

@Composable
fun rememberSignInNavHostState(navController: NavHostController = rememberNavController()) =
    remember { SignInNavHostState(navController) }