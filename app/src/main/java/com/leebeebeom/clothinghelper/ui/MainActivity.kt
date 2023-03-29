package com.leebeebeom.clothinghelper.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.ui.MainActivityRoutes.MainGraphRoute
import com.leebeebeom.clothinghelper.ui.MainActivityRoutes.SignInGraphRoute
import com.leebeebeom.clothinghelper.ui.drawer.Drawer
import com.leebeebeom.clothinghelper.ui.drawer.component.EssentialMenuType
import com.leebeebeom.clothinghelper.ui.main.*
import com.leebeebeom.clothinghelper.ui.setting.SettingDestination
import com.leebeebeom.clothinghelper.ui.signin.ui.signInGraph
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import com.leebeebeom.clothinghelper.ui.util.getCurrentRoute
import dagger.hilt.android.AndroidEntryPoint


object MainActivityRoutes {
    const val SignInGraphRoute = "sign in graph"
    const val MainGraphRoute = "main graph"
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
                startDestination = if (user == null) SignInGraphRoute else MainGraphRoute // no recomposition
            ) {
                signInGraph(navController = navController)
                mainGraph(navController = navController)
            }
        }
    }
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
        navigate(route = MainGraphDestinations.DetailGraphRoute)
}

private fun NavHostController.navigateToSetting() {
    if (currentBackStackEntry.getCurrentRoute() != SettingDestination.Setting.route)
        navigate(route = MainGraphDestinations.SettingGraphRoute)
}