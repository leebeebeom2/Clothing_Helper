package com.leebeebeom.clothinghelper.data.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.leebeebeom.clothinghelper.data.repository.UserRepositoryImpl.UserCallback
import com.leebeebeom.clothinghelper.data.repository.util.LoadingStateProviderImpl
import com.leebeebeom.clothinghelper.data.repository.util.callbackFlowEmit
import com.leebeebeom.clothinghelper.di.AppScope
import com.leebeebeom.clothinghelper.di.DispatcherIO
import com.leebeebeom.clothinghelper.domain.model.User
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    @AppScope private val appScope: CoroutineScope,
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
) : UserRepository, LoadingStateProviderImpl() {
    private val auth = FirebaseAuth.getInstance()
    private var authCallback: FirebaseAuth.AuthStateListener? = null
    private var userCallback: UserCallback? = null

    /**
     * @throws NullPointerException [authCallback]이 null일 경우
     */
    override val user = callbackFlow {
        if (userCallback == null) userCallback = UserCallback {
            trySend(element = runCatching { it.toUserModel() })
            loadingOff()
        }

        if (authCallback == null) authCallback = FirebaseAuth.AuthStateListener {
            trySend(element = runCatching { it.currentUser.toUserModel() })
            loadingOff()
        }

        auth.addAuthStateListener(authCallback!!)

        awaitClose {
            auth.removeAuthStateListener(authCallback!!)
            authCallback = null
            userCallback = null
        }
    }.shareIn(
        scope = appScope, started = SharingStarted.WhileSubscribed(5000), replay = 1
    )

    override suspend fun googleSignIn(credential: AuthCredential) =
        withContext { auth.signInWithCredential(credential).await() }

    /**
     * @throws FirebaseNetworkException - 인터넷에 연결되지 않았을 경우
     * @throws FirebaseTooManyRequestsException - 너무 많은 요청이 발생했을 경우
     * @throws FirebaseAuthException - InvalidEmail, NotFoundUser, WrongPassword 등
     */
    override suspend fun signIn(email: String, password: String) =
        withContext { auth.signInWithEmailAndPassword(email, password).await() }

    /**
     * @throws FirebaseNetworkException - 인터넷에 연결되지 않았을 경우
     * @throws FirebaseTooManyRequestsException - 너무 많은 요청이 발생했을 경우
     * @throws FirebaseAuthException - InvalidEmail, EmailAlreadyInUse 등
     */
    override suspend fun signUp(email: String, password: String, name: String) =
        withContext(appScope.coroutineContext) {
            withContext {
                val user = auth.createUserWithEmailAndPassword(email, password).await().user!!

                val request = userProfileChangeRequest { displayName = name }

                user.updateProfile(request).await()

                callbackFlowEmitWrapper { it(user = auth.currentUser) }
            }
        }

    /**
     * @throws FirebaseNetworkException - 인터넷에 연결되지 않았을 경우
     * @throws FirebaseTooManyRequestsException - 너무 많은 요청이 발생했을 경우
     * @throws FirebaseAuthException - InvalidEmail, NotFoundUser 등
     */
    override suspend fun sendResetPasswordEmail(email: String) =
        withContext { auth.sendPasswordResetEmail(email).await() }

    override fun signOut() = auth.signOut()

    /**
     * 호출 시 로딩 On
     */
    private suspend fun withContext(task: suspend CoroutineScope.() -> Unit) =
        withContext(dispatcher) {
            try {
                loadingOn()
                task()
            } catch (e: Exception) {
                loadingOff()
                throw e
            }
        }

    private fun FirebaseUser?.toUserModel() =
        this?.let { User(email = "$email", name = "$displayName", uid = uid) }

    private fun interface UserCallback {
        operator fun invoke(user: FirebaseUser?)
    }

    private suspend fun callbackFlowEmitWrapper(emit: suspend (UserCallback) -> Unit) =
        callbackFlowEmit({ userCallback }, flow = user, emit = emit)
}