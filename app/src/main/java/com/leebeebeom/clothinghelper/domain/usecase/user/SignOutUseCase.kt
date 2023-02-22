package com.leebeebeom.clothinghelper.domain.usecase.user

import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import com.leebeebeom.clothinghelper.domain.usecase.AllLocalDataClearUseCase
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SignOutUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val allLocalDataClearUseCase: AllLocalDataClearUseCase,
) {
    suspend fun signOut(onFail: (Exception) -> Unit) = userRepository.signOut(
        onFail = onFail,
        allLocalDataClear = allLocalDataClearUseCase::allLocalDataClear
    )
}