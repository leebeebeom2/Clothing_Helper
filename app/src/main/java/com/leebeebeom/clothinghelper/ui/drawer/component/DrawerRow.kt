package com.leebeebeom.clothinghelper.ui.drawer.component

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.gestures.PressGestureScope
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NoLiveLiterals
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@NoLiveLiterals
@Composable // skippable
fun DrawerRow(
    modifier: Modifier = Modifier,
    height: Dp = 48.dp,
    onClick: () -> Unit,
    onLongClick: ((Offset) -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    haptic: HapticFeedback = LocalHapticFeedback.current,
    onSizeChange: (IntSize) -> Unit = {},
    content: @Composable RowScope.() -> Unit
) {
    val localOnLongClick = remember<(Offset) -> Unit>(onLongClick, haptic) {
        { offset ->
            if (onLongClick != null) {
                onLongClick(offset)
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        }
    }
    val onPress = remember<suspend PressGestureScope.(Offset) -> Unit> {
        {
            val press = PressInteraction.Press(it)
            interactionSource.emit(press)
            tryAwaitRelease()
            interactionSource.emit(PressInteraction.Release(press))
        }
    }

    Row(modifier = modifier
        .heightIn(height)
        .onSizeChanged(onSizeChange)
        .fillMaxWidth()
        .padding(horizontal = 8.dp)
        .clip(MaterialTheme.shapes.small)
        .indication(interactionSource = interactionSource, LocalIndication.current)
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = onPress,
                onTap = { onClick() },
                onLongPress = localOnLongClick
            )
        }
        .padding(start = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content)
}