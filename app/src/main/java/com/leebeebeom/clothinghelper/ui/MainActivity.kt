package com.leebeebeom.clothinghelper.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.ui.MainActivityRoutes.MainGraphRoute
import com.leebeebeom.clothinghelper.ui.MainActivityRoutes.SignInGraphRoute
import com.leebeebeom.clothinghelper.ui.main.MainNavHost
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInNavHost
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import com.leebeebeom.clothinghelper.util.buildConfigLog
import dagger.hilt.android.AndroidEntryPoint


object MainActivityRoutes {
    const val SignInGraphRoute = "sign in graph"
    const val MainGraphRoute = "main graph"
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainActivityScreen() }
    }
}

/**
 * 크로스 페이드 or if-else를 쓰면 NavViewModel의 OnCleared가 호출되지 않는다.
 */
@Composable
fun MainActivityScreen(
    viewModel: ActivityViewModel = activityViewModel(),
    navController: NavHostController = rememberNavController(),
) {
    buildConfigLog("MainActivityScreen", "recomposition")

    val uiState by viewModel.activityUiState.collectAsStateWithLifecycle()

    ClothingHelperTheme {
        NavHost(
            navController = navController,
            startDestination = if (uiState.user == null) SignInGraphRoute else MainGraphRoute // no recomposition, called ViewModel onCleared
        ) {
            composable(route = SignInGraphRoute) { SignInNavHost() }
            composable(route = MainGraphRoute) { MainNavHost() }
        }
    }

    ToastWrapper(
        toastTexts = { uiState.toastTexts },
        toastShown = viewModel::removeFirstToastText
    )
}

@Composable
fun ToastWrapper(toastTexts: () -> List<Int>, toastShown: () -> Unit) {
    toastTexts().firstOrNull()?.let {
        Toast.makeText(LocalContext.current, stringResource(id = it), Toast.LENGTH_SHORT).show()
        toastShown()
    }
}