package com.leebeebeom.clothinghelper.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.domain.model.User
import com.leebeebeom.clothinghelper.ui.component.CenterDotProgressIndicator
import com.leebeebeom.clothinghelper.ui.drawer.Drawer
import com.leebeebeom.clothinghelper.ui.drawer.content.EssentialMenuType
import com.leebeebeom.clothinghelper.ui.main.*
import com.leebeebeom.clothinghelper.ui.setting.SettingDestination
import com.leebeebeom.clothinghelper.ui.signin.ui.signInGraph
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import com.leebeebeom.clothinghelper.ui.util.getCurrentRoute
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

const val TAG = "태그"

object MainNavRoute {
    const val LoadingScreen = "loading screen"
    const val SignInGraph = "sign in graph"
    const val MainGraph = "main graph"
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainNavHost() }
    }
}

@Composable // skippable
fun MainNavHost(
    viewModel: ActivityViewModel = activityViewModel(),
    navController: NavHostController = rememberNavController(),
) {
    val user by viewModel.userFlow.collectAsStateWithLifecycle()

    ClothingHelperTheme {
        Drawer(
            user = { user },
            navigateToSetting = navController::navigateToSetting,
            onEssentialMenuClick = { onEssentialMenuClick(navController, it) },
            navigateToMain = navController::navigateToSubCategory,
            navigateToSubCategory = navController::navigateToSizeChartList,
            navigateToFolder = navController::navigateToSizeChartList
        ) { paddingValues ->
            NavHost(
                modifier = Modifier.padding(paddingValues),
                navController = navController,
                startDestination = MainNavRoute.LoadingScreen
            ) {
                composable(route = MainNavRoute.LoadingScreen) {
                    CenterDotProgressIndicator(
                        backGround = MaterialTheme.colors.background,
                        show = { true }
                    )
                }
                mainGraph(navController = navController)
                signInGraph(navController = navController)
            }

        }
    }
    MainNavHostNavigateWrapper(
        user = { user },
        navigateToMainGraph = navController::navigateToMainGraph,
        navigateToSignInGraph = navController::navigateToSignInGraph
    )
}

@Composable
private fun MainNavHostNavigateWrapper(
    user: () -> User?,
    navigateToMainGraph: () -> Unit,
    navigateToSignInGraph: () -> Unit
) {
    val localUser by remember { derivedStateOf(user) }

    LaunchedEffect(key1 = localUser) {
        delay(500)
        localUser?.let { navigateToMainGraph() } ?: navigateToSignInGraph()
    }
}

private fun NavHostController.navigateToSignInGraph() = navigate(MainNavRoute.SignInGraph) {
    popUpTo(MainNavRoute.MainGraph) { inclusive = true }
    launchSingleTop = true
}

private fun NavHostController.navigateToMainGraph() = navigate(MainNavRoute.MainGraph) {
    popUpTo(MainNavRoute.SignInGraph) { inclusive = true }
    launchSingleTop = true
}

private fun onEssentialMenuClick(navController: NavHostController, type: EssentialMenuType) =
    when (type) {
        EssentialMenuType.MainScreen -> navController.navigateToMain()
        EssentialMenuType.Favorite -> {} // TODO
        EssentialMenuType.SeeAll -> {} // TODO
        EssentialMenuType.Trash -> {} // TODO
    }

private fun NavHostController.navigateToMain() {
    if (currentBackStackEntry.getCurrentRoute() != DetailDestination.CategoryRoute.route)
        navigate(route = MainNavRoute.MainGraph)
}

private fun NavHostController.navigateToSetting() {
    if (currentBackStackEntry.getCurrentRoute() != SettingDestination.Setting.route)
        navigate(route = MainGraphRoute.SettingGraph)
}