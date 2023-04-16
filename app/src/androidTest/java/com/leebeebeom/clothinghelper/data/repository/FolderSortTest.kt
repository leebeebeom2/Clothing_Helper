package com.leebeebeom.clothinghelper.data.repository

import com.leebeebeom.clothinghelper.data.SignInEmail
import com.leebeebeom.clothinghelper.data.SignInPassword
import com.leebeebeom.clothinghelper.data.backgroundLaunch
import com.leebeebeom.clothinghelper.data.repository.preference.Order
import com.leebeebeom.clothinghelper.data.repository.preference.Sort
import com.leebeebeom.clothinghelper.data.waitTime
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.FolderPreferenceRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun TestScope.folderSortTest(
    userRepository: UserRepository,
    repository: FolderRepository,
    preferencesRepository: FolderPreferenceRepository,
    data: List<Folder>,
) {
    suspend fun sortInit() {
        preferencesRepository.changeSort(Sort.Name)
        preferencesRepository.changeOrder(Order.Ascending)
    }

    sortInit()

    userRepository.signIn(email = SignInEmail, password = SignInPassword)
    backgroundLaunch { repository.allDataFlow.collect() }
    waitTime()
    assert(repository.allDataFlow.first().data.isEmpty())

    data.map { coroutineScope { async { repository.add(it) } } }.awaitAll()
    waitTime()
    val addedData = repository.allDataFlow.first().data
    assert(addedData.size == data.size)

    assert(addedData.sortedBy { it.name } == repository.allDataFlow.first().data) // Sort: Name, Order: Ascending

    preferencesRepository.changeOrder(Order.Descending)
    waitTime()
    assert(addedData.sortedByDescending { it.name } == repository.allDataFlow.first().data) // Sort: Name, Order: Descending

    preferencesRepository.changeOrder(Order.Ascending)
    waitTime()
    assert(addedData.sortedBy { it.name } == repository.allDataFlow.first().data) // Sort: Name, Order: Ascending

    preferencesRepository.changeSort(Sort.Create)
    waitTime()
    assert(addedData.sortedBy { it.createDate } == repository.allDataFlow.first().data) // Sort: CREATE, Order: Ascending

    preferencesRepository.changeOrder(Order.Descending)
    waitTime()
    assert(addedData.sortedByDescending { it.createDate } == repository.allDataFlow.first().data) // Sort: CREATE, Order: Descending

    preferencesRepository.changeOrder(Order.Ascending)
    waitTime()
    assert(addedData.sortedBy { it.createDate } == repository.allDataFlow.first().data) // Sort: CREATE, Order: Ascending

    preferencesRepository.changeSort(Sort.Edit)
    waitTime()
    assert(addedData.sortedBy { it.editDate } == repository.allDataFlow.first().data) // Sort: EDIT, Order: Ascending

    preferencesRepository.changeOrder(Order.Descending)
    waitTime()
    assert(addedData.sortedByDescending { it.editDate } == repository.allDataFlow.first().data)  // Sort: EDIT, Order: Descending

    preferencesRepository.changeOrder(Order.Ascending)
    waitTime()
    assert(addedData.sortedBy { it.editDate } == repository.allDataFlow.first().data)  // Sort: EDIT, Order: Ascending

    sortInit()
}