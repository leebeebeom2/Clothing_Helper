package com.leebeebeom.clothinghelper.ui.signin

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.MaxWidthButton
import com.leebeebeom.clothinghelper.ui.SimpleIcon
import com.leebeebeom.clothinghelper.ui.googleLogo

@Composable
fun VisibleIcon(onIconClick: () -> Unit) {
    var isVisible by rememberSaveable { mutableStateOf(false) }

    IconButton(onClick = {
        onIconClick()
        isVisible = !isVisible
    }) {
        SimpleIcon(drawable = if (isVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility)
    }
}

@Composable
fun GoogleSignInButton(
    googleSignInClick: (Intent, ManagedActivityResultLauncher<Intent, *>) -> Unit,
    googleSignIn: (ActivityResult) -> Unit,
    enabled: Boolean
) {
    val googleSignInIntent = GoogleSignIn.getClient(LocalContext.current, getGso()).signInIntent

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(), onResult = googleSignIn
    )

    MaxWidthButton(text = R.string.starts_with_google_email,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface),
        icon = googleLogo,
        enabled = enabled,
        onClick = { googleSignInClick(googleSignInIntent, launcher) })
}

@Composable
private fun getGso() = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
    .requestIdToken(stringResource(id = R.string.default_web_client_id)).requestEmail().build()


@Composable
fun OrDivider() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val weightModifier = Modifier.weight(1f)

        WeightDivider(weightModifier)
        Text(
            text = stringResource(id = R.string.or),
            modifier = Modifier
                .padding(horizontal = 14.dp)
                .align(Alignment.CenterVertically),
            style = MaterialTheme.typography.body2,
            color = LocalContentColor.current.copy(ContentAlpha.medium)
        )
        WeightDivider(weightModifier)
    }
}

@Composable
private fun WeightDivider(modifier: Modifier = Modifier) = Divider(modifier = modifier)