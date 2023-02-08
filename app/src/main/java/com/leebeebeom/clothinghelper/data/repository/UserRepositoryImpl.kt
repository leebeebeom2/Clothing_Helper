package com.leebeebeom.clothinghelper.data.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.leebeebeom.clothinghelper.util.LoadingStateProviderImpl
import com.leebeebeom.clothinghelper.data.repository.util.AuthCallSite
import com.leebeebeom.clothinghelper.data.repository.util.logE
import com.leebeebeom.clothinghelper.domain.model.AuthResult
import com.leebeebeom.clothinghelper.domain.model.AuthResult.*
import com.leebeebeom.clothinghelper.domain.model.data.User
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import com.leebeebeom.clothinghelper.ui.util.FirebaseAuthErrorCode.A_NETWORK_ERROR
import com.leebeebeom.clothinghelper.ui.util.FirebaseAuthErrorCode.TOO_MANY_REQUEST
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor() : UserRepository {
    private val loadingStateImpl = LoadingStateProviderImpl(false)

    override val isLoading get() = loadingStateImpl.isLoading
    private val auth = FirebaseAuth.getInstance()

    private val _isSignIn = MutableStateFlow(auth.currentUser != null)
    override val isSignIn = _isSignIn.asStateFlow()

    private val _user = MutableStateFlow(auth.currentUser.toUser())
    override val user = _user.asStateFlow()

    init {
        auth.addAuthStateListener {
            val currentUser = it.currentUser
            _isSignIn.update { currentUser != null }
            _user.update { currentUser.toUser() }
        }
    }

    override suspend fun googleSignIn(credential: AuthCredential): AuthResult =
        authTry(callSite = AuthCallSite("googleSignIn")) {

            val authResult = auth.signInWithCredential(credential).await()

            val user = authResult.user.toUser()!!

            val isNewer = authResult.additionalUserInfo!!.isNewUser

            if (isNewer) pushNewUser(user)
            else updateUserAndUpdateSignIn(user)

            Success(user = user, isNewer = isNewer)
        }

    override suspend fun signIn(email: String, password: String) =
        authTry(callSite = AuthCallSite("signIn")) {
            val user = auth.signInWithEmailAndPassword(email, password).await().user.toUser()!!
            updateUserAndUpdateSignIn(user)
            Success(user = user, isNewer = false)
        }

    override suspend fun signUp(email: String, password: String, name: String) =
        authTry(callSite = AuthCallSite("signUp")) {
            val firebaseUser = auth.createUserWithEmailAndPassword(email, password).await().user!!

            val request = userProfileChangeRequest { displayName = name }
            firebaseUser.updateProfile(request).await()

            val user = firebaseUser.toUser()!!.copy(name = name)

            pushNewUser(user)

            Success(user = user, isNewer = true)
        }

    override suspend fun resetPasswordEmail(email: String) =
        authTry(callSite = AuthCallSite("resetPasswordEmail")) {
            auth.sendPasswordResetEmail(email).await()
            EmptySuccess
        }

    override suspend fun signOut() = authTry(callSite = AuthCallSite("signOut")) {
        auth.signOut()
        updateUser(null)
        updateSignIn(false)
        EmptySuccess
    }

    private suspend fun pushNewUser(user: User) = withContext(Dispatchers.IO) {
        FirebaseDatabase.getInstance().reference.child(user.uid).child(DatabasePath.USER_INFO)
            .setValue(user)
        updateUserAndUpdateSignIn(user)
    }

    private suspend fun updateUserAndUpdateSignIn(user: User) = withContext(NonCancellable) {
        updateUser(user = user)
        updateSignIn(state = true)
    }

    private fun updateSignIn(state: Boolean) = _isSignIn.update { state }
    private fun updateUser(user: User?) = _user.update { user }

    private suspend fun authTry(
        callSite: AuthCallSite,
        task: suspend () -> AuthResult,
    ) = withContext(context = Dispatchers.IO) {
        try {
            loadingStateImpl.loadingOn()
            withContext(context = NonCancellable) { task() }
        } catch (e: FirebaseAuthException) {
            logE(site = callSite.site, e = e)
            Fail(errorCode = e.errorCode)
        } catch (e: FirebaseNetworkException) {
            Fail(errorCode = A_NETWORK_ERROR)
        } catch (e: FirebaseTooManyRequestsException) {
            Fail(errorCode = TOO_MANY_REQUEST)
        } catch (e: Exception) {
            logE(site = callSite.site, e = e)
            UnknownFail
        } finally {
            loadingStateImpl.loadingOff()
        }
    }

    private fun FirebaseUser?.toUser() = this?.let {
        User(
            email = email!!, name = displayName!!, uid = uid
        )
    }
}