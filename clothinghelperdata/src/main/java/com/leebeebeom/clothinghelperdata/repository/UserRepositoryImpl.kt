package com.leebeebeom.clothinghelperdata.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.leebeebeom.clothinghelperdata.datasource.UserRemoteDataSource
import com.leebeebeom.clothinghelperdomain.model.User
import com.leebeebeom.clothinghelperdomain.repository.FireBaseListeners
import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserRepositoryImpl(private val userRemoteDataSource: UserRemoteDataSource) : UserRepository {
    private val auth = FirebaseAuth.getInstance()

    private var user = MutableStateFlow(User())

    override suspend fun getUser(): StateFlow<User> {
        collectUser()
        return user.asStateFlow()
    }

    private suspend fun collectUser(): StateFlow<User> {
        userRemoteDataSource.user.collect {
            user.value = if (it == null) User()
            else User(isLogin = true, email = it.email ?: "", it.displayName ?: "", it.uid)
        }
    }

    override fun signIn(
        email: String, password: String, signInListener: FireBaseListeners.SignInListener
    ) {
        signInListener.taskStart()

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) signInListener.taskSuccess(it.result)
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

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                signUpListener.taskSuccess(it.result)
                val user = it.result.user
                if (user == null) updateNameListener.userNull()
                else updateName(updateNameListener, user, name)
            } else signUpListener.taskFailed(it.exception)
            signUpListener.taskFinish()
        }
    }

    private fun updateName(
        updateNameListener: FireBaseListeners.UpdateNameListener, user: FirebaseUser, name: String
    ) {
        updateNameListener.taskStart()

        val request = userProfileChangeRequest { displayName = name }

        user.updateProfile(request).addOnCompleteListener {
            if (it.isSuccessful) {
                updateNameListener.taskSuccess(null)
                _name.value = name
            } else updateNameListener.nameUpdateFailed()
            updateNameListener.taskFinish()
        }
    }

    override fun googleSignIn(
        googleCredential: Any?, googleSignInListener: FireBaseListeners.GoogleSignInListener
    ) {
        googleSignInListener.taskStart()
        if (googleCredential is AuthCredential) signInWithCredential(
            googleCredential,
            googleSignInListener
        )

    }

    private fun signInWithCredential(
        googleCredential: AuthCredential?,
        googleSignInListener: FireBaseListeners.GoogleSignInListener
    ) {
        googleCredential?.let { credential ->

            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                if (it.isSuccessful) googleSignInListener.taskSuccess(it.result)
                else googleSignInListener.taskFailed(it.exception)
                googleSignInListener.taskFinish()
            }
        }
    }

    override fun resetPasswordEmail(
        email: String, resetPasswordListener: FireBaseListeners.ResetPasswordListener
    ) {
        resetPasswordListener.taskStart()

        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) resetPasswordListener.taskSuccess(null)
            else resetPasswordListener.taskFailed(it.exception)
            resetPasswordListener.taskFinish()
        }
    }

    override fun nameUpdate(name: String) {
        this._name.value = name
    }
}