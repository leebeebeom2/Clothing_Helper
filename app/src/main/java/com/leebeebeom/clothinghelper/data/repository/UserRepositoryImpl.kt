package com.leebeebeom.clothinghelper.data.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.leebeebeom.clothinghelper.data.datasourse.folder.FolderRoomDataSource
import com.leebeebeom.clothinghelper.data.datasourse.subcategory.SubCategoryRoomDataSource
import com.leebeebeom.clothinghelper.data.repository.container.firebaseDbRoot
import com.leebeebeom.clothinghelper.data.repository.util.AuthCallSite
import com.leebeebeom.clothinghelper.data.repository.util.LoadingStateProviderImpl
import com.leebeebeom.clothinghelper.data.repository.util.logE
import com.leebeebeom.clothinghelper.domain.model.FirebaseResult
import com.leebeebeom.clothinghelper.domain.model.data.User
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val subCategoryRoomDataSource: SubCategoryRoomDataSource,
    private val folderRoomDataSource: FolderRoomDataSource,
) : UserRepository, LoadingStateProviderImpl(false) {
    private val auth = FirebaseAuth.getInstance()

    private val _isSignIn = MutableStateFlow(auth.currentUser != null)
    override val isSignIn = _isSignIn.asStateFlow()

    private val _user = MutableStateFlow(auth.currentUser.toUser())
    override val user = _user.asStateFlow()

    init {
        auth.addAuthStateListener {
            if (it.currentUser == null) {
                _isSignIn.update { false }
                _user.update { null }
            }
        }
    }

    override suspend fun googleSignIn(credential: AuthCredential, firebaseResult: FirebaseResult) =
        authTry(callSite = AuthCallSite("googleSignIn"), onFail = firebaseResult::fail) {
            // 어떠한 경우로 비정상적 로그아웃이 되었을 경우 다시 로그인 시 로컬 데이터 삭제
            allLocalDataClear()

            val authResult = auth.signInWithCredential(credential).await()

            val user = authResult.user.toUser()!!

            val isNewer = authResult.additionalUserInfo!!.isNewUser

            /**
             * 새 유저일 시 데이터베이스에 유저 정보 Push
             * 기존 유저일 시 [_user], [_isSignIn] 업데이트
             */
            if (isNewer) pushNewUser(user) else updateUserAndSignIn(user)

            firebaseResult.success()
        }

    override suspend fun signIn(email: String, password: String, firebaseResult: FirebaseResult) =
        authTry(callSite = AuthCallSite("signIn"), onFail = firebaseResult::fail) {
            // 어떠한 경우로 비정상적 로그아웃이 되었을 경우 다시 로그인 시 로컬 데이터 삭제
            allLocalDataClear()

            val user = auth.signInWithEmailAndPassword(email, password).await().user.toUser()!!

            updateUserAndSignIn(user)

            firebaseResult.success()
        }

    override suspend fun signUp(
        email: String,
        password: String,
        name: String,
        firebaseResult: FirebaseResult,
    ) = authTry(callSite = AuthCallSite("signUp"), onFail = firebaseResult::fail) {
        // 어떠한 경우로 비정상적 로그아웃이 되었을 경우 다시 로그인 시 로컬 데이터 삭제
        allLocalDataClear()

        val firebaseUser = auth.createUserWithEmailAndPassword(email, password).await().user!!

        val request = userProfileChangeRequest { displayName = name }
        firebaseUser.updateProfile(request).await()

        val user = firebaseUser.toUser()!!.copy(name = name)

        pushNewUser(user)

        firebaseResult.success()
    }

    override suspend fun resetPasswordEmail(email: String, firebaseResult: FirebaseResult) =
        authTry(callSite = AuthCallSite("resetPasswordEmail"), onFail = firebaseResult::fail) {

            auth.sendPasswordResetEmail(email).await()

            firebaseResult.success()
        }

    /**
     * 로그아웃 시 로컬데이터 모두 삭제
     */
    override suspend fun signOut(onFail: (Exception) -> Unit) =
        authTry(callSite = AuthCallSite("signOut"), onFail = onFail) {

            auth.signOut()
            allLocalDataClear()

            _user.update { null }
            _isSignIn.update { false }
        }

    /**
     * 데이터 베이스에 유저 정보 입력 후 [_user], [_isSignIn] 업데이트
     */
    private suspend fun pushNewUser(user: User) {
        firebaseDbRoot.child(user.uid).child(DatabasePath.USER_INFO).setValue(user).await()
        updateUserAndSignIn(user)
    }

    private fun updateUserAndSignIn(user: User) {
        _user.update { user }
        _isSignIn.update { true }
    }

    private suspend fun allLocalDataClear() =
        coroutineScope {
            launch { subCategoryRoomDataSource.deleteAll() }
            launch { folderRoomDataSource.deleteAll() }
        }

    /**
     * 호출 시 로딩 On
     *
     * 작업이 끌날 시 로딩 Off
     *
     * 취소할 수 없음
     *
     * @param callSite 예외 발생 시 로그에 찍힐 Site
     */
    private suspend fun authTry(
        callSite: AuthCallSite,
        onFail: (Exception) -> Unit,
        task: suspend CoroutineScope.() -> Unit,
    ) = withContext(context = Dispatchers.IO) {
        try {
            loadingOn()
            withContext(context = NonCancellable) { task() }
        } catch (e: FirebaseAuthException) {
            logE(site = callSite.site, e = e)
            onFail(e)
        } catch (e: FirebaseNetworkException) {
            logE(site = callSite.site, e = e)
            onFail(e)
        } catch (e: FirebaseTooManyRequestsException) {
            logE(site = callSite.site, e = e)
            onFail(e)
        } catch (e: Exception) {
            logE(site = callSite.site, e = e)
            onFail(e)
        } finally {
            loadingOff()
        }
    }

    private fun FirebaseUser?.toUser() =
        this?.let { User(email = "$email", name = "$displayName", uid = uid) }
}