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
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SortPreferencesRepositoryTest {
    private lateinit var folderPreferencesRepository: SortPreferenceRepository
    private lateinit var subCategoryPreferencesRepository: SortPreferenceRepository
    private val dispatcher = StandardTestDispatcher()


    @Before
    fun init() {
        val repositoryProvider = RepositoryProvider(dispatcher)
        folderPreferencesRepository = repositoryProvider.createFolderPreferenceRepository()
        subCategoryPreferencesRepository =
            repositoryProvider.createSubCategoryPreferenceRepository()
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

        suspend fun assert(sort: SortPreferences) = assert(sortFlow.first() == sort)

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
}