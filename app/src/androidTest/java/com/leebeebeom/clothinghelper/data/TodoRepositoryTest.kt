package com.leebeebeom.clothinghelper.data

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.domain.model.Todo
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TodoRepositoryTest {
    private lateinit var dataRepositoryTestUtil: DataRepositoryTestUtil<Todo, TodoRepository>
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun init() {
        val repositoryProvider = RepositoryProvider(dispatcher)

        dataRepositoryTestUtil = DataRepositoryTestUtil(
            repositoryProvider = repositoryProvider,
            repository = repositoryProvider.createTodoRepository(),
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun crudTest() = runTest(dispatcher) {
        val todo = Todo(text = "todo")

        repositoryCrudTest(
            backgroundScope = backgroundScope,
            dataRepositoryTestUtil = dataRepositoryTestUtil,
            data = todo,
            addAssert = {
                assert(it.text == todo.text)
                assert(it.done == todo.done)
                assert(it.order == todo.order)
            },
            editData = { it.copy(text = "new todo", done = true, order = 1) },
            editAssert = { origin, new ->
                assert(origin.key == new.key)
                assert(new.done)
                assert(new.order == 1)
                assert(new.text == "new todo")
            },
            loadAssert = { origin, loaded -> assert(origin == loaded) }
        )
    }
}