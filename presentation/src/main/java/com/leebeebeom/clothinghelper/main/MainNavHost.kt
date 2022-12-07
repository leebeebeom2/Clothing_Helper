package com.leebeebeom.clothinghelper.main

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.leebeebeom.clothinghelper.main.root.MainRoot
import com.leebeebeom.clothinghelper.main.root.model.EssentialMenuType
import com.leebeebeom.clothinghelper.main.setting.settingGraph
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.util.getCurrentRoute
import com.leebeebeom.clothinghelper.util.getStringArg
import com.leebeebeom.clothinghelper.util.navigateSingleTop
import com.leebeebeom.clothinghelperdomain.model.data.BaseModel
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent

sealed class MainDestinations(val route: String) {
    object MainCategory : MainDestinations("mainCategory")

    object SubCategory : MainDestinations("subCategory") {
        const val parent = "parent"
        val routeWithArg = "$route/{$parent}"
        val arguments = listOf(navArgument(parent) { type = NavType.StringType })
    }

    object Setting : MainDestinations("setting")

    object SizeChartList : MainDestinations("SizeChartList") {
        const val parentKey = "parentKey"
        const val subCategoryKey = "subCategoryKey"
        val routeWithArg = "$route/{$parentKey}/{${subCategoryKey}}"
        val arguments = listOf(
            navArgument(parentKey) { type = NavType.StringType },
            navArgument(subCategoryKey) { type = NavType.StringType },
        )
    }

    object SizeChart : MainDestinations("sizeChart") {
        const val path = "path"
        val routeWithArg = "$route/{$path}"
        val arguments = listOf(
            navArgument(path) { type = NavType.StringType }
        )
    }
}

@Composable
fun MainNavHost(state: MainNavHostState = rememberMainNavHostState()) {
    MainRoot(
        onEssentialMenuClick = state::onEssentialMenuClick,
        onMainCategoryClick = state::navigateToSubCategory,
        onSubCategoryClick = state::navigateToSizeChartList,
        onSettingIconClick = state::navigateToSetting,
        onFolderClick = state::navigateToSizeChartList
    ) {
        NavHost(
            navController = state.navController,
            startDestination = MainDestinations.MainCategory.route,
            modifier = Modifier.padding(it)
        ) {
            mainGraph(
                onMainCategoryClick = state::navigateToSubCategory,
                onSubCategoryClick = state::navigateToSizeChartList,
                onFolderClick = state::navigateToSizeChartList,
                onSizeChartListFabClick = state::navigateToSizeChart
            )

            settingGraph(navController = state.navController)
        }
    }
}

class MainNavHostState(
    val navController: NavHostController, currentBackStack: State<NavBackStackEntry?>
) {
    private val currentBackStack by currentBackStack

    fun onEssentialMenuClick(type: EssentialMenuType) =
        when (type) {
            EssentialMenuType.MainScreen -> navigateToMain()
            EssentialMenuType.Favorite -> {} // TODO
            EssentialMenuType.SeeAll -> {} // TODO
            EssentialMenuType.Trash -> {} // TODO
        }

    fun navigateToSubCategory(parent: SubCategoryParent) {
        val currentParent = currentBackStack.getStringArg(MainDestinations.SubCategory.parent)

        if (currentParent != parent.name) navController.navigate("${MainDestinations.SubCategory.route}/${parent.name}")
    }

    private fun navigateToMain() {
        val currentRoute = currentBackStack.getCurrentRoute()
        if (currentRoute != MainDestinations.MainCategory.route)
            navController.navigateSingleTop(route = MainDestinations.MainCategory.route)
    }

    fun navigateToSetting() {
        val currentRoute = currentBackStack.getCurrentRoute()
        if (currentRoute != MainDestinations.Setting.route) navController.navigateSingleTop(route = MainDestinations.Setting.route)
    }

    fun navigateToSizeChartList(container: BaseModel) {
        val parentKey = container.key
        val currentParentKey =
            currentBackStack.getStringArg(MainDestinations.SizeChartList.parentKey)

        if (parentKey != currentParentKey) {
            val subCategoryKey =
                if (container is StableSubCategory) container.key else (container as StableFolder).subCategoryKey
            navController.navigate(route = "${MainDestinations.SizeChartList.route}/${parentKey}/${subCategoryKey}")
        }
    }

    fun navigateToSizeChart(path: String) {
        navController.navigate(route = "${MainDestinations.SizeChart.route}/$path")
    }
}

@Composable
fun rememberMainNavHostState(
    navController: NavHostController = rememberNavController(),
    currentBackStack: State<NavBackStackEntry?> = navController.currentBackStackEntryAsState()
) = remember { MainNavHostState(navController, currentBackStack) }