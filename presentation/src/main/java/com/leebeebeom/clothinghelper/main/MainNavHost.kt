package com.leebeebeom.clothinghelper.main

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
fun MainNavHost() {
    val navController = rememberNavController()

    MainScreenRoot(
        onEssentialMenuClick = navController::navigateToEssentialMenu,
        onMainCategoryClick = navController::navigateToSubCategory,
        onSubCategoryClick = { key -> /*TODO*/ },
        onSettingIconClick = { navController.mainNavigate(MainDestinations.Setting.route) }) { padding, drawerCloseBackHandler ->
        MainNavHostWithArg(
            navController = navController,
            paddingValues = padding,
            drawerCloseBackHandler = drawerCloseBackHandler
        )
    }
}

fun NavController.mainNavigate(destination: String) = navigate(destination) {
    popUpTo(MainDestinations.MainCategory.route)
    launchSingleTop = true
}

fun NavController.navigateToSubCategory(subCategoryParent: SubCategoryParent) =
    mainNavigate("${MainDestinations.SubCategory.route}/${subCategoryParent.name}")

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
    drawerCloseBackHandler: @Composable () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = MainDestinations.MainCategory.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(MainDestinations.MainCategory.route) {
            MainCategoryScreen(
                onMainCategoryClick = navController::navigateToSubCategory,
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
                subCategoryParent = enumValueOf(mainCategoryName),
                drawerCloseBackHandler = drawerCloseBackHandler
            )
        }
        composable(MainDestinations.Setting.route) {
            SettingScreen(
                drawerCloseBackHandler = drawerCloseBackHandler,
                onSignOutButtonClick = { navController.mainNavigate(MainDestinations.MainCategory.route) })
        }
    }
}