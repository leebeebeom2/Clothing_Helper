package com.leebeebeom.clothinghelper.main.maincategory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.composables.DotProgressIndicator
import com.leebeebeom.clothinghelper.base.composables.SimpleIcon
import com.leebeebeom.clothinghelper.base.composables.SingleLineText
import com.leebeebeom.clothinghelper.main.base.composables.MaxWidthCard
import com.leebeebeom.clothinghelper.main.base.composables.titleStyle
import com.leebeebeom.clothinghelper.main.root.model.MainCategory
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent
import kotlinx.collections.immutable.ImmutableList

@Composable
fun MainCategoryCard(
    modifier: Modifier,
    mainCategory: MainCategory,
    subCategories: (SubCategoryParent) -> ImmutableList<StableSubCategory>,
    isLoading: () -> Boolean,
    onMainContentClick: (SubCategoryParent) -> Unit,
) {
    MaxWidthCard(modifier = modifier, onClick = { onMainContentClick(mainCategory.type) }) {
        Box(
            modifier = Modifier.padding(start = 16.dp, end = 8.dp, top = 10.dp, bottom = 12.dp)
        ) {
            SingleLineText(
                text = mainCategory.name,
                style = titleStyle()
            )

            SimpleIcon(
                modifier = Modifier.align(Alignment.CenterEnd),
                drawable = R.drawable.ic_navigate_next,
                tint = LocalContentColor.current.copy(ContentAlpha.medium)
            )

            SubCategoryCountText(
                isLoading = isLoading,
                subCategories = { subCategories(mainCategory.type) }
            )
        }
    }
}

@Composable
private fun BoxScope.SubCategoryCountText(
    isLoading: () -> Boolean,
    subCategories: () -> ImmutableList<StableSubCategory>
) {
    if (isLoading())
        DotProgressIndicator(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 4.dp, start = 4.dp),
            size = 4.dp
        )
    else SingleLineText(
        text = stringResource(
            id = R.string.categories,
            formatArgs = arrayOf(subCategories().size)
        ),
        modifier = Modifier.align(Alignment.BottomStart),
        style = MaterialTheme.typography.caption.copy(
            fontWeight = FontWeight.Bold,
            color = LocalContentColor.current.copy(ContentAlpha.medium)
        )
    )
}