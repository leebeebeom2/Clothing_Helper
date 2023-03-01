package com.leebeebeom.clothinghelper.data.repository

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.leebeebeom.clothinghelper.data.repository.preference.NetworkPreferenceRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.util.NetworkChecker
import com.leebeebeom.clothinghelper.data.repositoryCrudTest
import com.leebeebeom.clothinghelper.data.repositorySignOutTest
import com.leebeebeom.clothinghelper.domain.model.DatabaseTodo
import com.leebeebeom.clothinghelper.domain.model.Todo
import com.leebeebeom.clothinghelper.domain.model.toDatabaseModel
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.NetworkPreferenceRepository
import org.junit.Before
import org.junit.Test

class TodoRepositoryTest {
    private lateinit var networkPreferenceRepository: NetworkPreferenceRepository
    private lateinit var netWorkChecker: NetworkChecker
    private lateinit var todoRepository: TodoRepository

    private val uid = "todo test"

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        networkPreferenceRepository = NetworkPreferenceRepositoryImpl(context = context)
        netWorkChecker = NetworkChecker(
            context = context,
            networkPreferenceRepository = networkPreferenceRepository
        )
        todoRepository = TodoRepositoryImpl(netWorkChecker)
    }

    @Test
    fun crudTest() {
        val todo = Todo(text = "todo")

        repositoryCrudTest(
            data = todo.toDatabaseModel(),
            repository = todoRepository,
            uid = uid,
            type = DatabaseTodo::class.java,
            addAssert = {
                assert(it.text == todo.text)
                assert(it.done == todo.done)
                assert(it.order == todo.order)
            },
            newData = { it.copy(text = "new todo", done = true, order = 1) },
            editAssert = { origin, new ->
                assert(origin.key == new.key)
                assert(new.done)
                assert(new.order == 1)
                assert(new.text == "new todo")
            }
        )
    }

    @Test
    fun signOutTest() {
        repositorySignOutTest(
            data1 = Todo("todo 1").toDatabaseModel(),
            data2 = Todo("todo 2").toDatabaseModel(),
            repository = todoRepository,
            uid = uid,
            type = DatabaseTodo::class.java
        )
    }
}