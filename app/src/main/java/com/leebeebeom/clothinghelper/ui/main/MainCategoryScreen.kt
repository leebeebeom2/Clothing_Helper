package com.leebeebeom.clothinghelper.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.FinishActivityOnBackPressed

@Composable
fun HomeScreen(onNavigateToSubCategory: () -> Unit) {
    MainCategoryScreenRoot {
        HomeScreenDivider()
        HomeScreenContent(onNavigateToSubCategory)
    }
    FinishActivityOnBackPressed()
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
private fun HomeScreenContent(onNavigateToSubCategory: () -> Unit) {
    Column {
        val weightModifier = Modifier
            .weight(1f)
            .fillMaxHeight()

        HomeScreenRow(weightModifier) {
            HomeScreenText(
                modifier = weightModifier, textId = R.string.top,
                onClick = onNavigateToSubCategory
            )
            HomeScreenText(modifier = weightModifier, textId = R.string.bottom) {}
        }
        HomeScreenRow(weightModifier) {
            HomeScreenText(modifier = weightModifier, textId = R.string.outer) {}
            HomeScreenText(modifier = weightModifier, textId = R.string.etc) {}
        }
    }
}

@Composable
fun HomeScreenRow(modifier: Modifier, content: @Composable RowScope.() -> Unit) =
    Row(modifier = modifier, content = content)

@Composable
fun HomeScreenText(modifier: Modifier, textId: Int, onClick: () -> Unit) =
    Box(modifier = modifier.clickable { onClick() }, contentAlignment = Alignment.Center) {
        Text(text = stringResource(id = textId), style = MaterialTheme.typography.h4)
    }

@Composable
private fun MainCategoryScreenRoot(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
        content = content
    )
}