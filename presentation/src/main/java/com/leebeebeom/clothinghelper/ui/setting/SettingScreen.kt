package com.leebeebeom.clothinghelper.ui.setting

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.composable.MaxWidthButton
import com.leebeebeom.clothinghelper.ui.setting.base.SettingButton
import com.leebeebeom.clothinghelper.composable.TitleWithDivider

@Composable
fun SettingScreen(
    onSizeChartTemplateButtonClick: () -> Unit,
    viewModel: SettingViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            TitleWithDivider(title = R.string.setting)
            SettingButton(
                text = R.string.size_chart_template,
                onClick = onSizeChartTemplateButtonClick
            )
        }
        SignOutButton(onClick = viewModel::signOut)
    }
}

@Composable
private fun BoxScope.SignOutButton(onClick: () -> Unit) {
    MaxWidthButton(
        modifier = Modifier.align(Alignment.BottomCenter),
        text = R.string.sign_out,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error),
        onClick = onClick
    )
}