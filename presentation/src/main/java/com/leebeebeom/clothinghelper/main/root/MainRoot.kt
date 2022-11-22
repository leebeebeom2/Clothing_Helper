package com.leebeebeom.clothinghelper.main.root

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.base.composables.BackHandler
import com.leebeebeom.clothinghelper.base.composables.BlockBacKPressWhenLoading
import com.leebeebeom.clothinghelper.base.composables.SimpleToast
import com.leebeebeom.clothinghelper.main.root.contents.DrawerContents
import com.leebeebeom.clothinghelper.main.root.model.EssentialMenuType
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.theme.ClothingHelperTheme
import com.leebeebeom.clothinghelperdomain.model.container.SubCategoryParent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/*
제스쳐로 드로어 열리는 지 확인

헤더에 유저 이름, 이메일 표시 확인

최초 구동 시 로딩 확인

설정 아이콘 클릭 시 리플 확인, 세팅 스크린으로 이동 확인
드로어 아이템 클릭 시 드로어 닫히는 지 확인

에센셜 메뉴 클릭 시 이동 확인 (메인 메뉴 빼고 아직 미구현)

메인 카테고리 클릭 시 이동 확인
서브 카테고리 클릭 시 이동 확인(미구현)

메인 카테고리 익스팬드 아이콘 로테이션 및 동작 확인

드로어가 열려있을때 뒤로 가기 시 드로어 닫히는지 확인

TODO 마지막 열림 상태 유지
 */

@Composable
fun MainRoot(
    onSettingIconClick: () -> Unit,
    onEssentialMenuClick: (type: EssentialMenuType) -> Unit,
    onMainCategoryClick: (SubCategoryParent) -> Unit,
    onSubCategoryClick: (StableSubCategory) -> Unit,
    viewModel: MainRootViewModel = hiltViewModel(),
    uiState: MainRootUIState = viewModel.uiState,
    state: MainRootState = rememberMainRootState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerClose = remember {
        {
            coroutineScope.launch { state.onDrawerClose() }
            Unit
        }
    }

    ClothingHelperTheme {
        Scaffold(
            scaffoldState = state.scaffoldState,
            drawerContent = {
                DrawerContents(
                    user = { uiState.user },
                    isLoading = { uiState.isLoading },
                    subCategories = uiState::getSubCategories,
                    onEssentialMenuClick = {
                        onEssentialMenuClick(it)
                        drawerClose()
                    },
                    onMainCategoryClick = {
                        onMainCategoryClick(it)
                        drawerClose()
                    },
                    onSubCategoryClick = {
                        onSubCategoryClick(it)
                        drawerClose()
                    },
                    onSettingIconClick = {
                        onSettingIconClick()
                        drawerClose()
                    },
                    onAddSubCategoryPositiveButtonClick = viewModel::addSubCategory,
                    onEditSUbCategoryNamePositiveClick = viewModel::editSubCategoryName
                )
            },
            drawerShape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp),
            drawerBackgroundColor = MaterialTheme.colors.primary,
        ) {
            content(it)
            BackHandler(enabled = { state.drawerState.isOpen }, task = drawerClose)
            BlockBacKPressWhenLoading { uiState.isLoading }
        }
        SimpleToast(text = { uiState.toastText }, toastShown = uiState::toastShown)
    }
}

data class MainRootState(
    val scaffoldState: ScaffoldState,
    val drawerState: DrawerState = scaffoldState.drawerState
) {
    suspend fun onDrawerClose() = drawerState.close()
}

@Composable
fun rememberMainRootState(scaffoldState: ScaffoldState = rememberScaffoldState()) =
    remember { MainRootState(scaffoldState = scaffoldState) }