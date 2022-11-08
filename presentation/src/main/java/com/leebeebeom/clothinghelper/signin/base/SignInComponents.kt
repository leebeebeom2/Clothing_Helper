package com.leebeebeom.clothinghelper.signin.base

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.*

@Composable
fun VisibleIcon(isVisible: Boolean, onClick: () -> Unit) =
    CustomIconButton(
        drawable = if (isVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility,
        tint = LocalContentColor.current.copy(0.4f),
        onClick = onClick,
        modifier = Modifier.size(24.dp),
        contentDescription = if (isVisible) "closeEye" else "openEye"
    )

@Composable
fun GoogleSignInButton(
    state: MaxWidthButtonState,
    onActivityResult: (ActivityResult) -> Unit,
    disabled: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = onActivityResult
    )

    val intent = GoogleSignIn.getClient(LocalContext.current, getGso()).signInIntent

    MaxWidthButton(
        state = state,
        icon = { GoogleIcon() },
        onClick = {
            disabled()
            launcher.launch(intent)
        }
    )
}

@Composable
fun GoogleIcon() {
    Image(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_google_icon),
        contentDescription = null,
        modifier = Modifier.padding(start = 8.dp)
    )
}

@Composable
private fun getGso() = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
    .requestIdToken(stringResource(id = R.string.default_web_client_id)).requestEmail().build()

@Composable
fun OrDivider() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val weightModifier = Modifier.weight(1f)

        Divider(modifier = weightModifier)
        Text(
            text = stringResource(id = R.string.or),
            modifier = Modifier.padding(horizontal = 14.dp),
            style = MaterialTheme.typography.body2,
            color = LocalContentColor.current.copy(ContentAlpha.medium)
        )
        Divider(modifier = weightModifier)
    }
}