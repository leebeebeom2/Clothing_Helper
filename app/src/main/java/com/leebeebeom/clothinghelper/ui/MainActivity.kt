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
import com.leebeebeom.clothinghelper.domain.model.User
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
 * 크로스 페이드를 쓰면 각 ViewModel의 OnCleared가 호출되지 않는다.
 */
@Composable
fun MainActivityScreen(
    viewModel: ActivityViewModel = activityViewModel(),
    navController: NavHostController = rememberNavController(),
) {
    buildConfigLog("MainActivityScreen", "recomposition")

    val uiState by viewModel.activityUiState.collectAsStateWithLifecycle()
    val userStream by viewModel.userStream.collectAsStateWithLifecycle(initialValue = uiState.user)

    /**
     * uiState에 user는 초기값으로서 변경되지 않음. 앱 실행 시 첫 화면을 위한 것.
     * (로그인 상태로 앱 실행 시 메인화면, 로그아웃 상태로 앱 실행 시 로그인 화면)
     *
     * UserRepository에 user가 null일 시 자동으로 SignInNavHost로 이동.
     * (로그인 시에는 자동으로 이동되지 않음.)
     * 따라서 mainNavHost에는 navigateToSignInGraph가 필요없음..
     */
    ClothingHelperTheme {
        NavHost(
            navController = navController,
            startDestination = if (uiState.user == null) SignInGraphRoute else MainGraphRoute, // 전체 리컴포지션 안 됨 확인
        ) {
            composable(route = SignInGraphRoute) {
                SignInNavHost(navigateToMainGraph = navController::navigateToMainGraph)
            }
            composable(route = MainGraphRoute) {
                MainNavHost()
            }
        }
    }

    NavigateToSignInGraphWrapper(userStream = { userStream }, navController = navController)

    ToastWrapper(
        toastTexts = { uiState.toastTexts },
        toastShown = viewModel::removeFirstToastText
    )
}

@Composable
private fun NavigateToSignInGraphWrapper(
    userStream: () -> User?,
    navController: NavHostController
) {
    if (userStream() == null) navController.navigateToSignInGraph()
}

private fun NavHostController.navigateToSignInGraph() = navigate(SignInGraphRoute) {
    popUpTo(MainGraphRoute) { inclusive = true }
    launchSingleTop = true
}

private fun NavHostController.navigateToMainGraph() = navigate(MainGraphRoute) {
    popUpTo(SignInGraphRoute) { inclusive = true }
    launchSingleTop = true
}

@Composable
fun ToastWrapper(toastTexts: () -> List<Int>, toastShown: () -> Unit) {
    toastTexts().firstOrNull()?.let {
        Toast.makeText(LocalContext.current, stringResource(id = it), Toast.LENGTH_SHORT).show()
        toastShown()
    }
}