package com.leebeebeom.clothinghelper.main.base.interfaces

import androidx.annotation.StringRes
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import kotlinx.coroutines.TimeoutCancellationException

interface BaseContainerAddAndEdit {
    fun showToast(@StringRes text: Int)
    val uid: String?

    fun showFailToast(
        result: FirebaseResult,
        @StringRes networkFail: Int,
        @StringRes fail: Int
    ) {
        if (result is FirebaseResult.Fail)
            when (result.exception) {
                is TimeoutCancellationException -> showToast(networkFail)
                else -> showToast(fail)
            }
    }
}