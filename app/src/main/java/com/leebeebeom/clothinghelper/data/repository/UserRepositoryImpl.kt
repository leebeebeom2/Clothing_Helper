package com.leebeebeom.clothinghelper.data.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.leebeebeom.clothinghelper.data.repository.UserRepositoryImpl.UserCallback
import com.leebeebeom.clothinghelper.data.repository.util.LoadingStreamProviderImpl
import com.leebeebeom.clothinghelper.data.repository.util.callbackFlowEmit
import com.leebeebeom.clothinghelper.di.AppScope
import com.leebeebeom.clothinghelper.di.DispatcherIO
import com.leebeebeom.clothinghelper.domain.model.User
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    @AppScope private val appScope: CoroutineScope,
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
) : UserRepository, LoadingStreamProviderImpl(initialState = false) {
    private val auth = FirebaseAuth.getInstance()
    private var userCallback: UserCallback? = null

    override val userStream = callbackFlow {
        if (userCallback == null) userCallback = UserCallback { firebaseUser ->
            trySend(element = firebaseUser.toUserModel())
        }

        val authCallback = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(element = firebaseAuth.currentUser.toUserModel())
        }

        auth.addAuthStateListener(authCallback)

        awaitClose {
            loadingOff()
            auth.removeAuthStateListener(authCallback)
        }
    }.onEach { loadingOff() }.distinctUntilChanged().shareIn(
        scope = appScope,
        started = SharingStarted.WhileSubscribed(5000),
        replay = 1
    )

    /**
     * @throws FirebaseNetworkException 인터넷에 연결되지 않았을 경우
     */
    override suspend fun googleSignIn(credential: AuthCredential) =
        withContext { auth.signInWithCredential(credential).await() }

    /**
     * @throws FirebaseNetworkException 인터넷에 연결되지 않았을 경우
     * @throws FirebaseTooManyRequestsException 너무 많은 요청이 발생했을 경우
     * @throws FirebaseAuthException InvalidEmail, NotFoundUser, WrongPassword 등
     */
    override suspend fun signIn(email: String, password: String) =
        withContext { auth.signInWithEmailAndPassword(email, password).await() }

    /**
     * @throws FirebaseNetworkException 인터넷에 연결되지 않았을 경우
     * @throws FirebaseTooManyRequestsException 너무 많은 요청이 발생했을 경우
     * @throws FirebaseAuthException InvalidEmail, EmailAlreadyInUse 등
     */
    override suspend fun signUp(email: String, password: String, name: String) =
        // 호출 스코프 취소 시 취소 안 되는 거 테스트 확인
        withContext(appScope.coroutineContext) {
            withContext {
                val user = auth.createUserWithEmailAndPassword(email, password).await().user!!

                val request = userProfileChangeRequest { displayName = name }

                user.updateProfile(request).await()

                callbackFlowEmitWrapper { it(user = auth.currentUser) }
            }
        }

    /**
     * @throws FirebaseNetworkException 인터넷에 연결되지 않았을 경우
     * @throws FirebaseTooManyRequestsException 너무 많은 요청이 발생했을 경우
     * @throws FirebaseAuthException InvalidEmail, NotFoundUser 등
     */
    override suspend fun sendResetPasswordEmail(email: String) = withContext {
        auth.sendPasswordResetEmail(email).await()
        loadingOff()
    }

    override fun signOut() = auth.signOut()

    /**
     * 호출 시 로딩 On, 예외 발생 시 로딩 Off
     */
    private suspend fun withContext(task: suspend () -> Unit) =
        withContextWithJob { launch { task() } }

    private suspend fun withContextWithJob(task: suspend CoroutineScope.() -> Job) =
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
        callbackFlowEmit({ userCallback }, flow = userStream, emit = emit)
}