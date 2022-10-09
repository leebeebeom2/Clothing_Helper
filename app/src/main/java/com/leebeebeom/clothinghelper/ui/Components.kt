package com.leebeebeom.clothinghelper.ui

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.base.TextFieldError
import com.leebeebeom.clothinghelper.ui.base.TextFieldUIState
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import com.leebeebeom.clothinghelper.ui.theme.Disabled
import com.leebeebeom.clothinghelper.ui.theme.DisabledDeep

@Composable
fun ThemeRoot(content: @Composable () -> Unit) {
    ClothingHelperTheme {
        SetStatusBarColor()
        content()
    }
}

@Composable
fun SetStatusBarColor(systemUiController: SystemUiController = rememberSystemUiController()) =
    systemUiController.setStatusBarColor(color = Color.White, darkIcons = true)

@Composable
fun MaxWidthTextField(
    modifier: Modifier = Modifier,
    textFieldUIState: TextFieldUIState,
    onValueChange: (String) -> Unit,
    labelResId: Int,
    placeHolderResId: Int = R.string.empty,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    val focusManager = LocalFocusManager.current
    val isError = textFieldUIState.error != TextFieldError.ERROR_OFF

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = textFieldUIState.text,
        onValueChange = onValueChange,
        label = { Text(text = stringResource(id = labelResId)) },
        placeholder = { Text(text = stringResource(id = placeHolderResId)) },
        isError = isError,
        visualTransformation = textFieldUIState.visualTransformation,
        singleLine = true,
        keyboardOptions = textFieldUIState.keyboardOptions,
        trailingIcon = trailingIcon,
        keyboardActions = if (textFieldUIState.imeAction == ImeAction.Done) KeyboardActions(
            onDone = { focusManager.clearFocus() }) else KeyboardActions.Default,
        colors =
        TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Color(0xFFDADADA),
            unfocusedLabelColor = Color(0xFF8391A1),
            backgroundColor = Color(0xFFF7F8F9),
            placeholderColor = DisabledDeep
        )
    )

    val errorTextHeight by animateDpAsState(targetValue = if (isError) 14.dp else 0.dp)
    ErrorText(error = textFieldUIState.error, errorTextHeight)
}

@Composable
fun ErrorText(error: TextFieldError, height: Dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(14.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .animateContentSize(
                    spring(
                        Spring.DampingRatioHighBouncy,
                        Spring.StiffnessMedium
                    )
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
}

@Composable
fun SimpleIcon(modifier: Modifier = Modifier, drawableId: Int, contentDescription: String? = null) =
    Icon(
        modifier = modifier,
        painter = painterResource(id = drawableId),
        contentDescription = contentDescription
    )

@Composable
fun ClickableIcon(
    modifier: Modifier = Modifier,
    drawableId: Int,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    SimpleIcon(
        modifier = modifier
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(8.dp),
        drawableId = drawableId,
        contentDescription = contentDescription
    )
}

@Composable
fun MaxWidthButton(
    modifier: Modifier = Modifier,
    text: String,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    textColor: Color = Color.Unspecified,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
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
                color = if (enabled) textColor else Color.Unspecified,
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.body2,
            )
        }
    }
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
fun SimpleToast(resId: Int, shownToast: () -> Unit) {
    Toast.makeText(LocalContext.current, stringResource(id = resId), Toast.LENGTH_SHORT).show()
    shownToast()
}