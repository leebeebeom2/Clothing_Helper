package com.leebeebeom.clothinghelper.domain.usecase.todo

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.DataRepositoryTestUtil
import com.leebeebeom.clothinghelper.data.addUseCaseTest
import com.leebeebeom.clothinghelper.domain.model.Todo
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddTodoUseCaseTest {
    private lateinit var dataRepositoryTestUtil: DataRepositoryTestUtil<Todo, TodoRepository>
    private lateinit var addTodoUseCase: AddTodoUseCase
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun init() {
        val repositoryProvider = RepositoryProvider(dispatcher)
        dataRepositoryTestUtil =
            DataRepositoryTestUtil(
                repositoryProvider = repositoryProvider,
                repository = repositoryProvider.createTodoRepository()
            )
        addTodoUseCase = AddTodoUseCase(todoRepository = dataRepositoryTestUtil.repository)
    }

    @Test
    fun addTodoUseCaseTest() = runTest(dispatcher) {
        addUseCaseTest(
            dataRepositoryTestUtil = dataRepositoryTestUtil,
            add = {
                addTodoUseCase.add(
                    dataRepositoryTestUtil.userRepositoryTestUtil.uid!!,
                    it,
                    done = false,
                    order = 0,
                    onFail = { assert(false) }
                )
            },
            addAssert = { addedData, text ->
                assert(addedData.text == text)
            },
            addListAssert = { addedDataList, textList ->
                assert(addedDataList.map { it.text } == textList)
            }
        )
    }
}