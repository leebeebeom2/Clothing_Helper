package com.leebeebeom.clothinghelper.main.subcategory

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.base.SimpleIcon
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent

@Composable
fun SubCategoryScreen(
    parentName: String,
    viewModel: SubCategoryViewModel = hiltViewModel(),
    getIsSubCategoriesLoading: (SubCategoryParent) -> Boolean
) {
    val viewModelState = viewModel.viewModelState
    val state = rememberSubCategoryScreenUIState(parentName = parentName)

    Box(modifier = Modifier.fillMaxSize()) {
        if (getIsSubCategoriesLoading(state.subCategoryParent)) CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = LocalContentColor.current.copy(ContentAlpha.medium)
        )
        // TODO 삭제, 이름 수정
        else LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp, end = 16.dp, top = 16.dp, bottom = 120.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                Header(
                    subCategoryParent = state.subCategoryParent,
                    toggleAllExpand = viewModel::toggleAllExpand,
                    allExpand = viewModelState.allExpand
                )
            }
            itemsIndexed(items = viewModelState.getSubCategories(state.subCategoryParent),
                key = { _, subCategory -> subCategory.key }) { index, subCategory ->
                SubCategoryContent(
                    subCategory = subCategory,
                    isExpanded = viewModelState.getExpandState(index),
                    expandToggle = { viewModelState.expandToggle(index) },
                    deletedSubCategory = viewModel::deleteSubCategory
                )
            }
        }

        AddCategoryDialogFab(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp),
            onPositiveButtonClick = {
                viewModel.addSubCategory(state.subCategoryParent, it)
            },
            subCategories = viewModelState.getSubCategories(state.subCategoryParent)
        )
    }
}

@Composable
private fun Header(
    subCategoryParent: SubCategoryParent, toggleAllExpand: () -> Unit, allExpand: Boolean
) {
    SubCategoryHeaderText(getHeaderTextRes(subCategoryParent))
    Row(verticalAlignment = Alignment.CenterVertically) {
        Divider(modifier = Modifier.weight(1f))
        AllExpandIcon(onClick = toggleAllExpand, allExpand)
        HeaderIcon(R.drawable.ic_sort) {}
    }
}

@Composable
private fun HeaderIcon(@DrawableRes drawable: Int, onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = Modifier.size(22.dp)) {
        SimpleIcon(
            drawable = drawable, tint = LocalContentColor.current.copy(0.5f)
        )
    }
}

@Composable
fun AllExpandIcon(onClick: () -> Unit, allExpand: Boolean) {
    IconButton(
        onClick = onClick, modifier = Modifier.size(22.dp)
    ) {
        SimpleIcon(
            drawable = if (allExpand) R.drawable.ic_unfold_less else R.drawable.ic_all_expand,
            tint = LocalContentColor.current.copy(0.5f)
        )
    }
}

