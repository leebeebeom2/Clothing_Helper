package com.leebeebeom.clothinghelper.data.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.leebeebeom.clothinghelper.data.repository.UserRepositoryImpl.UserCallback
import com.leebeebeom.clothinghelper.data.repository.util.AuthCallSite
import com.leebeebeom.clothinghelper.data.repository.util.LoadingStateProviderImpl
import com.leebeebeom.clothinghelper.data.repository.util.callbackFlowEmit
import com.leebeebeom.clothinghelper.data.repository.util.logE
import com.leebeebeom.clothinghelper.di.AppScope
import com.leebeebeom.clothinghelper.domain.model.User
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    @AppScope private val appScope: CoroutineScope,
) : UserRepository, LoadingStateProviderImpl(
    initialValue = false, appScope = appScope
) {
    private val auth = FirebaseAuth.getInstance()
    private var userCallback: UserCallback? = null

    override val user = callbackFlow {
        val userCallback = UserCallback { trySend(it.toUserModel()) }

        val callback = FirebaseAuth.AuthStateListener {
            trySend(it.currentUser.toUserModel())
        }

        auth.addAuthStateListener(callback)
        this@UserRepositoryImpl.userCallback = userCallback

        awaitClose {
            auth.removeAuthStateListener(callback)
            this@UserRepositoryImpl.userCallback = null
        }
    }.stateIn(scope = appScope, started = SharingStarted.WhileSubscribed(5000), initialValue = null)

    override suspend fun googleSignIn(
        credential: AuthCredential,
        firebaseResult: FirebaseResult,
        dispatcher: CoroutineDispatcher,
    ) = withContext(
        callSite = AuthCallSite("googleSignIn"),
        onFail = firebaseResult::fail,
        dispatcher = dispatcher
    ) {
        val user = auth.signInWithCredential(credential).await().user!!
        callbackFlowEmitWrapper { it(user) }
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
    ) = withContext(
        callSite = AuthCallSite("signIn"), onFail = firebaseResult::fail, dispatcher = dispatcher
    ) {
        val user = auth.signInWithEmailAndPassword(email, password).await().user!!
        callbackFlowEmitWrapper { it(user) }
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
    ) = withContext(
        callSite = AuthCallSite("signUp"), onFail = firebaseResult::fail, dispatcher = dispatcher
    ) {
        val user = auth.createUserWithEmailAndPassword(email, password).await().user!!

        val request = userProfileChangeRequest { displayName = name }

        user.updateProfile(request).await()

        callbackFlowEmitWrapper { it(auth.currentUser) }
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
    ) = withContext(
        callSite = AuthCallSite("resetPasswordEmail"),
        onFail = firebaseResult::fail,
        dispatcher = dispatcher
    ) {
        auth.sendPasswordResetEmail(email).await()

        firebaseResult.success()
    }

    override suspend fun signOut() {
        auth.signOut()
        callbackFlowEmitWrapper { it(null) }
    }

    /**
     * 호출 시 로딩 On
     *
     * 작업 끌날 시 로딩 Off
     *
     * @param callSite 예외 발생 시 로그에 찍힐 Site
     */
    private suspend fun withContext(
        callSite: AuthCallSite,
        onFail: (Exception) -> Unit,
        dispatcher: CoroutineDispatcher,
        task: suspend CoroutineScope.() -> Unit,
    ) = withContext(dispatcher) {
        try {
            loadingOn()
            task()
        } catch (_: CancellationException) {
        } catch (e: Exception) {
            logE(callSite.site, e)
            onFail(e)
        } finally {
            loadingOff()
        }
    }

    private fun FirebaseUser?.toUserModel() =
        this?.let { User(email = "$email", name = "$displayName", uid = uid) }

    private fun interface UserCallback {
        operator fun invoke(user: FirebaseUser?)
    }

    private suspend fun callbackFlowEmitWrapper(
        emit: suspend (UserCallback) -> Unit,
    ) = callbackFlowEmit(
        { userCallback },
        flow = user,
        emit = emit
    )
}