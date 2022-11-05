package com.leebeebeom.clothinghelperdata.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.leebeebeom.clothinghelperdomain.model.*
import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepositoryImpl : UserRepository {
    private val auth = FirebaseAuth.getInstance()

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _isSignIn = MutableStateFlow(auth.currentUser != null)
    override val isSignIn: StateFlow<Boolean> get() = _isSignIn

    private val _user = MutableStateFlow(auth.currentUser.toUser())
    override val user: StateFlow<User?> get() = _user

    override suspend fun googleSignIn(credential: Any?): AuthResult = withContext(Dispatchers.IO) {
        try {
            val authCredential = credential as AuthCredential

            loadingOn()

            val authResult = auth.signInWithCredential(authCredential).await()

            val userObj = authResult.user.toUser()!!

            val pushResult = pushNewUser(userObj)
            if (pushResult is FirebaseResult.Fail) return@withContext AuthResult.Fail(pushResult.exception)
            AuthResult.Success(userObj, authResult.additionalUserInfo!!.isNewUser)
        } catch (e: Exception) {
            AuthResult.Fail(e)
        } finally {
            loadingOff()
        }
    }

    override suspend fun signIn(signIn: SignIn) = withContext(Dispatchers.IO) {
        try {
            loadingOn()

            val authResult = auth.signInWithEmailAndPassword(signIn.email, signIn.password).await()
            val userObj = authResult.user.toUser()!!
            updateUserAndUpdateSignIn(userObj)
            AuthResult.Success(userObj, false)
        } catch (e: Exception) {
            AuthResult.Fail(e)
        } finally {
            loadingOff()
        }
    }

    override suspend fun signUp(signUp: SignUp) = withContext(Dispatchers.IO) {
        try {
            loadingOn()

            val authResult =
                auth.createUserWithEmailAndPassword(signUp.email, signUp.password).await()

            val user = authResult.user!! // update display name
            val request = userProfileChangeRequest { displayName = signUp.name }
            user.updateProfile(request).await()

            val userObj = user.toUser()!!.copy(name = signUp.name)

            val pushResult = pushNewUser(userObj)
            if (pushResult is FirebaseResult.Fail) return@withContext AuthResult.Fail(pushResult.exception)
            AuthResult.Success(user = userObj, isNewer = true)
        } catch (e: Exception) {
            AuthResult.Fail(e)
        } finally {
            loadingOff()
        }
    }

    override suspend fun resetPasswordEmail(email: String) = withContext(Dispatchers.IO) {
        try {
            loadingOn()
            auth.sendPasswordResetEmail(email).await()
            FirebaseResult.Success
        } catch (e: Exception) {
            FirebaseResult.Fail(e)
        } finally {
            loadingOff()
        }
    }

    override suspend fun signOut() {
        auth.signOut()
        updateSignIn(false)
        updateUser(null)
    }

    private suspend fun pushNewUser(user: User) = withContext(Dispatchers.IO) {
        try {
            FirebaseDatabase.getInstance().reference.child(user.uid).child(DatabasePath.USER_INFO)
                .setValue(user).await()
            updateUserAndUpdateSignIn(user)
            FirebaseResult.Success
        } catch (e: Exception) {
            FirebaseResult.Fail(e)
        }
    }

    private fun updateUserAndUpdateSignIn(userObj: User) {
        updateUser(user = userObj)
        updateSignIn(state = true)
    }

    private fun updateSignIn(state: Boolean) {
        _isSignIn.value = state
    }

    private fun updateUser(user: User?) {
        this._user.value = user
    }

    private suspend fun loadingOn() = withContext(Dispatchers.Main) {
        _isLoading.value = true
    }


    private suspend fun loadingOff() = withContext(Dispatchers.Main) {
        _isLoading.value = false
    }
}

fun FirebaseUser?.toUser() = this?.let { User(email!!, displayName ?: "", uid) }