package com.leebeebeom.clothinghelper.ui.main.maincategory

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.OnClick
import com.leebeebeom.clothinghelper.ui.FinishActivityOnBackPressed
import com.leebeebeom.clothinghelper.ui.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.ui.SimpleIcon
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme

@Preview(showSystemUi = true, showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun MainScreenPreview() {
    ClothingHelperTheme {
        MainCategoryScreen {}
    }
}

@Composable
fun MainCategoryScreen(
    viewModel: MainCategoryViewModel = viewModel(),
    onMainCategoryClick: () -> Unit
) {
    val state = viewModel.mainCategoryState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        for ((i, mainCategory) in state.mainCategories.withIndex())
            MainCategoryContent(Modifier.weight(1f), mainCategory, i, onMainCategoryClick)
    }

    FinishActivityOnBackPressed()
}

@Composable
private fun MainCategoryContent(
    modifier: Modifier,
    @StringRes mainCategory: Int,
    index: Int,
    onMainContentClick: OnClick
) {
    val shape = RoundedCornerShape(20.dp)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        elevation = 2.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape)
                .clickable(onClick = onMainContentClick)
                .padding(start = 16.dp, end = 8.dp)
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = stringResource(id = mainCategory),
                style = MaterialTheme.typography.h2,
                fontSize = 32.sp
            )

            SimpleIcon(
                modifier = Modifier.align(Alignment.CenterEnd),
                drawable = R.drawable.ic_navigate_next,
                tint = LocalContentColor.current.copy(ContentAlpha.medium)
            )

            Text(
                text = "10 Categories",
                modifier = Modifier.align(Alignment.BottomStart),
                style = MaterialTheme.typography.caption.copy(
                    fontWeight = FontWeight.Bold,
                    color = LocalContentColor.current.copy(ContentAlpha.medium)
                )
            )

        }
    }
    if (index != 3) SimpleHeightSpacer(dp = 16)
}