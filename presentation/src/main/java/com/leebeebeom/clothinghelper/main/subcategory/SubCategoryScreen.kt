package com.leebeebeom.clothinghelper.main.subcategory

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.base.SimpleIcon
import com.leebeebeom.clothinghelper.theme.ClothingHelperTheme
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SubCategoryScreenPreview() {
    ClothingHelperTheme {
        SubCategoryScreen("")
    }
}

@Composable
fun SubCategoryScreen(
    parentName: String,
    viewModel: SubCategoryViewModel = hiltViewModel()
) {
    val subCategoryParent = enumValueOf<SubCategoryParent>(parentName)
    val viewModelState = viewModel.viewModelState

    Box(modifier = Modifier.fillMaxSize()) {
        // TODO 헤어
        // TODO 올 익스팬드, 정렬, 삭제, 이름 수정
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            this.items(viewModelState.getSubCategories(subCategoryParent), key = { it.id }) {
                SubCategoryContent(it)
            }
        }

        AddCategoryDialogFab(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp),
            onPositiveButtonClick = { viewModel.addSubCategory(subCategoryParent, it) },
            subCategories = viewModelState.getSubCategories(subCategoryParent)
        )
    }
}

@Composable
private fun SubCategoryContent(subCategory: SubCategory) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Card(elevation = 2.dp, shape = RoundedCornerShape(12.dp)) {
        Column {
            SubCategoryTitle(subCategory.name, isExpanded) { isExpanded = !isExpanded }
            SubCategoryInfo(isExpanded)
        }
    }
}

@Composable
private fun SubCategoryInfo(isExpanded: Boolean) {
    AnimatedVisibility(
        visible = isExpanded,
        enter = expandVertically(animationSpec = tween(250)),
        exit = shrinkVertically(animationSpec = tween(250))
    ) {
        Surface(color = MaterialTheme.colors.background) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                val weightModifier = Modifier.weight(1f)

                SubCategoryInfoText( // TODO
                    modifier = weightModifier,
                    infoTitle = R.string.average_size,
                    info = R.string.top_info
                )

                SubCategoryInfoText(
                    modifier = weightModifier,
                    infoTitle = R.string.most_have_size,
                    info = R.string.top_info
                )
            }
        }
    }
}

@Composable
private fun SubCategoryInfoText(
    modifier: Modifier, @StringRes infoTitle: Int, @StringRes info: Int
) {
    ProvideTextStyle(
        value = MaterialTheme.typography.caption.copy(
            color = LocalContentColor.current.copy(ContentAlpha.medium), fontSize = 13.sp
        )
    ) {
        Column(modifier = modifier.padding(start = 8.dp)) {
            Text(
                text = stringResource(id = infoTitle), fontWeight = FontWeight.Bold
            )
            SimpleHeightSpacer(dp = 2)
            Text(text = stringResource(id = info))
        }
    }
}


@Composable
private fun SubCategoryTitle(title: String, isExpanded: Boolean, onExpandIconClick: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxSize()
        .clickable { /*TODO*/ }
        .padding(start = 12.dp)
        .padding(vertical = 4.dp)) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f),
            text = title,
            style = MaterialTheme.typography.subtitle1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        ExpandIcon(isExpanded = isExpanded, onExpandIconClick = onExpandIconClick)
    }
}

@Composable
fun ExpandIcon(modifier: Modifier = Modifier, isExpanded: Boolean, onExpandIconClick: () -> Unit) {
    val rotate by animateFloatAsState(
        targetValue = if (!isExpanded) 0f else 180f, animationSpec = tween(durationMillis = 300)
    )

    IconButton(onClick = onExpandIconClick) {
        SimpleIcon(
            drawable = R.drawable.ic_expand_more,
            modifier = modifier.rotate(rotate),
            tint = LocalContentColor.current.copy(ContentAlpha.medium)
        )
    }
}