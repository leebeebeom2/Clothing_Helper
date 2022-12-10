package com.leebeebeom.clothinghelper.ui.main.interfaces.addandedit

import androidx.annotation.StringRes
import com.leebeebeom.clothinghelper.state.ToastUIState
import com.leebeebeom.clothinghelper.ui.main.interfaces.UserUIState
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import kotlinx.coroutines.TimeoutCancellationException

interface BaseContainerAddAndEdit {
    val toastState: ToastUIState
    val userState: UserUIState
    val uid get() = userState.user?.uid

    fun showFailToast(
        result: FirebaseResult,
        @StringRes networkFailError: Int,
        @StringRes failError: Int
    ) {
        if (result is FirebaseResult.Fail)
            when (result.exception) {
                is TimeoutCancellationException -> toastState.showToast(networkFailError)
                else -> toastState.showToast(failError)
            }
    }
}