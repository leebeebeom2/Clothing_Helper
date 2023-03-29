package com.leebeebeom.clothinghelper.ui.signin.components

import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.components.MaxWidthButton

@Composable // skippable
fun GoogleSignInButton(
    onResult: (ActivityResult, googleSignInButtonEnable: () -> Unit) -> Unit,
    context: Context = LocalContext.current,
) {
    var enabled by rememberSaveable { mutableStateOf(false) }
    val launcher: ManagedActivityResultLauncher<Intent, ActivityResult> =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
            onResult = {
                onResult(it) { enabled = true }
            }
        )
    val gso = rememberedGso()
    val intent = remember { GoogleSignIn.getClient(context, gso).signInIntent }

    MaxWidthButton(text = R.string.starts_with_google_email,
        enabled = { enabled },
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface),
        icon = { GoogleIcon() },
        onClick = {
            enabled = false
            launcher.launch(intent)
        })
}

@Composable
private fun rememberedGso(): GoogleSignInOptions {
    val clientId = stringResource(id = R.string.default_web_client_id)

    return remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(clientId)
            .requestEmail().build()
    }
}

@Composable
fun GoogleIcon() {
    Image(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_google_icon),
        contentDescription = null
    )
}