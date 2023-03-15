package com.leebeebeom.clothinghelper.data

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_EMAIL_ALREADY_IN_USE
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_INVALID_EMAIL
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_USER_NOT_FOUND
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_WRONG_PASSWORD
import com.leebeebeom.clothinghelper.isConnectedNetwork
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

const val SignInEmail = "1@a.com"
const val SignUpEmail = "2@a.com"
const val InvalidEmail = "invalidemail"
const val NotFoundEmail = "notfoundemail@a.com"
const val SignInPassword = "111111"
const val WrongPassword = "123456"
const val SignUpName = "test"
const val SendPasswordEmail = "boole92@naver.com"

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTest {
    private lateinit var userRepositoryTestUtil: UserRepositoryTestUtil
    private val dispatcher = StandardTestDispatcher()
    private suspend fun getUser() = userRepositoryTestUtil.getUser()

    private inline fun TestScope.failRunCatching(
        errorCode: String, test: () -> Unit,
    ) = runCatching {
        test()
    }.onSuccess { assert(false) }.onFailure {
        val firebaseAuthException = it as FirebaseAuthException
        assert(firebaseAuthException.errorCode == errorCode)
    }

    private suspend fun TestScope.successRunCatching(
        test: suspend () -> Unit,
    ) = runCatching {
        test()
    }.onSuccess { assert(true) }.onFailure { assert(false) }

    @Before
    fun init() {
        Firebase.auth.signOut()

        userRepositoryTestUtil =
            UserRepositoryTestUtil(repositoryProvider = RepositoryProvider(dispatcher))
    }

    @After
    fun signOut() {
        userRepositoryTestUtil.signOut()
    }

    @Test
    fun signInTest() = runTest(dispatcher) {
        userRepositoryTestUtil.userCollect(backgroundScope)

        assert(getUser() == null)

        failRunCatching(errorCode = ERROR_INVALID_EMAIL) {
            userRepositoryTestUtil.signIn( // invalidEmail
                email = InvalidEmail
            )
        }
        advanceUntilIdle()
        assert(getUser() == null)

        failRunCatching(errorCode = ERROR_USER_NOT_FOUND) {
            userRepositoryTestUtil.signIn( // notFoundEmail
                email = NotFoundEmail
            )
        }
        advanceUntilIdle()
        assert(getUser() == null)

        failRunCatching(errorCode = ERROR_WRONG_PASSWORD) {
            userRepositoryTestUtil.signIn( // WrongPassword
                password = WrongPassword
            )
        }
        advanceUntilIdle()
        assert(getUser() == null)

        successRunCatching {
            userRepositoryTestUtil.signIn()
        }
        advanceUntilIdle()
        assert(getUser() != null)
        assert(getUser()?.email == SignInEmail)
    }

    @Test
    fun signUpTest() = runTest(dispatcher) {
        userRepositoryTestUtil.userCollect(backgroundScope)

        assert(getUser() == null)

        failRunCatching(errorCode = ERROR_INVALID_EMAIL) {
            userRepositoryTestUtil.signUp( // invalidEmail
                email = InvalidEmail
            )
        }
        advanceUntilIdle()
        assert(getUser() == null)

        failRunCatching(errorCode = ERROR_EMAIL_ALREADY_IN_USE) {
            userRepositoryTestUtil.signUp( // EmailAlreadyInUse
                email = SignInEmail
            )
        }
        advanceUntilIdle()
        assert(getUser() == null)

        successRunCatching {
            userRepositoryTestUtil.signUp()
        }
        advanceUntilIdle()
        assert(getUser() != null)
        assert(getUser()?.email == SignUpEmail)
        assert(getUser()?.name == SignUpName)

        userRepositoryTestUtil.deleteUser()
    }

    @Test
    fun resetPasswordTest() = runTest(dispatcher) {
        userRepositoryTestUtil.userCollect(backgroundScope)

        failRunCatching(errorCode = ERROR_INVALID_EMAIL) {
            userRepositoryTestUtil.sendResetPasswordEmail( // invalidEmail
                email = InvalidEmail
            )
        }
        advanceUntilIdle()

        failRunCatching(errorCode = ERROR_USER_NOT_FOUND) {
            userRepositoryTestUtil.sendResetPasswordEmail( // userNotFound
                email = NotFoundEmail
            )
        }
        advanceUntilIdle()

        successRunCatching {
            userRepositoryTestUtil.sendResetPasswordEmail()
        }
        advanceUntilIdle()
    }

    @Test
    fun networkExceptionTest() = runTest(dispatcher) {
        assert(!isConnectedNetwork()) // turn on airplane mode

        suspend fun networkExceptionRunCatching(test: suspend () -> Unit) {
            runCatching {
                test()
            }.onSuccess { assert(false) }
                .onFailure {
                    assert(it is FirebaseNetworkException)
                }
        }

        networkExceptionRunCatching {
            userRepositoryTestUtil.signIn()
        }

        networkExceptionRunCatching {
            userRepositoryTestUtil.signUp()
        }

        networkExceptionRunCatching {
            userRepositoryTestUtil.sendResetPasswordEmail()
        }
    }

    @Test
    fun userRepositoryLoadingTest() = runTest(dispatcher) {
        // check the logcat
        Log.d(userLoadingTag, "loadingCollect start")
        userRepositoryTestUtil.loadingCollect(backgroundScope)
        Log.d(userLoadingTag, "userCollect start")
        userRepositoryTestUtil.userCollect(backgroundScope)

        Log.d(userLoadingTag, "sign in start")
        userRepositoryTestUtil.signIn()
        advanceUntilIdle()

        Log.d(userLoadingTag, "sign up start")
        userRepositoryTestUtil.signUp()
        advanceUntilIdle()
        wait()
        userRepositoryTestUtil.deleteUser()
        advanceUntilIdle()

        Log.d(userLoadingTag, "send reset password start")
        userRepositoryTestUtil.sendResetPasswordEmail()
        advanceUntilIdle()
        wait()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class UserFlowTestWithSignInStart {
    private lateinit var userRepositoryTestUtil: UserRepositoryTestUtil
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun init() {
        runBlocking {
            Firebase.auth.signInWithEmailAndPassword(SignInEmail, SignInPassword).await()
        }
        userRepositoryTestUtil = UserRepositoryTestUtil(RepositoryProvider(dispatcher))
    }

    @After
    fun signOut() {
        userRepositoryTestUtil.signOut()
    }

    @Test
    fun runWithSignIn() = runTest(dispatcher) {
        userRepositoryTestUtil.userCollect(backgroundScope)

        suspend fun getUser() = userRepositoryTestUtil.getUser()

        assert(getUser() != null)
        assert(getUser()?.email == SignInEmail)

        userRepositoryTestUtil.signOut()
        wait()
        assert(getUser() == null)

        userRepositoryTestUtil.signIn()
        advanceUntilIdle()
        assert(getUser() != null)
        assert(getUser()?.email == SignInEmail)

        userRepositoryTestUtil.signOut()
        wait()
        assert(getUser() == null)

        userRepositoryTestUtil.signUp()
        advanceUntilIdle()
        wait()
        assert(getUser() != null)
        assert(getUser()?.email == SignUpEmail)
        assert(getUser()?.name == SignUpName)

        userRepositoryTestUtil.deleteUser()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class UserFlowTestWithSignOutStart {
    private lateinit var userRepositoryTestUtil: UserRepositoryTestUtil
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun init() {
        runBlocking { launch { Firebase.auth.signOut() }.join() }
        userRepositoryTestUtil = UserRepositoryTestUtil(RepositoryProvider(dispatcher))
    }

    @After
    fun signOut() {
        userRepositoryTestUtil.signOut()
    }

    @Test
    fun runWithSignOut() = runTest(dispatcher) {
        userRepositoryTestUtil.userCollect(backgroundScope)

        suspend fun getUser() = userRepositoryTestUtil.getUser()

        assert(getUser() == null)

        userRepositoryTestUtil.signIn()
        advanceUntilIdle()
        assert(getUser() != null)
        assert(getUser()?.email == SignInEmail)

        userRepositoryTestUtil.signOut()
        wait()
        assert(getUser() == null)

        userRepositoryTestUtil.signUp()
        advanceUntilIdle()
        assert(getUser() != null)
        assert(getUser()?.email == SignUpEmail)
        assert(getUser()?.name == SignUpName)

        userRepositoryTestUtil.deleteUser()
    }
}