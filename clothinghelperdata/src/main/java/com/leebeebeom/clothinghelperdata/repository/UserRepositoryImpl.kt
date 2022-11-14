package com.leebeebeom.clothinghelperdata.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.leebeebeom.clothinghelperdomain.model.*
import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
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

    override suspend fun googleSignIn(credential: Any?): AuthResult {
        return withContext(Dispatchers.IO) {
            try {
                loadingOn()
                delay(5000)
                val authCredential = credential as AuthCredential

                val authResult = auth.signInWithCredential(authCredential).await()

                val user = authResult.user.toUser()!!

                val isNewer = authResult.additionalUserInfo!!.isNewUser

                if (isNewer) pushNewUser(user)
                else updateUserAndUpdateSignIn(user)

                AuthResult.Success(user, isNewer)
            } catch (e: Exception) {
                AuthResult.Fail(e)
            } finally {
                loadingOff()
            }
        }
    }

    override suspend fun signIn(signIn: SignIn): AuthResult {
        return withContext(Dispatchers.IO) {
            try {
                loadingOn()

                val user = auth.signInWithEmailAndPassword(signIn.email, signIn.password)
                    .await().user.toUser()!!
                updateUserAndUpdateSignIn(user)
                AuthResult.Success(user, false)
            } catch (e: Exception) {
                AuthResult.Fail(e)
            } finally {
                loadingOff()

            }
        }
    }

    override suspend fun signUp(signUp: SignUp): AuthResult {
        return withContext(Dispatchers.IO) {
            try {
                loadingOn()

                val user = auth.createUserWithEmailAndPassword(signUp.email, signUp.password)
                    .await().user!!

                val request = userProfileChangeRequest { displayName = signUp.name }
                user.updateProfile(request).await()

                val userObj = user.toUser()!!.copy(name = signUp.name)

                pushNewUser(userObj)

                AuthResult.Success(user = userObj, isNewer = true)
            } catch (e: Exception) {
                AuthResult.Fail(e)
            } finally {
                loadingOff()
            }
        }
    }

    override suspend fun resetPasswordEmail(email: String): FirebaseResult {
        return withContext(Dispatchers.IO) {
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
    }

    override suspend fun signOut() {
        withContext(Dispatchers.IO) { auth.signOut() }
        updateSignIn(false)
        updateUser(null)
    }

    private suspend fun pushNewUser(user: User) {
        withContext(Dispatchers.IO) {
            FirebaseDatabase.getInstance().reference.child(user.uid)
                .child(DatabasePath.USER_INFO).setValue(user).await()
            updateUserAndUpdateSignIn(user)
        }
    }

    private suspend fun updateUserAndUpdateSignIn(userObj: User) = withContext(Dispatchers.Main) {
        updateUser(user = userObj)
        updateSignIn(state = true)
    }

    private fun updateSignIn(state: Boolean) {
        _isSignIn.value = state
    }

    private fun updateUser(user: User?) {
        this._user.value = user
    }

    private fun loadingOn() {
        _isLoading.value = true
    }


    private suspend fun loadingOff() {
        withContext(NonCancellable) {
            _isLoading.value = false
        }
    }
}

fun FirebaseUser?.toUser() = this?.let { User(email!!, displayName ?: "", uid) }