package com.leebeebeom.clothinghelper.data

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.data.repository.UserRepositoryImpl
import com.leebeebeom.clothinghelper.domain.model.User
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import com.leebeebeom.clothinghelper.isConnectedNetwork
import com.leebeebeom.clothinghelper.ui.util.FirebaseAuthErrorCode.ERROR_EMAIL_ALREADY_IN_USE
import com.leebeebeom.clothinghelper.ui.util.FirebaseAuthErrorCode.ERROR_INVALID_EMAIL
import com.leebeebeom.clothinghelper.ui.util.FirebaseAuthErrorCode.ERROR_USER_NOT_FOUND
import com.leebeebeom.clothinghelper.ui.util.FirebaseAuthErrorCode.ERROR_WRONG_PASSWORD
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

const val SignInEmail = "1@a.com"
const val SignUpEmail = "2@a.com"
const val InvalidEmail = "invalidemail"
const val NotFoundEmail = "notfoundemail@a.com"
const val SignInPassword = "111111"
const val WrongPassword = "123456"
const val SignUpName = "테스트 유저 2"
const val SendPasswordEmail = "boole92@naver.com"

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTest {
    private val dispatcher = StandardTestDispatcher()
    private lateinit var userRepository: UserRepository
    private lateinit var userFlow: SharedFlow<User?>

    private inline fun TestScope.failRunCatching(
        errorCode: String, test: () -> Unit,
    ) = runCatching {
        test()
    }.onSuccess { assert(false) }.onFailure {
        val firebaseAuthException = it as FirebaseAuthException
        assert(firebaseAuthException.errorCode == errorCode)
    }

    private inline fun TestScope.successRunCatching(
        test: () -> Unit,
    ) = runCatching {
        test()
    }.onSuccess { assert(true) }.onFailure {
        println(it)
        assert(false)
    }

    @Before
    fun init() {
        runBlocking {
            Firebase.auth.signOut()
            delay(1000)
        }

        userRepository = UserRepositoryImpl(
            appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
            dispatcherIO = dispatcher
        )
        userFlow = userRepository.userFlow
    }

    @After
    fun signOut() {
        Firebase.auth.signOut()
    }

    @Test
    fun signInTest() = runTest(dispatcher) {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { userFlow.collect() }

        failRunCatching(errorCode = ERROR_INVALID_EMAIL) { // invalidEmail
            userRepository.signIn(email = InvalidEmail, password = SignInPassword)
        }
        failRunCatching(errorCode = ERROR_USER_NOT_FOUND) { // notFoundEmail
            userRepository.signIn(email = NotFoundEmail, password = SignInPassword)
        }
        failRunCatching(errorCode = ERROR_WRONG_PASSWORD) { // wrongPassword
            userRepository.signIn(email = SignInEmail, password = WrongPassword)
        }

        successRunCatching {
            userRepository.signIn(email = SignInEmail, password = SignInPassword)
        }
        val signedUser = userFlow.first()
        assert(signedUser != null)
        assert(signedUser?.email == SignInEmail)
    }

    @Test
    fun signUpTest() = runTest(dispatcher) {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { userFlow.collect() }

        failRunCatching(errorCode = ERROR_INVALID_EMAIL) { // invalidEmail
            userRepository.signUp(
                email = InvalidEmail, password = SignInPassword, name = SignUpName
            )
        }

        failRunCatching(errorCode = ERROR_EMAIL_ALREADY_IN_USE) { // emailAlreadyInUse
            userRepository.signUp(
                email = SignInEmail, password = SignInPassword, name = SignUpName
            )
        }

        successRunCatching {
            userRepository.signUp(
                email = SignUpEmail, password = SignInPassword, name = SignUpName
            )
        }
        val signedUser = userFlow.first()
        assert(signedUser?.email == SignUpEmail)
        assert(signedUser?.name == SignUpName)

        Firebase.auth.currentUser!!.delete()
    }

    @Test
    fun resetPasswordTest() = runTest(dispatcher) {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { userFlow.collect() }

        failRunCatching(errorCode = ERROR_INVALID_EMAIL) { // invalidEmail
            userRepository.sendResetPasswordEmail(email = InvalidEmail)
        }
        failRunCatching(errorCode = ERROR_USER_NOT_FOUND) { // userNotFound
            userRepository.sendResetPasswordEmail(email = NotFoundEmail)
        }
        successRunCatching { userRepository.sendResetPasswordEmail(email = SendPasswordEmail) }
    }

    @Test
    fun networkExceptionTest() = runTest(dispatcher) {
        assert(!isConnectedNetwork()) // turn on airplane mode

        suspend fun networkExceptionRunCatching(test: suspend () -> Unit) {
            runCatching {
                test()
            }.onSuccess { assert(false) }.onFailure { assert(it is FirebaseNetworkException) }
        }

        networkExceptionRunCatching {
            userRepository.signIn(email = SignInEmail, password = SignInPassword)
        }

        networkExceptionRunCatching {
            userRepository.signUp(email = SignUpEmail, password = SignInPassword, name = SignUpName)
        }

        networkExceptionRunCatching {
            userRepository.sendResetPasswordEmail(email = SendPasswordEmail)
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class RunWithSignInStartTest {
    private val dispatcher = StandardTestDispatcher()
    private lateinit var userRepository: UserRepository
    private lateinit var userFlow: SharedFlow<User?>

    @Before
    fun init() {
        runBlocking {
            Firebase.auth.signInWithEmailAndPassword(SignInEmail, SignInPassword).await()
        }

        userRepository = UserRepositoryImpl(
            appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
            dispatcherIO = dispatcher
        )
        userFlow = userRepository.userFlow
    }

    @After
    fun signOut() {
        Firebase.auth.signOut()
    }

    @Test
    fun runWithSignIn() = runTest(dispatcher) {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { userFlow.collect() }

        assert(userFlow.first()?.email == SignInEmail)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class RunWithSignOutStart {
    private val dispatcher = StandardTestDispatcher()
    private lateinit var userRepository: UserRepository
    private lateinit var userFlow: SharedFlow<User?>

    @Before
    fun init() {
        runBlocking {
            Firebase.auth.signOut()
            delay(1000)
        }
        userRepository = UserRepositoryImpl(
            appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
            dispatcherIO = dispatcher
        )
        userFlow = userRepository.userFlow
    }

    @After
    fun signOut() {
        Firebase.auth.signOut()
    }

    @Test
    fun runWithSignOut() = runTest(dispatcher) {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { userFlow.collect() }

        assert(userFlow.first() == null)
    }
}