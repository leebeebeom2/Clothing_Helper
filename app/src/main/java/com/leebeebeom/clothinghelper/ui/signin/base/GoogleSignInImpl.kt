package com.leebeebeom.clothinghelper.ui.signin.base

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.annotation.StringRes
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.leebeebeom.clothinghelper.R


interface GoogleSignInImpl {
    /**
     * loadingOff
     * googleButtonEnabled true
     * showToast
     */
    fun googleSignInTaskFinished(@StringRes toastText: Int)

    /**
     * loadingOn
     * googleButtonEnabled false
     */
    fun googleSignInTaskStart()

    /*
    런쳐 런치 -> 구글 로그인 액티비티 띄운 후 결과 반환
    반환된 결과 Intent에 담겨서 googleSignIn 호출
    반환된 결과로 구글 크레덴셜 흭득
    구글 크레덴셜로 파이어베이스 크레덴셜 로그인
    gso는 구글 로그인 인텐트를 얻기 위해 필요
    구글 로그인 인텐트는 런쳐를 런치하기 위해 필요
     */
    fun googleSignIn(activityResult: ActivityResult) {
        when (activityResult.resultCode) {
            RESULT_CANCELED -> {
                googleSignInTaskFinished(R.string.canceled)
            }
            RESULT_OK -> {
                signInWithCredential(activityResult)
            }
            else -> {
                googleSignInTaskFinished(R.string.unknown_error)
            }
        }
    }

    private fun signInWithCredential(activityResult: ActivityResult) {
        getGoogleCredential(activityResult.data)?.let { credential ->

            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener {
                    if (it.isSuccessful) googleSignInTaskFinished(R.string.google_login_complete)
                    else googleSignInTaskFinished(R.string.google_sign_in_failed)
                }

        }
    }

    private fun getGoogleCredential(resultData: Intent?): AuthCredential? {
        return if (resultData == null) {
            googleSignInTaskFinished(R.string.google_sign_in_failed)
            null
        } else {
            val account =
                GoogleSignIn.getSignedInAccountFromIntent(resultData)
                    .getResult(ApiException::class.java)
            return GoogleAuthProvider.getCredential(account.idToken, null)
        }
    }

    fun googleSignInLauncherLaunch(
        googleSingInIntent: Intent,
        launcher: ManagedActivityResultLauncher<Intent, *>
    ) {
        googleSignInTaskStart()
        launcher.launch(googleSingInIntent)
    }
}