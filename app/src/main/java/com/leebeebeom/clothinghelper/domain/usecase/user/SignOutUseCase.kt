package com.leebeebeom.clothinghelper.domain.usecase.user

import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun signOut(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        onFail: (Exception) -> Unit,
    ) = userRepository.signOut(onFail = onFail, dispatcher = dispatcher)
}