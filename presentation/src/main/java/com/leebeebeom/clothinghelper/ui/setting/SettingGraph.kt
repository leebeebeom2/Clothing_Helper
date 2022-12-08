package com.leebeebeom.clothinghelper.ui.setting

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.leebeebeom.clothinghelper.ui.main.MainDestinations
import com.leebeebeom.clothinghelper.ui.setting.SettingDestination.*
import com.leebeebeom.clothinghelper.ui.setting.sizecharttemplate.SizeChartTemplateListScreen
import com.leebeebeom.clothinghelper.ui.setting.sizecharttemplate.SizeChartTemplateScreen
import com.leebeebeom.clothinghelper.util.navigateSingleTop

sealed class SettingDestination(val route: String) {
    object Setting : SettingDestination("settingMain")

    object SizeChartTemplateList : SettingDestination("SizeChartTemplateList")

    object SizeChartTemplate : SettingDestination("SizeChartTemplate") {
        const val title = "title"
        val routeWithArg = "$route/{$title}"
        val arguments = listOf(navArgument(name = title) { type = NavType.IntType })
    }
}

fun NavGraphBuilder.settingGraph(navController: NavHostController) {
    navigation(
        route = MainDestinations.Setting.route,
        startDestination = Setting.route,
    ) {
        // setting main
        composable(route = Setting.route) {
            SettingScreen(onSizeChartTemplateButtonClick = {
                navController.navigateSingleTop(SizeChartTemplateList.route)
            })
        }

        // size chart template list
        composable(route = SizeChartTemplateList.route) {
            SizeChartTemplateListScreen(
                onAddTemplateClick = {
                    navController.navigateSingleTop("${SizeChartTemplate.route}/$it")
                }
            )
        }

        // size chart template
        composable(
            route = SizeChartTemplate.routeWithArg,
            arguments = SizeChartTemplate.arguments
        ) { entry ->
            val title = entry.arguments!!.getInt(SizeChartTemplate.title)
            SizeChartTemplateScreen(title)
        }
    }
}