package com.leebeebeom.clothinghelper.data.repository.preference

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SortPreferencesRepositoryTest {
    private val dispatcher = StandardTestDispatcher()
    private val repositoryProvider = RepositoryProvider(dispatcher)
    private val folderPreferencesRepository = repositoryProvider.createFolderPreferenceRepository()
    private val subCategoryPreferencesRepository =
        repositoryProvider.createSubCategoryPreferenceRepository()

    @After
    fun init() = runTest(dispatcher) {
        folderPreferencesRepository.changeSort(Sort.Name)
        folderPreferencesRepository.changeOrder(Order.Ascending)
        subCategoryPreferencesRepository.changeSort(Sort.Name)
        subCategoryPreferencesRepository.changeOrder(Order.Ascending)
    }

    @Test
    fun sotFlowTest() = runTest(dispatcher) {
        preferenceSortFlowTest(repository = folderPreferencesRepository)
        preferenceSortFlowTest(repository = subCategoryPreferencesRepository)
    }

    private suspend fun TestScope.preferenceSortFlowTest(repository: SortPreferenceRepository) {
        val sortFlow = repository.sort

        backgroundScope.launch(dispatcher) { sortFlow.collect() }

        assert(sortFlow.first() == SortPreferences()) // initial

        suspend fun assertSort(sort: SortPreferences) = assert(sortFlow.first() == sort)

        assertSort(sort = SortPreferences(sort = Sort.Name, order = Order.Ascending)) // initial

        repository.changeSort(sort = Sort.Edit)
        assertSort(sort = SortPreferences(sort = Sort.Edit, order = Order.Ascending))

        repository.changeSort(sort = Sort.Create)
        assertSort(sort = SortPreferences(sort = Sort.Create, order = Order.Ascending))

        repository.changeSort(sort = Sort.Name)
        assertSort(sort = SortPreferences(sort = Sort.Name, order = Order.Ascending))

        repository.changeOrder(Order.Descending)
        assertSort(sort = SortPreferences(sort = Sort.Name, order = Order.Descending))

        repository.changeOrder(Order.Ascending)
        assertSort(sort = SortPreferences(sort = Sort.Name, order = Order.Ascending))
    }
}