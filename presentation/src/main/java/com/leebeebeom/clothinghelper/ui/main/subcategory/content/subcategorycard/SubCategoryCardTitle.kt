package com.leebeebeom.clothinghelper.ui.main.subcategory.content.subcategorycard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.composable.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.composable.SingleLineText
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.ui.main.composables.CircleCheckBox
import com.leebeebeom.clothinghelper.ui.main.composables.ExpandIcon
import com.leebeebeom.clothinghelper.util.Anime
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun SubCategoryCardTitle(
    subCategory: () -> StableSubCategory,
    isExpanded: () -> Boolean,
    onExpandIconClick: () -> Unit,
    onCheckBoxClick: () -> Unit,
    selectedCategoryKeys: () -> ImmutableSet<String>,
    isSelectMode: () -> Boolean,
    folders: () -> ImmutableList<StableFolder>
) {
    Surface(elevation = 4.dp) {
        Row(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp, end = 0.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CheckBox(
                isSelectMode = isSelectMode,
                subCategory = subCategory,
                onClick = onCheckBoxClick,
                selectedCategoryKeys = selectedCategoryKeys
            )
            AnimateSpacer(isSelectMode = isSelectMode)
            Column(modifier = Modifier.weight(1f)) {
                Name { subCategory().name }
                SimpleHeightSpacer(dp = 4)
                Count(folders)
            }
            ExpandIcon(
                modifier = Modifier.size(30.dp),
                isExpanded = isExpanded,
                onClick = onExpandIconClick
            )
        }
    }
}

@Composable
private fun CheckBox(
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
            modifier = Modifier.padding(start = 4.dp),
            onClick = onClick,
            size = 18.dp
        )
    }
}

@Composable
private fun AnimateSpacer(isSelectMode: () -> Boolean) {
    val width by animateDpAsState(targetValue = if (isSelectMode()) 4.dp else 12.dp)

    Spacer(modifier = Modifier.width(width))
}

@Composable
private fun Name(name: () -> String) {
    SingleLineText(
        text = name(),
        style = MaterialTheme.typography.subtitle1
    )
}

@Composable
private fun Count(folders: () -> ImmutableList<StableFolder>) {
    SingleLineText(
        text = stringResource(
            id = R.string.folders_charts, // TODO Chart 카운트
            formatArgs = arrayOf(folders().size, 0)
        ),
        style = MaterialTheme.typography.caption.copy(
            color = LocalContentColor.current.copy(ContentAlpha.medium)
        )
    )
}