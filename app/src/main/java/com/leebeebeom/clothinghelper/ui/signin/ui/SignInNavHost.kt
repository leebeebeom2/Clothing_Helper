package com.leebeebeom.clothinghelper.ui.signin.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.ui.ActivityViewModel
import com.leebeebeom.clothinghelper.ui.activityViewModel
import com.leebeebeom.clothinghelper.ui.components.BlockBacKPressWhenLoading
import com.leebeebeom.clothinghelper.ui.components.CenterDotProgressIndicator
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInDestinations.ResetPassword_Route
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInDestinations.SignInNav_Route
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInDestinations.SignIn_Route
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInDestinations.SignUp_Route
import com.leebeebeom.clothinghelper.ui.signin.ui.resetpassword.ResetPasswordScreen
import com.leebeebeom.clothinghelper.ui.signin.ui.signin.SignInScreen
import com.leebeebeom.clothinghelper.ui.signin.ui.signup.SignUpScreen
import com.leebeebeom.clothinghelper.ui.util.navigateSingleTop

object SignInDestinations {
    const val SignInNav_Route = "signInNav"
    const val SignIn_Route = "signIn"
    const val SignUp_Route = "signUp"
    const val ResetPassword_Route = "resetPassword"
}

@Composable
fun SignInNavHost(
    navController: NavHostController = rememberNavController(),
    activityViewModel: ActivityViewModel = activityViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = SignIn_Route,
        route = SignInNav_Route
    ) {
        composable(route = SignIn_Route) {
            SignInScreen(
                navigateToResetPassword = navController::navigateToResetPassword,
                navigateToSignUp = navController::navigateToSignUp,
                signInNavViewModel =
                signInNavViewModel(entry = it, navGraphEntry = navController.navGraphEntry)
            )
        }
        composable(route = SignUp_Route) {
            SignUpScreen(
                signInNavViewModel =
                signInNavViewModel(entry = it, navGraphEntry = navController.navGraphEntry)
            )
        }
        composable(route = ResetPassword_Route) {
            ResetPasswordScreen(popBackStack = { navController.popBackStack() })
        }
    }

    CenterDotProgressIndicator(show = { activityViewModel.activityUiState.isSignInLoading })
    BlockBacKPressWhenLoading { activityViewModel.activityUiState.isSignInLoading }
}

private fun NavHostController.navigateToSignUp() = navigateSingleTop(SignUp_Route)
private fun NavHostController.navigateToResetPassword() = navigateSingleTop(ResetPassword_Route)
private val NavHostController.navGraphEntry get() = getBackStackEntry(SignInNav_Route)