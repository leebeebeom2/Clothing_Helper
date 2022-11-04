package com.leebeebeom.clothinghelper.main.setting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.MaxWidthButton
import com.leebeebeom.clothinghelper.base.rememberMaxWidthButtonState
import kotlinx.coroutines.launch

@Composable
fun SettingScreen(
    viewModel: SettingViewModel = hiltViewModel(),
    onSignOutButtonClick: suspend () -> Unit,
    drawerCloseBackHandler: @Composable () -> Unit
) {
    drawerCloseBackHandler()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        val coroutineScope = rememberCoroutineScope()

        SignOutButton(onSignOutClick = {
            viewModel.signOut()
            coroutineScope.launch { onSignOutButtonClick() }
        })
    }
}

@Composable
private fun SignOutButton(onSignOutClick: () -> Unit) {
    MaxWidthButton(
        state = rememberMaxWidthButtonState(
            text = R.string.sign_out,
            enabled = true,
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
        ),
        onClick = onSignOutClick
    )
}