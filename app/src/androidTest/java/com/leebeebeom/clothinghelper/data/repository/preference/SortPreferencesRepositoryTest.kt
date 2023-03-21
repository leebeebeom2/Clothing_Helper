package com.leebeebeom.clothinghelper.data.repository.preference

import androidx.test.core.app.ApplicationProvider
import com.leebeebeom.clothinghelper.data.waitTime
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SortPreferencesRepositoryTest {
    private val dispatcher = StandardTestDispatcher()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val folderPreferencesRepository = FolderPreferencesRepositoryImpl(
        context = ApplicationProvider.getApplicationContext(),
        appScope = scope
    )
    private val subCategoryPreferencesRepository = SubCategoryPreferencesRepositoryImpl(
        context = ApplicationProvider.getApplicationContext(),
        appScope = scope
    )

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
        val sortFlow = repository.sortStream

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { sortFlow.collect() }

        assert(sortFlow.first() == SortPreferences()) // initial

        suspend fun assertSort(sort: SortPreferences) = assert(sortFlow.first() == sort)

        repository.changeSort(sort = Sort.Edit)
        waitTime()
        assertSort(sort = SortPreferences(sort = Sort.Edit, order = Order.Ascending))

        repository.changeSort(sort = Sort.Create)
        waitTime()
        assertSort(sort = SortPreferences(sort = Sort.Create, order = Order.Ascending))

        repository.changeSort(sort = Sort.Name)
        waitTime()
        assertSort(sort = SortPreferences(sort = Sort.Name, order = Order.Ascending))

        repository.changeOrder(Order.Descending)
        waitTime()
        assertSort(sort = SortPreferences(sort = Sort.Name, order = Order.Descending))

        repository.changeOrder(Order.Ascending)
        waitTime()
        assertSort(sort = SortPreferences(sort = Sort.Name, order = Order.Ascending))
    }
}