package com.leebeebeom.clothinghelper.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.repository.FirebaseResult
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_EMAIL_ALREADY_IN_USE
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_INVALID_EMAIL
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_USER_NOT_FOUND
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_WRONG_PASSWORD
import kotlinx.coroutines.*
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

    @Before
    fun init() {
        firebaseSignOut()

        userRepositoryTestUtil =
            UserRepositoryTestUtil(repositoryProvider = RepositoryProvider(dispatcher))
    }

    @Test
    fun userFlowTest() = runTest(dispatcher) {
        userRepositoryTestUtil.assertSignOut()

        signInAndAssert(userRepositoryTestUtil = userRepositoryTestUtil)

        signOutAndAssert(userRepositoryTestUtil = userRepositoryTestUtil)

        signUpAndAssert(userRepositoryTestUtil = userRepositoryTestUtil)

        userRepositoryTestUtil.deleteUser()

        signOutAndAssert(userRepositoryTestUtil = userRepositoryTestUtil)
    }

    @Test
    fun signInTest() = runTest(dispatcher) {
        userRepositoryTestUtil.assertSignOut()

        userRepositoryTestUtil.signIn( // invalidEmail
            email = invalidEmail, firebaseResult = invalidEmailResult
        )
        advanceUntilIdle()
        userRepositoryTestUtil.assertSignOut()

        userRepositoryTestUtil.signIn( // notFoundEmail
            email = notFoundEmail, firebaseResult = userNotFoundResult
        )
        advanceUntilIdle()
        userRepositoryTestUtil.assertSignOut()

        userRepositoryTestUtil.signIn( // WrongPassword
            password = wrongPassword, firebaseResult = wrongPasswordResult
        )
        advanceUntilIdle()
        userRepositoryTestUtil.assertSignOut()

        signInAndAssert(userRepositoryTestUtil = userRepositoryTestUtil)
        signOutAndAssert(userRepositoryTestUtil = userRepositoryTestUtil)
    }

    @Test
    fun signUpTest() = runTest(dispatcher) {
        userRepositoryTestUtil.assertSignOut()

        userRepositoryTestUtil.signUp(
            // invalidEmail
            email = invalidEmail,
            firebaseResult = invalidEmailResult,
        )
        advanceUntilIdle()
        userRepositoryTestUtil.assertSignOut()

        userRepositoryTestUtil.signUp(
            // EmailAlreadyInUse
            email = signInEmail,
            firebaseResult = emailAlreadyInUserResult,
        )
        advanceUntilIdle()
        userRepositoryTestUtil.assertSignOut()

        userRepositoryTestUtil.signUp()
        advanceUntilIdle()
        userRepositoryTestUtil.assertSignUp()

        userRepositoryTestUtil.deleteUser()
    }

    @Test
    fun resetPasswordTest() = runTest(dispatcher) {
        userRepositoryTestUtil.sendResetPasswordEmail( // invalidEmail
            email = invalidEmail,
            firebaseResult = invalidEmailResult
        )
        advanceUntilIdle()

        userRepositoryTestUtil.sendResetPasswordEmail( // userNotFound
            email = notFoundEmail,
            firebaseResult = userNotFoundResult
        )
        advanceUntilIdle()

        userRepositoryTestUtil.sendResetPasswordEmail()
        advanceUntilIdle()
    }
}

val invalidEmailResult = object : FirebaseResult {
    override fun success() = assert(false)

    override fun fail(exception: Exception) {
        val firebaseAuthException = exception as FirebaseAuthException
        assert(firebaseAuthException.errorCode == ERROR_INVALID_EMAIL)
    }
}

val userNotFoundResult = object : FirebaseResult {
    override fun success() = assert(false)

    override fun fail(exception: Exception) {
        val firebaseAuthException = exception as FirebaseAuthException
        assert(firebaseAuthException.errorCode == ERROR_USER_NOT_FOUND)
    }
}

val wrongPasswordResult = object : FirebaseResult {
    override fun success() = assert(false)

    override fun fail(exception: Exception) {
        val firebaseAuthException = exception as FirebaseAuthException
        assert(firebaseAuthException.errorCode == ERROR_WRONG_PASSWORD)
    }
}

val successResult = object : FirebaseResult {
    override fun success() = assert(true)

    override fun fail(exception: Exception) = assert(false)
}

val emailAlreadyInUserResult = object : FirebaseResult {
    override fun success() = assert(false)

    override fun fail(exception: Exception) {
        val firebaseAuthException = exception as FirebaseAuthException
        assert(firebaseAuthException.errorCode == ERROR_EMAIL_ALREADY_IN_USE)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun TestScope.signOutAndAssert(userRepositoryTestUtil: UserRepositoryTestUtil) {
    userRepositoryTestUtil.signOut()
    advanceUntilIdle()
    userRepositoryTestUtil.assertSignOut()
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun TestScope.signUpAndAssert(userRepositoryTestUtil: UserRepositoryTestUtil) {
    userRepositoryTestUtil.signUp()
    advanceUntilIdle()
    userRepositoryTestUtil.assertSignUp()
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun TestScope.signInAndAssert(userRepositoryTestUtil: UserRepositoryTestUtil) {
    userRepositoryTestUtil.signIn()
    advanceUntilIdle()
    userRepositoryTestUtil.assertSignIn()
}

private fun firebaseSignOut() = FirebaseAuth.getInstance().signOut()