package com.leebeebeom.clothinghelper.ui

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.data.TextFieldAttr
import com.leebeebeom.clothinghelper.ui.signin.SignInBaseViewModel
import com.leebeebeom.clothinghelper.ui.signin.SignInNavigationRoute
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import com.leebeebeom.clothinghelper.ui.theme.Disabled
import com.leebeebeom.clothinghelper.ui.theme.DisabledDeep
import com.leebeebeom.clothinghelper.ui.signin.GoogleSignInImpl as GoogleSignInInterface

@Composable
fun ThemeRoot(content: @Composable () -> Unit) {
    ClothingHelperTheme {
        SetStatusBarColor()
        content()
    }
}

@Composable
fun MaxWidthTextField(attr: TextFieldAttr) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        value = attr.text,
        onValueChange = attr.onValueChange,
        label = { Text(text = stringResource(id = attr.labelId)) },
        placeholder = { Text(text = stringResource(id = attr.placeHolderId)) },
        isError = attr.isErrorEnable,
        visualTransformation = attr.visualTransformation,
        keyboardOptions = attr.keyboardOptions,
        trailingIcon = attr.visibleIcon,
        keyboardActions = if (attr.keyboardOptions.imeAction == ImeAction.Done) KeyboardActions(
            onDone = { focusManager.clearFocus() }) else KeyboardActions(),
        colors =
        TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Color(0xFFDADADA),
            unfocusedLabelColor = Color(0xFF8391A1),
            backgroundColor = Color(0xFFF7F8F9),
            placeholderColor = DisabledDeep,
        )
    )
    if (attr.isErrorEnable) ErrorText(errorTextId = attr.errorTextId)
}

@Composable
fun ErrorText(errorTextId: Int) =
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = errorTextId),
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(start = 4.dp)
        )
    }

@Composable
fun SimpleIcon(drawableId: Int) =
    Icon(painter = painterResource(id = drawableId), contentDescription = null)

@Composable
fun ClickableIcon(drawableId: Int, onClick: () -> Unit) {
    Icon(
        painter = painterResource(id = drawableId), contentDescription = null,
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Composable
fun MaxWidthButton(
    text: String,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    textColor: Color = Color.Unspecified,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    SimpleHeightSpacer(dp = 12)
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        onClick = onClick,
        colors = colors,
        enabled = enabled
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            icon?.invoke()
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.body2,
            )
        }
    }
}

@Composable
fun FirebaseButton(textId: Int, viewModel: SignInBaseViewModel) {
    MaxWidthButton(
        text = stringResource(id = textId),
        enabled = viewModel.firebaseButtonEnable,
        onClick = viewModel.onFirebaseButtonClick
    )
}

val googleLogo = @Composable {
    SimpleWidthSpacer(dp = 8)
    Image(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_google_icon),
        contentDescription = null,
        alignment = Alignment.CenterStart,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun CenterCircularProgressIndicator() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Disabled)
        .clickable(enabled = false) { }) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun FinishActivity() = (LocalContext.current as ComponentActivity).finish()

@Composable
fun SimpleHeightSpacer(dp: Int) = Spacer(modifier = Modifier.height(dp.dp))

@Composable
fun SimpleWidthSpacer(dp: Int) = Spacer(modifier = Modifier.width(dp.dp))

@Composable
fun GoogleSignInBtn(googleSignInImpl: GoogleSignInInterface) {
    val googleSignInIntent =
        GoogleSignIn.getClient(
            LocalContext.current,
            googleSignInImpl.getGso(stringResource(id = R.string.default_web_client_id))
        ).signInIntent

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            googleSignInImpl.signInWithCredential(it.data)
        }

    MaxWidthButton(
        text = stringResource(id = R.string.starts_with_google_email),
        colors = ButtonDefaults.textButtonColors(backgroundColor = Color.White),
        textColor = Color.Black,
        icon = googleLogo,
        onClick = { googleSignInImpl.launch(googleSignInIntent, launcher) }
    )
}


@Composable
fun SimpleToast(resId: Int) =
    Toast.makeText(LocalContext.current, stringResource(id = resId), Toast.LENGTH_SHORT).show()


@Composable
fun SetStatusBarColor() {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(color = Color.White, darkIcons = true)
    }
}

fun navigate(navController: NavController, destination: String) {
    navController.navigate(destination) {
        popUpTo(SignInNavigationRoute.SIGN_IN)
    }
}