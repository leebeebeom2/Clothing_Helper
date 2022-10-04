package com.leebeebeom.clothinghelper.ui.signin

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider

interface CHGoogleSignIn {
    var isFirebaseTaskSuccessful: Boolean
    var progressionOn: Boolean
    val onCompleteListener: (Task<*>) -> Unit

    fun getGso(webClientId: String) =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()

    fun signInWithCredential(intent: Intent?) {
        intent?.let {
            val account =
                GoogleSignIn.getSignedInAccountFromIntent(intent)
                    .getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            CHFirebase(onCompleteListener).signInWithCredential(credential)
        }
    }

    fun launch(
        googleSignInIntent: Intent,
        launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
    ) {
        progressionOn = true
        launcher.launch(googleSignInIntent)
        progressionOn = false
    }
}