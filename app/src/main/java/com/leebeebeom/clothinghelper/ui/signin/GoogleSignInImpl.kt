package com.leebeebeom.clothinghelper.ui.signin

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.leebeebeom.clothinghelper.R

interface GoogleSignInImpl {
    fun loadingOn()
    fun loadingOff()
    fun showToast(resId: Int)

    /*
    런쳐 런치 -> 구글 로그인 액티비티 띄운 후 결과 반환
    반환된 결과 Intent에 담겨서 googleSignIn 호출
    반환된 결과로 구글 크레덴셜 흭득
    구글 크레덴셜로 파이어베이스 크레덴셜 로그인
    gso는 구글 로그인 인텐트를 얻기 위해 필요
    구글 로그인 인텐트는 런쳐를 런치하기 위해 필요
     */
    fun getGso(webClientId: String) =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()

    fun googleSignInLauncherLaunch(
        googleSingInIntent: Intent,
        launcher: ManagedActivityResultLauncher<Intent, *>
    ) {
        loadingOn()
        showToast(R.string.wait_please)
        launcher.launch(googleSingInIntent)
    }

    fun googleSignIn(activityResult: ActivityResult) {
        val googleCredential = getGoogleCredential(activityResult.data)

        googleCredential?.let { credential ->
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener {
                    if (it.isSuccessful) showToast(R.string.google_login_complete)
                    else showToast(R.string.google_sign_in_failed)
                    loadingOff()
                }
        }
    }

    private fun getGoogleCredential(data: Intent?): AuthCredential? {
        return if (data == null) {
            showToast(R.string.google_sign_in_failed)
            null
        } else {
            val account =
                GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException::class.java)
            return GoogleAuthProvider.getCredential(account.idToken, null)
        }
    }
}