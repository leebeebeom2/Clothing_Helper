package com.leebeebeom.clothinghelper.main.root

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.composables.CustomIconButton
import com.leebeebeom.clothinghelper.base.composables.SingleLineText
import com.leebeebeom.clothinghelper.map.StableUser

@Composable
fun DrawerHeader(user: () -> StableUser?, onSettingIconClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderText(name = { user()?.name ?: "" }, email = { user()?.email ?: "" })
        CustomIconButton(
            drawable = R.drawable.ic_settings,
            onClick = onSettingIconClick
        )
    }
}

@Composable
private fun RowScope.HeaderText(name: () -> String, email: () -> String) {
    SingleLineText(
        modifier = Modifier
            .padding(start = 4.dp)
            .weight(1f),
        style = MaterialTheme.typography.body1,
        text = "${name()}(${email()})"
    )
}