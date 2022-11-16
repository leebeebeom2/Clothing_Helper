package com.leebeebeom.clothinghelperdata.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.leebeebeom.clothinghelperdata.repository.base.BaseRepository
import com.leebeebeom.clothinghelperdomain.model.AuthResult
import com.leebeebeom.clothinghelperdomain.model.SignIn
import com.leebeebeom.clothinghelperdomain.model.SignUp
import com.leebeebeom.clothinghelperdomain.model.User
import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import com.leebeebeom.clothinghelperdomain.util.logE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

const val A_NETWORK_ERROR = "A_NETWORK_ERROR"
const val TOO_MANY_REQUEST = "TOO_MANY+REQUEST"

@Singleton
class UserRepositoryImpl @Inject constructor() : BaseRepository(false), UserRepository {
    private val auth = FirebaseAuth.getInstance()

    private val _isSignIn = MutableStateFlow(auth.currentUser != null)
    override val isSignIn get() = _isSignIn.asStateFlow()

    private val _user = MutableStateFlow(auth.currentUser.toUser())
    override val user get() = _user.asStateFlow()

    override suspend fun googleSignIn(credential: Any?): AuthResult {
        return authTry("googleSignIn") {
            val authCredential = credential as AuthCredential

            val authResult = auth.signInWithCredential(authCredential).await()

            val user = authResult.user.toUser()!!

            val isNewer = authResult.additionalUserInfo!!.isNewUser

            if (isNewer) pushNewUser(user)
            else updateUserAndUpdateSignIn(user)

            AuthResult.Success(user, isNewer)
        }
    }

    override suspend fun signIn(signIn: SignIn): AuthResult {
        return authTry("signIn") {
            val user = auth.signInWithEmailAndPassword(signIn.email, signIn.password)
                .await().user.toUser()!!
            updateUserAndUpdateSignIn(user)
            AuthResult.Success(user, false)
        }
    }

    override suspend fun signUp(signUp: SignUp): AuthResult {
        return authTry("signUp") {
            val user = auth.createUserWithEmailAndPassword(signUp.email, signUp.password)
                .await().user!!

            val request = userProfileChangeRequest { displayName = signUp.name }
            user.updateProfile(request).await()

            val userObj = user.toUser()!!.copy(name = signUp.name)

            pushNewUser(userObj)

            AuthResult.Success(user = userObj, isNewer = true)
        }
    }

    override suspend fun resetPasswordEmail(email: String): AuthResult {
        return authTry("resetPasswordEmail") {
            auth.sendPasswordResetEmail(email).await()
            AuthResult.Success()
        }
    }

    override suspend fun signOut() {
        withContext(Dispatchers.IO) { auth.signOut() }
        updateSignIn(false)
        updateUser(null)
    }

    private suspend fun pushNewUser(user: User) {
        withContext(Dispatchers.IO) {
            FirebaseDatabase.getInstance().reference.child(user.uid).child(DatabasePath.USER_INFO)
                .setValue(user).await()
            updateUserAndUpdateSignIn(user)
        }
    }

    private suspend fun updateUserAndUpdateSignIn(userObj: User) {
        withContext(Dispatchers.Main) {
            updateUser(user = userObj)
            updateSignIn(state = true)
        }
    }

    private fun updateSignIn(state: Boolean) {
        _isSignIn.value = state
    }

    private fun updateUser(user: User?) {
        this._user.value = user
    }

    private suspend fun authTry(site: String, task: suspend () -> AuthResult): AuthResult {
        return withContext(Dispatchers.IO) {
            try {
                loadingOn()
                task()
            } catch (e: FirebaseAuthException) {
                logE(site, e)
                AuthResult.Fail(e.errorCode)
            } catch (e: FirebaseNetworkException) {
                AuthResult.Fail(A_NETWORK_ERROR)
            } catch (e: FirebaseTooManyRequestsException) {
                AuthResult.Fail(TOO_MANY_REQUEST)
            } catch (e: Exception) {
                logE(site, e)
                AuthResult.UnknownFail
            } finally {
                loadingOff()
            }
        }
    }
}

fun FirebaseUser?.toUser() = this?.let { User(email!!, displayName ?: "", uid) }