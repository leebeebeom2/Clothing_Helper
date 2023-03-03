package com.leebeebeom.clothinghelper.data.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.leebeebeom.clothinghelper.data.repository.util.AuthCallSite
import com.leebeebeom.clothinghelper.data.repository.util.LoadingStateProviderImpl
import com.leebeebeom.clothinghelper.data.repository.util.logE
import com.leebeebeom.clothinghelper.di.AppScope
import com.leebeebeom.clothinghelper.domain.model.User
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    @AppScope private val appScope: CoroutineScope,
) : UserRepository, LoadingStateProviderImpl() {
    private val auth = FirebaseAuth.getInstance()

    override val user = callbackFlow {
        val callback = FirebaseAuth.AuthStateListener {
            trySend(it.currentUser.toUserModel())
        }
        auth.addAuthStateListener(callback)

        awaitClose { auth.removeAuthStateListener(callback) }
    }

    override suspend fun googleSignIn(
        credential: AuthCredential,
        firebaseResult: FirebaseResult,
        dispatcher: CoroutineDispatcher,
    ) = withAppScope(
        callSite = AuthCallSite("googleSignIn"),
        onFail = firebaseResult::fail,
        dispatcher = dispatcher
    ) {
        auth.signInWithCredential(credential).await()
        firebaseResult.success()
    }

    /**
     * @throws FirebaseNetworkException - 인터넷에 연결되지 않았을 경우
     * @throws FirebaseTooManyRequestsException - 너무 많은 요청이 발생했을 경우
     * @throws FirebaseAuthException - InvalidEmail, NotFoundUser, WrongPassword 등
     */
    override suspend fun signIn(
        email: String,
        password: String,
        firebaseResult: FirebaseResult,
        dispatcher: CoroutineDispatcher,
    ) = withAppScope(
        callSite = AuthCallSite("signIn"), onFail = firebaseResult::fail, dispatcher = dispatcher
    ) {
        auth.signInWithEmailAndPassword(email, password).await()
        firebaseResult.success()
    }

    /**
     * @throws FirebaseNetworkException - 인터넷에 연결되지 않았을 경우
     * @throws FirebaseTooManyRequestsException - 너무 많은 요청이 발생했을 경우
     * @throws FirebaseAuthException - InvalidEmail, EmailAlreadyInUse 등
     */
    override suspend fun signUp(
        email: String,
        password: String,
        name: String,
        firebaseResult: FirebaseResult,
        dispatcher: CoroutineDispatcher,
    ) = withAppScope(
        callSite = AuthCallSite("signUp"), onFail = firebaseResult::fail, dispatcher = dispatcher
    ) {
        val request = userProfileChangeRequest { displayName = name }

        val user = auth.createUserWithEmailAndPassword(email, password).await().user!!

        user.updateProfile(request).await()

        firebaseResult.success()
    }

    /**
     * @throws FirebaseNetworkException - 인터넷에 연결되지 않았을 경우
     * @throws FirebaseTooManyRequestsException - 너무 많은 요청이 발생했을 경우
     * @throws FirebaseAuthException - InvalidEmail, NotFoundUser 등
     */
    override suspend fun sendResetPasswordEmail(
        email: String,
        firebaseResult: FirebaseResult,
        dispatcher: CoroutineDispatcher,
    ) = withAppScope(
        callSite = AuthCallSite("resetPasswordEmail"),
        onFail = firebaseResult::fail,
        dispatcher = dispatcher
    ) {
        auth.sendPasswordResetEmail(email).await()

        firebaseResult.success()
    }

    override fun signOut() = auth.signOut()

    /**
     * 호출 시 로딩 On
     *
     * 작업 끌날 시 로딩 Off
     *
     * @param callSite 예외 발생 시 로그에 찍힐 Site
     */
    private suspend fun withAppScope(
        callSite: AuthCallSite,
        onFail: (Exception) -> Unit,
        dispatcher: CoroutineDispatcher,
        task: suspend CoroutineScope.() -> Unit,
    ) = withContext(appScope.coroutineContext) {
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

    private fun FirebaseUser?.toUserModel() =
        this?.let { User(email = "$email", name = "$displayName", uid = uid) }
}