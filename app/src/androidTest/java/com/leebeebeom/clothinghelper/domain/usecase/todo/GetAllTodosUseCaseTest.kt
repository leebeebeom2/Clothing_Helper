package com.leebeebeom.clothinghelper.domain.usecase.todo

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.DataRepositoryTestUtil
import com.leebeebeom.clothinghelper.data.getAllDataUseCaseTest
import com.leebeebeom.clothinghelper.domain.model.Todo
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetAllTodosUseCaseTest {
    private lateinit var getAllTodosUseCase: GetAllTodosUseCase
    private lateinit var dataRepositoryTestUtil: DataRepositoryTestUtil<Todo, TodoRepository>
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun init() {
        val repositoryProvider = RepositoryProvider(dispatcher)
        dataRepositoryTestUtil =
            DataRepositoryTestUtil(repositoryProvider, repositoryProvider.createTodoRepository())
        getAllTodosUseCase = GetAllTodosUseCase(dataRepositoryTestUtil.repository)
    }

    @Test
    fun getAllDataTest() = runTest(dispatcher) {
        getAllDataUseCaseTest(
            dataRepositoryTestUtil = dataRepositoryTestUtil,
            initDataList = initDataList,
            allDataFlow = getAllTodosUseCase.allTodos,
            assertLoadedData = { initDataList, loadedDataList ->
                assert(initDataList.map { it.text } == loadedDataList.map { it.text })
            }
        )
    }

    private val initDataList = List(8) { Todo(text = "test $it") }
}