package com.leebeebeom.clothinghelper.ui.drawer.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.component.IconWrapper

@Composable
fun DrawerDotIcon() {
    IconWrapper(modifier = Modifier.padding(end = 8.dp), drawable = R.drawable.ic_dot)
}