@Composable
fun SubCategoryHeaderText(@StringRes headerTextRes: Int) {
    Text(
        modifier = Modifier.padding(4.dp),
        text = stringResource(id = headerTextRes),
        style = MaterialTheme.typography.h2,
        fontSize = 32.sp
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SubCategoryContent(
    subCategory: SubCategory,
    isExpanded: Boolean,
    expandToggle: () -> Unit,
    deletedSubCategory: (key: String) -> Unit
) {
    var showDropDownMenu by rememberSaveable { mutableStateOf(false) }
    Box {
        Card(elevation = 2.dp, shape = RoundedCornerShape(12.dp)) {
            Column(modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .combinedClickable(
                    onClick = { /*TODO*/ },
                    onLongClick = { showDropDownMenu = !showDropDownMenu }
                )) {
                SubCategoryTitle(subCategory.name, isExpanded, onExpandIconClick = expandToggle)
                SubCategoryInfo(isExpanded)
            }
        }

        @Composable
        fun DropDownMenuText(@StringRes text: Int, onClick: () -> Unit) {
            Text(
                text = stringResource(id = text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(onClick = onClick)
                    .padding(start = 8.dp, end = 24.dp)
                    .padding(vertical = 8.dp)
            )
        }

        MaterialTheme(shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(20.dp))) {
            DropdownMenu(
                expanded = showDropDownMenu,
                onDismissRequest = { showDropDownMenu = false },
                offset = DpOffset(30.dp, (-30).dp)
            ) {
                DropDownMenuText(text = R.string.select_mode) {
                    showDropDownMenu = false
                }
                DropDownMenuText(text = R.string.change_name) {
                    showDropDownMenu = false
                }
                DropDownMenuText(text = R.string.delete) {
                    deletedSubCategory(subCategory.key)
                    showDropDownMenu = false
                }
            }
        }

    }

}

@Composable
private fun SubCategoryInfo(isExpanded: Boolean) {
    AnimatedVisibility(
        visible = isExpanded,
        enter = expandVertically(animationSpec = tween(250)),
        exit = shrinkVertically(animationSpec = tween(250))
    ) {
        Surface(color = MaterialTheme.colors.background) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                val weightModifier = Modifier.weight(1f)

                SubCategoryInfoText( // TODO
                    modifier = weightModifier,
                    infoTitle = R.string.average_size,
                    info = R.string.top_info
                )

                SubCategoryInfoText(
                    modifier = weightModifier,
                    infoTitle = R.string.most_have_size,
                    info = R.string.top_info
                )
            }
        }
    }
}

@Composable
private fun SubCategoryInfoText(
    modifier: Modifier, @StringRes infoTitle: Int, @StringRes info: Int
) {
    ProvideTextStyle(
        value = MaterialTheme.typography.caption.copy(
            color = LocalContentColor.current.copy(ContentAlpha.medium), fontSize = 13.sp
        )
    ) {
        Column(modifier = modifier.padding(start = 8.dp)) {
            Text(
                text = stringResource(id = infoTitle), fontWeight = FontWeight.Bold
            )
            SimpleHeightSpacer(dp = 2)
            Text(text = stringResource(id = info))
        }
    }
}


@Composable
private fun SubCategoryTitle(
    title: String, isExpanded: Boolean, onExpandIconClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 12.dp)
            .padding(vertical = 4.dp)
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f),
            text = title,
            style = MaterialTheme.typography.subtitle1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        ExpandIcon(
            isExpanded = isExpanded, onExpandIconClick = onExpandIconClick
        )
    }
}

@Composable
fun ExpandIcon(
    modifier: Modifier = Modifier, isExpanded: Boolean, onExpandIconClick: () -> Unit
) {
    val rotate by animateFloatAsState(
        targetValue = if (!isExpanded) 0f else 180f, animationSpec = tween(durationMillis = 300)
    )

    IconButton(onClick = onExpandIconClick) {
        SimpleIcon(
            drawable = R.drawable.ic_expand_more,
            modifier = modifier.rotate(rotate),
            tint = LocalContentColor.current.copy(ContentAlpha.medium)
        )
    }
}

fun getHeaderTextRes(subCategoryParent: SubCategoryParent): Int {
    return when (subCategoryParent) {
        SubCategoryParent.TOP -> R.string.top
        SubCategoryParent.BOTTOM -> R.string.bottom
        SubCategoryParent.OUTER -> R.string.outer
        SubCategoryParent.ETC -> R.string.etc
    }
}

class SubCategoryScreenUIState(parentName: String) {
    val subCategoryParent by mutableStateOf(enumValueOf<SubCategoryParent>(parentName))

    companion object {
        val Saver: Saver<SubCategoryScreenUIState, *> = listSaver(save = {
            listOf(it.subCategoryParent.name)
        }, restore = {
            SubCategoryScreenUIState(it[0])
        })
    }
}

@Composable
private fun rememberSubCategoryScreenUIState(parentName: String) =
    rememberSaveable(saver = SubCategoryScreenUIState.Saver) { SubCategoryScreenUIState(parentName) }