package com.leebeebeom.clothinghelper.data.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.leebeebeom.clothinghelper.data.repository.util.AuthCallSite
import com.leebeebeom.clothinghelper.data.repository.util.LoadingStateProviderImpl
import com.leebeebeom.clothinghelper.data.repository.util.logE
import com.leebeebeom.clothinghelper.di.AppScope
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import com.leebeebeom.clothinghelper.domain.model.FirebaseUser as FirebaseUserModel

@Singleton
class UserRepositoryImpl @Inject constructor(@AppScope private val appCoroutineScope: CoroutineScope) :
    UserRepository, LoadingStateProviderImpl(false) {
    private val auth = FirebaseAuth.getInstance()

    override val firebaseUser = MutableStateFlow(auth.currentUser.toFirebaseUser())

    override suspend fun googleSignIn(
        credential: AuthCredential,
        firebaseResult: FirebaseResult,
        dispatcher: CoroutineDispatcher,
    ) = withExternalScope(
        callSite = AuthCallSite("googleSignIn"),
        onFail = firebaseResult::fail,
        dispatcher = dispatcher
    ) {
        firebaseUser.value = auth.signInWithCredential(credential).await().user.toFirebaseUser()!!
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
        firebaseUser.value =
            auth.signInWithEmailAndPassword(email, password).await().user.toFirebaseUser()!!
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
        val request = userProfileChangeRequest { displayName = name }

        val firebaseUser = auth.createUserWithEmailAndPassword(email, password).await().user!!

        firebaseUser.updateProfile(request).await()

        this@UserRepositoryImpl.firebaseUser.value = firebaseUser.toFirebaseUser()

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
            callSite = AuthCallSite("signOut"),
            onFail = onFail,
            dispatcher = dispatcher
        ) { auth.signOut() }

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