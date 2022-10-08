package com.leebeebeom.clothinghelper.ui.maincategory.subCategory

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.leebeebeom.clothinghelper.data.TextFieldState

class SubCategoryViewModel : ViewModel() {
    val subCategories = mutableStateListOf("반팔 티셔츠", "긴팔 티셔츠", "셔츠", "반팔 셔츠", "니트", "반팔 니트", "니트 베스트")

    val newCategoryTextFieldState by mutableStateOf(
        TextFieldState().apply {
            onValueChange = {
                onValueChange(it)
                if (subCategories.contains(it)) errorEnable(TextFieldState.TextFieldError.ERROR_SAME_CATEGORY_NAME)
            }
        }
    )
}