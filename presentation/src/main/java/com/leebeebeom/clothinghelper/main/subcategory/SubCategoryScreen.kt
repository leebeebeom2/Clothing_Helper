package com.leebeebeom.clothinghelper.main.subcategory

import androidx.activity.compose.BackHandler
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent

/*
* TODO
*  네비게이션 애니메이션, 다이얼로그 애니메이션, 삭제 다이얼로그
*  */

@Composable
fun SubCategoryScreen(
    parentName: String,
    viewModel: SubCategoryViewModel = hiltViewModel(),
    getIsSubCategoriesLoading: (SubCategoryParent) -> Boolean
) {
    val viewModelState = viewModel.getViewModelState(parentName)

    Scaffold(bottomBar = {
        SubCategoryBottomAppBar(
            isSelectMode = viewModelState.selectMode,
            selectedSubCategoriesSize = viewModelState.selectedSubCategories.size,
            onAllSelectCheckBoxClick = viewModelState::toggleAllSelect,
            isAllSelected = viewModelState.isAllSelected
        )
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (getIsSubCategoriesLoading(viewModelState.subCategoryParent))
                CircularProgressIndicator( // TODO 닷 프로그레스 교체(사인인, 사인업, 구글 사인인, 리셋 패스워드 전부 적용)
                    modifier = Modifier.align(Alignment.Center),
                    color = LocalContentColor.current.copy(ContentAlpha.medium)
                )
            // TODO 이름 수정, 삭제
            else SubCategoryContent(
                subCategoryParent = viewModelState.subCategoryParent,
                allExpandIconClick = viewModel::toggleAllExpand,
                allExpand = viewModelState.allExpand,
                subCategories = viewModelState.getSubCategories(),
                onLongClick = { viewModelState.selectModeOn() },
                isSelectMode = viewModelState.selectMode,
                onSelect = viewModelState::onSelect,
                selectedSubCategories = viewModelState.selectedSubCategories
            )

            AddCategoryDialogFab(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 16.dp),
                onPositiveButtonClick = viewModel::addSubCategory,
                subCategories = viewModelState.getSubCategories()
            )
        }
    }

    BackHandler(enabled = viewModelState.selectMode) { viewModelState.selectModeOff() }
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun CircleCheckBox(modifier: Modifier = Modifier, isChecked: Boolean) {
    Icon(
        modifier = modifier,
        painter = rememberAnimatedVectorPainter(
            animatedImageVector = AnimatedImageVector.animatedVectorResource(
                id = R.drawable.check_anim
            ), atEnd = isChecked
        ),
        contentDescription = null,
        tint = LocalContentColor.current.copy(0.7f)
    )
}