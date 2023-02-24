package com.leebeebeom.clothinghelper.domain.usecase.user

import com.leebeebeom.clothinghelper.domain.model.User
import com.leebeebeom.clothinghelper.domain.model.toUser
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val appCoroutineScope: CoroutineScope,
) {
    private lateinit var user: StateFlow<User?>
    fun getUser(): StateFlow<User?> = if (::user.isInitialized) user
    else {
        user = userRepository.firebaseUser.map { it?.toUser() }.stateIn(
            scope = appCoroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )
        user
    }
}