package com.leebeebeom.clothinghelper.main.maincategory

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.main.base.BaseMainUIState
import com.leebeebeom.clothinghelper.main.root.model.getMainCategories
import com.leebeebeom.clothinghelperdomain.model.container.SubCategoryParent

/*
최초 구동 시 로딩 확인

카드 클릭 시 이동 확인

서브 카테고리 갯수 일치 확인

TODO 스와이프 투 리프레쉬
 */

@Composable
fun MainCategoryScreen(
    viewModel: MainCategoryViewModel = hiltViewModel(),
    uiState: BaseMainUIState = viewModel.uiState,
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
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT)
                Modifier.weight(1f)
            else Modifier.heightIn(160.dp)

        val mainCategories = remember { getMainCategories() }
        mainCategories.forEach {
            key(it.type.name) {
                MainCategoryCard(
                    modifier = modifier,
                    mainCategory = it,
                    subCategoriesSize = uiState::getSubCategoriesSize,
                    isLoading = { uiState.isLoading },
                    onMainContentClick = onMainCategoryClick
                )
            }
        }
    }
}


