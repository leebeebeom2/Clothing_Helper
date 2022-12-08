package com.leebeebeom.clothinghelper.ui.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.ui.main.MainDestinations.*
import com.leebeebeom.clothinghelper.ui.main.maincategory.MainCategoryScreen
import com.leebeebeom.clothinghelper.ui.main.sizeChart.SizeChartScreen
import com.leebeebeom.clothinghelper.ui.main.sizechartlist.SizeChartListScreen
import com.leebeebeom.clothinghelper.ui.main.subcategory.SubCategoryScreen
import com.leebeebeom.clothinghelper.util.getStringArg
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent

fun NavGraphBuilder.mainGraph(
    onMainCategoryClick: (SubCategoryParent) -> Unit,
    onSubCategoryClick: (StableSubCategory) -> Unit,
    onFolderClick: (StableFolder) -> Unit,
    onSizeChartListFabClick: (path: String) -> Unit,
) {
    // main
    composable(route = MainCategory.route) {
        MainCategoryScreen(onMainCategoryClick = onMainCategoryClick)
    }

    // sub category
    composable(
        route = SubCategory.routeWithArg,
        arguments = SubCategory.arguments
    ) { entry ->
        SubCategoryScreen(
            parent = enumValueOf(entry.getStringArg(SubCategory.parent)!!),
            onSubCategoryClick = onSubCategoryClick
        )
    }

    // size chart list
    composable(
        route = SizeChartList.routeWithArg,
        arguments = SizeChartList.arguments
    ) { entry ->
        val arguments = entry.arguments!!
        SizeChartListScreen(
            parentKey = arguments.getString(SizeChartList.parentKey)!!,
            subCategoryKey = arguments.getString(SizeChartList.subCategoryKey)!!,
            onFolderClick = onFolderClick,
            onFabClick = onSizeChartListFabClick
        )
    }

    // size chart
    composable(
        route = SizeChart.routeWithArg,
        arguments = SizeChart.arguments
    ) { entry ->
        val path = entry.arguments?.getString(SizeChart.path)!!
        SizeChartScreen(path = path)
    }
}