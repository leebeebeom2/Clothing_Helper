package com.leebeebeom.clothinghelper.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.data.SignInEmail
import com.leebeebeom.clothinghelper.data.SignInPassword
import com.leebeebeom.clothinghelper.data.repository.preference.Order
import com.leebeebeom.clothinghelper.data.repository.preference.Sort
import com.leebeebeom.clothinghelper.data.waitTime
import com.leebeebeom.clothinghelper.domain.model.BaseContainerModel
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T : BaseContainerModel> TestScope.containerSortTest(
    userRepository: UserRepository,
    repository: BaseDataRepository<T>,
    preferencesRepository: SortPreferenceRepository,
    data: List<T>,
) {
    suspend fun sortInit() {
        preferencesRepository.changeSort(Sort.Name)
        preferencesRepository.changeOrder(Order.Ascending)
    }

    sortInit()

    userRepository.signIn(email = SignInEmail, password = SignInPassword)
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { repository.allDataStream.collect() }
    waitTime()
    assert(repository.allDataStream.first().data.isEmpty())

    data.forEach { repository.add(it) }
    advanceUntilIdle()
    waitTime()
    val addedData = repository.allDataStream.first().data
    assert(addedData.size == data.size)

    assert(addedData.sortedBy { it.name } == repository.allDataStream.first().data) // Sort: Name, Order: Ascending

    preferencesRepository.changeOrder(Order.Descending)
    waitTime()
    assert(addedData.sortedByDescending { it.name } == repository.allDataStream.first().data) // Sort: Name, Order: Descending

    preferencesRepository.changeOrder(Order.Ascending)
    waitTime()
    assert(addedData.sortedBy { it.name } == repository.allDataStream.first().data) // Sort: Name, Order: Ascending

    preferencesRepository.changeSort(Sort.Create)
    waitTime()
    assert(addedData.sortedBy { it.createDate } == repository.allDataStream.first().data) // Sort: CREATE, Order: Ascending

    preferencesRepository.changeOrder(Order.Descending)
    waitTime()
    assert(addedData.sortedByDescending { it.createDate } == repository.allDataStream.first().data) // Sort: CREATE, Order: Descending

    preferencesRepository.changeOrder(Order.Ascending)
    waitTime()
    assert(addedData.sortedBy { it.createDate } == repository.allDataStream.first().data) // Sort: CREATE, Order: Ascending

    preferencesRepository.changeSort(Sort.Edit)
    waitTime()
    assert(addedData.sortedBy { it.editDate } == repository.allDataStream.first().data) // Sort: EDIT, Order: Ascending

    preferencesRepository.changeOrder(Order.Descending)
    waitTime()
    assert(addedData.sortedByDescending { it.editDate } == repository.allDataStream.first().data)  // Sort: EDIT, Order: Descending

    preferencesRepository.changeOrder(Order.Ascending)
    waitTime()
    assert(addedData.sortedBy { it.editDate } == repository.allDataStream.first().data)  // Sort: EDIT, Order: Ascending

    sortInit()

    Firebase.database.reference.child(userRepository.userStream.first()!!.uid).removeValue()
}