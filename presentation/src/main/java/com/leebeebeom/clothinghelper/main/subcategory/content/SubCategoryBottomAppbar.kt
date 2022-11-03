package com.leebeebeom.clothinghelper.main.subcategory.content

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
import com.leebeebeom.clothinghelper.main.subcategory.SubCategoryStateHolder

@Composable
fun SubCategoryBottomAppBar(
    stateHolder: SubCategoryBottomAppbarState,
    onAllSelectCheckBoxClick: () -> Unit,
    onEditSubCategoryNameClick: () -> Unit
) {
    AnimatedVisibility(
        visible = stateHolder.isSelectModeState,
        enter = bottomAppbarExpandIn,
        exit = bottomAppbarShrinkOut
    ) {
        BottomAppBar {
            SimpleWidthSpacer(dp = 4)
            CircleCheckBox(
                isChecked = stateHolder.isAllSelected,
                modifier = Modifier.size(22.dp),
                onClick = onAllSelectCheckBoxClick
            )
            SimpleWidthSpacer(dp = 10)
            Text(
                text = stringResource(
                    id = R.string.count_selected,
                    formatArgs = arrayOf(stateHolder.selectedSubCategoriesSizeState)
                ), modifier = Modifier.offset((-8).dp, 1.dp)
            )
            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End) {

                if (stateHolder.showEditName)
                    BottomAppBarIcon(
                        onClick = onEditSubCategoryNameClick,
                        drawable = R.drawable.ic_edit,
                        text = R.string.change_name
                    )

                if (stateHolder.showDeleted)
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
    val isSelectModeState: Boolean,
    val selectedSubCategoriesSizeState: Int,
    val subCategoriesSizeState: Int,
) {
    val isAllSelected get() = selectedSubCategoriesSizeState == subCategoriesSizeState
    val showEditName get() = selectedSubCategoriesSizeState == 1
    val showDeleted get() = selectedSubCategoriesSizeState > 0
}

@Composable
fun rememberSubCategoryBottomAppbarState(
    stateHolder: SubCategoryStateHolder,
    subCategoriesSizeState: Int
) = remember {
    derivedStateOf {
        SubCategoryBottomAppbarState(
            isSelectModeState = stateHolder.isSelectModeState,
            selectedSubCategoriesSizeState = stateHolder.selectedSubCategoriesSizeState,
            subCategoriesSizeState = subCategoriesSizeState
        )
    }
}