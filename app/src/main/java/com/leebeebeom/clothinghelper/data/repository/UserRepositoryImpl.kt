package com.leebeebeom.clothinghelper.data.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.leebeebeom.clothinghelper.data.repository.util.AuthCallSite
import com.leebeebeom.clothinghelper.data.repository.util.logE
import com.leebeebeom.clothinghelper.domain.model.AuthResult
import com.leebeebeom.clothinghelper.domain.model.AuthResult.*
import com.leebeebeom.clothinghelper.domain.model.data.User
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import com.leebeebeom.clothinghelper.ui.util.FirebaseAuthErrorCode.A_NETWORK_ERROR
import com.leebeebeom.clothinghelper.ui.util.FirebaseAuthErrorCode.TOO_MANY_REQUEST
import com.leebeebeom.clothinghelper.data.repository.util.LoadingStateProviderImpl
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
class UserRepositoryImpl @Inject constructor() : UserRepository, LoadingStateProviderImpl(false) {
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

    /**
     * 성공 시 [AuthResult.Success] 반환
     *
     * 실패 시 [FirebaseAuthException]이 담긴 [AuthResult.Fail] 혹은 [AuthResult.UnknownFail] 반환
     *
     * [AuthResult.Fail]의 경우 처리 가능, [AuthResult.UnknownFail]의 경우 처리 불가능
     */
    override suspend fun googleSignIn(credential: AuthCredential): AuthResult =
        authTry(callSite = AuthCallSite("googleSignIn")) {

            val authResult = auth.signInWithCredential(credential).await()

            val user = authResult.user.toUser()!!

            val isNewer = authResult.additionalUserInfo!!.isNewUser

            /**
             * 새 유저일 시 데이터베이스에 유저 정보 Push
             * 기존 유저일 시 [_user], [_isSignIn] 업데이트
             */
            if (isNewer) pushNewUser(user) else updateUserAndUpdateSignIn(user)

            Success
        }

    /**
     * 성공 시 [AuthResult.Success] 반환
     *
     * 실패 시 [FirebaseAuthException]이 담긴 [AuthResult.Fail] 혹은 [AuthResult.UnknownFail] 반환
     *
     * [AuthResult.Fail]의 경우 처리 가능, [AuthResult.UnknownFail]의 경우 처리 불가능
     */
    override suspend fun signIn(email: String, password: String) =
        authTry(callSite = AuthCallSite("signIn")) {
            val user = auth.signInWithEmailAndPassword(email, password).await().user.toUser()!!
            updateUserAndUpdateSignIn(user)
            Success
        }

    /**
     * 성공 시 [AuthResult.Success] 반환
     *
     * 실패 시 [FirebaseAuthException]이 담긴 [AuthResult.Fail] 혹은 [AuthResult.UnknownFail] 반환
     *
     * [AuthResult.Fail]의 경우 처리 가능, [AuthResult.UnknownFail]의 경우 처리 불가능
     */
    override suspend fun signUp(
        email: String,
        password: String,
        name: String,
    ): AuthResult =
        authTry(callSite = AuthCallSite("signUp")) {
            val firebaseUser = auth.createUserWithEmailAndPassword(email, password).await().user!!

            val request = userProfileChangeRequest { displayName = name }
            firebaseUser.updateProfile(request).await()

            val user = firebaseUser.toUser()!!.copy(name = name)

            pushNewUser(user)

            Success
        }

    /**
     * 성공 시 [AuthResult.Success] 반환
     *
     * 실패 시 [FirebaseAuthException]이 담긴 [AuthResult.Fail] 혹은 [AuthResult.UnknownFail] 반환
     *
     * [AuthResult.Fail]의 경우 처리 가능, [AuthResult.UnknownFail]의 경우 처리 불가능
     */
    override suspend fun resetPasswordEmail(email: String) =
        authTry(callSite = AuthCallSite("resetPasswordEmail")) {
            auth.sendPasswordResetEmail(email).await()
            Success
        }

    /**
     * 성공 시 [AuthResult.Success] 반환
     *
     * 실패 시 [FirebaseAuthException]이 담긴 [AuthResult.Fail] 혹은 [AuthResult.UnknownFail] 반환
     *
     * [AuthResult.Fail]의 경우 처리 가능, [AuthResult.UnknownFail]의 경우 처리 불가능
     */
    override suspend fun signOut() = authTry(callSite = AuthCallSite("signOut")) {
        auth.signOut()
        _user.update { null }
        _isSignIn.update { false }
        Success
    }

    /**
     * 데이터 베이스에 유저 정보 입력 후 [_user], [_isSignIn] 업데이트
     */
    private suspend fun pushNewUser(user: User) = withContext(Dispatchers.IO) {
        FirebaseDatabase.getInstance().reference.child(user.uid).child(DatabasePath.USER_INFO)
            .setValue(user)
        updateUserAndUpdateSignIn(user)
    }

    private suspend fun updateUserAndUpdateSignIn(user: User) = withContext(NonCancellable) {
        _user.update { user }
        _isSignIn.update { true }
    }

    /**
     * 호출 시 로딩 On
     *
     * 작업이 끌날 시 로딩 Off
     *
     * 취소할 수 없음
     *
     * 계정 관련 예외 발생시 [FirebaseAuthException]이 담긴 [AuthResult.Fail] 반환
     *
     * 네트워크 미 연결 시 [FirebaseNetworkException]이 담긴 [AuthResult.Fail] 반환
     *
     * 너무 많은 요청 시 [FirebaseTooManyRequestsException]이 담긴 [AuthResult.Fail] 반환
     *
     * 다른 예외 발생 시 해당 [AuthResult.UnknownFail] 반환
     *
     * @param callSite 예외 발생 시 로그에 찍힐 Site
     */
    private suspend fun authTry(
        callSite: AuthCallSite,
        task: suspend () -> AuthResult,
    ) = withContext(context = Dispatchers.IO) {
        try {
            loadingOn()
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
            loadingOff()
        }
    }

    private fun FirebaseUser?.toUser() =
        this?.let { User(email = "$email", name = "$displayName", uid = uid) }
}