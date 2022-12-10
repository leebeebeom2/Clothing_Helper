package com.leebeebeom.clothinghelper.ui.main.root.contents

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.composable.CustomIconButton
import com.leebeebeom.clothinghelper.composable.SimpleIcon
import com.leebeebeom.clothinghelper.composable.SimpleWidthSpacer
import com.leebeebeom.clothinghelper.composable.SingleLineText
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.map.StableUser
import com.leebeebeom.clothinghelper.theme.DarkGray
import com.leebeebeom.clothinghelper.theme.Disabled
import com.leebeebeom.clothinghelper.ui.main.root.components.DrawerRow
import com.leebeebeom.clothinghelper.ui.main.root.model.*
import com.leebeebeom.clothinghelper.util.AddFolder
import com.leebeebeom.clothinghelper.util.AddSubCategory
import com.leebeebeom.clothinghelper.util.EditFolder
import com.leebeebeom.clothinghelper.util.EditSubCategory
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerContents(
    state: DrawerContentsState = remember { DrawerContentsState() },
    user: () -> StableUser?,
    isLoading: () -> Boolean,
    subCategories: (parentName: String) -> ImmutableList<StableSubCategory>,
    folders: (parentKey: String) -> ImmutableList<StableFolder>,
    onEssentialMenuClick: (essentialMenu: EssentialMenuType) -> Unit,
    onMainCategoryClick: (SubCategoryParent) -> Unit,
    onSubCategoryClick: (StableSubCategory) -> Unit,
    onFolderClick: (StableFolder) -> Unit,
    onSettingIconClick: () -> Unit,
    onAddSubCategoryPositiveButtonClick: AddSubCategory,
    onEditSubCategoryPositiveClick: EditSubCategory,
    onAddFolderPositiveClick: AddFolder,
    onEditFolderPositiveClick: EditFolder
) {
    Column {
        Header(user = user, onSettingIconClick = onSettingIconClick)

        Surface(color = DarkGray) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 4.dp, bottom = 40.dp)
            ) {
                // essential menu
                essentialMenus(
                    essentialMenus = state.essentialMenus,
                    onEssentialMenuClick = onEssentialMenuClick
                )
                divider()
                mainCategories(
                    mainCategories = state.mainCategories,
                    isLoading = isLoading,
                    subCategories = subCategories,
                    folders = folders,
                    onClick = onMainCategoryClick,
                    onSubCategoryClick = onSubCategoryClick,
                    onFolderClick = onFolderClick,
                    onAddSubCategoryPositiveButtonClick = onAddSubCategoryPositiveButtonClick,
                    onEditSubCategoryPositiveClick = onEditSubCategoryPositiveClick,
                    onAddFolderPositiveClick = onAddFolderPositiveClick,
                    onEditFolderPositiveClick = onEditFolderPositiveClick
                )
            }
        }
    }
}

data class DrawerContentsState(
    val essentialMenus: ImmutableList<EssentialMenu> = getEssentialMenus(),
    val mainCategories: ImmutableList<MainCategory> = getMainCategories()
)

@Composable
private fun Header(user: () -> StableUser?, onSettingIconClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderText(name = { user()?.name ?: "" }, email = { user()?.email ?: "" })
        CustomIconButton(
            drawable = R.drawable.ic_settings, onClick = onSettingIconClick
        )
    }
}

@Composable
private fun RowScope.HeaderText(name: () -> String, email: () -> String) {
    SingleLineText(
        modifier = Modifier
            .padding(start = 4.dp)
            .weight(1f),
        style = MaterialTheme.typography.subtitle1,
        text = "${name()}(${email()})"
    )
}

private fun LazyListScope.essentialMenus(
    essentialMenus: ImmutableList<EssentialMenu>,
    onEssentialMenuClick: (essentialMenu: EssentialMenuType) -> Unit
) {
    items(items = essentialMenus, key = { it.name }) {
        EssentialMenu(
            essentialMenu = it,
            onClick = onEssentialMenuClick,
        )
    }
}

@Composable
private fun EssentialMenu(
    essentialMenu: EssentialMenu, onClick: (EssentialMenuType) -> Unit
) {
    DrawerRow(modifier = Modifier.heightIn(40.dp), onClick = { onClick(essentialMenu.type) }) {
        SimpleIcon(modifier = Modifier.size(22.dp), drawable = essentialMenu.drawable)
        SimpleWidthSpacer(dp = 8)
        SingleLineText(
            text = stringResource(id = essentialMenu.name),
            style = MaterialTheme.typography.body1.copy(letterSpacing = 0.75.sp)
        )
    }
}

private fun LazyListScope.divider() {
    item {
        Divider(
            color = Disabled, modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

private fun LazyListScope.mainCategories(
    mainCategories: ImmutableList<MainCategory>,
    isLoading: () -> Boolean,
    subCategories: (parentName: String) -> ImmutableList<StableSubCategory>,
    folders: (parentKey: String) -> ImmutableList<StableFolder>,
    onClick: (SubCategoryParent) -> Unit,
    onSubCategoryClick: (StableSubCategory) -> Unit,
    onFolderClick: (StableFolder) -> Unit,
    onAddSubCategoryPositiveButtonClick: AddSubCategory,
    onEditSubCategoryPositiveClick: EditSubCategory,
    onAddFolderPositiveClick: AddFolder,
    onEditFolderPositiveClick: EditFolder
) {
    items(mainCategories, key = { it.name }) {
        DrawerMainCategory(
            mainCategory = it,
            isLoading = isLoading,
            subCategories = subCategories,
            folders = folders,
            onClick = onClick,
            onSubCategoryClick = onSubCategoryClick,
            onFolderClick = onFolderClick,
            onAddSubCategoryPositiveClick = onAddSubCategoryPositiveButtonClick,
            onEditSubCategoryPositiveClick = onEditSubCategoryPositiveClick,
            onAddFolderPositiveClick = onAddFolderPositiveClick,
            onEditFolderPositiveClick = onEditFolderPositiveClick
        )
    }
}