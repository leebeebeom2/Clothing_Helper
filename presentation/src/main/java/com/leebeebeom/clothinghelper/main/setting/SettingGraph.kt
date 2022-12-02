package com.leebeebeom.clothinghelper.main.setting

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leebeebeom.clothinghelper.main.MainDestinations
import com.leebeebeom.clothinghelper.main.setting.sizecharttemplate.SizeChartTemplateScreen
import com.leebeebeom.clothinghelper.util.navigateSingleTop

sealed class SettingDestination(val route: String) {
    object Setting : SettingDestination("settingMain[")
    object SizeChartTemplate : SettingDestination("SizeChartTemplate")
}

fun NavGraphBuilder.settingGraph(navController: NavHostController) {
    navigation(
        route = MainDestinations.Setting.route,
        startDestination = SettingDestination.Setting.route,
    ) {
        composable(route = SettingDestination.Setting.route) {
            SettingScreen(onSizeChartTemplateButtonClick = {
                navController.navigateSingleTop(SettingDestination.SizeChartTemplate.route)
            })
        }
        composable(route = SettingDestination.SizeChartTemplate.route) {
            SizeChartTemplateScreen()
        }
    }
}