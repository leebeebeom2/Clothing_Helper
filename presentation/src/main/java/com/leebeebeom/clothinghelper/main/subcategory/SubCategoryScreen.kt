package com.leebeebeom.clothinghelper.main.subcategory

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.base.CenterDotProgressIndicator
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent

/*
최초 구동 시 로딩 확인
타이틀 확인

올 익스팬드 아이콘 동작 확인
올 익스팬드 혹은 올 폴드 시 카드 리컴포즈돼도 유지되는 지 확인
화면 회전 시 유지 확인

소트 아이콘 동작 화인 (미구현)

익스팬드 아이콘 동작 확인
화면 밖으로 사라져도 이전 상태 유지되는지 확인

인포 텍스트 확인(미구현)

애드 카테고리 다이얼로그 동작 확인 TODO 슬라이드 인 애니메이션 TODO 컬러지정? 할까말까?
화면 방향 혹은 다크모드 변경 시 커서 맨 뒤에 있는지 확인
이미 존재하는 카테고리 명일시 "이미 존재하는 카테고리 입니다." 에러 표시
텍스트 변경 시 에러 숨김
카테고리 추가 시 올 익스팬드 상태에 따라 익스팬드 상태로 추가
카테고리 추가 시 정렬 지키는지 확인
트림 확인

카드 롱 클릭 시 선택모드 활성화 확인
체크 박스 애니메이션 확인
바텀 앱바 애니메이션 확인
하나 초과 혹은 미만 선택 시 이름 수정 애니메이션 확인
하나 미만 선택 시 삭제 애니메이션 확인
롱클릭 된 카드는 셀렉트 되어야 함 // TODO 롱클릭 시 진동
선택모드 종료 시 셀렉티드 카테고리 초기화 되어야 함 // TODO 선택모드 종료 후 애니메이션 사라지고 초기화

선택 갯수에 따라 바텀 앱바에 선택된 갯수 표시
전체 선택 시 바텀앱바 체크박스 체크
전체 선택에서 하나라도 선택해제시 체크박스 체크 해제
바텀 앱바 체크박스 클릭 시 전체선택 토글

이름 수정 다이얼로그 동작 확인
트림 확인
본래 이름일 시 에러 표시 x 확인 버튼만 disable
이름 변경 시 정렬 지키는 지 확인
 */


@Composable
fun SubCategoryScreen(
    subCategoryParent: SubCategoryParent,
    viewModel: SubCategoryViewModel = hiltViewModel(),
    viewModelState: SubCategoryViewModelState = viewModel.viewModelState,
    state: SubCategoryScreenState = rememberSubCategoryScreenUIState(),
    editNameDialogState: EditSubCategoryNameDialogState = rememberEditSubCategoryNameDialogState(),
    drawerCloseBackHandler: @Composable () -> Unit
) {
    drawerCloseBackHandler()


    Scaffold(bottomBar = {
        SubCategoryBottomAppBar(isSelectMode = state.isSelectMode,
            selectedSubCategoriesSize = viewModelState.selectedSubCategoriesSize,
            subCategoriesSize = viewModelState.getSubCategories(subCategoryParent).size,
            onAllSelectCheckBoxClick = { viewModelState.toggleAllSelect(subCategoryParent) },
            onEditSubCategoryNameClick = { editNameDialogState.showDialog(viewModelState.selectedSubCategories.first().name) })
    }) {
        if (viewModelState.isLoading) CenterDotProgressIndicator(backGroundColor = Color.Transparent)
        else Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            SubCategoryContent(mainCategoryName = subCategoryParent.name,
                allExpandIconClick = viewModel::toggleAllExpand,
                isAllExpand = viewModelState.isAllExpand,
                subCategories = viewModelState.getSubCategories(subCategoryParent),
                onLongClick = { subCategory ->
                    state.selectModeOn()
                    viewModelState.onSelect(subCategory)
                },
                isSelectMode = state.isSelectMode,
                onSubCategoryClick = if (!state.isSelectMode) {
                    {/*TODO*/ }
                } else viewModelState::onSelect,
                selectedSubCategories = viewModelState.selectedSubCategories,
                selectedSort = viewModelState.selectedSort,
                selectedOder = viewModelState.selectedOrder,
                onSortClick = viewModel::changeSort,
                onOrderClick = viewModel::changeOrder)

            AddCategoryDialogFab(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 16.dp),
                onPositiveButtonClick = viewModel::addSubCategory,
                subCategories = viewModelState.getSubCategories(subCategoryParent),
                subCategoryParent = subCategoryParent
            )

            EditSubCategoryNameDialog(
                state = editNameDialogState,
                subCategories = viewModelState.getSubCategories(subCategoryParent),
            ) { newName ->
                viewModel.editSubCategoryName(newName)
                state.selectModeOff(viewModelState::clearSelectedSubCategories)
            }
        }
    }

    BackHandler(enabled = state.isSelectMode, onBack = {
        state.selectModeOff(clearSelectedSubCategories = viewModelState::clearSelectedSubCategories)
    })
}

class SubCategoryScreenState(isSelectMode: Boolean = false) {
    var isSelectMode by mutableStateOf(isSelectMode)
        private set

    val selectModeOn = { this.isSelectMode = true }

    fun selectModeOff(clearSelectedSubCategories: () -> Unit) {
        this.isSelectMode = false
        clearSelectedSubCategories()
    }

    companion object {
        val Saver: Saver<SubCategoryScreenState, *> =
            listSaver(save = { listOf(it.isSelectMode) }, restore = {
                SubCategoryScreenState(it[0])
            })
    }
}

@Composable
fun rememberSubCategoryScreenUIState() =
    rememberSaveable(saver = SubCategoryScreenState.Saver) { SubCategoryScreenState() }