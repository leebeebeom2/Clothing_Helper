package com.leebeebeom.clothinghelper.signin

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.leebeebeom.clothinghelper.base.navigation.BaseNavHost
import com.leebeebeom.clothinghelper.base.navigation.NavigateAnime.slideInBottom
import com.leebeebeom.clothinghelper.base.navigation.NavigateAnime.slideInRight
import com.leebeebeom.clothinghelper.base.navigation.NavigateAnime.slideOutBottom
import com.leebeebeom.clothinghelper.base.navigation.NavigateAnime.slideOutRight
import com.leebeebeom.clothinghelper.signin.resetpassword.ResetPasswordScreen
import com.leebeebeom.clothinghelper.signin.signin.SignInScreen
import com.leebeebeom.clothinghelper.signin.signup.SignUpScreen
import com.leebeebeom.clothinghelper.theme.ClothingHelperTheme

object SignInDestinations {
    const val SIGN_IN = "signIn"
    const val SIGN_UP = "signUp"
    const val RESET_PASSWORD = "resetPassword"
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SignInNavHost() {
    val navController = rememberAnimatedNavController()

    ClothingHelperTheme {
        Scaffold {
            BaseNavHost(
                modifier = Modifier.padding(it),
                navController = navController,
                startDestination = SignInDestinations.SIGN_IN
            ) {
                composable(route = SignInDestinations.SIGN_IN) {
                    SignInScreen(
                        onForgotPasswordClick = { navController.signNavigate(SignInDestinations.RESET_PASSWORD) },
                        onEmailSignUpClick = { navController.signNavigate(SignInDestinations.SIGN_UP) }
                    )
                }
                composable(
                    route = SignInDestinations.SIGN_UP,
                    enterTransition = slideInBottom,
                    exitTransition = slideOutBottom
                ) { SignUpScreen() }
                composable(
                    route = SignInDestinations.RESET_PASSWORD,
                    enterTransition = slideInRight,
                    exitTransition = slideOutRight
                ) { ResetPasswordScreen() }
            }
        }
    }
}

fun NavController.signNavigate(destination: String) =
    navigate(destination) {
        launchSingleTop = true
        popUpTo(SignInDestinations.SIGN_IN)
    }