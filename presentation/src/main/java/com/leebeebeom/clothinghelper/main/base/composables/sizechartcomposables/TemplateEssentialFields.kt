package com.leebeebeom.clothinghelper.main.base.composables.sizechartcomposables

import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.R

@Composable
fun TopTemplateFields() {
    EssentialFieldsRoot(
        { SizeChartTemplateTextField(label = R.string.length) },
        { SizeChartTemplateTextField(label = R.string.shoulder) },
        { SizeChartTemplateTextField(label = R.string.chest) },
        { SizeChartTemplateTextField(label = R.string.sleeve) },
    )
}

@Composable
fun BottomTemplateFields() {
    EssentialFieldsRoot(
        { SizeChartTemplateTextField(label = R.string.length) },
        { SizeChartTemplateTextField(label = R.string.waist_cm) },
        { SizeChartTemplateTextField(label = R.string.rise) },
        { SizeChartTemplateTextField(label = R.string.thigh) },
        { SizeChartTemplateTextField(label = R.string.hem) },
    )
}