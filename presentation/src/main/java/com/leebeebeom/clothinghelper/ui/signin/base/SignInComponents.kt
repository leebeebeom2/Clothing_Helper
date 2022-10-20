package com.leebeebeom.clothinghelper.ui.signin.base

import android.content.Context
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.base.MaxWidthButton
import com.leebeebeom.clothinghelper.ui.base.SimpleIcon
import com.leebeebeom.clothinghelper.ui.base.googleIcon

@Composable
fun VisibleIcon(visualTransformation: VisualTransformation, onIconClick: (Boolean) -> Unit) {
    var isVisible by rememberSaveable { mutableStateOf(visualTransformation == VisualTransformation.None) }

    IconButton(onClick = {
        isVisible = !isVisible
        onIconClick(isVisible)
    }) {
        SimpleIcon(drawable = if (isVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility)
    }
}

@Composable // 검수
fun GoogleSignInButton(
    signInWithGoogleEmail: (ActivityResult) -> Unit,
    enabled: Boolean
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = signInWithGoogleEmail
    )

    val intent = getGoogleSignInIntent(LocalContext.current, getGso())

    MaxWidthButton(
        text = R.string.starts_with_google_email,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface),
        icon = googleIcon,
        enabled = enabled,
        onClick = { launcher.launch(intent) }
    )
}

private fun getGoogleSignInIntent(context: Context, gso: GoogleSignInOptions) =
    GoogleSignIn.getClient(context, gso).signInIntent

@Composable
private fun getGso() = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
    .requestIdToken(stringResource(id = R.string.default_web_client_id)).requestEmail().build()

@Composable
fun OrDivider() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Divider(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(id = R.string.or),
            modifier = Modifier
                .padding(horizontal = 14.dp),
            style = MaterialTheme.typography.body2,
            color = LocalContentColor.current.copy(ContentAlpha.medium)
        )
        Divider(modifier = Modifier.weight(1f))
    }
}