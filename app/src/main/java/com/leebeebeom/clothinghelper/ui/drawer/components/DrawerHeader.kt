package com.leebeebeom.clothinghelper.ui.drawer

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.components.CustomIconButton
import com.leebeebeom.clothinghelper.ui.components.SingleLineText

const val SettingIconTag = "setting icon"

@Composable // skippable
fun DrawerHeader(
    userName: () -> String?,
    userEmail: () -> String?,
    navigateToSetting: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderUserInfo(userName = userName, userEmail = userEmail)
        CustomIconButton(
            modifier = Modifier.testTag(SettingIconTag),
            drawable = R.drawable.ic_settings,
            onClick = navigateToSetting
        )
    }
}

@Composable // skippable
private fun RowScope.HeaderUserInfo(userName: () -> String?, userEmail: () -> String?) {
    SingleLineText(
        modifier = Modifier.weight(1f),
        style = MaterialTheme.typography.body1,
        text = "${userName()}(${userEmail()})"
    )
}