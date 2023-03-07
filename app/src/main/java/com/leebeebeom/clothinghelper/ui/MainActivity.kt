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
import com.leebeebeom.clothinghelper.domain.model.User
import com.leebeebeom.clothinghelper.ui.MainActivityDestinations.MainGraphDestination
import com.leebeebeom.clothinghelper.ui.MainActivityDestinations.SignInGraphDestination
import com.leebeebeom.clothinghelper.ui.main.MainNavHost
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInNavHost
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import dagger.hilt.android.AndroidEntryPoint

object MainActivityDestinations {
    const val SignInGraphDestination = "sign in graph"
    const val MainGraphDestination = "main graph"
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
    navController: NavHostController = rememberNavController(),
) {
    val uiState by viewModel.activityUiState.collectAsStateWithLifecycle()

    ClothingHelperTheme {
        NavHost(
            navController = navController,
            startDestination = SignInGraphDestination,
        ) {
            composable(SignInGraphDestination) { SignInNavHost() }
            composable(MainGraphDestination) { MainNavHost() }
        }
    }

    MainActivityNavigateWrapper(
        user = { uiState.user },
        navigateToMainGraph = navController::navigateToMainGraph,
        navigateToSignInGraph = navController::navigateToSignInGraph
    )
    ToastWrapper(text = { uiState.toastText }, toastShown = viewModel::toastShown) // 테스트 불가
}

@Composable
private fun MainActivityNavigateWrapper(
    user: () -> User?,
    navigateToMainGraph: () -> Unit,
    navigateToSignInGraph: () -> Unit,
) {
    user()?.let { navigateToMainGraph() } ?: navigateToSignInGraph()
}

private fun NavHostController.navigateToSignInGraph() =
    navigate(SignInGraphDestination) {
        popUpTo(MainGraphDestination) { inclusive = true }
        launchSingleTop = true
    }

private fun NavHostController.navigateToMainGraph() =
    navigate(MainGraphDestination) {
        popUpTo(SignInGraphDestination) { inclusive = true }
        launchSingleTop = true
    }

@Composable
fun ToastWrapper(@StringRes text: () -> Int?, toastShown: () -> Unit) {
    text()?.let {
        Toast.makeText(LocalContext.current, stringResource(id = it), Toast.LENGTH_SHORT).show()
        toastShown()
    }
}