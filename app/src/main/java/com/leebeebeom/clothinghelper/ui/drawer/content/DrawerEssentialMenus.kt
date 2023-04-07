package com.leebeebeom.clothinghelper.ui.drawer.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
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
import com.leebeebeom.clothinghelper.ui.theme.Disabled
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable // skippable
fun DrawerEssentialMenus(
    onEssentialMenuClick: (EssentialMenuType) -> Unit, essentialMenus: ImmutableList<EssentialMenu>
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        essentialMenus.forEach { essentialMenu ->
            key(essentialMenu.name) {
                EssentialMenu(
                    essentialMenu = essentialMenu,
                    onClick = { essentialMenuType -> onEssentialMenuClick(essentialMenuType) },
                )
            }
        }
    }
    Divider(color = Disabled)
    HeightSpacer(dp = 4)
}

@Composable // skippable
private fun EssentialMenu(
    essentialMenu: EssentialMenu, onClick: (EssentialMenuType) -> Unit
) {
    DrawerRow(height = 44.dp, onClick = { onClick(essentialMenu.type) }) {
        IconWrapper(
            modifier = Modifier.size(22.dp), drawable = essentialMenu.drawable
        )
        WidthSpacer(dp = 8)
        SingleLineText(
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