package com.leebeebeom.clothinghelper.data.repository.preference

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import org.junit.Before
import org.junit.Test

class SortPreferencesRepositoryTest {
    private lateinit var folderPreferencesRepository: SortPreferenceRepository
    private lateinit var subCategoryPreferencesRepository: SortPreferenceRepository

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        folderPreferencesRepository = FolderPreferencesRepositoryImpl(
            context = context,
            appScope = RepositoryProvider.getAppScope()
        )
        subCategoryPreferencesRepository = SubCategoryPreferencesRepositoryImpl(
            context = context,
            appScope = RepositoryProvider.getAppScope()
        )
    }

    @Test
    fun sotFlowTest() {
        preferenceSortFlowTest(folderPreferencesRepository)
        preferenceSortFlowTest(subCategoryPreferencesRepository)
    }
}