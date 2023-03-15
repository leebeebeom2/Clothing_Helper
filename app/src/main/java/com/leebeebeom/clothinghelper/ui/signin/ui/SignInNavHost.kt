package com.leebeebeom.clothinghelper.ui.signin.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.ui.components.CenterDotProgressIndicator
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInDestinations.ResetPasswordRoute
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInDestinations.SignInNavRoute
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInDestinations.SignInRoute
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInDestinations.SignUpRoute
import com.leebeebeom.clothinghelper.ui.signin.ui.resetpassword.ResetPasswordScreen
import com.leebeebeom.clothinghelper.ui.signin.ui.signin.SignInScreen
import com.leebeebeom.clothinghelper.ui.signin.ui.signup.SignUpScreen

const val SignInNavTag = "sign in nav"

object SignInDestinations {
    const val SignInNavRoute = "signInNav"
    const val SignInRoute = "signIn"
    const val SignUpRoute = "signUp"
    const val ResetPasswordRoute = "resetPassword"
}

@Composable
fun SignInNavHost(
    navController: NavHostController = rememberNavController(),
    viewModel: SignInNavViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NavHost(
        modifier = Modifier.testTag(SignInNavTag),
        navController = navController,
        startDestination = SignInRoute,
        route = SignInNavRoute
    ) {
        composable(route = SignInRoute) {
            SignInScreen(
                navigateToResetPassword = navController::navigateToResetPassword,
                navigateToSignUp = navController::navigateToSignUp,
                signInNavViewModel = viewModel
            )
        }
        composable(route = SignUpRoute) {
            SignUpScreen(signInNavViewModel = viewModel)
        }
        composable(route = ResetPasswordRoute) {
            ResetPasswordScreen(popBackStack = { navController.popBackStack() })
        }
    }

    CenterDotProgressIndicator(show = { uiState.isLoading })
}

private fun NavHostController.navigateToSignUp() = navigate(SignUpRoute)
private fun NavHostController.navigateToResetPassword() = navigate(ResetPasswordRoute)