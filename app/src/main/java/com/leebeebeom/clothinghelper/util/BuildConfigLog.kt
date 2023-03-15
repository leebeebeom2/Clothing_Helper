package com.leebeebeom.clothinghelper.util

import android.util.Log
import com.leebeebeom.clothinghelper.BuildConfig

const val Tag = "태그"

fun buildConfigLog(site: String, msg: String = "", throwable: Throwable? = null) {
    if (BuildConfig.DEBUG) {
        throwable?.let {
            Log.e(Tag, "$site: $msg", throwable)
        }
        Log.d(Tag, "$site: $msg")
    }
}