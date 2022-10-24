package com.leebeebeom.clothinghelper.main.subcategory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
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
    val viewModelState = viewModel.viewModelState
    val state = rememberSubCategoryScreenUIState(parentName = parentName)

    Box(modifier = Modifier.fillMaxSize()) {
        if (getIsSubCategoriesLoading(state.subCategoryParent)) CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = LocalContentColor.current.copy(ContentAlpha.medium)
        )
        // TODO 이름 수정
        else SubCategoryContent(
            subCategoryParent = state.subCategoryParent,
            toggleAllExpand = viewModel::toggleAllExpand,
            allExpand = viewModelState.allExpand,
            subCategories = viewModelState.getSubCategories(state.subCategoryParent),
            getExpandState = viewModelState::getExpandState,
            toggleExpand = viewModelState::expandToggle,
            deleteSubCategory = viewModel::deleteSubCategory
        )

        AddCategoryDialogFab(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp),
            onPositiveButtonClick = { viewModel.addSubCategory(state.subCategoryParent, it) },
            subCategories = viewModelState.getSubCategories(state.subCategoryParent)
        )
    }
}

class SubCategoryScreenUIState(parentName: String) { // TODO 뷰모델 스테이트로 결합
    val subCategoryParent by mutableStateOf(enumValueOf<SubCategoryParent>(parentName))

    companion object {
        val Saver: Saver<SubCategoryScreenUIState, *> = listSaver(save = {
            listOf(it.subCategoryParent.name)
        }, restore = {
            SubCategoryScreenUIState(it[0])
        })
    }
}

@Composable
private fun rememberSubCategoryScreenUIState(parentName: String) =
    rememberSaveable(saver = SubCategoryScreenUIState.Saver) { SubCategoryScreenUIState(parentName) }