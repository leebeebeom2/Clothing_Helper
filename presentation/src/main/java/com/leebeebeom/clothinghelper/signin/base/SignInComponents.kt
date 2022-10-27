package com.leebeebeom.clothinghelper.signin.base

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import com.leebeebeom.clothinghelper.base.MaxWidthButton
import com.leebeebeom.clothinghelper.base.SimpleIcon

@Composable
fun VisibleIcon(isVisible: Boolean, onIconClick: () -> Unit) {
    IconButton(onClick = onIconClick) {
        SimpleIcon(drawable = if (isVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility)
    }
}

@Composable
fun GoogleSignInButton(
    signInWithGoogleEmail: (ActivityResult) -> Unit,
    enabled: Boolean,
    onGoogleSignInClick: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = signInWithGoogleEmail
    )

    val intent = GoogleSignIn.getClient(LocalContext.current, getGso()).signInIntent

    MaxWidthButton(
        text = R.string.starts_with_google_email,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface),
        icon = googleIcon,
        enabled = enabled,
        onClick = {
            onGoogleSignInClick()
            launcher.launch(intent)
        }
    )
}

private val googleIcon = @Composable {
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

@Composable
fun PopBackStack(
    taskSuccess: Boolean,
    popBackStack: () -> Unit,
    popBackStackDone: () -> Unit
) {
    if (taskSuccess) {
        popBackStack()
        popBackStackDone()
    }
}