package com.leebeebeom.clothinghelper.ui.drawer.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.component.HeightSpacer
import com.leebeebeom.clothinghelper.ui.component.IconWrapper
import com.leebeebeom.clothinghelper.ui.component.SingleLineText
import com.leebeebeom.clothinghelper.ui.component.WidthSpacer
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerRow
import com.leebeebeom.clothinghelper.ui.drawer.rememberDrawerCurrentPositionBackgroundColor
import com.leebeebeom.clothinghelper.ui.main.MainGraphRoute
import com.leebeebeom.clothinghelper.ui.theme.Disabled
import com.leebeebeom.clothinghelper.ui.util.CurrentBackStack
import com.leebeebeom.clothinghelper.ui.util.OnEssentialMenuClick
import kotlinx.collections.immutable.persistentListOf

@Composable // skippable
fun DrawerEssentialMenus(
    onEssentialMenuClick: OnEssentialMenuClick,
    currentBackStack: CurrentBackStack
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        rememberEssentialMenus().forEach { essentialMenu ->
            key(essentialMenu.name) {
                EssentialMenu(
                    essentialMenu = essentialMenu,
                    onClick = { essentialMenuType -> onEssentialMenuClick(essentialMenuType) },
                    currentBackStack = currentBackStack
                )
            }
        }
    }
    Divider(color = Disabled)
    HeightSpacer(dp = 4)
}

@Composable // skippable
private fun EssentialMenu(
    essentialMenu: EssentialMenu,
    onClick: (EssentialMenuType) -> Unit,
    currentBackStack: CurrentBackStack
) {
    val route = remember(essentialMenu) {
        when (essentialMenu.type) {
            EssentialMenuType.MainScreen -> MainGraphRoute.MainScreen
            EssentialMenuType.Favorite -> MainGraphRoute.FavoriteScreen
            EssentialMenuType.SeeAll -> MainGraphRoute.SeeAllScreen
            EssentialMenuType.Trash -> MainGraphRoute.TrashScreen
        }
    }

    val currentPositionBackgroundColor by rememberDrawerCurrentPositionBackgroundColor(
        currentBackStack = currentBackStack,
        route = route
    )

    DrawerRow(
        currentPositionBackgroundColor = { currentPositionBackgroundColor },
        onClick = { onClick(essentialMenu.type) },
        height = { 44.dp }) {
        IconWrapper(
            modifier = Modifier.size(22.dp), drawable = essentialMenu.drawable
        )
        WidthSpacer(dp = 8)
        SingleLineText(
            modifier = Modifier.padding(vertical = 4.dp),
            text = stringResource(id = essentialMenu.name),
            style = MaterialTheme.typography.subtitle1.copy(
                fontSize = 18.sp, letterSpacing = 0.75.sp
            )
        )
    }
}

data class EssentialMenu( // stable
    val name: Int, val drawable: Int, val type: EssentialMenuType
)

enum class EssentialMenuType {
    MainScreen, Favorite, SeeAll, Trash
}

@Composable
fun rememberEssentialMenus() = remember {
    persistentListOf(
        EssentialMenu(R.string.main_screen, R.drawable.ic_home, EssentialMenuType.MainScreen),
        EssentialMenu(R.string.favorite, R.drawable.ic_star, EssentialMenuType.Favorite),
        EssentialMenu(R.string.see_all, R.drawable.ic_list, EssentialMenuType.SeeAll),
        EssentialMenu(R.string.trash, R.drawable.ic_delete, EssentialMenuType.Trash)
    )
}