package com.leebeebeom.clothinghelper.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.leebeebeom.clothinghelper.data.repository.FirebaseResult
import com.leebeebeom.clothinghelper.data.repository.UserRepositoryImpl
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_EMAIL_ALREADY_IN_USE
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_INVALID_EMAIL
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_USER_NOT_FOUND
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_WRONG_PASSWORD
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTest {
    private val email = "1@a.com"
    private val password = "111111"
    private val invalidEmail = "invalidEmail"
    private val notFoundEmail = "notFoundEmail@a.com"
    private val wrongPassword = "123456"
    private val name = "test"
    private lateinit var userRepository: UserRepository

    @Before
    fun init() {
        userRepository = UserRepositoryImpl()
    }

    @Test
    fun signInTest() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)

        userRepository.signIn( // InvalidEmail
            email = invalidEmail, password = password,
            firebaseResult = invalidEmailResult,
            dispatcher = dispatcher
        )

        userRepository.signIn( // UserNotFound
            email = notFoundEmail, password = password,
            firebaseResult = userNotFoundResult,
            dispatcher = dispatcher
        )

        userRepository.signIn( // WrongPassword
            email = email, password = wrongPassword,
            firebaseResult = wrongPasswordResult,
            dispatcher = dispatcher
        )

        userRepository.signIn( // success
            email = email, password = password,
            firebaseResult = signInSuccessResult,
            dispatcher = dispatcher
        )
    }

    @Test
    fun signUpTest() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)

        userRepository.signUp( // InvalidEmail
            email = invalidEmail,
            password = password,
            name = name,
            firebaseResult = invalidEmailResult,
            dispatcher = dispatcher
        )

        userRepository.signUp( // EmailAlreadyInUse
            email = email,
            password = password,
            name = name,
            firebaseResult = emailAlreadyInUserResult,
            dispatcher = dispatcher
        )

        userRepository.signUp( // success
            email = notFoundEmail,
            password = password,
            name = name,
            firebaseResult = signInSuccessResult,
            dispatcher = dispatcher
        )

        assert(FirebaseAuth.getInstance().currentUser!!.displayName == name)

        FirebaseAuth.getInstance().currentUser!!.delete()
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
            val user = FirebaseAuth.getInstance().currentUser
            assert(user != null)
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