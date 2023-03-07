package com.leebeebeom.clothinghelper.domain.usecase.todo

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.DataRepositoryTestUtil
import com.leebeebeom.clothinghelper.data.dataLoadingFlowTest
import com.leebeebeom.clothinghelper.domain.model.Todo
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetIsTodoLoadingStateUseCaseTest {
    private lateinit var getIsFolderLoadingStateUseCase: GetIsTodoLoadingStateUseCase
    private lateinit var dataRepositoryTestUtil: DataRepositoryTestUtil<Todo, TodoRepository>
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun init() {
        val repositoryProvider = RepositoryProvider(dispatcher)
        dataRepositoryTestUtil =
            DataRepositoryTestUtil(
                repositoryProvider = repositoryProvider,
                repository = repositoryProvider.createTodoRepository()
            )
        getIsFolderLoadingStateUseCase =
            GetIsTodoLoadingStateUseCase(dataRepositoryTestUtil.repository)
    }

    @Test
    fun folderLoadingFlowTest() = runTest(dispatcher) {
        dataLoadingFlowTest(
            dataRepositoryTestUtil = dataRepositoryTestUtil,
            initDataList = initDataList,
            loadingFlow = getIsFolderLoadingStateUseCase.isLoading
        )
    }

    private val initDataList = List(20) { Todo(text = "test $it") }
}