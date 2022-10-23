package com.leebeebeom.clothinghelper.main.subcategory

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
                    allExpandToggle = state::allExpandToggle,
                    isAllExpand = state.isAllExpand
                )
            }
            itemsIndexed(items = viewModelState.getSubCategories(state.subCategoryParent),
                key = { _, subCategory -> subCategory.id }) { index, subCategory ->
                SubCategoryContent(
                    subCategory = subCategory, state.getExpandState(index)
                ) { state.expandToggle(index) }
            }
        }

        AddCategoryDialogFab(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp),
            onPositiveButtonClick = {
                state.addExpandState()
                viewModel.addSubCategory(state.subCategoryParent, it)
            },
            subCategories = viewModelState.getSubCategories(state.subCategoryParent)
        )
    }
}

@Composable
private fun Header(
    subCategoryParent: SubCategoryParent, allExpandToggle: () -> Unit, isAllExpand: Boolean
) {
    SubCategoryHeaderText(getHeaderTextRes(subCategoryParent))
    Row(verticalAlignment = Alignment.CenterVertically) {
        Divider(modifier = Modifier.weight(1f))
        AllExpandIcon(onClick = allExpandToggle, isAllExpand)
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
fun AllExpandIcon(onClick: () -> Unit, isAllExpand: Boolean) {
    IconButton(
        onClick = onClick, modifier = Modifier.size(22.dp)
    ) {
        SimpleIcon(
            drawable = if (isAllExpand) R.drawable.ic_unfold_less else R.drawable.ic_all_expand,
            tint = LocalContentColor.current.copy(0.5f)
        )
    }
}

@Composable
fun SubCategoryHeaderText(headerTextRes: Int) {
    Text(
        modifier = Modifier.padding(4.dp),
        text = stringResource(id = headerTextRes),
        style = MaterialTheme.typography.h2,
        fontSize = 32.sp
    )
}

@Composable
private fun SubCategoryContent(
    subCategory: SubCategory, isExpanded: Boolean, expandToggle: () -> Unit
) {
    Card(elevation = 2.dp, shape = RoundedCornerShape(12.dp)) {
        Column {
            SubCategoryTitle(subCategory.name, isExpanded, onExpandIconClick = expandToggle)
            SubCategoryInfo(isExpanded)
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
    Row(modifier = Modifier
        .fillMaxSize()
        .clickable { /*TODO*/ }
        .padding(start = 12.dp)
        .padding(vertical = 4.dp)) {
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

class SubCategoryScreenUIState(
    parentName: String, isAllExpand: Boolean = false, vararg expands: Boolean = BooleanArray(0)
) {
    val subCategoryParent by mutableStateOf(enumValueOf<SubCategoryParent>(parentName))
    val expandStates = mutableListOf<MutableState<Boolean>>().apply {
        addAll(expands.map { mutableStateOf(it) })
    }
    var isAllExpand by mutableStateOf(isAllExpand) // TODO 설정으로
        private set


    fun allExpandToggle() {
        isAllExpand = !isAllExpand
        for (isExpand in expandStates) isExpand.value = isAllExpand
    }

    fun getExpandState(index: Int): Boolean {
        if (expandStates.getOrNull(index) == null) expandStates.add(mutableStateOf(isAllExpand))
        return expandStates[index].value
    }

    fun expandToggle(index: Int) {
        expandStates[index].value = !expandStates[index].value
    }

    fun addExpandState() = expandStates.add(mutableStateOf(isAllExpand))

    companion object {
        val Saver: Saver<SubCategoryScreenUIState, *> = listSaver(save = {
            listOf(it.subCategoryParent.name,
                it.isAllExpand,
                it.expandStates.map { state -> state.value })
        }, restore = {
            val expands = it[2] as List<*>
            val booleanArray = BooleanArray(expands.size)
            for ((index, expand) in expands.withIndex()) {
                booleanArray[index] = expand as Boolean
            }
            SubCategoryScreenUIState(it[0] as String, it[1] as Boolean, *booleanArray)
        })
    }
}

@Composable
private fun rememberSubCategoryScreenUIState(parentName: String) =
    rememberSaveable(saver = SubCategoryScreenUIState.Saver) {
        SubCategoryScreenUIState(parentName)
    }