package com.leebeebeom.clothinghelper.data.container

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.repository.UserRepositoryImpl
import com.leebeebeom.clothinghelper.data.repositoryCrudTest
import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import org.junit.Before
import org.junit.Test

class SubCategoryRepositoryTest {
    private lateinit var userRepository: UserRepository
    private lateinit var subCategoryRepository: SubCategoryRepository

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        subCategoryRepository = RepositoryProvider.getSubCategoryRepository(context = context)
        userRepository = UserRepositoryImpl(appScope = RepositoryProvider.getAppScope())
    }


    @Test
    fun crudTest() {
        val subCategory = SubCategory(name = "subcategory test")

        repositoryCrudTest(
            userRepository = userRepository,
            data = subCategory,
            repository = subCategoryRepository,
            addAssert = {
                assert(it.name == subCategory.name)
                assert(it.createDate == it.editDate)
            },
            newData = { it.copy(name = "new subCategory") }
        ) { origin, new ->
            assert(origin.key == new.key)
            assert(origin.name != new.name)
            assert(new.name == "new subCategory")
            assert(origin.key == new.key)
            assert(origin.mainCategoryType == new.mainCategoryType)
            assert(origin.createDate == new.createDate)
            assert(origin.editDate != new.editDate)
            assert(origin.editDate < new.editDate)
        }
    }
}