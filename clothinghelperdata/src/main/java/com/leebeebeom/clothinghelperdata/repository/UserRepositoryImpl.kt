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
import kotlinx.coroutines.NonCancellable
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

/**
 * 로그인 시도 혹은 종료 시 [isLoading] 상태 변경
 *
 * 모든 로그인 혹은 가입 성공 시 [user], [isSignIn] 상태 변경
 *
 * 로그인 만료등의 상태 변경 시 [user], [isSignIn] 상태 변경
 */
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

    /**
     * @return 로그인 된 [user] 객체와 최초 유저 여부
     */
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

    /**
     * @return 로그인 된 유저 객체 포함
     *
     * 로그인 성공 시 최초 유저가 아니기 때문에 [AuthResult.Success.isNewer]는 항상 false
     */
    override suspend fun signIn(email: String, password: String) =
        authTry("signIn") {
            val user = auth.signInWithEmailAndPassword(email, password)
                .await().user.toUser()!!
            updateUserAndUpdateSignIn(user)
            AuthResult.Success(user, false)
        }

    /**
     * 가입 성공 시 닉네임 업데이트
     *
     * 때문에 동작 도중 앱이 종료되거나 인터넷이 끊길 경우 닉네임이 업데이트 되지 않을 수 있음
     *
     * @return 로그인 된 유저 객체 포함
     * 가입 성공 시 최초 유저이기 떄문에 [AuthResult.Success.isNewer]는 항상 true
     */
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

    /**
     *  인터넷이 끊겨도 인터넷 복구 시 푸
     *
     *  앱이 종료될 경우 따로 처리해주어야 함
     */
    private suspend fun pushNewUser(user: User) =
        withContext(Dispatchers.IO) {
            FirebaseDatabase.getInstance().reference.child(user.uid).child(DatabasePath.USER_INFO)
                .setValue(user)
            updateUserAndUpdateSignIn(user)
        }

    /**
     * [NonCancellable]로 동작 보장
     */
    private suspend fun updateUserAndUpdateSignIn(userObj: User) = withContext(NonCancellable) {
        updateUser(user = userObj)
        updateSignIn(state = true)
    }

    private fun updateSignIn(state: Boolean) = _isSignIn.update { state }
    private fun updateUser(user: User?) = _user.update { user }

    /**
     * [task] 시작 시 로딩 온
     *
     * 함수 종료 시 로딩 오프
     *
     * 로딩 오프는 [NonCancellable]로 동작 보장
     *
     * 에러 발생 시 로그 설정
     * 
     * [task]는 [NonCancellable]로 동작 보장
     *
     * @param [site] 로그에 찍힐 콜 사이트
     * @param [task] try 안에서 실행할 동작
     * @return [FirebaseAuthException] 발생 시 에러코드가 포함된 객체 반환
     *
     * 인터넷 미 연결 시 [A_NETWORK_ERROR]가 포함된 객체 반환
     *
     * 너무 많은 시도 시 [TOO_MANY_REQUEST]가 포함된 객체 반환
     */
    private suspend fun authTry(site: String, task: suspend () -> AuthResult) =
        withContext(Dispatchers.IO) {
            try {
                loadingRepositoryImpl.loadingOn()
               withContext(NonCancellable) { task() }
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

/**
 * 자신이 null일 경우 null 반환
 */
private fun FirebaseUser?.toUser() = this?.let { User(email!!, displayName ?: "", uid) }