package com.leebeebeom.clothinghelper.domain.usecase.user

import com.leebeebeom.clothinghelper.di.AppScope
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import com.leebeebeom.clothinghelper.domain.usecase.BaseGetIsDataLoadingStateUseCase
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class GetSignInLoadingStateUseCase @Inject constructor(
    userRepository: UserRepository,
    @AppScope appScope: CoroutineScope,
) : BaseGetIsDataLoadingStateUseCase(repository = userRepository, appScope = appScope)