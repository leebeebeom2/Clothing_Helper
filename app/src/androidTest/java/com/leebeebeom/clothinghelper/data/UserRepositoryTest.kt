package com.leebeebeom.clothinghelper.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.leebeebeom.clothinghelper.data.repository.FirebaseResult
import com.leebeebeom.clothinghelper.data.repository.UserRepositoryImpl
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_EMAIL_ALREADY_IN_USE
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_INVALID_EMAIL
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_USER_NOT_FOUND
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_WRONG_PASSWORD
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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

    @Before
    fun init() {
        userRepository = UserRepositoryImpl(CoroutineScope(SupervisorJob() + Dispatchers.IO))
    }

    @Test
    fun userFlowTest() = runTest {
        FirebaseAuth.getInstance().signOut()

        val dispatcher = StandardTestDispatcher(testScheduler)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            userRepository.user.collectLatest {}
        }

        assert(userRepository.user.value == null)

        userRepository.signIn(
            email = "1@a.com", password = "111111", dispatcher = dispatcher,
            firebaseResult = signInSuccessResult
        )

        withContext(Dispatchers.Default) {
            delay(1000)
            assert(userRepository.user.value != null)
            assert(userRepository.user.value?.email == "1@a.com")
        }

        withContext(Dispatchers.Default) {
            userRepository.signOut()
            delay(1000)
            assert(userRepository.user.value == null)
        }

        userRepository.signUp(
            email = "2@a.com",
            password = "111111",
            name = "test",
            dispatcher = dispatcher,
            firebaseResult = signInSuccessResult
        )

        withContext(Dispatchers.Default) {
            delay(1000)
            assert(userRepository.user.value != null)
            assert(userRepository.user.value?.email == "2@a.com")
            assert(userRepository.user.value?.name == "test")
        }

        FirebaseAuth.getInstance().currentUser!!.delete()
    }

    @Test
    fun signInTest() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            userRepository.user.collect {}
        }

        withContext(Dispatchers.Default) {
            userRepository.signOut()
            delay(1000)
            assert(userRepository.user.value == null)
        }

        userRepository.signIn( // InvalidEmail
            email = invalidEmail, password = password,
            firebaseResult = invalidEmailResult,
            dispatcher = dispatcher
        )
        assert(userRepository.user.value == null)

        userRepository.signIn( // UserNotFound
            email = notFoundEmail, password = password,
            firebaseResult = userNotFoundResult,
            dispatcher = dispatcher
        )
        assert(userRepository.user.value == null)

        userRepository.signIn( // WrongPassword
            email = email, password = wrongPassword,
            firebaseResult = wrongPasswordResult,
            dispatcher = dispatcher
        )
        assert(userRepository.user.value == null)

        userRepository.signIn( // success
            email = email, password = password,
            firebaseResult = signInSuccessResult,
            dispatcher = dispatcher
        )
        withContext(Dispatchers.Default) {
            delay(1000)
            assert(userRepository.user.value != null)
            assert(userRepository.user.value!!.email == email)

            userRepository.signOut()
            delay(1000)
            assert(userRepository.user.value == null)
        }
    }

    @Test
    fun signUpTest() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            userRepository.user.collect {}
        }

        withContext(Dispatchers.Default) {
            userRepository.signOut()
            delay(1000)
            assert(userRepository.user.value == null)
        }

        userRepository.signUp( // InvalidEmail
            email = invalidEmail,
            password = password,
            name = name,
            firebaseResult = invalidEmailResult,
            dispatcher = dispatcher
        )
        assert(userRepository.user.value == null)

        userRepository.signUp( // EmailAlreadyInUse
            email = email,
            password = password,
            name = name,
            firebaseResult = emailAlreadyInUserResult,
            dispatcher = dispatcher
        )
        assert(userRepository.user.value == null)

        userRepository.signUp( // success
            email = notFoundEmail,
            password = password,
            name = name,
            firebaseResult = signInSuccessResult,
            dispatcher = dispatcher
        )

        withContext(Dispatchers.IO) {
            delay(1000)
            assert(userRepository.user.value != null)
            Log.d("TAG", "signUpTest: ${userRepository.user.value}")
            assert(userRepository.user.value?.email == notFoundEmail)
            assert(userRepository.user.value?.name == name)
        }

        FirebaseAuth.getInstance().currentUser!!.delete()

        withContext(Dispatchers.IO) {
            userRepository.signOut()
            delay(1000)
            assert(userRepository.user.value == null)
        }
    }

    @Test
    fun resetPasswordTest() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)

        userRepository.sendResetPasswordEmail( // invalidEmail
            email = invalidEmail, firebaseResult = invalidEmailResult, dispatcher = dispatcher
        )

        userRepository.sendResetPasswordEmail( // userNotFound
            email = notFoundEmail, firebaseResult = userNotFoundResult, dispatcher = dispatcher
        )

        userRepository.sendResetPasswordEmail(
            email = email, firebaseResult = object : FirebaseResult {
                override fun success() = assert(true)

                override fun fail(exception: Exception) = assert(false)
            }, dispatcher = dispatcher
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

    private val signInSuccessResult = object : FirebaseResult {
        override fun success() {
            assert(true)
        }

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