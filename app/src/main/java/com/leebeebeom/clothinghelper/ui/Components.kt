package com.leebeebeom.clothinghelper.ui

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.theme.Disabled

@Composable
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

@Composable
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
            .heightIn(52.dp), onClick = {
            focusManager.clearFocus()
            onClick()
        }, colors = colors, enabled = enabled
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart
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
    Surface(color = Disabled) {
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable(enabled = false) { }) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
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