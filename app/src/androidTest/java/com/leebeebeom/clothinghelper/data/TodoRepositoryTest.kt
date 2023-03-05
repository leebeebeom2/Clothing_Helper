package com.leebeebeom.clothinghelper.data

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.domain.model.Todo
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TodoRepositoryTest {
    private lateinit var userRepository: UserRepository
    private lateinit var todoRepository: TodoRepository
    private val dispatcher = StandardTestDispatcher()
    private val repositoryProvider = RepositoryProvider(dispatcher = dispatcher)

    @Before
    fun init() {
        todoRepository = repositoryProvider.getTodoRepository()
        userRepository = repositoryProvider.getUserRepository()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun crudTest() = runTest(dispatcher) {
        val todo = Todo(text = "todo")

          repositoryCrudTest(dispatcher = dispatcher,
              userRepository = userRepository,
              data = todo,
              repository = todoRepository,
              addAssert = {
                  assert(it.text == todo.text)
                  assert(it.done == todo.done)
                  assert(it.order == todo.order)
              },
              newData = { it.copy(text = "new todo", done = true, order = 1) }) { origin, new ->
              assert(origin.key == new.key)
              assert(new.done)
              assert(new.order == 1)
              assert(new.text == "new todo")
          }
    }
}