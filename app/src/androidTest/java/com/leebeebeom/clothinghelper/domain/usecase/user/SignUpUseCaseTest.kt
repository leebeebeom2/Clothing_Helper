package com.leebeebeom.clothinghelper.domain.usecase.user

import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.emailAlreadyInUserResult
import com.leebeebeom.clothinghelper.data.invalidEmailResult
import com.leebeebeom.clothinghelper.data.successResult
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignUpUseCaseTest {
    lateinit var userRepository: UserRepository
    lateinit var signUpUseCase: SignUpUseCase
    private val dispatcher = StandardTestDispatcher()
    private val repositoryProvider = RepositoryProvider(dispatcher)

    @Before
    fun init() {
        userRepository = repositoryProvider.getUserRepository()
        signUpUseCase = SignUpUseCase(userRepository)
    }

    @Test
    fun signUpTest() = runTest(dispatcher) {
        var email = "invalidEmail"
        val password = "111111"
        val name = "test"

        userRepository.signUp(
            email = email,
            password = password,
            name = name,
            firebaseResult = invalidEmailResult,
            dispatcher = dispatcher
        )
        advanceUntilIdle()
        assert(userRepository.user.value == null)

        email = "1@a.com"
        userRepository.signUp(
            email = email,
            password = password,
            name = name,
            firebaseResult = emailAlreadyInUserResult,
            dispatcher = dispatcher
        )
        advanceUntilIdle()
        assert(userRepository.user.value == null)

        email = "2@a.com"
        userRepository.signUp(
            email = email,
            password = password,
            name = name,
            firebaseResult = successResult,
            dispatcher = dispatcher
        )
        advanceUntilIdle()
        assert(userRepository.user.value != null)
        assert(userRepository.user.value!!.email == email)
        assert(userRepository.user.value!!.name == name)

        FirebaseAuth.getInstance().currentUser!!.delete()
    }
}