package com.leebeebeom.clothinghelper.main.subcategory.content

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.Anime
import com.leebeebeom.clothinghelper.base.CircleCheckBox
import com.leebeebeom.clothinghelper.base.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.base.SimpleWidthSpacer
import com.leebeebeom.clothinghelper.main.base.ExpandIcon
import com.leebeebeom.clothinghelper.main.base.isExpandStateWithIsAllExpand
import com.leebeebeom.clothinghelperdomain.model.SubCategory

// TODO 선택 모드 애니메이션 렉

@Composable
fun SubCategoryCard(
    state: SubCategoryCardState, onClick: () -> Unit
) {
    val isExpandState = isExpandStateWithIsAllExpand(isAllExpand = state.isAllExpand)

    Card(elevation = 2.dp, shape = RoundedCornerShape(12.dp)) {
        Column(
            modifier = Modifier.clip(RoundedCornerShape(12.dp))
        ) {
            val subCategoryCardTitleState =
                rememberSubCategoryCardTitleState(subCategoryCardState = state)
            SubCategoryCardTitle(
                state = subCategoryCardTitleState,
                isExpanded = isExpandState.value,
                onExpandIconClick = { isExpandState.value = !isExpandState.value },
                onCheckBoxClick = onClick
            )

            AnimatedVisibility(
                visible = isExpandState.value,
                enter = Anime.SubCategoryCard.expandIn,
                exit = Anime.SubCategoryCard.shrinkOut
            ) { SubCategoryInfo() }
        }
    }
}

@Composable
private fun SubCategoryCardTitle(
    state: SubCategoryCardTitleState,
    isExpanded: Boolean,
    onExpandIconClick: () -> Unit,
    onCheckBoxClick: () -> Unit
) {
    Surface(elevation = 4.dp) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp)
                .padding(end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically
            ) {
                TitleCircleCheckBox(
                    isSelectMode = state.isSelectMode,
                    isChecked = state.isChecked,
                    onClick = onCheckBoxClick
                )
                Spacer(
                    modifier = Modifier
                        .animateContentSize(tween(Anime.CircleCheckBox.duration))
                        .width(if (state.isSelectMode) 2.dp else 12.dp)
                        .background(Color.Transparent)
                )
                Title(name = state.title)
                SimpleWidthSpacer(dp = 4)
                TotalCount(isExpanded = isExpanded)
            }
            ExpandIcon(
                isExpanded = isExpanded,
                onClick = onExpandIconClick,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

data class SubCategoryCardTitleState(
    val title: String, val isSelectMode: Boolean, val isChecked: Boolean
)

@Composable
fun rememberSubCategoryCardTitleState(
    subCategoryCardState: SubCategoryCardState
) = remember(subCategoryCardState) {
    SubCategoryCardTitleState(
        title = subCategoryCardState.subCategory.name,
        isSelectMode = subCategoryCardState.isSelectMode,
        isChecked = subCategoryCardState.isChecked
    )
}

@Composable
private fun TotalCount(isExpanded: Boolean) {
    AnimatedVisibility(
        visible = !isExpanded,
        enter = Anime.SubCategoryCard.fadeIn,
        exit = Anime.SubCategoryCard.fadeOut
    ) {
        Text( // TODO total count
            text = "(10)", style = MaterialTheme.typography.caption.copy(
                color = LocalContentColor.current.copy(ContentAlpha.medium)
            ), maxLines = 1, overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun Title(name: String) {
    Text(
        modifier = Modifier.widthIn(max = 300.dp),
        text = name,
        style = MaterialTheme.typography.subtitle1,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun RowScope.TitleCircleCheckBox(
    isSelectMode: Boolean, isChecked: Boolean, onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = isSelectMode,
        enter = Anime.CircleCheckBox.expandIn,
        exit = Anime.CircleCheckBox.shrinkOut
    ) {
        CircleCheckBox(
            isChecked = isChecked,
            modifier = Modifier
                .padding(start = 4.dp)
                .size(18.dp),
            onClick = onClick
        )
    }
}

@Composable
private fun SubCategoryInfo() {
    Surface(color = MaterialTheme.colors.background) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            SubCategoryInfoText( // TODO
                infoTitle = R.string.average_size, info = R.string.top_info
            )

            SubCategoryInfoText(
                infoTitle = R.string.most_have_size, info = R.string.top_info
            )
        }
    }
}

@Composable
private fun RowScope.SubCategoryInfoText(@StringRes infoTitle: Int, @StringRes info: Int) {
    ProvideTextStyle(
        value = MaterialTheme.typography.caption.copy(
            color = LocalContentColor.current.copy(ContentAlpha.medium), fontSize = 13.sp
        )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(text = stringResource(id = infoTitle), fontWeight = FontWeight.Bold)
            SimpleHeightSpacer(dp = 2)
            Text(text = stringResource(id = info))
        }
    }
}

data class SubCategoryCardState(
    val subCategory: SubCategory,
    val isSelectMode: Boolean,
    val isAllExpand: Boolean,
    val isChecked: Boolean,
)

@Composable
fun rememberSubCategoryCardState(
    subCategory: SubCategory,
    subCategoryContentState: State<SubCategoryContentState>,
) = remember(subCategory) {
    derivedStateOf {
        SubCategoryCardState(
            subCategory = subCategory,
            isSelectMode = subCategoryContentState.value.isSelectMode,
            isAllExpand = subCategoryContentState.value.isAllExpand,
            isChecked = subCategoryContentState.value.selectedSubCategories.contains(subCategory)
        )
    }
}