package com.leebeebeom.clothinghelperdomain.util

import android.util.Log

const val TAG = "TAG"

fun logE(site: String, e: Throwable) {
    Log.e(TAG, "site: $site", e)
}