package com.leebeebeom.clothinghelper.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.ui.ActivityDestinations.MAIN_ROUTE
import com.leebeebeom.clothinghelper.ui.ActivityDestinations.SIGN_IN_ROUTE
import com.leebeebeom.clothinghelper.ui.main.MainNavHost
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInNavHost
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import dagger.hilt.android.AndroidEntryPoint

object ActivityDestinations {
    const val SIGN_IN_ROUTE = "signIn"
    const val MAIN_ROUTE = "main"
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainActivityScreen() }
    }
}

@Composable
fun MainActivityScreen(
    viewModel: ActivityViewModel = activityViewModel(),
    uiState: ActivityUiState = viewModel.activityUiState,
    navController: NavHostController = rememberNavController()
) {
    val isSignIn by viewModel.isSignIn.collectAsStateWithLifecycle()

    ClothingHelperTheme {
        NavHost(
            navController = navController,
            startDestination = SIGN_IN_ROUTE,
        ) {
            composable(SIGN_IN_ROUTE) { SignInNavHost() }
            composable(MAIN_ROUTE) { MainNavHost() }
        }
    }

    MainActivityNavigateWrapper(
        isSignIn = { isSignIn },
        navigateToMainGraph = navController::navigateToMainGraph,
        navigateToSignInGraph = navController::navigateToSignInGraph
    )
    ToastWrapper(text = { uiState.toastText }, toastShown = viewModel::toastShown) // 테스트 불가
}

@Composable
private fun MainActivityNavigateWrapper(
    isSignIn: () -> Boolean,
    navigateToMainGraph: () -> Unit,
    navigateToSignInGraph: () -> Unit,
) {
    if (isSignIn()) navigateToMainGraph() else navigateToSignInGraph()
}

private fun NavHostController.navigateToSignInGraph() =
    navigate(SIGN_IN_ROUTE) {
        popUpTo(MAIN_ROUTE) { inclusive = true }
        launchSingleTop = true
    }

private fun NavHostController.navigateToMainGraph() =
    navigate(MAIN_ROUTE) {
        popUpTo(SIGN_IN_ROUTE) { inclusive = true }
        launchSingleTop = true
    }

@Composable
fun ToastWrapper(@StringRes text: () -> Int?, toastShown: () -> Unit) {
    text()?.let {
        Toast.makeText(LocalContext.current, stringResource(id = it), Toast.LENGTH_SHORT).show()
        toastShown()
    }
}