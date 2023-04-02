package com.leebeebeom.clothinghelper.ui.drawer.content

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.ui.component.SingleLineText
import com.leebeebeom.clothinghelper.ui.drawer.DrawerItemState
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerExpandIcon
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerItemsWrapperWithExpandAnimation
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerRow
import com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus.DrawerMainCategoryDropDownMenu
import com.leebeebeom.clothinghelper.ui.drawer.rememberDrawerItemState
import com.leebeebeom.clothinghelper.ui.util.AddSubCategory
import kotlinx.collections.immutable.*

// TODO expand 시 마지막 아이템으로 스크롤

@Composable // skippable
fun DrawerMainCategories(
    mainCategories: ImmutableList<MainCategory>,
    onMainCategoryClick: (MainCategoryType) -> Unit,
    allSubCategories: () -> ImmutableList<SubCategory>,
    subCategoryNamesMap: () -> ImmutableMap<MainCategoryType, ImmutableSet<String>>,
    subCategorySizeMap: () -> ImmutableMap<MainCategoryType, Int>,
    addSubCategory: AddSubCategory,
    drawerSubCategories: @Composable (filteredSubCategories: () -> ImmutableList<SubCategory>, subCategoryNames: () -> ImmutableSet<String>) -> Unit
) {
    mainCategories.forEach { mainCategory ->
        key(mainCategory.type) {
            DrawerMainCategory(
                mainCategory = mainCategory,
                onMainCategoryClick = onMainCategoryClick,
                allSubCategories = allSubCategories,
                subCategoryNames = subCategoryNamesMap,
                subCategorySize = subCategorySizeMap,
                addSubCategory = addSubCategory,
                drawerSubCategories = drawerSubCategories
            )
        }
    }
}

@Composable // skippable
private fun DrawerMainCategory(
    mainCategory: MainCategory,
    onMainCategoryClick: (MainCategoryType) -> Unit,
    allSubCategories: () -> ImmutableList<SubCategory>,
    subCategoryNames: () -> ImmutableMap<MainCategoryType, ImmutableSet<String>>,
    subCategorySize: () -> ImmutableMap<MainCategoryType, Int>,
    addSubCategory: AddSubCategory,
    density: Density = LocalDensity.current,
    state: DrawerItemState = rememberDrawerItemState(),
    drawerSubCategories: @Composable (filteredSubCategories: () -> ImmutableList<SubCategory>, subCategoryNames: () -> ImmutableSet<String>) -> Unit
) {
    val localSubCategorySize by remember {
        derivedStateOf { subCategorySize().getOrDefault(key = mainCategory.type, defaultValue = 0) }
    }
    val localSubCategoryNames by remember {
        derivedStateOf {
            subCategoryNames().getOrDefault(
                key = mainCategory.type,
                defaultValue = persistentSetOf()
            )
        }
    }
    val longClickOffset by remember {
        derivedStateOf {
            with(density) {
                DpOffset(
                    x = state.longClickOffsetX.toDp(),
                    y = state.longClickOffsetY.toDp() - state.itemHeight.toDp()
                )
            }
        }
    }

    DrawerRow(
        modifier = Modifier.heightIn(44.dp),
        onClick = { onMainCategoryClick(mainCategory.type) },
        onLongClick = state::onLongClick,
        onSizeChange = state::onSizeChanged
    ) {
        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
            SingleLineText(
                modifier = Modifier.padding(start = 8.dp),
                text = mainCategory.name,
                style = MaterialTheme.typography.h6
            )
            MainCategoryCount(subCategoriesSize = { localSubCategorySize })
        }
        DrawerExpandIcon(expanded = { state.expanded },
            toggleExpand = state::toggleExpand,
            dataSize = { localSubCategorySize })
        DrawerMainCategoryDropDownMenu(
            show = { state.showDropDownMenu },
            offset = { longClickOffset },
            onDismiss = state::onDropdownMenuDismiss,
            subCategoryNames = { localSubCategoryNames },
            onAddSubCategoryPositiveClick = { name ->
                addSubCategory(name, mainCategory.type)
                state.expand()
            })
    }

    val filteredSubCategories by remember {
        derivedStateOf {
            allSubCategories().filter { it.mainCategoryType == mainCategory.type }.toImmutableList()
        }
    }
    DrawerItemsWrapperWithExpandAnimation(expand = { state.expanded }) {
        drawerSubCategories(
            filteredSubCategories = { filteredSubCategories },
            subCategoryNames = { localSubCategoryNames }
        )
    }
}

@Composable // skippable
private fun MainCategoryCount(subCategoriesSize: () -> Int) {
    SingleLineText(
        modifier = Modifier.padding(start = 4.dp),
        text = "(${subCategoriesSize()})",
        style = MaterialTheme.typography.caption.copy(
            LocalContentColor.current.copy(ContentAlpha.disabled)
        )
    )
}

data class MainCategory(
    val name: Int, val type: MainCategoryType
)

enum class MainCategoryType {
    Top, Bottom, Outer, Etc
}

fun getMainCategories() = persistentListOf(
    MainCategory(R.string.top, MainCategoryType.Top),
    MainCategory(R.string.bottom, MainCategoryType.Bottom),
    MainCategory(R.string.outer, MainCategoryType.Outer),
    MainCategory(R.string.etc, MainCategoryType.Etc)
)