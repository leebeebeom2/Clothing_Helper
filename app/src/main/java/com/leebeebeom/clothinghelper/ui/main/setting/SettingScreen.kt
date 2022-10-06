package com.leebeebeom.clothinghelper.ui.main.setting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.MaxWidthButton
import com.leebeebeom.clothinghelper.ui.signin.FirebaseExecutor

@Composable
fun SettingScreen(onNavigateToMain: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        SignOutButton(onNavigateToMain)
    }
}

@Composable
private fun SignOutButton(onNavigateToMain: () -> Unit) {
    MaxWidthButton(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
        text = stringResource(id = R.string.sign_out),
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error),
        onClick = {
            FirebaseExecutor.signOut()
            onNavigateToMain()
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SignOutButtonPreview() {
    SettingScreen {}
}