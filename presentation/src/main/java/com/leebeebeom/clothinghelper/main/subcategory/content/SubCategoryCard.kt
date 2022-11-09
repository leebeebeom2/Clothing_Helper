package com.leebeebeom.clothinghelper.main.subcategory.content

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
// TODO Expand 트랜지션

@Composable
fun SubCategoryCard(
    subCategory: () -> SubCategory,
    isAllExpand: () -> Boolean,
    isChecked: () -> Boolean,
    selectModeTransition: Transition<Boolean>,
    onClick: () -> Unit
) {
    var isExpand by isExpandStateWithIsAllExpand(isAllExpand = isAllExpand)

    Card(elevation = 2.dp, shape = RoundedCornerShape(12.dp)) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onClick)
        ) {
            SubCategoryCardTitle(
                isExpanded = { isExpand },
                onExpandIconClick = { isExpand = !isExpand },
                onCheckBoxClick = onClick,
                subCategory = subCategory,
                selectModeTransition = selectModeTransition,
                isChecked = isChecked
            )
            SubCategoryInfo { isExpand }
        }
    }
}

@Composable
private fun SubCategoryCardTitle(
    subCategory: () -> SubCategory,
    selectModeTransition: Transition<Boolean>,
    isChecked: () -> Boolean,
    isExpanded: () -> Boolean,
    onExpandIconClick: () -> Unit,
    onCheckBoxClick: () -> Unit
) = Surface(elevation = 4.dp) {
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
                    selectModeTransition = selectModeTransition,
                    isChecked = isChecked,
                    onClick = onCheckBoxClick
                )
                val width by selectModeTransition.animateDp(label = "width") { if (it) 2.dp else 12.dp }
                Spacer(modifier = Modifier.width(width))
                Name(subCategory = subCategory)
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

@Composable
private fun TotalCount(isExpanded: () -> Boolean) =
    AnimatedVisibility(
        visible = !isExpanded(),
        enter = Anime.SubCategoryCard.fadeIn,
        exit = Anime.SubCategoryCard.fadeOut
    ) {
        Text( // TODO total count
            text = "(10)", style = MaterialTheme.typography.caption.copy(
                color = LocalContentColor.current.copy(ContentAlpha.medium)
            ), maxLines = 1, overflow = TextOverflow.Ellipsis
        )
    }

@Composable
private fun Name(subCategory: () -> SubCategory) =
    Text(
        modifier = Modifier.widthIn(max = 300.dp),
        text = subCategory().name,
        style = MaterialTheme.typography.subtitle1,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun TitleCircleCheckBox(
    selectModeTransition: Transition<Boolean>,
    isChecked: () -> Boolean,
    onClick: () -> Unit
) = selectModeTransition.AnimatedVisibility(
        visible = { it },
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

@Composable
private fun SubCategoryInfo(isExpand: () -> Boolean) {
    AnimatedVisibility(
        visible = isExpand(),
        enter = Anime.SubCategoryCard.expandIn,
        exit = Anime.SubCategoryCard.shrinkOut
    ) {

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