package com.leebeebeom.clothinghelper.ui.state

import kotlinx.collections.immutable.ImmutableList

interface ToastState {
    val toastTexts: ImmutableList<Int>
}

interface LoadingState2 {
    val isLoading: Boolean
}