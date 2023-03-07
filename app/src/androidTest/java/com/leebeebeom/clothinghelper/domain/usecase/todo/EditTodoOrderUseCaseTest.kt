package com.leebeebeom.clothinghelper.domain.usecase.todo

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.DataRepositoryTestUtil
import com.leebeebeom.clothinghelper.data.editUseCaseTest
import com.leebeebeom.clothinghelper.domain.model.Todo
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EditTodoOrderUseCaseTest {
    private lateinit var editTodoOrderUseCaseTest: EditTodoOrderUseCase
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
        editTodoOrderUseCaseTest =
            EditTodoOrderUseCase(todoRepository = dataRepositoryTestUtil.repository)
    }

    @Test
    fun todoOrderEditTest() = runTest(dispatcher) {
        val todo = Todo(text = "test")
        val editOrder = 9
        editUseCaseTest(
            dataRepositoryTestUtil = dataRepositoryTestUtil,
            addData = todo,
            addAssert = {
                assert(it.text == todo.text)
                assert(it.done == todo.done)
                assert(it.order == todo.order)
            },
            edit = {
                editTodoOrderUseCaseTest.editOrder(
                    oldTodo = it,
                    order = editOrder,
                    uid = dataRepositoryTestUtil.userRepositoryTestUtil.uid!!,
                    onFail = { assert(false) }
                )
            },
            editAssert = { newData: Todo ->
                assert(newData.text == todo.text)
                assert(newData.order != todo.order)
                assert(newData.order == editOrder)
                assert(newData.done == todo.done)
            }
        )
    }
}