package com.leebeebeom.clothinghelper.main.root

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.base.Anime.List.listExpand
import com.leebeebeom.clothinghelper.base.Anime.List.listShrink
import com.leebeebeom.clothinghelper.base.DotProgressIndicator
import com.leebeebeom.clothinghelper.main.base.ExpandIcon
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerMainCategory(
    mainCategory: MainCategory,
    subCategories: (SubCategoryParent) -> ImmutableList<SubCategory>,
    isLoading: () -> Boolean,
    isAllExpand: () -> Boolean,
    onMainCategoryClick: (SubCategoryParent) -> Unit,
    onSubCategoryClick: (SubCategory) -> Unit,
) {
    var isExpand by rememberSaveable { mutableStateOf(isAllExpand()) }

    Column {
        DrawerContentRow(
            modifier = Modifier.heightIn(44.dp),
            onClick = { onMainCategoryClick(mainCategory.type) }
        ) {
            DrawerContentText(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(id = mainCategory.name),
                style = MaterialTheme.typography.subtitle1
            )
            TotalCount(
                subCategories = { subCategories(mainCategory.type) },
                isLoading = isLoading
            )
            ExpandIcon(
                isLoading = isLoading,
                isExpand = { isExpand },
                isAllExpand = isAllExpand,
                onClick = { isExpand = !isExpand },
                updateIsExpand = { isExpand = it }
            )
        }
        SubCategories(
            isExpand = { isExpand },
            subCategories = { subCategories(mainCategory.type) },
            onClick = onSubCategoryClick
        )
    }
}

@Composable
private fun ExpandIcon(
    isLoading: () -> Boolean,
    isExpand: () -> Boolean,
    onClick: () -> Unit,
    isAllExpand: () -> Boolean,
    updateIsExpand: (Boolean) -> Unit
) {
    val isAllExpandVal = isAllExpand()
    var rememberedIsAllExpand by rememberSaveable { mutableStateOf(isAllExpandVal) }
    if (isAllExpand() != rememberedIsAllExpand) {
        rememberedIsAllExpand = isAllExpandVal
        updateIsExpand(isAllExpandVal)
    }

    if (isLoading())
        DotProgressIndicator(
            modifier = Modifier.padding(end = 4.dp),
            size = 4.dp,
            color = MaterialTheme.colors.surface.copy(ContentAlpha.disabled)
        )
    else ExpandIcon(isExpanded = isExpand, onClick = onClick)
}

@Composable
private fun SubCategories(
    isExpand: () -> Boolean,
    subCategories: () -> ImmutableList<SubCategory>,
    onClick: (SubCategory) -> Unit
) {
    AnimatedVisibility(
        visible = isExpand(),
        enter = listExpand,
        exit = listShrink
    ) {
        Surface(color = MaterialTheme.colors.primary) {
            Column {
                for (subCategory in subCategories())
                    key(subCategory.key) {
                        SubCategory(
                            subCategory = { subCategory },
                            onClick = onClick
                        ) // TODO 익스팬드 시 두번씩 리컴포즈 됨 해결 못함
                    }
            }
        }
    }
}

@Composable
private fun SubCategory(
    subCategory: () -> SubCategory,
    onClick: (SubCategory) -> Unit
) {
    DrawerContentRow(
        modifier = Modifier
            .heightIn(40.dp)
            .padding(horizontal = 8.dp),
        onClick = { onClick(subCategory()) }
    ) {
        DrawerContentText(
            modifier = Modifier.padding(start = 12.dp),
            text = { subCategory().name },
            style = MaterialTheme.typography.subtitle2
        )
    }
}

@Composable
private fun RowScope.TotalCount(
    subCategories: () -> ImmutableList<SubCategory>,
    isLoading: () -> Boolean,
) {

    Text(
        modifier = Modifier
            .weight(1f)
            .padding(start = 4.dp),
        text = if (isLoading()) "" else "(${subCategories().size})",
        style = MaterialTheme.typography.caption.copy(
            LocalContentColor.current.copy(
                ContentAlpha.disabled
            )
        )
    )
}