package com.leebeebeom.clothinghelper.ui.main

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.ui.main.MainDestinations.Main
import com.leebeebeom.clothinghelper.ui.main.MainDetailDestination.MainCategory
import com.leebeebeom.clothinghelper.ui.main.MainDetailDestination.SizeChart
import com.leebeebeom.clothinghelper.ui.main.MainDetailDestination.SizeChartList
import com.leebeebeom.clothinghelper.ui.main.MainDetailDestination.SubCategory
import com.leebeebeom.clothinghelper.ui.main.maincategory.MainCategoryScreen
import com.leebeebeom.clothinghelper.ui.main.sizeChart.SizeChartScreen
import com.leebeebeom.clothinghelper.ui.main.sizechartlist.SizeChartListScreen
import com.leebeebeom.clothinghelper.ui.main.subcategory.SubCategoryScreen
import com.leebeebeom.clothinghelper.util.getStringArg
import com.leebeebeom.clothinghelperdomain.model.data.BaseModel
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent

object MainDetailDestination {
    object MainCategory : MainDestinations("mainCategory")

    object SubCategory : MainDestinations("subCategory") {
        const val parent = "parent"
        val routeWithArg = "$route/{$parent}"
        val arguments = listOf(navArgument(parent) { type = NavType.StringType })
    }

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
        val arguments = listOf(navArgument(path) { type = NavType.StringType })
    }
}

fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    navigation(
        route = Main.route, startDestination = MainCategory.route
    ) {
        // main
        composable(route = MainCategory.route) {
            MainCategoryScreen(onMainCategoryClick = navController::navigateToSubCategory)
        }
        // sub category
        composable(
            route = SubCategory.routeWithArg, arguments = SubCategory.arguments
        ) { entry ->
            SubCategoryScreen(
                parent = enumValueOf(entry.getStringArg(SubCategory.parent)!!),
                onSubCategoryClick = navController::navigateToSizeChartList
            )
        }
        // size chart list
        composable(
            route = SizeChartList.routeWithArg, arguments = SizeChartList.arguments
        ) { entry ->
            val arguments = entry.arguments!!
            SizeChartListScreen(
                parentKey = arguments.getString(SizeChartList.parentKey)!!,
                subCategoryKey = arguments.getString(SizeChartList.subCategoryKey)!!,
                onFolderClick = navController::navigateToSizeChartList,
                onFabClick = navController::navigateToSizeChart
            )
        }
        // size chart
        composable(
            route = SizeChart.routeWithArg, arguments = SizeChart.arguments
        ) { entry ->
            val path = entry.arguments?.getString(SizeChart.path)!!
            SizeChartScreen(path = path)
        }
    }
}

fun NavHostController.navigateToSubCategory(parent: SubCategoryParent) {
    val currentParent = currentBackStackEntry.getStringArg(SubCategory.parent)

    if (currentParent != parent.name) navigate("${SubCategory.route}/${parent.name}")
}

fun NavHostController.navigateToSizeChartList(container: BaseModel) {
    val parentKey = container.key
    val currentParentKey =
        currentBackStackEntry.getStringArg(SizeChartList.parentKey)

    if (parentKey != currentParentKey) {
        val subCategoryKey =
            if (container is StableSubCategory) container.key
            else (container as StableFolder).subCategoryKey
        navigate(route = "${SizeChartList.route}/${parentKey}/${subCategoryKey}")
    }
}

fun NavHostController.navigateToSizeChart(path: String) {
    navigate(route = "${SizeChart.route}/$path")
}