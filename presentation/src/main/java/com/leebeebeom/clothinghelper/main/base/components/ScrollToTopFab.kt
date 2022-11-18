package com.leebeebeom.clothinghelper.main.base.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.composables.SimpleIcon

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BoxScope.ScrollToTopFab(showFab: () -> Boolean, onClick: () -> Unit) {
    AnimatedVisibility(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(4.dp),
        visible = showFab(),
        enter = scaleIn(
            spring(stiffness = Spring.StiffnessMedium),
            transformOrigin = TransformOrigin(0.5f, 0.5f)
        ),
        exit = scaleOut(
            animationSpec = spring(stiffness = Spring.StiffnessMedium),
            transformOrigin = TransformOrigin(0.5f, 0.5f)
        )
    ) {
        FloatingActionButton(
            onClick = onClick,
            backgroundColor = MaterialTheme.colors.surface,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .size(36.dp),
            elevation = FloatingActionButtonDefaults.elevation(4.dp, 10.dp, 6.dp, 6.dp)
        ) {
            SimpleIcon(drawable = R.drawable.ic_expand_less)
        }
    }
}