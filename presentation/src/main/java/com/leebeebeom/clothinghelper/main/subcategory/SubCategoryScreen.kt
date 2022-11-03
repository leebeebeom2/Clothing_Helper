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
    stateHolder: SubCategoryStateHolder = rememberSubCategoryStateHolder(),
    drawerCloseBackHandler: @Composable () -> Unit,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val subCategoriesState by remember {
        derivedStateOf {
            uiState.allSubCategories[parent.ordinal]
        }
    }
    drawerCloseBackHandler()

    Scaffold(bottomBar = {
        val subCategoryBottomAppbarState by rememberSubCategoryBottomAppbarState(
            subCategoryStateHolder = stateHolder,
            subCategoriesSizeState = subCategoriesState.size
        )
        SubCategoryBottomAppBar(
            stateHolder = subCategoryBottomAppbarState,
            onAllSelectCheckBoxClick = { stateHolder.toggleAllSelect(subCategoriesState) },
            onEditSubCategoryNameClick = { stateHolder.showEditDialog() })
    }) { paddingValue ->
        if (uiState.isLoading) CenterDotProgressIndicator(backGroundColor = Color.Transparent)
        else {
            val subCategoryContentState by rememberSubCategoryContentState(
                parent = parent,
                uiState = uiState,
                subCategoriesState = subCategoriesState,
                SubCategoryStateHolder = stateHolder
            )
            SubCategoryContent(
                state = subCategoryContentState,
                paddingValues = paddingValue,
                allExpandIconClick = viewModel::toggleAllExpand,
                onLongClick = { subCategory ->
                    stateHolder.selectModeOn()
                    stateHolder.onSelect(subCategory)
                },
                onSubCategoryClick = if (!stateHolder.isSelectModeState) {
                    {/*TODO*/ }
                } else stateHolder::onSelect,
                onSortClick = viewModel::changeSort,
                onOrderClick = viewModel::changeOrder,
                onAddCategoryPositiveButtonClick = viewModel::addSubCategory
            )

            if (stateHolder.showEditDialogState)
                EditSubCategoryNameDialog(
                    getInitialName = stateHolder::getFirstSelectedSubCategoryName,
                    subCategoriesState = subCategoriesState
                ) { newName ->
                    viewModel.editSubCategoryName(
                        newName,
                        stateHolder.getFirstSelectedSubCategory()
                    )
                    stateHolder.dismissEditDialog()
                    coroutineScope.launch {
                        stateHolder.selectModeOff()
                    }
                }
        }

        BackHandler(enabled = stateHolder.isSelectModeState, onBack = {
            coroutineScope.launch { stateHolder.selectModeOff() }
        })
    }
}

@Suppress("UNCHECKED_CAST")
class SubCategoryStateHolder(
    initialIsSelectMode: Boolean = false,
    initialSelectedSubCategories: Set<SubCategory> = emptySet(),
    initialShowEditDialog: Boolean = false
) {
    var isSelectModeState by mutableStateOf(initialIsSelectMode)
        private set
    var selectedSubCategoriesState by mutableStateOf(initialSelectedSubCategories)
        private set
    var showEditDialogState by mutableStateOf(initialShowEditDialog)

    val selectedSubCategoriesSizeState get() = selectedSubCategoriesState.size

    fun onSelect(subCategory: SubCategory) {
        selectedSubCategoriesState =
            if (selectedSubCategoriesState.contains(subCategory))
                selectedSubCategoriesState.taskAndReturn { it.remove(subCategory) }
            else this.selectedSubCategoriesState.taskAndReturn { it.add(subCategory) }
    }

    fun toggleAllSelect(subCategories: List<SubCategory>) {
        selectedSubCategoriesState =
            if (selectedSubCategoriesState.size == subCategories.size) emptySet() else subCategories.toSet()
    }

    fun clearSelectedSubCategories() {
        selectedSubCategoriesState = selectedSubCategoriesState.taskAndReturn { it.clear() }
    }

    fun selectModeOn() {
        this.isSelectModeState = true
    }

    suspend fun selectModeOff() {
        this.isSelectModeState = false
        delay(Anime.BottomAppbar.duration.toLong())
        clearSelectedSubCategories()
    }

    fun showEditDialog() {
        this.showEditDialogState = true
    }

    fun dismissEditDialog() {
        this.showEditDialogState = false
    }

    fun getFirstSelectedSubCategory() = selectedSubCategoriesState.first()

    fun getFirstSelectedSubCategoryName() = getFirstSelectedSubCategory().name

    companion object {
        val Saver: Saver<SubCategoryStateHolder, *> = mapSaver(
            save = {
                mapOf(
                    IsSelectMode to it.isSelectModeState,
                    SelectedSubCategories to it.selectedSubCategoriesState.toList(),
                    ShowEditDialog to it.showEditDialogState
                )
            },
            restore = {
                SubCategoryStateHolder(
                    initialIsSelectMode = it.getOrElse(IsSelectMode) { false } as Boolean,
                    initialSelectedSubCategories = (it.getOrElse(SelectedSubCategories) { emptyList<SubCategory>() } as List<SubCategory>).toSet(),
                    initialShowEditDialog = it.getOrElse(ShowEditDialog) { false } as Boolean
                )
            }
        )

        private const val IsSelectMode = "isSelectMode"
        private const val SelectedSubCategories = "selectedSubCategories"
        private const val ShowEditDialog = "showEditDialog"
    }
}

@Composable
fun rememberSubCategoryStateHolder() =
    rememberSaveable(saver = SubCategoryStateHolder.Saver) { SubCategoryStateHolder() }