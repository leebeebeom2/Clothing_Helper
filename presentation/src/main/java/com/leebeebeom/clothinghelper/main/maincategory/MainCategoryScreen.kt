package com.leebeebeom.clothinghelper.main.maincategory

import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.base.SimpleIcon
import com.leebeebeom.clothinghelper.main.base.getMainCategories
import com.leebeebeom.clothinghelper.theme.ClothingHelperTheme
import com.leebeebeom.clothinghelperdomain.model.BaseMenuIds
import com.leebeebeom.clothinghelperdomain.model.MainCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent

@Preview(showSystemUi = true, showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun MainCategoryPreview() {
    ClothingHelperTheme {
        MainCategoryScreen {}
    }
}

@Composable
fun MainCategoryScreen(
    viewModel: MainCategoryViewModel = hiltViewModel(),
    onMainCategoryClick: (Int) -> Unit
) {
    val state = rememberMainCategoryScreenUIState()
    val viewModelState = viewModel.viewModelState
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        val modifier =
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT)
                Modifier.weight(1f)
            else Modifier.heightIn(160.dp)

        for (mainCategory in state.mainCategories)
            MainCategoryContent(
                modifier,
                mainCategory,
                onMainCategoryClick,
                viewModelState::getSubCategoriesSize
            )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MainCategoryContent(
    modifier: Modifier,
    mainCategory: MainCategory,
    onMainContentClick: (Int) -> Unit,
    getSubCategoriesSize: (SubCategoryParent) -> Int
) {
    val shape = RoundedCornerShape(20.dp)

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = shape,
        elevation = 2.dp,
        onClick = { onMainContentClick(mainCategory.id) }
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

            Text(
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
    if (mainCategory.id != BaseMenuIds.ETC) SimpleHeightSpacer(dp = 16)
}

class MainCategoryScreenUIState {
    val mainCategories = getMainCategories()
}

@Composable
fun rememberMainCategoryScreenUIState() =
    remember {
        MainCategoryScreenUIState()
    }