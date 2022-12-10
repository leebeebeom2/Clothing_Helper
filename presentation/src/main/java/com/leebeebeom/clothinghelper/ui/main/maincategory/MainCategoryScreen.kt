package com.leebeebeom.clothinghelper.ui.main.maincategory

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.composable.DotProgressIndicator
import com.leebeebeom.clothinghelper.composable.SimpleIcon
import com.leebeebeom.clothinghelper.composable.SingleLineText
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.ui.main.composables.MaxWidthCard
import com.leebeebeom.clothinghelper.ui.main.root.model.MainCategory
import com.leebeebeom.clothinghelper.ui.main.root.model.getMainCategories
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent
import kotlinx.collections.immutable.ImmutableList

/*
최초 구동 시 로딩 확인

카드 클릭 시 이동 확인

서브 카테고리 갯수 일치 확인
 */

@Composable
fun MainCategoryScreen(
    viewModel: MainCategoryViewModel = hiltViewModel(),
    uiState: MainCategoryUIState = viewModel.uiState,
    onMainCategoryClick: (SubCategoryParent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val modifier =
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) Modifier.weight(
                1f
            )
            else Modifier.heightIn(160.dp)

        val mainCategories = remember { getMainCategories() }
        mainCategories.forEach {
            key(it.type.name) {
                MainCategoryCard(
                    modifier = modifier,
                    mainCategory = it,
                    subCategories = { uiState.getSubCategories(it.type.name) },
                    isLoading = { uiState.isLoading },
                    onMainContentClick = onMainCategoryClick
                )
            }
        }
    }
}

@Composable
private fun MainCategoryCard(
    modifier: Modifier,
    mainCategory: MainCategory,
    subCategories: () -> ImmutableList<StableSubCategory>,
    isLoading: () -> Boolean,
    onMainContentClick: (SubCategoryParent) -> Unit,
) {
    MaxWidthCard(modifier = modifier, onClick = { onMainContentClick(mainCategory.type) }) {
        Box(
            modifier = Modifier.padding(start = 16.dp, end = 8.dp, top = 10.dp, bottom = 12.dp)
        ) {
            SingleLineText(
                text = mainCategory.name, style = MaterialTheme.typography.h2.copy(fontSize = 32.sp)
            )

            SimpleIcon(
                modifier = Modifier.align(Alignment.CenterEnd),
                drawable = R.drawable.ic_navigate_next,
                tint = LocalContentColor.current.copy(ContentAlpha.medium)
            )

            Count(
                isLoading = isLoading, subCategories = subCategories
            )
        }
    }
}

@Composable
private fun BoxScope.Count(
    isLoading: () -> Boolean, subCategories: () -> ImmutableList<StableSubCategory>
) {
    if (isLoading()) DotProgressIndicator(
        modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(bottom = 4.dp, start = 4.dp),
        size = 4.dp
    )
    else SingleLineText(
        text = stringResource(
            id = R.string.categories, formatArgs = arrayOf(subCategories().size)
        ),
        modifier = Modifier.align(Alignment.BottomStart),
        style = MaterialTheme.typography.caption.copy(
            fontWeight = FontWeight.Bold,
            color = LocalContentColor.current.copy(ContentAlpha.medium)
        )
    )
}