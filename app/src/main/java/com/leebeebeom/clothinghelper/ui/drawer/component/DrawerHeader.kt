package com.leebeebeom.clothinghelper.ui.drawer.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.component.CustomIconButton
import com.leebeebeom.clothinghelper.ui.component.SingleLineText
import com.leebeebeom.clothinghelper.ui.theme.Black11

const val SettingIconTag = "setting icon"

@Composable // skippable
fun DrawerHeader(
    userName: () -> String?,
    userEmail: () -> String?,
    onSettingIconClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Black11)
            .padding(start = 12.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderUserInfo(userName = userName, userEmail = userEmail)
        CustomIconButton(
            modifier = Modifier.testTag(SettingIconTag),
            drawable = R.drawable.ic_settings,
            onClick = onSettingIconClick
        )
    }
}

@Composable // skippable
private fun RowScope.HeaderUserInfo(userName: () -> String?, userEmail: () -> String?) {
    val localUserName by remember(userName) { derivedStateOf(userName) }
    val localUserEmail by remember(userEmail) { derivedStateOf(userEmail) }

    SingleLineText(
        modifier = Modifier.weight(1f),
        style = MaterialTheme.typography.body1,
        text = "$localUserName($localUserEmail)"
    )
}