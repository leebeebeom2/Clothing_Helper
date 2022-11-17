package com.leebeebeom.clothinghelperdata.repository.util

import android.util.Log

const val TAG = "TAG"

fun logE(site: String, e: Throwable) {
    Log.e(TAG, "site: $site", e)
}