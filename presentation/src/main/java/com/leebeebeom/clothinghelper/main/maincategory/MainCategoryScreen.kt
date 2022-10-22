package com.leebeebeom.clothinghelper.main.maincategory

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.base.SimpleIcon
import com.leebeebeom.clothinghelper.main.base.getMainCategories
import com.leebeebeom.clothinghelperdomain.model.MainCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent

@Composable
fun MainCategoryScreen(
    viewModel: MainCategoryViewModel = hiltViewModel(),
    onMainCategoryClick: (parentName: String) -> Unit,
    getIsSubCategoriesLoading: (SubCategoryParent) -> Boolean
) {
    val state = rememberMainCategoryScreenUIState()
    val viewModelState = viewModel.viewModelState
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        MainHeaderText(R.string.main_categories)
        SimpleHeightSpacer(dp = 4)
        Divider()
        SimpleHeightSpacer(dp = 8)

        val modifier =
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT)
                Modifier.weight(1f)
            else Modifier.heightIn(160.dp)

        for (mainCategory in state.mainCategories)
            key(mainCategory.type.name) {
                MainCategoryContent(
                    modifier = modifier,
                    mainCategory = mainCategory,
                    onMainContentClick = onMainCategoryClick,
                    getSubCategoriesSize = viewModelState::getSubCategoriesSize,
                    isSubCategoriesLoading = getIsSubCategoriesLoading(mainCategory.type)
                )
            }
    }
}

@Composable
private fun MainHeaderText(@StringRes text: Int) {
    Text(
        modifier = Modifier.padding(start = 4.dp),
        text = stringResource(id = text),
        style = MaterialTheme.typography.h4.copy(letterSpacing = 0.75.sp),
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        color = LocalContentColor.current.copy(0.8f)
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MainCategoryContent(
    modifier: Modifier,
    mainCategory: MainCategory,
    onMainContentClick: (parentName: String) -> Unit,
    getSubCategoriesSize: (SubCategoryParent) -> Int,
    isSubCategoriesLoading: Boolean
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = 2.dp,
        onClick = { onMainContentClick(mainCategory.type.name) }
    ) {
        Box(
            modifier = Modifier
                .padding(start = 16.dp, end = 8.dp)
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = stringResource(id = mainCategory.name),
                style = MaterialTheme.typography.h2,
                fontSize = 32.sp
            )

            SimpleIcon(
                modifier = Modifier.align(Alignment.CenterEnd),
                drawable = R.drawable.ic_navigate_next,
                tint = LocalContentColor.current.copy(ContentAlpha.medium)
            )

            if (isSubCategoriesLoading) LoadingIcon(Modifier.align(Alignment.BottomStart))
            else Text(
                text = stringResource(
                    id = R.string.categories,
                    formatArgs = arrayOf(getSubCategoriesSize(mainCategory.type))
                ),
                modifier = Modifier.align(Alignment.BottomStart),
                style = MaterialTheme.typography.caption.copy(
                    fontWeight = FontWeight.Bold,
                    color = LocalContentColor.current.copy(ContentAlpha.medium)
                )
            )

        }
    }
    if (mainCategory.type != SubCategoryParent.ETC) SimpleHeightSpacer(dp = 16)
}

@Composable
private fun LoadingIcon(modifier: Modifier) {
    CircularProgressIndicator(
        modifier = modifier.size(16.dp),
        strokeWidth = 1.dp,
        color = LocalContentColor.current.copy(ContentAlpha.medium)
    )
}

class MainCategoryScreenUIState {
    val mainCategories = getMainCategories()
}

@Composable
fun rememberMainCategoryScreenUIState() =
    remember {
        MainCategoryScreenUIState()
    }