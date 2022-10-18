package com.leebeebeom.clothinghelper.data.repository

import android.app.Activity
import androidx.activity.result.ActivityResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.leebeebeom.clothinghelper.domain.repository.FireBaseListeners
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserRepositoryImpl : UserRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()

    private val _user = MutableStateFlow(firebaseAuth.currentUser)
    override val user: StateFlow<FirebaseUser?>
        get() = _user
    private val _isLogin = MutableStateFlow(user.value != null)
    override val isLogin: StateFlow<Boolean>
        get() = _isLogin
    private val _name = MutableStateFlow(user.value?.displayName ?: "")
    override val name: StateFlow<String>
        get() = _name
    private val _email = MutableStateFlow(user.value?.email ?: "")
    override val email: StateFlow<String>
        get() = _email
    private val _uid = MutableStateFlow(user.value?.uid ?: "")
    override val uid: StateFlow<String>
        get() = _uid

    init {
        FirebaseAuth.getInstance().addAuthStateListener {
            val user = it.currentUser
            _user.value = user
            _isLogin.value = user != null
            user?.run {
                _name.value = displayName ?: ""
                _email.value = email ?: ""
                _uid.value = uid
            }
        }
    }

    override fun signIn(
        email: String, password: String, signInListener: FireBaseListeners.SignInListener
    ) {
        signInListener.taskStart()

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) signInListener.taskSuccess()
            else signInListener.taskFailed(it.exception)
            signInListener.taskFinish()
        }
    }

    override fun signUp(
        email: String,
        password: String,
        name: String,
        signUpListener: FireBaseListeners.SignUpListener,
        updateNameListener: FireBaseListeners.UpdateNameListener
    ) {
        signUpListener.taskStart()

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                signUpListener.taskSuccess()
                val user = it.result.user
                if (user == null) updateNameListener.userNull()
                else updateName(updateNameListener, user, name)
            } else signUpListener.taskFailed(it.exception)
            signUpListener.taskFinish()
        }
    }

    private fun updateName(
        updateNameListener: FireBaseListeners.UpdateNameListener, user: FirebaseUser,
        name: String
    ) {
        updateNameListener.taskStart()

        val request = userProfileChangeRequest { displayName = name }

        user.updateProfile(request).addOnCompleteListener {
            if (it.isSuccessful) {
                updateNameListener.taskSuccess()
                _name.value = name
            } else updateNameListener.nameUpdateFailed()
            updateNameListener.taskFinish()
        }
    }

    override fun googleSignIn(
        activityResult: ActivityResult, googleSignInListener: FireBaseListeners.GoogleSignInListener
    ) {
        googleSignInListener.taskStart()

        when (activityResult.resultCode) {
            Activity.RESULT_OK -> {
                signInWithCredential(activityResult, googleSignInListener)
            }
            else -> {
                googleSignInListener.googleSignInFailed(activityResult)
            }
        }
    }

    private fun signInWithCredential(
        activityResult: ActivityResult, googleSignInListener: FireBaseListeners.GoogleSignInListener
    ) {
        getGoogleCredential(activityResult, googleSignInListener)?.let { credential ->

            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                if (it.isSuccessful) googleSignInListener.taskSuccess()
                else googleSignInListener.taskFailed(it.exception)
                googleSignInListener.taskFinish()
            }
        }
    }

    private fun getGoogleCredential(
        activityResult: ActivityResult, googleSignInListener: FireBaseListeners.GoogleSignInListener
    ): AuthCredential? {
        return if (activityResult.data == null) {
            googleSignInListener.googleSignInFailed(activityResult)
            null
        } else {
            val account = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
                .getResult(ApiException::class.java)
            return GoogleAuthProvider.getCredential(account.idToken, null)
        }
    }

    override fun resetPasswordEmail(
        email: String, resetPasswordListener: FireBaseListeners.ResetPasswordListener
    ) {
        resetPasswordListener.taskStart()

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) resetPasswordListener.taskSuccess()
            else resetPasswordListener.taskFailed(it.exception)
            resetPasswordListener.taskFinish()
        }
    }

    override fun nameUpdate(name: String) {
        this._name.value = name
    }
}