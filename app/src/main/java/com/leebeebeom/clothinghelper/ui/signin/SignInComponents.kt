package com.leebeebeom.clothinghelper.ui.signin

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.data.TextFieldState
import com.leebeebeom.clothinghelper.ui.*
import com.leebeebeom.clothinghelper.ui.theme.Disabled
import com.leebeebeom.clothinghelper.ui.theme.DisabledDeep
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignInColumn(
    progressOn: Boolean,
    isFirebaseTaskFailed: Boolean,
    progressOff: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        content = content
    )
    if (progressOn) CenterCircularProgressIndicator()
    if (isFirebaseTaskFailed) {
        SimpleToast(resId = R.string.unknown_error)
        progressOff()
    }
}

@Composable
fun EmailTextField(
    text: String,
    onValueChange: (String) -> Unit,
    textFieldError: TextFieldState.TextFieldError,
    isError: Boolean,
    imeAction: ImeAction = ImeAction.Default
) {
    MaxWidthTextField(
        text = text,
        onValueChange = onValueChange,
        labelResId = R.string.email,
        placeHolderResId = R.string.email_place_holder,
        textFieldError = textFieldError,
        isError = isError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = imeAction)
    )
}

@Composable
fun PasswordTextField(
    text: String,
    onValueChange: (String) -> Unit,
    textFieldError: TextFieldState.TextFieldError,
    isError: Boolean,
    visualTransformation: VisualTransformation,
    imeAction: ImeAction,
    visibleToggle: () -> Unit
) {
    MaxWidthTextField(
        text = text,
        onValueChange = onValueChange,
        labelResId = R.string.password,
        textFieldError = textFieldError,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        visibleIcon = { VisibleIcon(visibleToggle = visibleToggle) }
    )
}

@Composable
fun FirebaseButton(textId: Int, firebaseButtonEnabled: Boolean, onFirebaseButtonClick: () -> Unit) {
    MaxWidthButton(
        text = stringResource(id = textId),
        enabled = firebaseButtonEnabled,
        onClick = onFirebaseButtonClick
    )
}

@Composable
fun GoogleSignInButton(googleSignInImpl: GoogleSignInImpl) {
    val googleSignInIntent =
        GoogleSignIn.getClient(
            LocalContext.current,
            googleSignInImpl.getGso(stringResource(id = R.string.default_web_client_id))
        ).signInIntent

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            googleSignInImpl.signInWithCredential(it.data)
        }

    var isClicked by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    fun clickedToggle() {
        coroutineScope.launch {
            isClicked = true
            delay(3000)
            isClicked = false
        }
    }
    val waitToast = Toast.makeText(LocalContext.current, stringResource(R.string.wait_please), Toast.LENGTH_SHORT)

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
            waitToast.show()
            clickedToggle()
            googleSignInImpl.launch(googleSignInIntent, launcher)
        }
    )
}

@Composable
fun VisibleIcon(visibleToggle: () -> Unit) {
    var isVisible by remember { mutableStateOf(false) }

    IconButton(onClick = {
        visibleToggle()
        isVisible = !isVisible
    }) {
        if (isVisible) SimpleIcon(drawableId = R.drawable.ic_eye_close)
        else SimpleIcon(drawableId = R.drawable.ic_eye_open)
    }
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