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
import com.leebeebeom.clothinghelper.base.SimpleIcon
import com.leebeebeom.clothinghelper.base.SimpleWidthSpacer
import com.leebeebeom.clothinghelper.main.base.AllExpandIcon
import com.leebeebeom.clothinghelper.theme.Disabled
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.model.User

@Composable
fun DrawerContents(
    user: () -> User?,
    isLoading: () -> Boolean,
    isAllExpand: () -> Boolean,
    allSubCategories: () -> List<List<SubCategory>>,
    state: DrawerContentsState = rememberDrawerContentsState(),
    onEssentialMenuClick: (essentialMenu: EssentialMenus) -> Unit,
    onMainCategoryClick: (SubCategoryParent) -> Unit,
    onSubCategoryClick: (SubCategory) -> Unit,
    onSettingIconClick: () -> Unit,
    allExpandIconClick: () -> Unit
) = Column {
    DrawerHeader(user = user, onSettingIconClick = onSettingIconClick)

    Surface(color = Color(0xFF121212)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 8.dp, bottom = 40.dp)
        ) {
            items(state.essentialMenus, key = { it.name }) {
                EssentialMenu(essentialMenu = it, onClick = { onEssentialMenuClick(it.type) })
            }

            item {
                IconWithDivider(
                    isAllExpand = isAllExpand,
                    onCLick = allExpandIconClick
                )
            }

            items(state.mainCategories, key = { it.name }) {
                DrawerMainCategory(
                    mainCategory = it,
                    subCategories = { allSubCategories()[it.type.ordinal] },
                    isLoading = isLoading,
                    isAllExpand = isAllExpand,
                    onMainCategoryClick = onMainCategoryClick,
                    onSubCategoryClick = onSubCategoryClick
                )
            }
        }
    }
}

@Composable
private fun IconWithDivider(isAllExpand: () -> Boolean, onCLick: () -> Unit) =
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 4.dp)) {
        Divider(color = Disabled, modifier = Modifier.weight(1f))
        AllExpandIcon(
            size = 20.dp,
            onClick = onCLick,
            tint = LocalContentColor.current.copy(0.6f),
            allExpand = isAllExpand
        )
    }

@Composable
private fun EssentialMenu(essentialMenu: EssentialMenu, onClick: () -> Unit) =
    DrawerContentRow(
        modifier = Modifier.heightIn(40.dp),
        onClick = onClick
    ) {
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
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) = Row(
    modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp)
        .clip(RoundedCornerShape(12.dp))
        .clickable(onClick = onClick)
        .padding(start = 4.dp),
    verticalAlignment = Alignment.CenterVertically,
    content = content
)

@Composable
fun DrawerContentText(modifier: Modifier = Modifier, text: String, style: TextStyle) =
    Text(
        modifier = modifier,
        text = text,
        style = style,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )

data class DrawerContentsState(
    val essentialMenus: List<EssentialMenu> = getEssentialMenus(),
    val mainCategories: List<MainCategory> = getMainCategories()
)

@Composable
fun rememberDrawerContentsState() = remember { DrawerContentsState() }

fun getMainCategories() = listOf(
    MainCategory(R.string.top, SubCategoryParent.TOP),
    MainCategory(R.string.bottom, SubCategoryParent.BOTTOM),
    MainCategory(R.string.outer, SubCategoryParent.OUTER),
    MainCategory(R.string.etc, SubCategoryParent.ETC)
)

fun getEssentialMenus() = listOf(
    EssentialMenu(R.string.main_screen, R.drawable.ic_home, EssentialMenus.MainScreen),
    EssentialMenu(R.string.favorite, R.drawable.ic_star, EssentialMenus.Favorite),
    EssentialMenu(R.string.see_all, R.drawable.ic_list, EssentialMenus.SeeAll),
    EssentialMenu(R.string.trash, R.drawable.ic_delete, EssentialMenus.Trash)
)

enum class EssentialMenus {
    MainScreen, Favorite, SeeAll, Trash
}

data class EssentialMenu(
    val name: Int, val drawable: Int, val type: EssentialMenus
)

data class MainCategory(val name: Int, val type: SubCategoryParent)