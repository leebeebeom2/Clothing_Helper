package com.leebeebeom.clothinghelper.data.repository.preference

import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
fun preferenceSortFlowTest(repository: SortPreferenceRepository) = runTest {
    val sortFlow = repository.sort
    suspend fun assert(sort: SortPreferences) =
        assert(sortFlow.first() == sort)

    assert(sort = SortPreferences(sort = Sort.NAME, order = Order.ASCENDING))

    repository.changeSort(sort = Sort.EDIT)
    assert(sort = SortPreferences(sort = Sort.EDIT, order = Order.ASCENDING))

    repository.changeSort(sort = Sort.CREATE)
    assert(sort = SortPreferences(sort = Sort.CREATE, order = Order.ASCENDING))

    repository.changeSort(sort = Sort.NAME)
    assert(sort = SortPreferences(sort = Sort.NAME, order = Order.ASCENDING))

    repository.changeOrder(Order.DESCENDING)
    assert(sort = SortPreferences(sort = Sort.NAME, order = Order.DESCENDING))

    repository.changeOrder(Order.ASCENDING)
    assert(sort = SortPreferences(sort = Sort.NAME, order = Order.ASCENDING))
}