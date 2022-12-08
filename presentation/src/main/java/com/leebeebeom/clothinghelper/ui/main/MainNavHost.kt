package com.leebeebeom.clothinghelper.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.ui.main.MainDetailDestination.MainCategory
import com.leebeebeom.clothinghelper.ui.main.root.MainRoot
import com.leebeebeom.clothinghelper.ui.main.root.model.EssentialMenuType
import com.leebeebeom.clothinghelper.ui.setting.settingGraph
import com.leebeebeom.clothinghelper.util.getCurrentRoute
import com.leebeebeom.clothinghelper.util.navigateSingleTop

sealed class MainDestinations(val route: String) {
    object Main : MainDestinations("main")
    object Setting : MainDestinations("setting")
}

@Composable
fun MainNavHost(navController: NavHostController = rememberNavController()) {
    MainRoot(
        onEssentialMenuClick = { onEssentialMenuClick(navController, it) },
        onMainCategoryClick = navController::navigateToSubCategory,
        onSubCategoryClick = navController::navigateToSizeChartList,
        onSettingIconClick = navController::navigateToSetting,
        onFolderClick = navController::navigateToSizeChartList
    ) {
        NavHost(
            navController = navController,
            startDestination = MainDestinations.Main.route,
            modifier = Modifier.padding(it)
        ) {
            mainGraph(navController = navController)
            settingGraph(navController = navController)
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
    val currentRoute = currentBackStackEntry.getCurrentRoute()
    if (currentRoute != MainCategory.route)
        navigateSingleTop(route = MainCategory.route)
}

private fun NavHostController.navigateToSetting() {
    val currentRoute = currentBackStackEntry.getCurrentRoute()
    if (currentRoute != MainDestinations.Setting.route) navigateSingleTop(route = MainDestinations.Setting.route)
}