package com.leebeebeom.clothinghelper.ui.main.maincategory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.FinishActivityOnBackPressed

@Composable
fun MainCategoryScreen(onMainCategoryClick: () -> Unit) {
    MainCategoryScreenRoot {
        HomeScreenDivider()
        HomeScreenContent(onMainCategoryClick)
    }

    FinishActivityOnBackPressed()
}

@Composable
private fun MainCategoryScreenRoot(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
        content = content
    )
}

@Composable
fun HomeScreenDivider() {
    Divider(
        modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
    )
    Divider()
}

@Composable
private fun HomeScreenContent(onMainCategoryClick: () -> Unit) {
    Column {

        val weightModifier = Modifier
            .weight(1f)
            .fillMaxHeight()

        HomeScreenContentRow(weightModifier) {
            HomeScreenContent(
                modifier = weightModifier, textId = R.string.top,
                onMainCategoryClick = onMainCategoryClick
            )
            HomeScreenContent(
                modifier = weightModifier, textId = R.string.bottom,
                onMainCategoryClick = onMainCategoryClick
            )
        }
        HomeScreenContentRow(weightModifier) {
            HomeScreenContent(
                modifier = weightModifier, textId = R.string.outer,
                onMainCategoryClick = onMainCategoryClick
            )
            HomeScreenContent(
                modifier = weightModifier, textId = R.string.etc,
                onMainCategoryClick = onMainCategoryClick
            )
        }
    }
}

@Composable
fun HomeScreenContentRow(modifier: Modifier, content: @Composable RowScope.() -> Unit) =
    Row(modifier = modifier, content = content)

@Composable
fun HomeScreenContent(modifier: Modifier, textId: Int, onMainCategoryClick: () -> Unit) =
    Box(
        modifier = modifier
            .padding(1.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onMainCategoryClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(id = textId), style = MaterialTheme.typography.h4)
    }