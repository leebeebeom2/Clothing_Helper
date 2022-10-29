package com.leebeebeom.clothinghelperdata.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.leebeebeom.clothinghelperdomain.model.User
import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener
import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener2
import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserRepositoryImpl : UserRepository {
    private val auth = FirebaseAuth.getInstance()

    private val _isSignIn = MutableStateFlow(auth.currentUser != null)
    override val isSignIn: StateFlow<Boolean> get() = _isSignIn

    private val _user = MutableStateFlow(auth.currentUser.toUser())
    override val user: StateFlow<User?> get() = _user

    override fun googleSignIn(
        credential: Any?,
        listener: FirebaseListener2,
        pushInitialSubCategories: (uid: String) -> Unit
    ) {
        val authCredential = credential as? AuthCredential
        if (authCredential == null) {
            listener.taskFailed(NullPointerException("googleCredential is null"))
            return
        }

        auth.signInWithCredential(authCredential).addOnCompleteListener {
            if (it.isSuccessful) {
                val userObj = it.result.user.toUser()!!
                if (it.result.additionalUserInfo!!.isNewUser)
                    pushFirstUser(userObj, pushInitialSubCategories)
                signInSuccess(userObj)
                listener.taskSuccess()
            } else listener.taskFailed(it.exception)
            listener.taskFinish()
        }
    }

    override fun signIn(
        email: String,
        password: String,
        signInListener: FirebaseListener,
        taskFinish: () -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = it.result.user.toUser()!!
                signInSuccess(user)
                signInListener.taskSuccess()
            } else signInListener.taskFailed(it.exception)
            taskFinish()
        }
    }

    override fun signUp(
        email: String,
        password: String,
        name: String,
        signUpListener: FirebaseListener,
        updateNameListener: FirebaseListener,
        pushInitialSubCategories: (uid: String) -> Unit,
        taskFinish: () -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val firebaseUser = it.result.user!!
                val user = firebaseUser.toUser()!!
                pushFirstUser(user, pushInitialSubCategories)
                signInSuccess(user)
                signUpListener.taskSuccess()
                updateName(updateNameListener, firebaseUser, name)
            } else signUpListener.taskFailed(it.exception)
            taskFinish()
        }
    }

    private fun updateName(updateNameListener: FirebaseListener, user: FirebaseUser, name: String) {
        val request = userProfileChangeRequest { displayName = name }

        user.updateProfile(request)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val newNameUser = user.toUser()?.copy(name = name)!!
                    pushUser(newNameUser)
                    updateUser(newNameUser)
                    updateNameListener.taskSuccess()
                } else updateNameListener.taskFailed(it.exception)
            }
    }

    override fun resetPasswordEmail(
        email: String,
        resetPasswordListener: FirebaseListener,
        taskFinish: () -> Unit
    ) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) resetPasswordListener.taskSuccess()
            else resetPasswordListener.taskFailed(it.exception)
            taskFinish()
        }
    }

    private fun pushFirstUser(user: User, writeInitialSubCategory: (String) -> Unit) {
        pushUser(user)
        writeInitialSubCategory(user.uid)
    }

    private fun pushUser(user: User) =
        FirebaseDatabase.getInstance().reference.child(user.uid)
            .child(DatabasePath.USER_INFO).setValue(user)

    override fun signOut() {
        auth.signOut()
        _isSignIn.value = false
        this._user.value = null
    }

    private fun signInSuccess(user: User) {
        _isSignIn.value = true
        updateUser(user)
    }

    private fun updateUser(user: User) {
        this._user.value = user
    }
}

fun FirebaseUser?.toUser() = this?.let { User(email!!, displayName ?: "", uid) }