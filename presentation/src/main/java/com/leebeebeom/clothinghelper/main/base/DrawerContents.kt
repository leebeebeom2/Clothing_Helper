package com.leebeebeom.clothinghelper.main.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.base.SimpleIcon
import com.leebeebeom.clothinghelper.base.SimpleWidthSpacer
import com.leebeebeom.clothinghelper.theme.Disabled
import com.leebeebeom.clothinghelperdomain.model.*

@Composable
fun DrawerContents(
    user: User?,
    onDrawerContentClick: (parentName: String) -> Unit,
    onSettingIconClick: () -> Unit,
    essentialMenus: List<EssentialMenu>,
    mainCategories: List<MainCategory>,
    getSubCategories: (SubCategoryParent) -> List<SubCategory>,
    getIsSubCategoriesLoading: (SubCategoryParent) -> Boolean
) = Column {

    DrawerHeader(user = user, onSettingIconClick = onSettingIconClick)

    Surface(color = Color(0xFF121212)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 4.dp, bottom = 40.dp)
        ) {
            items(essentialMenus, key = { it.type.name }) {
                EssentialMenu(essentialMenu = it, onDrawerContentClick = onDrawerContentClick)
            }

            item {
                SimpleHeightSpacer(dp = 4)
                Divider(color = Disabled)
                SimpleHeightSpacer(dp = 4)
            }

            items(mainCategories, key = { it.type.name }) {
                DrawerMainCategory(
                    mainCategory = it,
                    subCategories = getSubCategories(it.type),
                    isSubCategoriesLoading = getIsSubCategoriesLoading(it.type),
                    onDrawerContentClick = onDrawerContentClick
                )
            }
        }
    }
}

@Composable
private fun DrawerHeader(user: User?, onSettingIconClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f),
            style = MaterialTheme.typography.body1,
            text = "${user?.name}(${user?.email})"
        )

        IconButton(onClick = onSettingIconClick) {
            SimpleIcon(drawable = R.drawable.ic_settings)
        }
    }
}

@Composable
private fun EssentialMenu(
    essentialMenu: EssentialMenu,
    onDrawerContentClick: (parentName: String) -> Unit
) = DrawerContentRow(
    modifier = Modifier.heightIn(44.dp),
    onDrawerContentClick = { onDrawerContentClick(essentialMenu.type.name) }) {
    SimpleIcon(modifier = Modifier.size(22.dp), drawable = essentialMenu.drawable)
    SimpleWidthSpacer(dp = 12)
    DrawerContentText(
        modifier = Modifier.weight(1f),
        text = stringResource(id = essentialMenu.name),
        style = MaterialTheme.typography.body1.copy(letterSpacing = 0.75.sp)
    )
}

@Composable
fun DrawerContentRow(
    modifier: Modifier,
    onDrawerContentClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) = Row(
    modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp)
        .clip(RoundedCornerShape(12.dp))
        .clickable(onClick = onDrawerContentClick)
        .padding(start = 4.dp),
    verticalAlignment = Alignment.CenterVertically,
    content = content
)

@Composable
fun DrawerContentText(modifier: Modifier, text: String, style: TextStyle) {
    Text(
        modifier = modifier,
        text = text,
        style = style,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}