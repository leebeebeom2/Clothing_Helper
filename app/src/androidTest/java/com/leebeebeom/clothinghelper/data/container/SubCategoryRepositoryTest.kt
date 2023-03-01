package com.leebeebeom.clothinghelper.data.container

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.leebeebeom.clothinghelper.data.repository.container.SubCategoryRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.preference.NetworkPreferenceRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.preference.SubCategoryPreferencesRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.util.NetworkChecker
import com.leebeebeom.clothinghelper.data.repositoryCrudTest
import com.leebeebeom.clothinghelper.domain.model.DatabaseSubCategory
import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.domain.model.toDatabaseModel
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.NetworkPreferenceRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.junit.Before
import org.junit.Test

class SubCategoryRepositoryTest {
    private lateinit var networkPreferenceRepository: NetworkPreferenceRepository
    private lateinit var networkChecker: NetworkChecker
    private lateinit var subCategoryPreferencesRepository: SortPreferenceRepository
    private lateinit var subCategoryRepository: SubCategoryRepository

    private val uid = "subcategory test"

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        networkPreferenceRepository = NetworkPreferenceRepositoryImpl(context = context)
        networkChecker = NetworkChecker(
            context = context, networkPreferenceRepository = networkPreferenceRepository
        )
        subCategoryPreferencesRepository = SubCategoryPreferencesRepositoryImpl(context = context)
        subCategoryRepository = SubCategoryRepositoryImpl(
            subCategoryPreferencesRepository = subCategoryPreferencesRepository,
            appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
            networkChecker = networkChecker
        )
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
}