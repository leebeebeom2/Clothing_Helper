package com.leebeebeom.clothinghelper.data.repository

import android.util.Log
import com.leebeebeom.clothinghelper.data.SignInEmail
import com.leebeebeom.clothinghelper.data.SignInPassword
import com.leebeebeom.clothinghelper.data.waitTime
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher

const val DataLoadingTag = "loading"

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun TestScope.repositoryLoadingTest(
    userRepository: UserRepository,
    repository: BaseDataRepository<*>
) {
    userRepository.signOut()
    waitTime()

    Log.d(DataLoadingTag, "loadingCollect start")
    val loadingCollectJob =
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.loadingStream.collect {
                Log.d(DataLoadingTag, "loadingCollect: 호출 $it")
            }
        }
    Log.d(DataLoadingTag, "allDataCollect start")
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
        repository.allDataStream.collect()
    }
    waitTime()

    Log.d(DataLoadingTag, "sign in start")
    userRepository.signIn(email = SignInEmail, password = SignInPassword).join()
    waitTime()

    Log.d(DataLoadingTag, "sign out start")
    userRepository.signOut()
    waitTime()

    Log.d(DataLoadingTag, "sign in 2  start")
    userRepository.signIn(email = RepositoryTestEmail, password = SignInPassword).join()
    waitTime()

    Log.d(DataLoadingTag, "sign out 2 start")
    userRepository.signOut()
    waitTime()

    loadingCollectJob.cancel()
}