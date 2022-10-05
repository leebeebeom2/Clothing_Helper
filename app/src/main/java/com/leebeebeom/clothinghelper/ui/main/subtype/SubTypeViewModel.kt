package com.leebeebeom.clothinghelper.ui.main.subtype

import androidx.lifecycle.ViewModel

class SubTypeViewModel : ViewModel() {
    val subTypes = getInitialSubTypes()

    private fun getInitialSubTypes(): MutableList<String> =
        mutableListOf("반팔 티셔츠", "긴팔 티셔츠", "셔츠", "반팔 셔츠", "니트", "반팔 니트", "니트 베스트")
}