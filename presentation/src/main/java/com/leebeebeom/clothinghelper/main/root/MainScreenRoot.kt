package com.leebeebeom.clothinghelper.main.root

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leebeebeom.clothinghelper.base.SimpleToast
import com.leebeebeom.clothinghelper.theme.ClothingHelperTheme
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/*
제스쳐로 두로어 열리는 지 확인

헤더에 유저 이름, 이메일 표시 확인

최초 구동 시 로딩 확인

설정 아이콘 클릭 시 리플 확인, 세팅 스크린으로 이동 확인
드로어 아이템 클릭 시 드로어 닫히는 지 확인

에센셜 메뉴 클릭 시 이동 확인 (메인 메뉴 빼고 아직 미구현)

메인 카테고리 클릭 시 이동 확인
서브 카테고리 클릭 시 이동 확인(미구현)

메인 카테고리 익스팬드 아이콘 로테이션 및 동작 확인

드로어가 열려있을때 뒤로 가기 시 드로어 닫히는지 확인
 */

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun MainScreenRoot(
    onSettingIconClick: () -> Unit,
    onEssentialMenuClick: (essentialMenu: EssentialMenus) -> Unit,
    onMainCategoryClick: (SubCategoryParent) -> Unit,
    onSubCategoryClick: (key: String) -> Unit,
    viewModel: MainScreenRootViewModel = hiltViewModel(),
    state: MainScreenRootState = rememberMainScreenRootState(),
    content: @Composable (PaddingValues, backHandler: @Composable () -> Unit) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SimpleToast(text = uiState.toastText, shownToast = viewModel::toastShown)

    ClothingHelperTheme {
        Scaffold(scaffoldState = state.scaffoldState,
            drawerContent = {
                val drawerMainCategoryState by rememberDrawerContentsState(uiState)
                DrawerContents(
                    drawerContentsState = drawerMainCategoryState,
                    onEssentialMenuClick = {
                        onEssentialMenuClick(it)
                        state.onDrawerClose()
                    },
                    onMainCategoryClick = {
                        onMainCategoryClick(it)
                        state.onDrawerClose()
                    },
                    onSubCategoryClick = {
                        onSubCategoryClick(it)
                        state.onDrawerClose()
                    },
                    onSettingIconClick = {
                        onSettingIconClick()
                        state.onDrawerClose()
                    },
                    allExpandIconClick = viewModel::toggleAllExpand,
                )
            },
            drawerShape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp),
            drawerBackgroundColor = MaterialTheme.colors.primary,
            content = {
                content(it) {
                    DrawerCloseBackHandler(
                        isDrawerOpen = state.drawerState.isOpen,
                        onDrawerClose = state::onDrawerClose
                    )
                }
            })
        DrawerCloseBackHandler(
            isDrawerOpen = state.drawerState.isOpen,
            onDrawerClose = state::onDrawerClose
        )
    }
}

@Composable
fun DrawerCloseBackHandler(isDrawerOpen: Boolean, onDrawerClose: () -> Unit) {
    BackHandler(enabled = isDrawerOpen, onBack = onDrawerClose)
}

class MainScreenRootState(
    val scaffoldState: ScaffoldState,
    private val coroutineScope: CoroutineScope,
    val drawerState: DrawerState = scaffoldState.drawerState
) {
    fun onDrawerClose() {
        coroutineScope.launch { drawerState.close() }
    }
}

@Composable
fun rememberMainScreenRootState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember { MainScreenRootState(scaffoldState, coroutineScope) }