package com.leebeebeom.clothinghelper.ui.signin.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.ui.util.clearFocus

@Composable
fun SignInBaseColumn(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .clearFocus()
            .padding(horizontal = 40.dp)
            .padding(bottom = 40.dp), verticalArrangement = Arrangement.Center,
        content = content
    )
}