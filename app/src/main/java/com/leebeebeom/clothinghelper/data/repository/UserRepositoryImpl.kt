package com.leebeebeom.clothinghelper.data.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.leebeebeom.clothinghelper.data.repository.util.AuthCallSite
import com.leebeebeom.clothinghelper.data.repository.util.LoadingStateProviderImpl
import com.leebeebeom.clothinghelper.data.repository.util.logE
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import com.leebeebeom.clothinghelper.domain.model.FirebaseUser as FirebaseUserModel

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val appCoroutineScope: CoroutineScope,
) : UserRepository, LoadingStateProviderImpl(false) {
    private val auth = FirebaseAuth.getInstance()

    override val user = callbackFlow {
        val callback =
            FirebaseAuth.AuthStateListener { launch { send(it.currentUser.toFirebaseUser()) } }
        auth.addAuthStateListener(callback)
        awaitClose { auth.removeAuthStateListener(callback) }
    }.stateIn(appCoroutineScope, SharingStarted.WhileSubscribed(), null)

    override suspend fun googleSignIn(
        credential: AuthCredential,
        firebaseResult: FirebaseResult,
        dispatcher: CoroutineDispatcher,
    ) = withExternalScope(
        callSite = AuthCallSite("googleSignIn"),
        onFail = firebaseResult::fail,
        dispatcher = dispatcher
    ) {
        val authResult = auth.signInWithCredential(credential).await()

        val user = authResult.user.toFirebaseUser()!!

        val isNewer = authResult.additionalUserInfo!!.isNewUser

        /**
         * 새 유저일 시 데이터베이스에 유저 정보 Push
         */
        if (isNewer) pushNewUser(user)

        firebaseResult.success()
    }

    override suspend fun signIn(
        email: String,
        password: String,
        firebaseResult: FirebaseResult,
        dispatcher: CoroutineDispatcher,
    ) = withExternalScope(
        callSite = AuthCallSite("signIn"), onFail = firebaseResult::fail, dispatcher = dispatcher
    ) {

        auth.signInWithEmailAndPassword(email, password).await()

        firebaseResult.success()
    }

    override suspend fun signUp(
        email: String,
        password: String,
        name: String,
        firebaseResult: FirebaseResult,
        dispatcher: CoroutineDispatcher,
    ) = withExternalScope(
        callSite = AuthCallSite("signUp"), onFail = firebaseResult::fail, dispatcher = dispatcher
    ) {

        val firebaseUser = auth.createUserWithEmailAndPassword(email, password).await().user!!

        val request = userProfileChangeRequest { displayName = name }
        firebaseUser.updateProfile(request).await()

        val user = firebaseUser.toFirebaseUser()!!.copy(name = name)

        pushNewUser(user)

        firebaseResult.success()
    }

    override suspend fun resetPasswordEmail(
        email: String,
        firebaseResult: FirebaseResult,
        dispatcher: CoroutineDispatcher,
    ) = withExternalScope(
        callSite = AuthCallSite("resetPasswordEmail"),
        onFail = firebaseResult::fail,
        dispatcher = dispatcher
    ) {

        auth.sendPasswordResetEmail(email).await()

        firebaseResult.success()
    }

    override suspend fun signOut(onFail: (Exception) -> Unit, dispatcher: CoroutineDispatcher) =
        withExternalScope(
            callSite = AuthCallSite("signOut"), onFail = onFail, dispatcher = dispatcher
        ) { auth.signOut() }

    private suspend fun pushNewUser(user: FirebaseUserModel) =
        firebaseDbRoot.child(user.uid).child(DatabasePath.USER_INFO).setValue(user).await()

    /**
     * 호출 시 로딩 On
     *
     * 작업 끌날 시 로딩 Off
     *
     * @param callSite 예외 발생 시 로그에 찍힐 Site
     */
    private suspend fun withExternalScope(
        callSite: AuthCallSite,
        onFail: (Exception) -> Unit,
        dispatcher: CoroutineDispatcher,
        task: suspend CoroutineScope.() -> Unit,
    ) = withContext(appCoroutineScope.coroutineContext) {
        withContext(dispatcher) {
            try {
                loadingOn()
                task()
            } catch (e: Exception) {
                logE(callSite.site, e)
                onFail(e)
            } finally {
                loadingOff()
            }
        }
    }

    private fun FirebaseUser?.toFirebaseUser() =
        this?.let { FirebaseUserModel(email = "$email", name = "$displayName", uid = uid) }
}