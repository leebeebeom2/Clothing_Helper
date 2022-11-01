package com.leebeebeom.clothinghelper.main.root

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.model.User

@Composable
fun DrawerContents(
    user: User?,
    onEssentialMenuClick: (essentialMenu: EssentialMenus) -> Unit,
    onMainCategoryClick: (SubCategoryParent) -> Unit,
    onSubCategoryClick: (key: String) -> Unit,
    onSettingIconClick: () -> Unit,
    getSubCategories: (SubCategoryParent) -> List<SubCategory>,
    isLoading: Boolean
) = Column {

    val state = rememberDrawerContentsUIState()

    DrawerHeader(user = user, onSettingIconClick = onSettingIconClick)

    Surface(color = Color(0xFF121212)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 4.dp, bottom = 40.dp)
        ) {
            this.items(state.essentialMenus, key = { it.type.name }) {
                EssentialMenu(essentialMenu = it, onEssentialMenuClick = onEssentialMenuClick)
            }

            item {
                SimpleHeightSpacer(dp = 4)
                IconWithDivider()
                SimpleHeightSpacer(dp = 4)
            }

            items(state.mainCategories, key = { it.type.name }) {
                DrawerMainCategory(
                    mainCategory = it,
                    subCategories = getSubCategories(it.type),
                    isLoading = isLoading,
                    onMainCategoryClick = onMainCategoryClick,
                    onSubCategoryClick = onSubCategoryClick
                )
            }
        }
    }
}

@Composable
private fun IconWithDivider() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Divider(color = Disabled, modifier = Modifier.weight(1f))
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .size(20.dp)
        ) {
            SimpleIcon(
                drawable = R.drawable.ic_all_expand,
                tint = LocalContentColor.current.copy(0.6f)
            )
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
    onEssentialMenuClick: (essentialMenu: EssentialMenus) -> Unit
) = DrawerContentRow(
    modifier = Modifier.heightIn(44.dp),
    onDrawerContentClick = { onEssentialMenuClick(essentialMenu.type) }) {
    SimpleIcon(modifier = Modifier.size(22.dp), drawable = essentialMenu.drawable)
    SimpleWidthSpacer(dp = 12)
    DrawerContentText(
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
fun RowScope.DrawerContentText(modifier: Modifier = Modifier, text: String, style: TextStyle) {
    Text(
        modifier = modifier.weight(1f),
        text = text,
        style = style,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

class DrawerContentsState {
    val essentialMenus = getEssentialMenus()
    val mainCategories = getMainCategories()
}

@Composable
fun rememberDrawerContentsUIState() = remember { DrawerContentsState() }

fun getMainCategories() = listOf(
    MainCategory(R.string.top, SubCategoryParent.TOP),
    MainCategory(R.string.bottom, SubCategoryParent.BOTTOM),
    MainCategory(R.string.outer, SubCategoryParent.OUTER),
    MainCategory(R.string.etc, SubCategoryParent.ETC)
)

fun getEssentialMenus() = listOf(
    EssentialMenu(R.string.main_screen, R.drawable.ic_home, EssentialMenus.MAIN_SCREEN),
    EssentialMenu(R.string.favorite, R.drawable.ic_star, EssentialMenus.FAVORITE),
    EssentialMenu(R.string.see_all, R.drawable.ic_list, EssentialMenus.SEE_ALL),
    EssentialMenu(R.string.trash, R.drawable.ic_delete, EssentialMenus.TRASH)
)

enum class EssentialMenus {
    MAIN_SCREEN, FAVORITE, SEE_ALL, TRASH
}

data class EssentialMenu(
    val name: Int,
    val drawable: Int,
    val type: EssentialMenus
)

data class MainCategory(val name: Int, val type: SubCategoryParent)