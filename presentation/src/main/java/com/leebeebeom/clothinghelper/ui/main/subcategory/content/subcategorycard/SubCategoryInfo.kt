package com.leebeebeom.clothinghelper.ui.main.subcategory.content.subcategorycard

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.composable.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.util.Anime

@Composable
fun SubCategoryInfo(isExpanded: () -> Boolean) {
    AnimatedVisibility(
        visible = isExpanded(),
        enter = Anime.SubCategoryCardInfo.expandIn,
        exit = Anime.SubCategoryCardInfo.shrinkOut
    ) {
        Surface(color = MaterialTheme.colors.background) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .padding(horizontal = 16.dp)
            ) {
                SubCategoryInfoText( // TODO
                    infoTitle = R.string.average_size, info = R.string.info
                )
                SimpleHeightSpacer(dp = 4)
                SubCategoryInfoText(
                    infoTitle = R.string.minimum_size, info = R.string.info
                )
                SimpleHeightSpacer(dp = 4)
                SubCategoryInfoText(
                    infoTitle = R.string.maximum_size, info = R.string.info
                )
            }
        }
    }
}

@Composable
fun SubCategoryInfoText(@StringRes infoTitle: Int, @StringRes info: Int) {
    ProvideTextStyle(
        value = MaterialTheme.typography.caption.copy(
            color = LocalContentColor.current.copy(ContentAlpha.medium), fontSize = 13.sp
        )
    ) {
        Column {
            Text(text = stringResource(id = infoTitle), fontWeight = FontWeight.Bold)
            Text(text = stringResource(id = info))
        }
    }
}