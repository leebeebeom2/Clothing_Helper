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
class EditTodoTextUseCaseTest {
    private lateinit var editTodoDoneUseCaseTest: EditTodoTextUseCase
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
        editTodoDoneUseCaseTest =
            EditTodoTextUseCase(todoRepository = dataRepositoryTestUtil.repository)
    }

    @Test
    fun todoTextEditTest() = runTest(dispatcher) {
        val todo = Todo(text = "test")
        val editText = "edit text"
        editUseCaseTest(
            dataRepositoryTestUtil = dataRepositoryTestUtil,
            addData = todo,
            addAssert = {
                assert(it.text == todo.text)
                assert(it.done == todo.done)
                assert(it.order == todo.order)
            },
            edit = {
                editTodoDoneUseCaseTest.editText(
                    oldTodo = it,
                    text = editText,
                    uid = dataRepositoryTestUtil.userRepositoryTestUtil.uid!!,
                    onFail = { assert(false) }
                )
            },
            editAssert = { newData: Todo ->
                assert(newData.text != todo.text)
                assert(newData.text == editText)
                assert(newData.order == todo.order)
                assert(newData.done == todo.done)
            }
        )
    }
}