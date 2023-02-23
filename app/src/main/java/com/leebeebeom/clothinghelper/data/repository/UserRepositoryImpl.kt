package com.leebeebeom.clothinghelper.data.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.leebeebeom.clothinghelper.data.repository.util.AuthCallSite
import com.leebeebeom.clothinghelper.data.repository.util.LoadingStateProviderImpl
import com.leebeebeom.clothinghelper.data.repository.util.logE
import com.leebeebeom.clothinghelper.domain.model.FirebaseResult
import com.leebeebeom.clothinghelper.domain.model.data.User
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val appCoroutineScope: CoroutineScope,
) : UserRepository, LoadingStateProviderImpl(false) {
    private val auth = FirebaseAuth.getInstance()

    private val _user = MutableStateFlow(auth.currentUser.toUser())
    override val user = _user.asStateFlow()

    init {
        /**
         * 로그아웃 시에만 작동
         */
        auth.addAuthStateListener {
            if (it.currentUser == null) _user.update { null }
        }
    }

    override suspend fun googleSignIn(
        credential: AuthCredential,
        firebaseResult: FirebaseResult,
    ) = withExternalScope(callSite = AuthCallSite("googleSignIn"), onFail = firebaseResult::fail) {
        val authResult = auth.signInWithCredential(credential).await()

        val user = authResult.user.toUser()!!

        val isNewer = authResult.additionalUserInfo!!.isNewUser

        /**
         * 새 유저일 시 데이터베이스에 유저 정보 Push
         */
        if (isNewer) pushNewUser(user)
        _user.emit(user)

        firebaseResult.success()
    }

    override suspend fun signIn(
        email: String,
        password: String,
        firebaseResult: FirebaseResult,
    ) = withExternalScope(callSite = AuthCallSite("signIn"), onFail = firebaseResult::fail) {

        val user = auth.signInWithEmailAndPassword(email, password).await().user.toUser()!!

        _user.emit(user)

        firebaseResult.success()
    }

    override suspend fun signUp(
        email: String,
        password: String,
        name: String,
        firebaseResult: FirebaseResult,
    ) = withExternalScope(callSite = AuthCallSite("signUp"), onFail = firebaseResult::fail) {

        val firebaseUser = auth.createUserWithEmailAndPassword(email, password).await().user!!

        val request = userProfileChangeRequest { displayName = name }
        firebaseUser.updateProfile(request).await()

        val user = firebaseUser.toUser()!!.copy(name = name)

        pushNewUser(user)
        _user.emit(user)

        firebaseResult.success()
    }

    override suspend fun resetPasswordEmail(email: String, firebaseResult: FirebaseResult) =
        withExternalScope(
            callSite = AuthCallSite("resetPasswordEmail"),
            onFail = firebaseResult::fail
        ) {

            auth.sendPasswordResetEmail(email).await()

            firebaseResult.success()
        }

    override suspend fun signOut(
        onFail: (Exception) -> Unit,
    ) = withExternalScope(callSite = AuthCallSite("signOut"), onFail = onFail) {

        auth.signOut()

        _user.emit(null)
    }

    private suspend fun pushNewUser(user: User) =
        firebaseDbRoot.child(user.uid).child(DatabasePath.USER_INFO).setValue(user).await()

    /**
     * 호출 시 로딩 On
     *
     * 작업이 끌날 시 로딩 Off
     *
     * @param callSite 예외 발생 시 로그에 찍힐 Site
     */
    private suspend fun withExternalScope(
        callSite: AuthCallSite,
        onFail: (Exception) -> Unit,
        task: suspend CoroutineScope.() -> Unit,
    ) {
        appCoroutineScope.launch {
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

    private fun FirebaseUser?.toUser() =
        this?.let { User(email = "$email", name = "$displayName", uid = uid) }
}