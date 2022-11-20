package com.leebeebeom.clothinghelperdata.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.leebeebeom.clothinghelperdata.repository.base.DatabasePath
import com.leebeebeom.clothinghelperdata.repository.base.LoadingRepositoryImpl
import com.leebeebeom.clothinghelperdata.repository.util.logE
import com.leebeebeom.clothinghelperdomain.model.AuthResult
import com.leebeebeom.clothinghelperdomain.model.User
import com.leebeebeom.clothinghelperdomain.repository.LoadingRepository
import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

const val A_NETWORK_ERROR = "A_NETWORK_ERROR"
const val TOO_MANY_REQUEST = "TOO_MANY_REQUEST"

private val loadingRepositoryImpl = LoadingRepositoryImpl(false)

@Singleton
class UserRepositoryImpl @Inject constructor() :
    LoadingRepository by loadingRepositoryImpl, UserRepository {
    private val auth = FirebaseAuth.getInstance()

    private val _isSignIn = MutableStateFlow(auth.currentUser != null)
    override val isSignIn get() = _isSignIn.asStateFlow()

    private val _user = MutableStateFlow(auth.currentUser.toUser())
    override val user get() = _user.asStateFlow()

    init {
        auth.addAuthStateListener {
            val currentUser = it.currentUser
            _user.update { currentUser.toUser() }
            _isSignIn.update { currentUser != null }
        }
    }

    override suspend fun googleSignIn(credential: Any?): AuthResult =
        authTry("googleSignIn") {
            val authCredential = credential as AuthCredential

            val authResult = auth.signInWithCredential(authCredential).await()

            val user = authResult.user.toUser()!!

            val isNewer = authResult.additionalUserInfo!!.isNewUser

            if (isNewer) pushNewUser(user)
            else updateUserAndUpdateSignIn(user)

            AuthResult.Success(user, isNewer)
        }

    override suspend fun signIn(email: String, password: String) =
        authTry("signIn") {
            val user = auth.signInWithEmailAndPassword(email, password)
                .await().user.toUser()!!
            updateUserAndUpdateSignIn(user)
            AuthResult.Success(user, false)
        }

    override suspend fun signUp(email: String, password: String, name: String) =
        authTry("signUp") {
            val user = auth.createUserWithEmailAndPassword(email, password)
                .await().user!!

            val request = userProfileChangeRequest { displayName = name }
            user.updateProfile(request).await()

            val userObj = user.toUser()!!.copy(name = name)

            pushNewUser(userObj)

            AuthResult.Success(userObj, true)
        }

    override suspend fun resetPasswordEmail(email: String) =
        authTry("resetPasswordEmail") {
            auth.sendPasswordResetEmail(email).await()
            AuthResult.Success()
        }

    override suspend fun signOut() =
        authTry("signOut") {
            auth.signOut()
            updateSignIn(false)
            updateUser(null)
            AuthResult.Success()
        }

    // 이 단계에서 인터넷이 끊겨도 인터넷 복구 시 푸쉬 됨
    private suspend fun pushNewUser(user: User) =
        withContext(Dispatchers.IO) {
            FirebaseDatabase.getInstance().reference.child(user.uid).child(DatabasePath.USER_INFO)
                .setValue(user)
            updateUserAndUpdateSignIn(user)
        }

    private fun updateUserAndUpdateSignIn(userObj: User) {
        updateUser(user = userObj)
        updateSignIn(state = true)
    }

    private fun updateSignIn(state: Boolean) = _isSignIn.update { state }
    private fun updateUser(user: User?) = _user.update { user }

    private suspend fun authTry(site: String, task: suspend () -> AuthResult) =
        withContext(Dispatchers.IO) {
            try {
                loadingRepositoryImpl.loadingOn()
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
                loadingRepositoryImpl.loadingOff()
            }
        }
}

private fun FirebaseUser?.toUser() = this?.let { User(email!!, displayName ?: "", uid) }