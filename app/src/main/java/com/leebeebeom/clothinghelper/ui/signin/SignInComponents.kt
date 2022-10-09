package com.leebeebeom.clothinghelper.ui.signin

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.*
import com.leebeebeom.clothinghelper.ui.theme.Disabled
import com.leebeebeom.clothinghelper.ui.theme.DisabledDeep
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun VisibleIcon(visualTransformationToggle: (Boolean) -> Unit) {
    var isVisible by rememberSaveable { mutableStateOf(false) }

    IconButton(onClick = {
        visualTransformationToggle(isVisible)
        isVisible = !isVisible
    }) {
        if (isVisible) SimpleIcon(
            drawableId = R.drawable.ic_eye_close,
            contentDescription = "invisible icon"
        )
        else SimpleIcon(drawableId = R.drawable.ic_eye_open, contentDescription = "visible icon")
    }
}

@Composable
fun GoogleSignInButton(
    googleSignInClick: (Intent, ManagedActivityResultLauncher<Intent, *>) -> Unit,
    googleSignIn: (ActivityResult) -> Unit,
    gso: GoogleSignInOptions
) {
    val googleSignInIntent = GoogleSignIn.getClient(LocalContext.current, gso).signInIntent

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = googleSignIn
    )

    var isClicked by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    fun clickedToggle() {
        coroutineScope.launch {
            isClicked = true
            delay(3000)
            isClicked = false
        }
    }

    MaxWidthButton(
        text = stringResource(id = R.string.starts_with_google_email),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White,
            disabledBackgroundColor = Disabled
        ),
        textColor = Color.Black,
        icon = googleLogo,
        enabled = !isClicked,
        onClick = {
            clickedToggle()
            googleSignInClick(googleSignInIntent, launcher)
        })
}

@Composable
fun OrDivider() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val weightModifier = Modifier.weight(1f)

        Weight1Divider(weightModifier)
        Text(
            text = stringResource(id = R.string.or),
            modifier = Modifier
                .padding(horizontal = 14.dp)
                .align(Alignment.CenterVertically),
            style = MaterialTheme.typography.body2,
            color = DisabledDeep
        )
        Weight1Divider(weightModifier)
    }
}

@Composable
fun Weight1Divider(modifier: Modifier = Modifier) = Divider(modifier = modifier)