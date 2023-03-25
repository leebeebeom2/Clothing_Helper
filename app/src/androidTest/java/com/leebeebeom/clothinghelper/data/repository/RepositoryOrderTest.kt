package com.leebeebeom.clothinghelper.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.data.SignInEmail
import com.leebeebeom.clothinghelper.data.SignInPassword
import com.leebeebeom.clothinghelper.data.waitTime
import com.leebeebeom.clothinghelper.domain.model.BaseModel
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle

@OptIn(ExperimentalCoroutinesApi::class)
suspend inline fun <T : BaseModel> TestScope.repositoryOrderTest(
    repository: BaseDataRepository<T>,
    userRepository: UserRepository,
    initDataList: List<T>,
    assertOrder: (origin: List<T>, new: List<T>) -> Unit,
    refPath: DataBasePath
) {
    userRepository.signIn(email = SignInEmail, password = SignInPassword)
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { repository.allDataStream.collect() }
    waitTime()

    initDataList.shuffled().forEach { repository.add(it) }
    advanceUntilIdle()
    waitTime()
    assertOrder(initDataList, repository.allDataStream.first().data)

    Firebase.database.reference.child(userRepository.userStream.first()!!.uid).child(refPath.path)
        .removeValue().await()
}