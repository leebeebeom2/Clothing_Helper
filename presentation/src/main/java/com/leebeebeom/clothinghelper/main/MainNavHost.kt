package com.leebeebeom.clothinghelper.main

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.leebeebeom.clothinghelper.main.maincategory.MainCategoryScreen
import com.leebeebeom.clothinghelper.main.root.EssentialMenus
import com.leebeebeom.clothinghelper.main.root.MainScreenRoot
import com.leebeebeom.clothinghelper.main.setting.SettingScreen
import com.leebeebeom.clothinghelper.main.subcategory.SubCategoryScreen
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent

sealed class MainDestinations(val route: String) {
    object MainCategory : MainDestinations("mainCategory")
    object SubCategory : MainDestinations("subCategory") {
        const val mainCategoryName: String = "mainCategoryName"
        val routeWithArg = "$route/{$mainCategoryName}"
        val arguments = listOf(navArgument(mainCategoryName) { type = NavType.StringType })
    }

    object Setting : MainDestinations("setting")
}

@Composable
fun MainNavHost(stateHolder: MainNavHostStateHolder = rememberMainNavHostStateHolder()) {

    MainScreenRoot(
        onEssentialMenuClick = stateHolder::onEssentialMenuClick,
        onMainCategoryClick = stateHolder::onMainCategoryClick,
        onSubCategoryClick = { key -> /*TODO*/ },
        onSettingIconClick = stateHolder::navigateToSetting
    ) { paddingValues, drawerCloseBackHandler ->
        NavHost(
            navController = stateHolder.navController,
            startDestination = MainDestinations.MainCategory.route,
            modifier = Modifier.padding(paddingValues = paddingValues)
        ) {
            composable(MainDestinations.MainCategory.route) {
                MainCategoryScreen(
                    onMainCategoryClick = stateHolder::navigateToSubCategory,
                    drawerCloseBackHandler = drawerCloseBackHandler
                )
            }
            composable(
                route = MainDestinations.SubCategory.routeWithArg,
                arguments = MainDestinations.SubCategory.arguments
            ) { entry ->
                val mainCategoryName =
                    entry.arguments?.getString(MainDestinations.SubCategory.mainCategoryName)!!
                SubCategoryScreen(
                    parent = enumValueOf(mainCategoryName),
                    drawerCloseBackHandler = drawerCloseBackHandler
                )
            }
            composable(MainDestinations.Setting.route) {
                SettingScreen(
                    drawerCloseBackHandler = drawerCloseBackHandler,
                    onSignOutButtonClick = stateHolder::navigateToSetting)
            }
        }
    }
}

data class MainNavHostStateHolder(val navController: NavHostController) {
    fun onEssentialMenuClick(essentialMenu: EssentialMenus) =
        when (essentialMenu) {
            EssentialMenus.MainScreen -> navigateToMain()
            EssentialMenus.Favorite -> {} // TODO
            EssentialMenus.SeeAll -> {} // TODO
            EssentialMenus.Trash -> {} // TODO
        }

    fun onMainCategoryClick(subCategoryParent: SubCategoryParent) =
        navController.navigate(route = "${MainDestinations.SubCategory.route}/${subCategoryParent.name}")

    private fun navigateToMain() =
        navController.navigate(route = MainDestinations.MainCategory.route) {
            launchSingleTop = true
        }

    fun navigateToSetting() =
        navController.navigate(route = MainDestinations.Setting.route) {
            popUpTo(route = MainDestinations.MainCategory.route)
            launchSingleTop = true
        }

    fun navigateToSubCategory(subCategoryParent: SubCategoryParent) =
        navController.navigate("${MainDestinations.SubCategory.route}/${subCategoryParent.name}") {
            popUpTo(route = MainDestinations.MainCategory.route)
        }
}

@Composable
fun rememberMainNavHostStateHolder(navController: NavHostController = rememberNavController()) =
    remember { MainNavHostStateHolder(navController) }