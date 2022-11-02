package com.leebeebeom.clothinghelper.main.subcategory

import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.*
import com.leebeebeom.clothinghelper.base.Anime.CircleCheckBox.checkBoxIn
import com.leebeebeom.clothinghelper.base.Anime.CircleCheckBox.checkBoxOut
import com.leebeebeom.clothinghelper.base.Anime.SubCategoryCard.cardExpandIn
import com.leebeebeom.clothinghelper.base.Anime.SubCategoryCard.cardShrinkOut
import com.leebeebeom.clothinghelper.main.base.AllExpandState
import com.leebeebeom.clothinghelper.main.base.ExpandIcon
import com.leebeebeom.clothinghelperdomain.model.SubCategory

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SubCategoryCard(
    subCategoryCardState: SubCategoryCardState,
    onLongClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(elevation = 2.dp, shape = RoundedCornerShape(12.dp)) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .combinedClickable(onClick = onClick, onLongClick = {
                    subCategoryCardState.performHaptic()
                    onLongClick()
                })
        ) {
            val subCategoryCardTitleState by rememberSubCategoryCardTitleState(subCategoryCardState = subCategoryCardState)
            SubCategoryCardTitle(
                subCategoryCardTitleState,
                onExpandIconClick = subCategoryCardState::isExpandToggle,
            )

            AnimatedVisibility(
                visible = subCategoryCardState.isExpand,
                enter = cardExpandIn,
                exit = cardShrinkOut
            ) { SubCategoryInfo() }
        }
    }
}

@Composable
private fun SubCategoryCardTitle(
    subCategoryCardTitleState: SubCategoryCardTitleState,
    onExpandIconClick: () -> Unit,
) {
    Surface(elevation = 4.dp) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp, bottom = 8.dp)
                .padding(end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically
            ) {
                CircleCheckBox(
                    isSelectMode = subCategoryCardTitleState.isSelectMode,
                    isChecked = subCategoryCardTitleState.isChecked
                )
                Title(
                    isSelectMode = subCategoryCardTitleState.isSelectMode,
                    name = subCategoryCardTitleState.title
                )
                SimpleWidthSpacer(dp = 4)
                TotalCount(isExpanded = subCategoryCardTitleState.isExpanded)
            }
            ExpandIcon(
                isExpanded = subCategoryCardTitleState.isExpanded,
                onExpandIconClick = onExpandIconClick,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

data class SubCategoryCardTitleState(
    val title: String, val isExpanded: Boolean, val isSelectMode: Boolean, val isChecked: Boolean
)

@Composable
fun rememberSubCategoryCardTitleState(
    subCategoryCardState: SubCategoryCardState
) = remember {
    derivedStateOf {
        SubCategoryCardTitleState(
            title = subCategoryCardState.subCategory.name,
            isExpanded = subCategoryCardState.isExpand,
            isSelectMode = subCategoryCardState.isSelectMode,
            isChecked = subCategoryCardState.isChecked
        )
    }
}

@Composable
private fun TotalCount(isExpanded: Boolean) {
    AnimatedVisibility(
        visible = !isExpanded, enter = fadeIn(tween(150)), exit = fadeOut(tween(150))
    ) {
        Text( // TODO total count
            text = "(10)",
            style = MaterialTheme.typography.caption.copy(
                color = LocalContentColor.current.copy(ContentAlpha.medium)
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun Title(isSelectMode: Boolean, name: String) {
    val padding by animateDpAsState(
        targetValue = if (isSelectMode) 4.dp else 14.dp,
        animationSpec = tween(Anime.CircleCheckBox.duration)
    )

    Text(
        modifier = Modifier
            .padding(start = padding)
            .widthIn(max = 300.dp),
        text = name,
        style = MaterialTheme.typography.subtitle1,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun RowScope.CircleCheckBox(
    isSelectMode: Boolean, isChecked: Boolean
) {
    AnimatedVisibility(
        visible = isSelectMode,
        enter = checkBoxIn,
        exit = checkBoxOut
    ) {
        CircleCheckBox(
            isChecked = isChecked,
            modifier = Modifier
                .padding(start = 6.dp)
                .size(18.dp),
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
            Text(text = stringResource(id = infoTitle), fontWeight = FontWeight.Bold)
            SimpleHeightSpacer(dp = 2)
            Text(text = stringResource(id = info))
        }
    }
}

data class SubCategoryCardState(
    val subCategory: SubCategory,
    val isSelectMode: Boolean,
    override val isAllExpand: Boolean,
    val isChecked: Boolean,
    val haptic: HapticFeedback,
    override val _isExpand: MutableState<Boolean>,
    override var rememberedIsAllExpand: Boolean
) : AllExpandState() {
    init {
        init()
    }

    fun performHaptic() = haptic.performHapticFeedback(HapticFeedbackType.LongPress)
}