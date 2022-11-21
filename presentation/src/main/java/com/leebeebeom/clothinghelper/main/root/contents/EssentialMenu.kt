package com.leebeebeom.clothinghelper.main.root.contents

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.base.composables.SimpleIcon
import com.leebeebeom.clothinghelper.base.composables.SimpleWidthSpacer
import com.leebeebeom.clothinghelper.base.composables.SingleLineText
import com.leebeebeom.clothinghelper.main.root.components.DrawerContentRow
import com.leebeebeom.clothinghelper.main.root.model.EssentialMenu
import com.leebeebeom.clothinghelper.main.root.model.EssentialMenuType

@Composable
fun EssentialMenu(
    essentialMenu: EssentialMenu,
    onClick: (EssentialMenuType) -> Unit
) {
    DrawerContentRow(
        modifier = Modifier.heightIn(40.dp),
        onClick = { onClick(essentialMenu.type) }
    ) {
        SimpleIcon(modifier = Modifier.size(22.dp), drawable = essentialMenu.drawable)
        SimpleWidthSpacer(dp = 12)
        SingleLineText(
            text = stringResource(id = essentialMenu.name),
            style = MaterialTheme.typography.body1.copy(letterSpacing = 0.75.sp)
        )
    }
}