package com.leebeebeom.clothinghelper.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.leebeebeom.clothinghelper.main.maincategory.MainCategoryScreen
import com.leebeebeom.clothinghelper.main.sizeChart.SizeChartScreen
import com.leebeebeom.clothinghelper.main.sizechartlist.SizeChartListScreen
import com.leebeebeom.clothinghelper.main.subcategory.SubCategoryScreen
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.util.getStringArg
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent

fun NavGraphBuilder.mainGraph(
    onMainCategoryClick: (SubCategoryParent) -> Unit,
    onSubCategoryClick: (StableSubCategory) -> Unit,
    onFolderClick: (StableFolder) -> Unit,
    onSizeChartListFabClick: (path: String) -> Unit,
) {
    // Main
    composable(route = MainDestinations.MainCategory.route) {
        MainCategoryScreen(onMainCategoryClick = onMainCategoryClick)
    }

    // SubCategory
    composable(
        route = MainDestinations.SubCategory.routeWithArg,
        arguments = MainDestinations.SubCategory.arguments
    ) { entry ->
        SubCategoryScreen(
            parent = enumValueOf(entry.getStringArg(MainDestinations.SubCategory.parent)!!),
            onSubCategoryClick = onSubCategoryClick
        )
    }

    // SizeChartList
    composable(
        route = MainDestinations.SizeChartList.routeWithArg,
        arguments = MainDestinations.SizeChartList.arguments
    ) { entry ->
        val arguments = entry.arguments!!
        SizeChartListScreen(
            parentKey = arguments.getString(MainDestinations.SizeChartList.parentKey)!!,
            subCategoryKey = arguments.getString(MainDestinations.SizeChartList.subCategoryKey)!!,
            onFolderClick = onFolderClick,
            onFabClick = onSizeChartListFabClick
        )
    }

    // SizeChart
    composable(
        route = MainDestinations.SizeChart.routeWithArg,
        arguments = MainDestinations.SizeChart.arguments
    ) { entry ->
        val path = entry.arguments?.getString(MainDestinations.SizeChart.path)!!
        SizeChartScreen(path = path)
    }
}