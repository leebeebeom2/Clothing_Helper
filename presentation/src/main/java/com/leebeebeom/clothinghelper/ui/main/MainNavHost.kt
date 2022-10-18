package com.leebeebeom.clothinghelper.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.ui.main.base.MainScreenRoot
import com.leebeebeom.clothinghelper.ui.main.maincategory.MainCategoryScreen
import com.leebeebeom.clothinghelper.ui.main.setting.SettingScreen
import com.leebeebeom.clothinghelper.ui.main.subcategory.SubCategoryScreen

object MainDestinations {
    const val MAIN_CATEGORY = "mainCategory"
    const val SUB_CATEGORY = "subCategory"
    const val SETTING = "setting"
}

@Composable
fun MainNavHost(name: String, email: String) {
    val navController = rememberNavController()

    MainScreenRoot(
        onSettingIconClick = { navController.mainNavigate(MainDestinations.SETTING) },
        onDrawerContentClick = { clickedMenu -> },
        name = name, email = email
    ) {
        NavHost(
            navController = navController,
            startDestination = MainDestinations.MAIN_CATEGORY,
            modifier = Modifier.padding(it)
        ) {
            composable(MainDestinations.MAIN_CATEGORY) {
                MainCategoryScreen(
                    onMainCategoryClick = { navController.mainNavigate(MainDestinations.SUB_CATEGORY) })
            }
            composable(MainDestinations.SUB_CATEGORY) { SubCategoryScreen() }
            composable(MainDestinations.SETTING) { SettingScreen() }
        }
    }
}

fun NavController.mainNavigate(destination: String) =
    navigate(destination) {
        launchSingleTop = true
        popUpTo(MainDestinations.MAIN_CATEGORY)
    }