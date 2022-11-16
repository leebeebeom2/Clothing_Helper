package com.leebeebeom.clothinghelper.main.subcategory.content

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.*
import com.leebeebeom.clothinghelper.main.base.ExpandIcon
import com.leebeebeom.clothinghelper.map.StableSubCategory
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun SubCategoryCard(
    isExpanded: () -> Boolean,
    subCategory: () -> StableSubCategory,
    isAllExpanded: () -> Boolean,
    onClick: () -> Unit,
    selectedCategoryKeys: () -> ImmutableSet<String>,
    updateIsExpanded: (Boolean) -> Unit,
    toggleIsExpanded: () -> Unit,
    isSelectMode: () -> Boolean
) {
    Card(elevation = 2.dp, shape = RoundedCornerShape(12.dp)) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onClick)
        ) {
            SubCategoryCardTitle(
                subCategory = subCategory,
                isSelectMode = isSelectMode,
                isExpanded = isExpanded,
                isAllExpanded = isAllExpanded,
                updateIsExpand = updateIsExpanded,
                onExpandIconClick = toggleIsExpanded,
                onCheckBoxClick = onClick,
                selectedCategoryKeys = selectedCategoryKeys,
            )
            SubCategoryInfo(isExpanded)
        }
    }
}

@Composable
private fun SubCategoryCardTitle(
    subCategory: () -> StableSubCategory,
    isExpanded: () -> Boolean,
    isAllExpanded: () -> Boolean,
    updateIsExpand: (Boolean) -> Unit,
    onExpandIconClick: () -> Unit,
    onCheckBoxClick: () -> Unit,
    selectedCategoryKeys: () -> ImmutableSet<String>,
    isSelectMode: () -> Boolean
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
                    isSelectMode = isSelectMode,
                    subCategory = subCategory,
                    onClick = onCheckBoxClick,
                    selectedCategoryKeys = selectedCategoryKeys
                )
                AnimateSpacer(isSelectMode = isSelectMode)
                Name(name = { subCategory().name })
                SimpleWidthSpacer(dp = 4)
                TotalCount()
            }

            ExpandIcon(
                isExpanded = isExpanded,
                onClick = onExpandIconClick,
                modifier = Modifier.size(24.dp),
                isAllExpanded = isAllExpanded,
                updateIsExpand = updateIsExpand
            )
        }
    }
}

@Composable
private fun AnimateSpacer(isSelectMode: () -> Boolean) {
    val width by animateDpAsState(targetValue = if (isSelectMode()) 2.dp else 12.dp)

    Spacer(modifier = Modifier.width(width))
}

@Composable
private fun TotalCount() {
    SingleLineText( // TODO total count
        text = "(10)", style = MaterialTheme.typography.caption.copy(
            color = LocalContentColor.current.copy(ContentAlpha.medium)
        )
    )
}

@Composable
private fun Name(name: () -> String) {
    SingleLineText(
        modifier = Modifier.widthIn(max = 300.dp),
        text = name(),
        style = MaterialTheme.typography.subtitle1
    )
}

@Composable
private fun TitleCircleCheckBox(
    subCategory: () -> StableSubCategory,
    onClick: () -> Unit,
    selectedCategoryKeys: () -> ImmutableSet<String>,
    isSelectMode: () -> Boolean
) {
    val isChecked by remember { derivedStateOf { selectedCategoryKeys().contains(subCategory().key) } }

    AnimatedVisibility(
        visible = isSelectMode(),
        enter = Anime.CircleCheckBox.expandIn,
        exit = Anime.CircleCheckBox.shrinkOut
    ) {
        CircleCheckBox(
            isChecked = { isChecked },
            modifier = Modifier
                .padding(start = 4.dp)
                .size(18.dp),
            onClick = onClick
        )
    }
}

@Composable
private fun SubCategoryInfo(isExpanded: () -> Boolean) {
    AnimatedVisibility(
        visible = isExpanded(),
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