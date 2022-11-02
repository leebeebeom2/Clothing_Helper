package com.leebeebeom.clothinghelper.main.subcategory

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomAppBar
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.Anime.BottomAppbar.bottomAppbarExpandIn
import com.leebeebeom.clothinghelper.base.Anime.BottomAppbar.bottomAppbarShrinkOut
import com.leebeebeom.clothinghelper.base.CircleCheckBox
import com.leebeebeom.clothinghelper.base.CustomIconButton
import com.leebeebeom.clothinghelper.base.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.base.SimpleWidthSpacer

@Composable
fun SubCategoryBottomAppBar(
    subCategoryBottomAppbarState: SubCategoryBottomAppbarState,
    onAllSelectCheckBoxClick: () -> Unit,
    onEditSubCategoryNameClick: () -> Unit
) {
    AnimatedVisibility(
        visible = subCategoryBottomAppbarState.isSelectMode,
        enter = bottomAppbarExpandIn,
        exit = bottomAppbarShrinkOut
    ) {
        BottomAppBar {
            SimpleWidthSpacer(dp = 4)
            CircleCheckBox(
                isChecked = subCategoryBottomAppbarState.isAllSelected,
                modifier = Modifier.size(22.dp),
                onClick = onAllSelectCheckBoxClick
            )
            SimpleWidthSpacer(dp = 10)
            Text(
                text = stringResource(
                    id = R.string.count_selected,
                    formatArgs = arrayOf(subCategoryBottomAppbarState.selectedSubCategoriesSize)
                ), modifier = Modifier.offset((-8).dp, 1.dp)
            )
            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End) {

                if (subCategoryBottomAppbarState.showEditName)
                    BottomAppBarIcon(
                        onClick = onEditSubCategoryNameClick,
                        drawable = R.drawable.ic_edit,
                        text = R.string.change_name
                    )

                if (subCategoryBottomAppbarState.showDeleted)
                    BottomAppBarIcon(
                        onClick = { /*TODO*/ },
                        drawable = R.drawable.ic_delete2,
                        text = R.string.delete
                    )
            }
            SimpleWidthSpacer(dp = 4)
        }
    }
}

@Composable
fun BottomAppBarIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @DrawableRes drawable: Int,
    @StringRes text: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomIconButton(modifier = modifier, onClick = onClick, drawable = drawable)
        SimpleHeightSpacer(dp = 4)
        Text(text = stringResource(id = text), style = MaterialTheme.typography.caption)
    }
}

data class SubCategoryBottomAppbarState(
    val isSelectMode: Boolean,
    val selectedSubCategoriesSize: Int,
    val subCategoriesSize: Int,
) {
    val isAllSelected get() = selectedSubCategoriesSize == subCategoriesSize
    val showEditName get() = selectedSubCategoriesSize == 1
    val showDeleted get() = selectedSubCategoriesSize > 0
}

@Composable
fun rememberSubCategoryBottomAppbarState(
    subCategoryScreenState: SubCategoryScreenState,
    subCategoriesSize: Int
) = remember {
    derivedStateOf {
        SubCategoryBottomAppbarState(
            isSelectMode = subCategoryScreenState.isSelectMode,
            selectedSubCategoriesSize = subCategoryScreenState.selectedSubCategoriesSize,
            subCategoriesSize = subCategoriesSize
        )
    }
}