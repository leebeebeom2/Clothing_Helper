package com.leebeebeom.clothinghelper.main.subcategory

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.SimpleIcon
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

    var isSelectMode by rememberSaveable { mutableStateOf(false) }

    Scaffold(bottomBar = { SubCategoryBottomAppBar(isSelectMode) }) {
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
    }

    BackHandler(enabled = isSelectMode) { isSelectMode = false }
}

@Composable
private fun SubCategoryBottomAppBar(isSelectMode: Boolean) {
    AnimatedVisibility(
        visible = isSelectMode,
        enter = expandVertically(expandFrom = Alignment.Top, animationSpec = tween(250)),
        exit = shrinkVertically(shrinkTowards = Alignment.Top, animationSpec = tween(250))
    ) {
        BottomAppBar {
            var isChecked by rememberSaveable { mutableStateOf(false) }
            IconButton(onClick = { isChecked = !isChecked }) {
                CircleCheckBox(isChecked = isChecked, modifier = Modifier.size(20.dp))
            }
            Text(text = "1개 선택됨", modifier = Modifier.offset((-8).dp, 1.dp)) //TODO
            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
                BottomAppBarIcon(
                    onClick = { /*TODO*/ },
                    drawable = R.drawable.ic_edit,
                    text = R.string.change_name
                )
                BottomAppBarIcon(
                    onClick = { /*TODO*/ },
                    drawable = R.drawable.ic_delete2,
                    text = R.string.delete
                )
            }
        }
    }
}

@Composable
fun BottomAppBarIcon(onClick: () -> Unit, @DrawableRes drawable: Int, @StringRes text: Int) {
    IconButton(onClick = onClick) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            SimpleIcon(drawable = drawable)
            Text(
                text = stringResource(id = text),
                style = MaterialTheme.typography.caption
            )
        }
    }
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