package com.leebeebeom.clothinghelper.main.subcategory

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent

@Composable
fun SubCategoryScreen(
    parentName: String,
    viewModel: SubCategoryViewModel = hiltViewModel(),
    getIsSubCategoriesLoading: (SubCategoryParent) -> Boolean
) {
    val viewModelState = viewModel.getViewModelState(parentName)

    //TODO 테스트
    var isSelectMode by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (getIsSubCategoriesLoading(viewModelState.subCategoryParent))
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = LocalContentColor.current.copy(ContentAlpha.medium)
            )
        // TODO 롱 클릭 시 바텀 앱바 show, 이름 수정, 삭제
        else SubCategoryContent(
            subCategoryParent = viewModelState.subCategoryParent,
            allExpandIconClick = viewModel::toggleAllExpand,
            allExpand = viewModelState.allExpand,
            subCategories = viewModelState.getSubCategories(),
            getExpandState = viewModelState::getExpandState,
            toggleExpand = viewModelState::expandToggle,
            onLongClick = { isSelectMode = true },
            isSelectMode = isSelectMode
        )

        AddCategoryDialogFab(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp),
            onPositiveButtonClick = viewModel::addSubCategory,
            subCategories = viewModelState.getSubCategories()
        )
    }

    BackHandler(enabled = isSelectMode) { isSelectMode = false }
}