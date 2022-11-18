package com.leebeebeom.clothinghelper.main.subcategory.content.subcategorycard

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.Anime
import com.leebeebeom.clothinghelper.base.composables.SimpleHeightSpacer

@Composable
fun SubCategoryInfo(isExpanded: () -> Boolean) {
    AnimatedVisibility(
        visible = isExpanded(),
        enter = Anime.SubCategoryCard.expandIn,
        exit = Anime.SubCategoryCard.shrinkOut
    ) {
        Surface(color = MaterialTheme.colors.background) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                SubCategoryInfoText( // TODO
                    infoTitle = R.string.average_size, info = R.string.top_info
                )

                SubCategoryInfoText(
                    infoTitle = R.string.most_have_size, info = R.string.top_info
                )
            }
        }
    }
}

@Composable
fun RowScope.SubCategoryInfoText(@StringRes infoTitle: Int, @StringRes info: Int) {
    ProvideTextStyle(
        value = MaterialTheme.typography.caption.copy(
            color = LocalContentColor.current.copy(ContentAlpha.medium), fontSize = 13.sp
        )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(text = stringResource(id = infoTitle), fontWeight = FontWeight.Bold)
            SimpleHeightSpacer(dp = 2)
            Text(text = stringResource(id = info))
        }
    }
}