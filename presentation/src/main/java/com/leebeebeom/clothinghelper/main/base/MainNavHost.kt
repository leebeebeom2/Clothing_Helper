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
import com.leebeebeom.clothinghelperdomain.model.EssentialMenus
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent

sealed class MainDestinations(val route: String) {
    object MainCategory : MainDestinations("mainCategory")
    object SubCategory : MainDestinations("subCategory") {
        const val parentNameArg: String = "parentName"
        val routeWithArg = "$route/{$parentNameArg}"
        val arguments = listOf(navArgument(parentNameArg) { type = NavType.StringType })
    }

    object Setting : MainDestinations("setting")
}

@Composable
fun MainNavHost() {
    val navController = rememberNavController()

    MainScreenRoot(onSettingIconClick = { navController.mainNavigate(MainDestinations.Setting.route) },
        onDrawerContentClick = { parentName -> navController.drawerNavigate(parentName) }) { padding, getIsSubCategoriesLoading, onDrawerIconClick ->
        MainNavHostWithArg(
            navController = navController,
            paddingValues = padding,
            getIsSubCategoriesLoading = getIsSubCategoriesLoading,
            onDrawerIconClick = onDrawerIconClick
        )
    }
}

fun NavController.mainNavigate(destination: String) = navigate(destination) {
    popUpTo(MainDestinations.MainCategory.route)
    launchSingleTop = true
}

fun NavController.navigateToSubCategory(parentName: String) {
    mainNavigate("${MainDestinations.SubCategory.route}/$parentName")
}

fun NavController.drawerNavigate(parentName: String) {
    when (parentName) {
        EssentialMenus.MAIN_SCREEN.name -> mainNavigate(MainDestinations.MainCategory.route)
        EssentialMenus.FAVORITE.name -> {/*TODO*/
        }
        EssentialMenus.SEE_ALL.name -> { /*TODO*/
        }
        EssentialMenus.TRASH.name -> { /*TODO*/
        }
        else -> navigateToSubCategory(parentName)
    }
}

@Composable
fun MainNavHostWithArg(
    navController: NavHostController,
    paddingValues: PaddingValues,
    getIsSubCategoriesLoading: (SubCategoryParent) -> Boolean,
    onDrawerIconClick: () -> Unit // TODO
) {
    NavHost(
        navController = navController,
        startDestination = MainDestinations.MainCategory.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(MainDestinations.MainCategory.route) {
            MainCategoryScreen(onMainCategoryClick = { parentName ->
                navController.navigateToSubCategory(
                    parentName
                )
            }, getIsSubCategoriesLoading = getIsSubCategoriesLoading)
        }
        composable(
            route = MainDestinations.SubCategory.routeWithArg,
            arguments = MainDestinations.SubCategory.arguments
        ) { entry ->
            val parentName =
                entry.arguments?.getString(MainDestinations.SubCategory.parentNameArg)!!
            SubCategoryScreen(
                parentName = parentName,
                getIsSubCategoriesLoading = getIsSubCategoriesLoading
            )
        }
        composable(MainDestinations.Setting.route) {
            SettingScreen()
        }
    }
}