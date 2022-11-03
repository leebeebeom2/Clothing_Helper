package com.leebeebeom.clothinghelper.main.maincategory

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.DotProgressIndicator
import com.leebeebeom.clothinghelper.base.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.base.SimpleIcon
import com.leebeebeom.clothinghelper.main.root.MainCategory
import com.leebeebeom.clothinghelper.main.root.getMainCategories
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent

/*
최초 구동 시 로딩 확인

카드 클릭 시 이동 확인

서브 카테고리 갯수 일치 확인

 */

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun MainCategoryScreen(
    viewModel: MainCategoryViewModel = hiltViewModel(),
    onMainCategoryClick: (SubCategoryParent) -> Unit,
    drawerCloseBackHandler: @Composable () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    drawerCloseBackHandler()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        val modifier =
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT)
                Modifier.weight(1f)
            else Modifier.heightIn(160.dp)

        val mainCategories = remember { getMainCategories() }
        for (mainCategory in mainCategories){
            val mainCategoryCardState by rememberMainCategoryCardStateHolder(
                mainCategory = mainCategory,
                mainCategoryUIState = uiState
            )
            key(mainCategory.type.name) {
                MainCategoryCard(
                    modifier = modifier,
                    stateHolder = mainCategoryCardState,
                    onMainContentClick = { onMainCategoryClick(mainCategory.type) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MainCategoryCard(
    modifier: Modifier,
    stateHolder: MainCategoryCardStateHolder,
    onMainContentClick: () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = 2.dp,
        onClick = onMainContentClick
    ) {
        Box(
            modifier = Modifier
                .padding(start = 16.dp, end = 8.dp)
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = stringResource(id = stateHolder.mainCategory.name),
                style = MaterialTheme.typography.h2,
                fontSize = 32.sp
            )

            SimpleIcon(
                modifier = Modifier.align(Alignment.CenterEnd),
                drawable = R.drawable.ic_navigate_next,
                tint = LocalContentColor.current.copy(ContentAlpha.medium)
            )
            if (stateHolder.isLoading)
                DotProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 4.dp, start = 4.dp),
                    dotSize = 4.dp
                )
            else Text(
                text = stringResource(
                    id = R.string.categories,
                    formatArgs = arrayOf(stateHolder.subCategoriesSize)
                ),
                modifier = Modifier.align(Alignment.BottomStart),
                style = MaterialTheme.typography.caption.copy(
                    fontWeight = FontWeight.Bold,
                    color = LocalContentColor.current.copy(ContentAlpha.medium)
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
    if (stateHolder.mainCategory.type != SubCategoryParent.ETC) SimpleHeightSpacer(dp = 16)
}

data class MainCategoryCardStateHolder(
    val mainCategory: MainCategory,
    val subCategoriesSize: Int,
    val isLoading: Boolean
)

@Composable
fun rememberMainCategoryCardStateHolder(
    mainCategory: MainCategory,
    mainCategoryUIState: MainCategoryUIState
) = remember {
    derivedStateOf {
        MainCategoryCardStateHolder(
            mainCategory = mainCategory,
            subCategoriesSize = mainCategoryUIState.allSubCategories[mainCategory.type.ordinal].size,
            isLoading = mainCategoryUIState.isLoading
        )
    }
}