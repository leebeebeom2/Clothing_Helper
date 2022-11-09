package com.leebeebeom.clothinghelper.base

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.theme.Disabled

@Composable
fun SimpleIcon(
    modifier: Modifier = Modifier,
    @DrawableRes drawable: Int,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
) = Icon(
    modifier = modifier,
    painter = painterResource(id = drawable),
    contentDescription = null,
    tint = tint
)

@Composable
fun CenterDotProgressIndicator(
    backGround: Color = Disabled,
    isLoading: () -> Boolean,
    content: @Composable () -> Unit = {}
) = if (isLoading()) Surface(color = backGround) {
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable(enabled = false) { }) {
            DotProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .semantics { contentDescription = "loading" },
                size = 8.dp,
                color = LocalContentColor.current.copy(ContentAlpha.medium)
            )
        }
    } else content()

@Composable
fun SimpleHeightSpacer(dp: Int) = Spacer(modifier = Modifier.height(dp.dp))

@Composable
fun SimpleWidthSpacer(dp: Int) = Spacer(modifier = Modifier.width(dp.dp))

@Composable
fun SimpleToast(@StringRes text: () -> Int?, shownToast: () -> Unit) =
    Box{
        text()?.let {
            Toast.makeText(LocalContext.current, stringResource(id = it), Toast.LENGTH_SHORT).show()
            shownToast()
        }
    }

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun CircleCheckBox(
    modifier: Modifier = Modifier, isChecked: () -> Boolean, onClick: () -> Unit = {}
) = CustomIconButton(
    modifier = modifier, onClick = onClick, painter = rememberAnimatedVectorPainter(
        animatedImageVector = AnimatedImageVector.animatedVectorResource(
            id = R.drawable.check_anim
        ), atEnd = isChecked()
    ), tint = LocalContentColor.current.copy(0.7f)
)

@Composable
fun CustomIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    @DrawableRes drawable: Int,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    contentDescription: String? = null
) = CustomIconButton(
    modifier = modifier,
    onClick = onClick,
    painter = painterResource(id = drawable),
    tint = tint,
    contentDescription = contentDescription
)

@Composable
fun CustomIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    painter: Painter,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    contentDescription: String? = null
) = Box(
    modifier = Modifier
        .clip(CircleShape)
        .clickable(onClick = onClick)
        .padding(4.dp)
) {
    Icon(
        painter = painter, contentDescription = contentDescription, modifier = modifier, tint = tint
    )
}

@Composable
fun FinishActivityBackHandler(activity: ComponentActivity = LocalContext.current as ComponentActivity) =
    BackHandler(enabled = true) { activity.finish() }
