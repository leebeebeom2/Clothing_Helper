package com.leebeebeom.clothinghelper.main.setting

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.composables.MaxWidthButton
import com.leebeebeom.clothinghelper.base.composables.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.base.composables.SimpleIcon
import com.leebeebeom.clothinghelper.base.composables.SingleLineText

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
        Column {
            SettingHeader()
            SettingButton(
                text = R.string.size_chart_template,
                onClick = onSizeChartTemplateButtonClick
            )
        }
        SignOutButton(onClick = viewModel::signOut)
    }
}

@Composable
fun SettingHeader() {
    Column(modifier = Modifier.fillMaxWidth()) {
        SingleLineText(
            text = R.string.setting,
            style = MaterialTheme.typography.h5.copy(fontSize = 28.sp)
        )
        SimpleHeightSpacer(dp = 8)
        Divider(modifier = Modifier.fillMaxWidth())
        SimpleHeightSpacer(dp = 8)
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SettingButton(@StringRes text: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val color = LocalContentColor.current.copy(0.8f)

            SingleLineText(
                modifier = Modifier.weight(1f),
                text = text,
                style = MaterialTheme.typography.body1.copy(fontSize = 17.sp, color = color)
            )
            SimpleIcon(
                drawable = R.drawable.ic_navigate_next,
                tint = color
            )
        }
    }
}