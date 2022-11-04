package com.leebeebeom.clothinghelper.main.subcategory

import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leebeebeom.clothinghelper.base.Anime
import com.leebeebeom.clothinghelper.base.CenterDotProgressIndicator
import com.leebeebeom.clothinghelper.main.subcategory.content.SubCategoryBottomAppBar
import com.leebeebeom.clothinghelper.main.subcategory.content.SubCategoryContent
import com.leebeebeom.clothinghelper.main.subcategory.content.rememberSubCategoryBottomAppbarState
import com.leebeebeom.clothinghelper.main.subcategory.content.rememberSubCategoryContentState
import com.leebeebeom.clothinghelper.util.taskAndReturn
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
최초 구동 시 로딩 확인
타이틀 확인

올 익스팬드 아이콘 동작 확인
올 익스팬드 혹은 올 폴드 시 카드 리컴포즈돼도 유지되는 지 확인
화면 회전 시 유지 확인

소트 아이콘 동작 화인 (미구현)

익스팬드 아이콘 동작 확인
화면 밖으로 사라져도 이전 상태 유지되는지 확인

인포 텍스트 확인

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
롱클릭 된 카드는 셀렉트 되어야 함
선택모드 종료 시 셀렉티드 카테고리 초기화 되어야 함

선택 갯수에 따라 바텀 앱바에 선택된 갯수 표시
전체 선택 시 바텀앱바 체크박스 체크
전체 선택에서 하나라도 선택해제시 체크박스 체크 해제
바텀 앱바 체크박스 클릭 시 전체선택 토글

이름 수정 다이얼로그 동작 확인
트림 확인
본래 이름일 시 에러 표시 x 확인 버튼만 disable
이름 변경 시 정렬 지키는 지 확인
 */


@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SubCategoryScreen(
    parent: SubCategoryParent,
    viewModel: SubCategoryViewModel = hiltViewModel(),
    states: SubCategoryStates = rememberSubCategoryStates(parent),
    drawerCloseBackHandler: @Composable () -> Unit,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val subCategoriesState = remember {
        derivedStateOf {
            uiState.value.allSubCategories[parent.ordinal]
        }
    }
    drawerCloseBackHandler()

    Scaffold(bottomBar = {
        val subCategoryBottomAppbarState = rememberSubCategoryBottomAppbarState(
            subCategoryStates = states,
            subCategoriesSize = subCategoriesState.value.size
        )
        SubCategoryBottomAppBar(
            state = subCategoryBottomAppbarState,
            onAllSelectCheckBoxClick = {
                states.toggleAllSelect(subCategoriesState.value)
            },
            onEditSubCategoryNameClick = states::showEditDialog
        )
    }) { paddingValue ->
        if (uiState.value.isLoading) CenterDotProgressIndicator(backGround = Color.Transparent) // 플레이스 홀더
        else {
            val subCategoryContentState = rememberSubCategoryContentState(
                uiState = uiState,
                subCategoriesState = subCategoriesState,
                subCategoryStates = states,
                paddingValues = paddingValue
            )
            SubCategoryContent(
                state = subCategoryContentState,
                allExpandIconClick = viewModel::toggleAllExpand,
                onLongClick = { subCategory ->
                    states.selectModeOn()
                    states.onSelect(subCategory)
                },
                onSubCategoryClick = if (!states.isSelectMode) {
                    {/*TODO*/ }
                } else states::onSelect,
                onSortClick = viewModel::changeSort,
                onOrderClick = viewModel::changeOrder,
                onAddCategoryPositiveButtonClick = viewModel::addSubCategory
            )

            if (states.showEditDialog)
                EditSubCategoryNameDialog(
                    getSelectedSubCategory = states::getFirstSelectedSubCategory,
                    subCategoriesState = subCategoriesState,
                    onPositiveButtonClick = viewModel::editSubCategoryName,
                    onDismiss = states::dismissEditDialog
                )
        }

        BackHandler(enabled = states.isSelectMode, onBack = {
            coroutineScope.launch { states.selectModeOff() }
        })
    }
}

@Suppress("UNCHECKED_CAST")
class SubCategoryStates(
    val parent: SubCategoryParent,
    initialIsSelectMode: Boolean = false,
    initialSelectedSubCategories: Set<SubCategory> = emptySet(),
    initialShowEditDialog: Boolean = false
) {
    private val _isSelectModeState = mutableStateOf(initialIsSelectMode)
    val isSelectMode get() = _isSelectModeState.value

    private val _selectedSubCategoriesState = mutableStateOf(initialSelectedSubCategories)
    val selectedSubCategories get() = _selectedSubCategoriesState.value

    private val _showEditDialogState = mutableStateOf(initialShowEditDialog)
    val showEditDialog get() = _showEditDialogState.value

    val selectedSubCategoriesSize get() = _selectedSubCategoriesState.value.size

    fun onSelect(subCategory: SubCategory) {
        _selectedSubCategoriesState.value =
            if (_selectedSubCategoriesState.value.contains(subCategory))
                _selectedSubCategoriesState.value.taskAndReturn { it.remove(subCategory) }
            else this._selectedSubCategoriesState.value.taskAndReturn { it.add(subCategory) }
    }

    fun toggleAllSelect(subCategories: List<SubCategory>) {
        _selectedSubCategoriesState.value =
            if (_selectedSubCategoriesState.value.size == subCategories.size) emptySet() else subCategories.toSet()
    }

    private fun clearSelectedSubCategories() {
        _selectedSubCategoriesState.value =
            _selectedSubCategoriesState.value.taskAndReturn { it.clear() }
    }

    fun selectModeOn() {
        _isSelectModeState.value = true
    }

    suspend fun selectModeOff() {
        _isSelectModeState.value = false
        delay(Anime.BottomAppbar.duration.toLong())
        clearSelectedSubCategories()
    }

    fun showEditDialog() {
        _showEditDialogState.value = true
    }

    fun dismissEditDialog() {
        _showEditDialogState.value = false
    }

    fun getFirstSelectedSubCategory() = _selectedSubCategoriesState.value.first()

    companion object {
        val Saver: Saver<SubCategoryStates, *> = mapSaver(
            save = {
                mapOf(
                    Parent to it.parent,
                    IsSelectMode to it._isSelectModeState,
                    SelectedSubCategories to it._selectedSubCategoriesState.value.toList(),
                    ShowEditDialog to it._showEditDialogState,
                )
            },
            restore = {
                SubCategoryStates(
                    parent = it.getOrElse(Parent) { SubCategoryParent.TOP } as SubCategoryParent,
                    initialIsSelectMode = it.getOrElse(IsSelectMode) { false } as Boolean,
                    initialSelectedSubCategories = (it.getOrElse(SelectedSubCategories) { emptyList<SubCategory>() } as List<SubCategory>).toSet(),
                    initialShowEditDialog = it.getOrElse(ShowEditDialog) { false } as Boolean,
                )
            }
        )

        private const val Parent = "parent"
        private const val IsSelectMode = "isSelectMode"
        private const val SelectedSubCategories = "selectedSubCategories"
        private const val ShowEditDialog = "showEditDialog"
    }
}

@Composable
fun rememberSubCategoryStates(parent: SubCategoryParent) =
    rememberSaveable(saver = SubCategoryStates.Saver) { SubCategoryStates(parent) }