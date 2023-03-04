package com.leebeebeom.clothinghelper.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.repository.UserRepositoryImpl
import com.leebeebeom.clothinghelper.domain.model.Todo
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import org.junit.Before
import org.junit.Test

class TodoRepositoryTest {
    private lateinit var userRepository: UserRepository
    private lateinit var todoRepository: TodoRepository

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        todoRepository = RepositoryProvider.getTodoRepository(context = context)
        userRepository = UserRepositoryImpl(appScope = RepositoryProvider.getAppScope())
    }

    @Test
    fun crudTest() {
        val todo = Todo(text = "todo")

        repositoryCrudTest(
            userRepository = userRepository,
            data = todo,
            repository = todoRepository,
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
}