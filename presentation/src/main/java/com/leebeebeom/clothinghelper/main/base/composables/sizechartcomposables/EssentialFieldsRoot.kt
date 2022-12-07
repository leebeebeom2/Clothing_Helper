package com.leebeebeom.clothinghelper.main.base.composables.sizechartcomposables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.leebeebeom.clothinghelper.base.composables.SimpleWidthSpacer
import kotlin.math.ceil

@Composable
fun EssentialFieldsRoot(
    vararg fields: @Composable () -> Unit
) {
    Column {
        val count = ceil(fields.size.toDouble() / 2).toInt()
        for (index in 0..count step 2)
            Row(Modifier.fillMaxWidth()) {
                fields.getOrNull(index)?.invoke()
                SimpleWidthSpacer(dp = 8)
                fields.getOrNull(index + 1)?.invoke()
            }
    }
}