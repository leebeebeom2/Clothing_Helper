package com.leebeebeom.clothinghelper.main.root

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.CustomIconButton
import com.leebeebeom.clothinghelperdomain.model.User

@Composable
fun DrawerHeader(user: User?, onSettingIconClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        HeaderText(user)
        CustomIconButton(drawable = R.drawable.ic_settings, onClick = onSettingIconClick, contentDescription = "settingIcon")
    }
}

@Composable
private fun RowScope.HeaderText(user: User?) {
    Text(
        modifier = Modifier
            .padding(start = 12.dp)
            .weight(1f),
        style = MaterialTheme.typography.body1,
        text = "${user?.name}(${user?.email})"
    )
}