package com.leebeebeom.clothinghelper.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.repository.FirebaseResult
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_EMAIL_ALREADY_IN_USE
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_INVALID_EMAIL
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_USER_NOT_FOUND
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_WRONG_PASSWORD
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTest {
    private val email = "1@a.com"
    private val password = "111111"
    private val invalidEmail = "invalidemail"
    private val notFoundEmail = "notfoundemail@a.com"
    private val wrongPassword = "123456"
    private val name = "test"
    private lateinit var userRepository: UserRepository
    private val dispatcher = StandardTestDispatcher()
    private val repositoryProvider = RepositoryProvider(dispatcher = dispatcher)

    @Before
    fun init() {
        FirebaseAuth.getInstance().signOut()
        userRepository = repositoryProvider.getUserRepository()
    }

    @Test
    fun userFlowTest() = runTest(dispatcher) {

        backgroundScope.launch(dispatcher) { userRepository.user.collectLatest {} }

        assert(userRepository.user.value == null)

        userRepository.signIn(
            email = "1@a.com",
            password = "111111",
            dispatcher = dispatcher,
            firebaseResult = successResult
        )
        advanceUntilIdle()

        assert(userRepository.user.value != null)
        assert(userRepository.user.value?.email == "1@a.com")

        userRepository.signOut()
        advanceUntilIdle()

        assert(userRepository.user.value == null)

        userRepository.signUp(
            email = "2@a.com",
            password = "111111",
            name = "test",
            dispatcher = dispatcher,
            firebaseResult = successResult
        )
        advanceUntilIdle()

        assert(userRepository.user.value != null)
        assert(userRepository.user.value?.email == "2@a.com")
        assert(userRepository.user.value?.name == "test")

        FirebaseAuth.getInstance().currentUser!!.delete()
    }

    @Test
    fun signInTest() = runTest(dispatcher) {
        backgroundScope.launch(dispatcher) { userRepository.user.collect {} }

        assert(userRepository.user.value == null)

        userRepository.signIn( // InvalidEmail
            email = invalidEmail,
            password = password,
            firebaseResult = invalidEmailResult,
            dispatcher = dispatcher
        )
        advanceUntilIdle()
        assert(userRepository.user.value == null)

        userRepository.signIn( // UserNotFound
            email = notFoundEmail,
            password = password,
            firebaseResult = userNotFoundResult,
            dispatcher = dispatcher
        )
        advanceUntilIdle()
        assert(userRepository.user.value == null)

        userRepository.signIn( // WrongPassword
            email = email,
            password = wrongPassword,
            firebaseResult = wrongPasswordResult,
            dispatcher = dispatcher
        )
        advanceUntilIdle()
        assert(userRepository.user.value == null)

        userRepository.signIn( // success
            email = email,
            password = password,
            firebaseResult = successResult,
            dispatcher = dispatcher
        )
        advanceUntilIdle()
        assert(userRepository.user.value != null)
        assert(userRepository.user.value!!.email == email)

        userRepository.signOut()
        advanceUntilIdle()
        assert(userRepository.user.value == null)
    }

    @Test
    fun signUpTest() = runTest(dispatcher) {
        backgroundScope.launch(dispatcher) { userRepository.user.collect {} }

        assert(userRepository.user.value == null)

        userRepository.signUp( // InvalidEmail
            email = invalidEmail,
            password = password,
            name = name,
            firebaseResult = invalidEmailResult,
            dispatcher = dispatcher
        )
        advanceUntilIdle()
        assert(userRepository.user.value == null)

        userRepository.signUp( // EmailAlreadyInUse
            email = email,
            password = password,
            name = name,
            firebaseResult = emailAlreadyInUserResult,
            dispatcher = dispatcher
        )
        advanceUntilIdle()
        assert(userRepository.user.value == null)

        userRepository.signUp( // success
            email = notFoundEmail,
            password = password,
            name = name,
            firebaseResult = successResult,
            dispatcher = dispatcher
        )
        advanceUntilIdle()
        assert(userRepository.user.value != null)
        assert(userRepository.user.value?.email == notFoundEmail)
        assert(userRepository.user.value?.name == name)

        FirebaseAuth.getInstance().currentUser!!.delete()
    }

    @Test
    fun resetPasswordTest() = runTest(dispatcher) {
        userRepository.sendResetPasswordEmail( // invalidEmail
            email = invalidEmail, firebaseResult = invalidEmailResult, dispatcher = dispatcher
        )

        userRepository.sendResetPasswordEmail( // userNotFound
            email = notFoundEmail, firebaseResult = userNotFoundResult, dispatcher = dispatcher
        )

        userRepository.sendResetPasswordEmail(
            email = email, firebaseResult = successResult, dispatcher = dispatcher
        )
    }

    private val invalidEmailResult = object : FirebaseResult {
        override fun success() = assert(false)

        override fun fail(exception: Exception) {
            val firebaseAuthException = exception as FirebaseAuthException
            assert(firebaseAuthException.errorCode == ERROR_INVALID_EMAIL)
        }
    }

    private val userNotFoundResult = object : FirebaseResult {
        override fun success() = assert(false)

        override fun fail(exception: Exception) {
            val firebaseAuthException = exception as FirebaseAuthException
            assert(firebaseAuthException.errorCode == ERROR_USER_NOT_FOUND)
        }
    }

    private val wrongPasswordResult = object : FirebaseResult {
        override fun success() = assert(false)

        override fun fail(exception: Exception) {
            val firebaseAuthException = exception as FirebaseAuthException
            assert(firebaseAuthException.errorCode == ERROR_WRONG_PASSWORD)
        }
    }

    private val successResult = object : FirebaseResult {
        override fun success() = assert(true)

        override fun fail(exception: Exception) = assert(false)
    }

    private val emailAlreadyInUserResult = object : FirebaseResult {
        override fun success() = assert(false)

        override fun fail(exception: Exception) {
            val firebaseAuthException = exception as FirebaseAuthException
            assert(firebaseAuthException.errorCode == ERROR_EMAIL_ALREADY_IN_USE)
        }
    }
}