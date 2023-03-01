package com.leebeebeom.clothinghelper.data.container

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.repositoryCrudTest
import com.leebeebeom.clothinghelper.data.repositorySignOutTest
import com.leebeebeom.clothinghelper.domain.model.DatabaseSubCategory
import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.domain.model.toDatabaseModel
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import org.junit.Before
import org.junit.Test

class SubCategoryRepositoryTest {
    private lateinit var subCategoryRepository: SubCategoryRepository

    private val uid = "subcategory test"

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        subCategoryRepository = RepositoryProvider.getSubCategoryRepository(context = context)
    }


    @Test
    fun crudTest() {
        val subCategory = SubCategory(name = "subcategory test")

        repositoryCrudTest(
            data = subCategory.toDatabaseModel(),
            repository = subCategoryRepository,
            uid = uid,
            type = DatabaseSubCategory::class.java,
            addAssert = {
                assert(it.name == subCategory.name)
                assert(it.createDate == it.editDate)
            },
            newData = { it.copy(name = "new subCategory") },
            editAssert = { origin, new ->
                assert(origin.key == new.key)
                assert(origin.name != new.name)
                assert(new.name == "new subCategory")
                assert(origin.key == new.key)
                assert(origin.parent == new.parent)
                assert(origin.createDate == new.createDate)
                assert(origin.editDate != new.editDate)
                assert(origin.editDate < new.editDate)
            }
        )
    }

    @Test
    fun signOutTest() {
        repositorySignOutTest(
            data1 = SubCategory("subCategory 1").toDatabaseModel(),
            data2 = SubCategory("subCategory 2").toDatabaseModel(),
            repository = subCategoryRepository,
            uid = uid,
            type = DatabaseSubCategory::class.java
        )
    }
}