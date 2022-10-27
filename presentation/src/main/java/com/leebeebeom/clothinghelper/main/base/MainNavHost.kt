package com.leebeebeom.clothinghelper.main.base

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.leebeebeom.clothinghelper.main.maincategory.MainCategoryScreen
import com.leebeebeom.clothinghelper.main.setting.SettingScreen
import com.leebeebeom.clothinghelper.main.subcategory.SubCategoryScreen

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
fun MainNavHost() {
    val navController = rememberNavController()

    MainScreenRoot(
        onEssentialMenuClick = navController::navigateToEssentialMenu,
        onMainCategoryClick = navController::navigateToSubCategory,
        onSubCategoryClick = { key -> /*TODO*/ },
        onSettingIconClick = { navController.mainNavigate(MainDestinations.Setting.route) }) { padding, isSubCategoryLoading ->
        MainNavHostWithArg(
            navController = navController,
            paddingValues = padding,
            isSubCategoriesLoading = isSubCategoryLoading
        )
    }
}

fun NavController.mainNavigate(destination: String) = navigate(destination) {
    popUpTo(MainDestinations.MainCategory.route)
    launchSingleTop = true
}

fun NavController.navigateToSubCategory(mainCategoryName: String) {
    mainNavigate("${MainDestinations.SubCategory.route}/$mainCategoryName")
}

fun NavController.navigateToEssentialMenu(essentialMenu: EssentialMenus) {
    when (essentialMenu) {
        EssentialMenus.MAIN_SCREEN -> mainNavigate(MainDestinations.MainCategory.route)
        EssentialMenus.FAVORITE -> {} // TODO 
        EssentialMenus.SEE_ALL -> {} // TODO
        EssentialMenus.TRASH -> {} // TODO
    }
}

@Composable
fun MainNavHostWithArg(
    navController: NavHostController,
    paddingValues: PaddingValues,
    isSubCategoriesLoading: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = MainDestinations.MainCategory.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(MainDestinations.MainCategory.route) {
            MainCategoryScreen(
                onMainCategoryClick = navController::navigateToSubCategory,
                isSubCategoriesLoading = isSubCategoriesLoading
            )
        }
        composable(
            route = MainDestinations.SubCategory.routeWithArg,
            arguments = MainDestinations.SubCategory.arguments
        ) { entry ->
            val mainCategoryName = entry.arguments?.getString(MainDestinations.SubCategory.mainCategoryName)!!
            SubCategoryScreen(
                mainCategoryName = mainCategoryName,
                isSubCategoriesLoading = isSubCategoriesLoading
            )
        }
        composable(MainDestinations.Setting.route) {
            SettingScreen()
        }
    }
}