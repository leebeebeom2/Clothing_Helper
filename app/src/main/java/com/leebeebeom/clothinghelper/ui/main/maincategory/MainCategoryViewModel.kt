package com.leebeebeom.clothinghelper.ui.main.maincategory

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.leebeebeom.clothinghelper.R

class MainCategoryViewModel : ViewModel() {
    var mainCategoryState by mutableStateOf(MainCategoryUIState())
        private set
}

data class MainCategoryUIState(
    @StringRes val mainCategories: List<Int> = listOf(
        R.string.top,
        R.string.bottom,
        R.string.outer,
        R.string.etc
    )
)