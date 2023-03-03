package com.leebeebeom.clothinghelper.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.domain.model.Todo
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import org.junit.Before
import org.junit.Test

class TodoRepositoryTest {
    private lateinit var todoRepository: TodoRepository

    private val uid = "todo test"

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        todoRepository = RepositoryProvider.getTodoRepository(context)
    }

    @Test
    fun crudTest() {
        val todo = Todo(text = "todo")

        repositoryCrudTest(
            type = Todo::class.java,
            data = todo,
            repository = todoRepository,
            uid = uid,
            addAssert = {
                assert(it.text == todo.text)
                assert(it.done == todo.done)
                assert(it.order == todo.order)
            },
            newData = { it.copy(text = "new todo", done = true, order = 1) }
        ) { origin, new ->
            assert(origin.key == new.key)
            assert(new.done)
            assert(new.order == 1)
            assert(new.text == "new todo")
        }
    }

    @Test
    fun signOutTest() {
        repositorySignOutTest(
            type = Todo::class.java,
            data1 = Todo("todo 1"),
            data2 = Todo("todo 2"),
            repository = todoRepository,
            uid = uid
        )
    }
}