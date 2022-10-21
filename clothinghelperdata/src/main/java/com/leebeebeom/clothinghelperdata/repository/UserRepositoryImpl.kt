package com.leebeebeom.clothinghelperdata.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.leebeebeom.clothinghelperdomain.model.User
import com.leebeebeom.clothinghelperdomain.repository.FireBaseListeners
import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class UserRepositoryImpl : UserRepository {
    private val auth = FirebaseAuth.getInstance()

    private lateinit var _isSignIn: MutableStateFlow<Boolean>
    override suspend fun isSignIn(): StateFlow<Boolean> = withContext(Dispatchers.IO) {
        if (!::_isSignIn.isInitialized) {
            _isSignIn = MutableStateFlow(false)
            auth.addAuthStateListener {
                _isSignIn.value = it.currentUser != null
            }
        }
        _isSignIn
    }

    private lateinit var user: MutableStateFlow<User?>
    override suspend fun getUser(): StateFlow<User?> = withContext(Dispatchers.IO) {
        if (!::user.isInitialized) {
            user = MutableStateFlow(null)
            auth.addAuthStateListener {
                user.value = it.currentUser.toUser()
            }
        }
        user
    }

    override suspend fun googleSignIn(
        googleCredential: Any?, googleSignInListener: FireBaseListeners.GoogleSignInListener
    ): Boolean = withContext(Dispatchers.IO) {
        var isFirstUser = false

        if (googleCredential is AuthCredential) FirebaseAuth.getInstance()
            .signInWithCredential(googleCredential).addOnCompleteListener {
                if (it.isSuccessful) {
                    googleSignInListener.taskSuccess()
                    it.result.additionalUserInfo?.isNewUser?.run { isFirstUser = this }
                } else googleSignInListener.taskFailed(it.exception)
            }
        else googleSignInListener.googleCredentialCastFailed()
        isFirstUser
    }

    override suspend fun signIn(
        email: String, password: String, signInListener: FireBaseListeners.SignInListener
    ): Unit = withContext(Dispatchers.IO) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) signInListener.taskSuccess()
            else signInListener.taskFailed(it.exception)
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        name: String,
        signUpListener: FireBaseListeners.SignUpListener,
        updateNameListener: FireBaseListeners.UpdateNameListener
    ): Unit = withContext(Dispatchers.IO) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                signUpListener.taskSuccess()
                // 이름 업데이트
                val user = it.result.user
                if (user == null) updateNameListener.userNull()
                else updateName(updateNameListener, user, name)
            } else signUpListener.taskFailed(it.exception)
        }
    }

    private fun updateName(
        updateNameListener: FireBaseListeners.UpdateNameListener,
        user: FirebaseUser,
        name: String
    ) {
        val request = userProfileChangeRequest { displayName = name }

        user.updateProfile(request).addOnCompleteListener {
            if (it.isSuccessful) {
                updateName(name)
                updateNameListener.taskSuccess()
            } else updateNameListener.nameUpdateFailed()
        }
    }

    private fun updateName(name: String) {
        user.value = user.value?.copy(name = name)
    }

    override suspend fun resetPasswordEmail(
        email: String, resetPasswordListener: FireBaseListeners.ResetPasswordListener
    ): Unit = withContext(Dispatchers.IO) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) resetPasswordListener.taskSuccess()
            else resetPasswordListener.taskFailed(it.exception)
        }
    }
}

fun FirebaseUser?.toUser() = this?.let { User(email!!, displayName!!, uid) }