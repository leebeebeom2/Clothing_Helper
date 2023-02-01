package com.leebeebeom.clothinghelper.ui.signin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.ui.util.noRippleClickable

@Composable
fun SignInBaseColumn(
    focusManager: FocusManager = LocalFocusManager.current,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .noRippleClickable(onClick = focusManager::clearFocus) // 테스트 불가
            .padding(horizontal = 40.dp)
            .padding(bottom = 40.dp),
        verticalArrangement = Arrangement.Center
    ) {
        content()
    }
}