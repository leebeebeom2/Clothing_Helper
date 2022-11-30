package com.leebeebeom.clothinghelper.main

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.leebeebeom.clothinghelper.main.detail.DetailScreen
import com.leebeebeom.clothinghelper.main.maincategory.MainCategoryScreen
import com.leebeebeom.clothinghelper.main.root.MainRoot
import com.leebeebeom.clothinghelper.main.root.model.EssentialMenuType
import com.leebeebeom.clothinghelper.main.setting.SettingScreen
import com.leebeebeom.clothinghelper.main.sizeChart.SizeChartScreen
import com.leebeebeom.clothinghelper.main.subcategory.SubCategoryScreen
import com.leebeebeom.clothinghelper.util.navigateSingleTop
import com.leebeebeom.clothinghelperdomain.model.container.BaseContainer
import com.leebeebeom.clothinghelperdomain.model.container.SubCategoryParent

sealed class MainDestinations(val route: String) {
    object MainCategory : MainDestinations("mainCategory")
    object SubCategory : MainDestinations("subCategory") {
        const val parent = "parent"
        val routeWithArg = "$route/{$parent}"
        val arguments = listOf(navArgument(parent) { type = NavType.StringType })
    }

    object Setting : MainDestinations("setting")
    object Detail : MainDestinations("detail") {
        const val parentKey = "parentKey"
        const val subCategoryKey = "subCategoryKey"
        const val subCategoryParent = "subCategoryParent"
        val routeWithArg = "$route/{$parentKey}/${subCategoryKey}/${subCategoryParent}"
        val arguments = listOf(
            navArgument(parentKey) { type = NavType.StringType },
            navArgument(subCategoryKey) { type = NavType.StringType },
            navArgument(subCategoryParent) { type = NavType.StringType },
        )
    }

    object SizeChart : MainDestinations("sizeChart")
}

@Composable
fun MainNavHost(state: MainNavHostState = rememberMainNavHostState()) {
    MainRoot(
        onEssentialMenuClick = state::onEssentialMenuClick,
        onMainCategoryClick = state::navigateToSubCategory,
        onSubCategoryClick = state::navigateToDetail,
        onSettingIconClick = state::navigateToSetting,
        onFolderClick = state::navigateToDetail
    ) {
        NavHost(
            navController = state.navController,
            startDestination = MainDestinations.MainCategory.route,
            modifier = Modifier.padding(it)
        ) {
            composable(MainDestinations.MainCategory.route) {
                MainCategoryScreen(onMainCategoryClick = state::navigateToSubCategory)
            }
            composable(
                route = MainDestinations.SubCategory.routeWithArg,
                arguments = MainDestinations.SubCategory.arguments
            ) { entry ->
                SubCategoryScreen(
                    parent = enumValueOf(entry.arguments?.getString(MainDestinations.SubCategory.parent)!!),
                    onSubCategoryClick = state::navigateToDetail
                )
            }
            composable(route = MainDestinations.Setting.route) { SettingScreen() }
            composable(
                route = MainDestinations.Detail.routeWithArg,
                arguments = MainDestinations.Detail.arguments
            ) { entry ->
                val arguments = entry.arguments!!
                DetailScreen(
                    parentKey = arguments.getString(MainDestinations.Detail.parentKey)!!,
                    subCategoryKey = arguments.getString(MainDestinations.Detail.subCategoryKey)!!,
                    subCategoryParent = enumValueOf(arguments.getString(MainDestinations.Detail.subCategoryParent)!!),
                    onFolderClick = state::navigateToDetail,
                    onFabClick = state::navigateToSizeChart
                )
            }
            composable(route = MainDestinations.SizeChart.route) {
                SizeChartScreen()
            }
        }
    }
}

data class MainNavHostState(
    val navController: NavHostController, val currentBackStack: State<NavBackStackEntry?>
) {
    fun onEssentialMenuClick(type: EssentialMenuType) = when (type) {
        EssentialMenuType.MainScreen -> navigateToMain()
        EssentialMenuType.Favorite -> {} // TODO
        EssentialMenuType.SeeAll -> {} // TODO
        EssentialMenuType.Trash -> {} // TODO
    }

    fun navigateToSubCategory(parent: SubCategoryParent) {
        val currentParent =
            currentBackStack.value?.arguments?.getString(MainDestinations.SubCategory.parent)
        if (currentParent != parent.name) navController.navigate("${MainDestinations.SubCategory.route}/${parent.name}")
    }

    private fun navigateToMain() {
        val currentRoute = currentBackStack.value?.destination?.route
        if (currentRoute != MainDestinations.MainCategory.route) navController.navigateSingleTop(
            route = MainDestinations.MainCategory.route
        )
    }

    fun navigateToSetting() {
        val currentRoute = currentBackStack.value?.destination?.route
        if (currentRoute != MainDestinations.Setting.route) navController.navigateSingleTop(route = MainDestinations.Setting.route)
    }

    fun navigateToDetail(container: BaseContainer) {
        val arguments = currentBackStack.value?.arguments!!

        val parentKey = container.key
        val currentParentKey = arguments.getString(MainDestinations.Detail.parentKey)

        if (parentKey != currentParentKey) {
            val subCategoryKey = arguments.getString(MainDestinations.Detail.subCategoryKey)
            val subCategoryParent = arguments.getString(MainDestinations.Detail.subCategoryParent)
            navController.navigate(route = "${MainDestinations.Detail.route}/${parentKey}/${subCategoryKey}/${subCategoryParent}")
        }
    }

    fun navigateToSizeChart() {
        navController.navigate(route = MainDestinations.SizeChart.route)
    }
}

@Composable
fun rememberMainNavHostState(
    navController: NavHostController = rememberNavController(),
    currentBackStack: State<NavBackStackEntry?> = navController.currentBackStackEntryAsState()
) = remember { MainNavHostState(navController, currentBackStack) }