package com.leebeebeom.clothinghelper.ui.state

import kotlinx.collections.immutable.ImmutableList

interface ToastUiState {
    val toastTexts: ImmutableList<Int>
}

interface LoadingUiState {
    val isLoading: Boolean
}