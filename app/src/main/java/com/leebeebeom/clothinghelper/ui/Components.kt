package com.leebeebeom.clothinghelper.ui

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.base.TextFieldError
import com.leebeebeom.clothinghelper.ui.base.TextFieldUIState
import com.leebeebeom.clothinghelper.ui.theme.Disabled
import kotlinx.coroutines.delay

@Composable
fun MaxWidthTextField(
    modifier: Modifier = Modifier,
    textFieldState: TextFieldUIState,
    onValueChange: (String) -> Unit,
    @StringRes label: Int,
    @StringRes placeHolder: Int = R.string.empty,
    trailingIcon: @Composable (() -> Unit)? = null,
    showKeyboardEnabled: Boolean = false
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()
    val isError = textFieldState.error != TextFieldError.ERROR_OFF

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        value = textFieldState.text,
        onValueChange = onValueChange,
        label = { Text(text = stringResource(id = label)) },
        placeholder = { Text(text = stringResource(id = placeHolder)) },
        isError = isError,
        visualTransformation = textFieldState.visualTransformation,
        singleLine = true,
        maxLines = 1,
        keyboardOptions = textFieldState.keyboardOptions,
        trailingIcon = trailingIcon,
        keyboardActions =
        if (textFieldState.imeAction == ImeAction.Done) KeyboardActions(onDone = { focusManager.clearFocus() })
        else KeyboardActions.Default,
        colors =
        TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Color(0xFFDADADA),
            unfocusedLabelColor = Color(0xFF8391A1),
            backgroundColor = Color(0xFFF7F8F9),
            placeholderColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled)
        )
    )

    ErrorText(isError, textFieldState.error)
    if (showKeyboardEnabled) ShowKeyboard(focusRequester)
}

@Composable
private fun ErrorText(
    isError: Boolean,
    error: TextFieldError
) {
    AnimatedVisibility(
        visible = isError,
        enter = expandVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ),
        exit = shrinkVertically(
            animationSpec = tween(1)
        )
    ) {
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = stringResource(id = error.resId),
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption,
        )
    }
}

@Composable // 검수 완
fun SimpleIcon(
    modifier: Modifier = Modifier,
    @DrawableRes drawable: Int,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
) = Icon(
    modifier = modifier,
    painter = painterResource(id = drawable),
    contentDescription = contentDescription,
    tint = tint
)

@Composable // 검수 완
fun ClickableIcon(
    modifier: Modifier = Modifier,
    @DrawableRes drawable: Int,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    onClick: () -> Unit
) {
    SimpleIcon(
        modifier = modifier
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(8.dp),
        drawable = drawable,
        contentDescription = contentDescription,
        tint = tint
    )
}

@Composable // 검수 완
fun MaxWidthButton(
    modifier: Modifier = Modifier,
    @StringRes text: Int,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    textColor: Color = Color.Unspecified,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Button(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(52.dp),
        onClick = {
            focusManager.clearFocus()
            onClick()
        },
        colors = colors,
        enabled = enabled
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            icon?.invoke()
            Text(
                text = stringResource(id = text),
                fontWeight = FontWeight.Bold,
                color = if (enabled) textColor else Color.Unspecified,
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.body2,
            )
        }
    }
}

val googleLogo = @Composable {
    Image(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_google_icon),
        contentDescription = null,
        alignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp)
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
fun FinishActivityOnBackPressed() {
    val context = LocalContext.current

    BackHandler {
        (context as ComponentActivity).finishAffinity()
    }
}

@Composable
fun SimpleHeightSpacer(dp: Int) = Spacer(modifier = Modifier.height(dp.dp))

@Composable
fun SimpleWidthSpacer(dp: Int) = Spacer(modifier = Modifier.width(dp.dp))

@Composable
fun SimpleToast(@StringRes text: Int, shownToast: () -> Unit) {
    Toast.makeText(LocalContext.current, stringResource(id = text), Toast.LENGTH_SHORT).show()
    shownToast()
}

@Composable
fun ClickableText(
    modifier: Modifier = Modifier,
    @StringRes text: Int,
    style: TextStyle = TextStyle.Default,
    onClick: () -> Unit
) {
    Text(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp, horizontal = 8.dp),
        text = stringResource(id = text),
        style = style
    )
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun ShowKeyboard(focusRequester: FocusRequester) {
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        delay(100)
        keyboardController?.show()
    }
}