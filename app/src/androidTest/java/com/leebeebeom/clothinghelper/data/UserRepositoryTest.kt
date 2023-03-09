package com.leebeebeom.clothinghelper.data

import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_EMAIL_ALREADY_IN_USE
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_INVALID_EMAIL
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_USER_NOT_FOUND
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_WRONG_PASSWORD
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

const val signInEmail = "1@a.com"
const val signUpEmail = "2@a.com"
const val invalidEmail = "invalidemail"
const val notFoundEmail = "notfoundemail@a.com"
const val signInPassword = "111111"
const val wrongPassword = "123456"
const val signUpName = "test"
const val sendPasswordEmail = "boole92@naver.com"

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTest {
    private lateinit var userRepositoryTestUtil: UserRepositoryTestUtil
    private val dispatcher = StandardTestDispatcher()
    private suspend fun getUser() = userRepositoryTestUtil.getUser()
    private suspend fun TestScope.failRunCatching(
        errorCode: String,
        test: suspend () -> Unit,
    ) = runCatching {
        test()
    }.onSuccess { assert(false) }
        .onFailure {
            val firebaseAuthException = it as FirebaseAuthException
            assert(firebaseAuthException.errorCode == errorCode)
        }

    private suspend fun TestScope.successRunCatching(
        test: suspend () -> Unit,
    ) = runCatching {
        test()
    }.onSuccess { assert(true) }
        .onFailure { assert(false) }

    @Before
    fun init() {
        runBlocking { launch { Firebase.auth.signOut() }.join() }

        userRepositoryTestUtil =
            UserRepositoryTestUtil(repositoryProvider = RepositoryProvider(dispatcher))
    }

    @Test
    fun signInTest() = runTest(dispatcher) {
        userRepositoryTestUtil.userCollect(backgroundScope)
        assert(getUser() == null)

        failRunCatching(ERROR_INVALID_EMAIL) {
            userRepositoryTestUtil.signIn( // invalidEmail
                email = invalidEmail
            )
        }
        advanceUntilIdle()
        assert(getUser() == null)

        failRunCatching(ERROR_USER_NOT_FOUND) {
            userRepositoryTestUtil.signIn( // notFoundEmail
                email = notFoundEmail
            )
        }
        advanceUntilIdle()
        assert(getUser() == null)

        failRunCatching(ERROR_WRONG_PASSWORD) {
            userRepositoryTestUtil.signIn( // WrongPassword
                password = wrongPassword
            )
        }
        advanceUntilIdle()
        assert(getUser() == null)

        successRunCatching {
            userRepositoryTestUtil.signIn()
        }
        advanceUntilIdle()
        assert(getUser() != null)
        assert(getUser()?.email == signInEmail)

        userRepositoryTestUtil.signOut()
        withContext(Dispatchers.Default) {
            delay(500)
            assert(getUser() == null)
        }
    }

    @Test
    fun signUpTest() = runTest(dispatcher) {
        userRepositoryTestUtil.userCollect(backgroundScope)

        assert(getUser() == null)

        failRunCatching(ERROR_INVALID_EMAIL) {
            userRepositoryTestUtil.signUp( // invalidEmail
                email = invalidEmail
            )
        }
        advanceUntilIdle()
        assert(getUser() == null)

        failRunCatching(ERROR_EMAIL_ALREADY_IN_USE) {
            userRepositoryTestUtil.signUp( // EmailAlreadyInUse
                email = signInEmail
            )
        }
        advanceUntilIdle()
        assert(getUser() == null)

        successRunCatching {
            userRepositoryTestUtil.signUp()
        }
        advanceUntilIdle()
        assert(getUser() != null)
        assert(getUser()?.email == signUpEmail)
        assert(getUser()?.name == signUpName)

        userRepositoryTestUtil.deleteUser()

        userRepositoryTestUtil.signOut()
        withContext(Dispatchers.Default) {
            delay(1000)
            assert(getUser() == null)
        }
    }

    @Test
    fun resetPasswordTest() = runTest(dispatcher) {
        userRepositoryTestUtil.userCollect(backgroundScope)

        failRunCatching(ERROR_INVALID_EMAIL) {
            userRepositoryTestUtil.sendResetPasswordEmail( // invalidEmail
                email = invalidEmail
            )
        }
        advanceUntilIdle()

        failRunCatching(ERROR_USER_NOT_FOUND) {
            userRepositoryTestUtil.sendResetPasswordEmail( // userNotFound
                email = notFoundEmail
            )
        }
        advanceUntilIdle()

        successRunCatching {
            userRepositoryTestUtil.sendResetPasswordEmail()
        }
        advanceUntilIdle()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class UserFlowTestWithSignInStart {
    private lateinit var userRepositoryTestUtil: UserRepositoryTestUtil
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun init() {
        runBlocking {
            Firebase.auth.signInWithEmailAndPassword(signInEmail, signInPassword).await()
        }
        userRepositoryTestUtil = UserRepositoryTestUtil(RepositoryProvider(dispatcher))
    }

    @Test
    fun signInRun() = runTest(dispatcher) {
        userRepositoryTestUtil.userCollect(backgroundScope)

        suspend fun getUser() = userRepositoryTestUtil.getUser()

        assert(getUser() != null)
        assert(getUser()?.email == signInEmail)

        userRepositoryTestUtil.signOut()
        withContext(Dispatchers.Default) {
            delay(500)
            assert(getUser() == null)
        }

        userRepositoryTestUtil.signIn()
        advanceUntilIdle()
        assert(getUser() != null)
        assert(getUser()?.email == signInEmail)

        userRepositoryTestUtil.signOut()
        withContext(Dispatchers.Default) {
            delay(500)
            assert(getUser() == null)
        }
        userRepositoryTestUtil.signUp()
        advanceUntilIdle()
        assert(getUser() != null)
        assert(getUser()?.email == signUpEmail)
        assert(getUser()?.name == signUpName)

        userRepositoryTestUtil.deleteUser()

        userRepositoryTestUtil.signOut()
        withContext(Dispatchers.Default) {
            delay(500)
            assert(getUser() == null)
        }
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

    @Test
    fun signInRun() = runTest(dispatcher) {
        userRepositoryTestUtil.userCollect(backgroundScope)

        suspend fun getUser() = userRepositoryTestUtil.getUser()

        assert(getUser() == null)

        userRepositoryTestUtil.signIn()
        advanceUntilIdle()
        assert(getUser() != null)
        assert(getUser()?.email == signInEmail)

        userRepositoryTestUtil.signOut()
        withContext(Dispatchers.Default) {
            delay(500)
            assert(getUser() == null)
        }
        userRepositoryTestUtil.signUp()
        advanceUntilIdle()
        assert(getUser() != null)
        assert(getUser()?.email == signUpEmail)
        assert(getUser()?.name == signUpName)

        userRepositoryTestUtil.deleteUser()

        userRepositoryTestUtil.signOut()
        withContext(Dispatchers.Default) {
            delay(500)
            assert(getUser() == null)
        }
    }
}