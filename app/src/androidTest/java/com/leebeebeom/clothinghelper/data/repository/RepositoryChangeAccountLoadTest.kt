package com.leebeebeom.clothinghelper.data.repository

import com.leebeebeom.clothinghelper.data.SignInEmail
import com.leebeebeom.clothinghelper.data.SignInPassword
import com.leebeebeom.clothinghelper.data.backgroundLaunch
import com.leebeebeom.clothinghelper.data.waitTime
import com.leebeebeom.clothinghelper.domain.model.BaseModel
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope

@OptIn(ExperimentalCoroutinesApi::class)
suspend inline fun <T : BaseModel> TestScope.repositoryChangeAccountLoadTest(
    repository: BaseDataRepository<T>,
    userRepository: UserRepository,
    addDataPair: Pair<T, T>,
    repositoryTestAccountSize: Int
) {
    backgroundLaunch { repository.allDataFlow.collect() }
    userRepository.signOut()

    userRepository.signIn(email = SignInEmail, password = SignInPassword) 
    waitTime()
    assert(repository.allDataFlow.first().data.isEmpty())

    repository.add(addDataPair.first) 
    repository.add(addDataPair.second) 
    waitTime()
    assert(repository.allDataFlow.first().data.size == 2)

    userRepository.signOut()
    waitTime()
    assert(repository.allDataFlow.first().data.isEmpty())

    userRepository.signIn(email = RepositoryTestEmail, password = SignInPassword) 
    waitTime()
    assert(repository.allDataFlow.first().data.size == repositoryTestAccountSize)

    userRepository.signOut()
    waitTime()
    assert(repository.allDataFlow.first().data.isEmpty())

    userRepository.signIn(email = SignInEmail, password = SignInPassword) 
    waitTime()
    assert(repository.allDataFlow.first().data.size == 2)
}