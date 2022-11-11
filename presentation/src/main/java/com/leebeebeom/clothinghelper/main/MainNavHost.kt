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
import com.leebeebeom.clothinghelper.main.root.EssentialMenus
import com.leebeebeom.clothinghelper.main.root.MainRoot
import com.leebeebeom.clothinghelper.main.setting.SettingScreen
import com.leebeebeom.clothinghelper.main.subcategory.SubCategoryScreen
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.util.navigateSingleTop
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent

sealed class MainDestinations(val route: String) {
    object MainCategory : MainDestinations("mainCategory")
    object SubCategory : MainDestinations("subCategory") {
        const val mainCategoryName = "mainCategoryName"
        val routeWithArg = "$route/{$mainCategoryName}"
        val arguments = listOf(navArgument(mainCategoryName) { type = NavType.StringType })
    }

    object Setting : MainDestinations("setting")
    object Detail : MainDestinations("detail") {
        const val subCategoryName = "subCategoryName"
        const val subCategoryKey = "subCategoryKey"
        val routeWithArg = "$route/{$subCategoryName}/{$subCategoryKey}"
        val arguments = listOf(
            navArgument(subCategoryName) { type = NavType.StringType },
            navArgument(subCategoryKey) { type = NavType.StringType },
        )
    }
}

@Composable
fun MainNavHost(state: MainNavHostState = rememberMainNavHostState()) {
    MainRoot(
        onEssentialMenuClick = state::onEssentialMenuClick,
        onMainCategoryClick = state::navigateToSubCategory,
        onSubCategoryClick = state::navigateToDetail,
        onSettingIconClick = state::navigateToSetting
    ) { paddingValues, drawerCloseBackHandler ->
        NavHost(
            navController = state.navController,
            startDestination = MainDestinations.MainCategory.route,
            modifier = Modifier.padding(paddingValues = paddingValues)
        ) {
            composable(MainDestinations.MainCategory.route) {
                MainCategoryScreen(onMainCategoryClick = state::navigateToSubCategory)
            }
            composable(
                route = MainDestinations.SubCategory.routeWithArg,
                arguments = MainDestinations.SubCategory.arguments
            ) {
                SubCategoryScreen(
                    parent = enumValueOf(it.arguments?.getString(MainDestinations.SubCategory.mainCategoryName)!!),
                    drawerCloseBackHandler = drawerCloseBackHandler,
                    onSubCategoryClick = state::navigateToDetail
                )
            }
            composable(route = MainDestinations.Setting.route) {
                SettingScreen(drawerCloseBackHandler = drawerCloseBackHandler)
            }
            composable(
                route = MainDestinations.Detail.routeWithArg,
                arguments = MainDestinations.Detail.arguments
            ) {
                val arguments = it.arguments!!
                DetailScreen(
                    subCategoryName = arguments.getString(MainDestinations.Detail.subCategoryName)!!,
                    subCategoryKey = arguments.getString(MainDestinations.Detail.subCategoryKey)!!,
                    drawerCloseBackHandler = drawerCloseBackHandler
                )
            }
        }
    }
}

data class MainNavHostState(
    val navController: NavHostController,
    val currentBackStack: State<NavBackStackEntry?>
) {
    fun onEssentialMenuClick(essentialMenu: EssentialMenus) = when (essentialMenu) {
        EssentialMenus.MainScreen -> navigateToMain()
        EssentialMenus.Favorite -> {} // TODO
        EssentialMenus.SeeAll -> {} // TODO
        EssentialMenus.Trash -> {} // TODO
    }

    fun navigateToSubCategory(subCategoryParent: SubCategoryParent) {
        if (currentBackStack.value?.arguments?.getString(MainDestinations.SubCategory.mainCategoryName) != subCategoryParent.name)
            navController.navigate("${MainDestinations.SubCategory.route}/${subCategoryParent.name}")
    }

    private fun navigateToMain() {
        if (currentBackStack.value?.destination?.route != MainDestinations.MainCategory.route)
            navController.navigateSingleTop(route = MainDestinations.MainCategory.route)
    }

    fun navigateToSetting() {
        if (currentBackStack.value?.destination?.route != MainDestinations.Setting.route)
            navController.navigateSingleTop(route = MainDestinations.Setting.route)
    }

    fun navigateToDetail(subCategory: StableSubCategory) {
        val argument = currentBackStack.value?.arguments
        val name = argument?.getString(MainDestinations.Detail.subCategoryName)
        val key = argument?.getString(MainDestinations.Detail.subCategoryKey)
        if (name != subCategory.name && key != subCategory.key)
            navController.navigate(route = "${MainDestinations.Detail.route}/${subCategory.name}/${subCategory.key}")
    }
}

@Composable
fun rememberMainNavHostState(
    navController: NavHostController = rememberNavController(),
    currentBackStack: State<NavBackStackEntry?> = navController.currentBackStackEntryAsState()
): MainNavHostState {
    return remember {
        MainNavHostState(navController, currentBackStack)
    }
}