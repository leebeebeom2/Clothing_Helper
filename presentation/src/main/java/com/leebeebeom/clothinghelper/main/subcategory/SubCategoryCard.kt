package com.leebeebeom.clothinghelper.main.subcategory

import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.*
import com.leebeebeom.clothinghelperdomain.model.SubCategory

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SubCategoryCard(
    subCategory: SubCategory,
    onLongClick: () -> Unit,
    isSelectMode: Boolean,
    onSubCategoryClick: () -> Unit,
    isAllExpand: Boolean,
    isChecked: Boolean
) {
    var isExpanded by rememberSaveable { mutableStateOf(isAllExpand) }
    var rememberedAllExpand by rememberSaveable { mutableStateOf(isAllExpand) }
    if (rememberedAllExpand != isAllExpand) {
        isExpanded = isAllExpand
        rememberedAllExpand = isAllExpand
    }

    Card(elevation = 2.dp, shape = RoundedCornerShape(12.dp)) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .combinedClickable(
                    onClick = onSubCategoryClick, onLongClick = onLongClick
                )
        ) {
            SubCategoryTitle(
                name = subCategory.name,
                isExpanded = isExpanded,
                onExpandIconClick = { isExpanded = !isExpanded },
                isSelectMode = isSelectMode,
                isChecked = isChecked
            )
            SubCategoryInfo(isExpanded)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SubCategoryTitle(
    name: String,
    isExpanded: Boolean,
    onExpandIconClick: () -> Unit,
    isSelectMode: Boolean,
    isChecked: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 4.dp)
            .padding(end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val duration = 300
            AnimatedVisibility(
                visible = isSelectMode,
                enter = expandHorizontally(tween(duration)) { 0 } + scaleIn(tween(duration)),
                exit = shrinkHorizontally(tween(duration)) { 0 } + scaleOut(tween(duration))
            ) {
                CircleCheckBox(
                    isChecked = isChecked,
                    modifier = Modifier.padding(start = 6.dp).size(18.dp),
                )
            }

            val padding by animateDpAsState(
                targetValue = if (isSelectMode) 4.dp else 12.dp,
                animationSpec = tween(duration)
            )
            Text(
                modifier = Modifier.padding(start = padding),
                text = name,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        ExpandIcon(
            isExpanded = isExpanded,
            onExpandIconClick = onExpandIconClick,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun ExpandIcon(modifier: Modifier = Modifier, isExpanded: Boolean, onExpandIconClick: () -> Unit) {
    val rotate by animateFloatAsState(
        targetValue = if (!isExpanded) 0f else 180f, animationSpec = tween(durationMillis = 300)
    )

    CustomIconButton(
        modifier = modifier.rotate(rotate),
        onClick = onExpandIconClick,
        drawable = R.drawable.ic_expand_more,
        tint = LocalContentColor.current.copy(alpha = 0.6f)
    )
}

@Composable
private fun SubCategoryInfo(isExpanded: Boolean) {
    val duration = 250

    AnimatedVisibility(
        visible = isExpanded,
        enter = expandVertically(animationSpec = tween(durationMillis = duration)),
        exit = shrinkVertically(animationSpec = tween(durationMillis = duration))
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
            Text(text = stringResource(id = infoTitle), fontWeight = FontWeight.Bold)
            SimpleHeightSpacer(dp = 2)
            Text(text = stringResource(id = info))
        }
    }
}