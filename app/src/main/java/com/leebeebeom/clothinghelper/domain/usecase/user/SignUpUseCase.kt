package com.leebeebeom.clothinghelper.domain.usecase.user

import com.leebeebeom.clothinghelper.data.repository.FirebaseResult
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun signUp(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        email: String,
        password: String,
        name: String,
        firebaseResult: FirebaseResult,
    ) = userRepository.signUp(
        email = email,
        password = password,
        name = name,
        firebaseResult = firebaseResult,
        dispatcher = dispatcher
    )
}