package com.leebeebeom.clothinghelper.ui.drawer.content

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable // skippable
fun DrawerMainCategories(
    mainCategories: ImmutableList<MainCategory>,
    onMainCategoryClick: (MainCategoryType) -> Unit,
    subCategories: () -> ImmutableList<SubCategory>,
    subCategoryNamesMap: () -> ImmutableMap<MainCategoryType, ImmutableSet<String>>,
    subCategorySizeMap: () -> ImmutableMap<MainCategoryType, Int>,
    addSubCategory: AddSubCategory,
    density: Density,
    drawerSubCategories: @Composable (filteredSubCategories: () -> ImmutableList<SubCategory>, filteredSubCategoryNames: () -> ImmutableSet<String>) -> Unit
) {
    mainCategories.forEach { mainCategory ->
        key(mainCategory.type) {
            DrawerMainCategory(
                mainCategory = mainCategory,
                onMainCategoryClick = onMainCategoryClick,
                subCategories = subCategories,
                subCategoryNames = subCategoryNamesMap,
                subCategorySize = subCategorySizeMap,
                addSubCategory = addSubCategory,
                drawerSubCategories = drawerSubCategories,
                density = density
            )
        }
    }
}

@Composable // skippable
private fun DrawerMainCategory(
    mainCategory: MainCategory,
    onMainCategoryClick: (MainCategoryType) -> Unit,
    subCategories: () -> ImmutableList<SubCategory>,
    subCategoryNames: () -> ImmutableMap<MainCategoryType, ImmutableSet<String>>,
    subCategorySize: () -> ImmutableMap<MainCategoryType, Int>,
    addSubCategory: AddSubCategory,
    density: Density,
    state: DrawerItemState = rememberDrawerItemState(),
    drawerSubCategories: @Composable (filteredSubCategories: () -> ImmutableList<SubCategory>, filteredSubCategoryNames: () -> ImmutableSet<String>) -> Unit
) {
    val childSubCategorySize by remember {
        derivedStateOf { subCategorySize().getOrDefault(key = mainCategory.type, defaultValue = 0) }
    }
    val childSubCategoryNames by remember {
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
            MainCategoryCount(subCategoriesSize = { childSubCategorySize })
        }
        DrawerExpandIcon(expanded = { state.expanded },
            toggleExpand = state::toggleExpand,
            dataSize = { childSubCategorySize })
        DrawerMainCategoryDropDownMenu(
            show = { state.showDropDownMenu },
            offset = { longClickOffset },
            onDismiss = state::onDropdownMenuDismiss,
            subCategoryNames = { childSubCategoryNames },
            onAddSubCategoryPositiveClick = { name ->
                addSubCategory(name, mainCategory.type)
                state.expand()
            })
    }

    val childSubCategories by remember {
        derivedStateOf {
            subCategories().filter { it.mainCategoryType == mainCategory.type }.toImmutableList()
        }
    }
    DrawerItemsWrapperWithExpandAnimation(expand = { state.expanded }) {
        drawerSubCategories(
            filteredSubCategories = { childSubCategories },
            filteredSubCategoryNames = { childSubCategoryNames }
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