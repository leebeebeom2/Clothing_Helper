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
import com.leebeebeom.clothinghelper.ui.util.navigateSingleTop
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
private fun MainActivityScreen(
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

    // TODO 화면 인스턴스가 하나인 지 테스트, 로그인 된 상태로 앱 실행 시 로그인 화면 안 보이는지 테스트
    if (isSignIn) navController.navigateSingleTop(MAIN_ROUTE)
    else navController.navigateSingleTop(SIGN_IN_ROUTE)

    ToastWrapper(text = { uiState.toastText }, toastShown = viewModel::toastShown)
}

@Composable
private fun ToastWrapper(@StringRes text: () -> Int?, toastShown: () -> Unit) {
    text()?.let {
        Toast.makeText(LocalContext.current, stringResource(id = it), Toast.LENGTH_SHORT).show()
        toastShown()
    }
}