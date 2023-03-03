package com.leebeebeom.clothinghelper.domain.usecase.user

import com.leebeebeom.clothinghelper.data.repository.FirebaseResult
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignInUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun signIn(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        email: String, password: String, firebaseResult: FirebaseResult,
    ) = withContext(dispatcher) {
        userRepository.signIn(
            email = email,
            password = password,
            firebaseResult = firebaseResult,
            dispatcher = dispatcher
        )
    }
}