package com.leebeebeom.clothinghelper.domain.usecase.user

import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    val userStream get() = userRepository.userStream

    fun getUser() = userRepository.getUser()
}