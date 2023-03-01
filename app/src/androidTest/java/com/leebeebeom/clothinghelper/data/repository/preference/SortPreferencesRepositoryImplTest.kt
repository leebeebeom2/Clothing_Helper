package com.leebeebeom.clothinghelper.data.repository.preference

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import org.junit.Before
import org.junit.Test

class SortPreferencesRepositoryImplTest {
    private lateinit var folderPreferencesRepository: SortPreferenceRepository
    private lateinit var subCategoryPreferencesRepository: SortPreferenceRepository

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        folderPreferencesRepository = FolderPreferencesRepositoryImpl(context)
        subCategoryPreferencesRepository = SubCategoryPreferencesRepositoryImpl(context)
    }

    @Test
    fun sotFlowTest() {
        preferenceSortFlowTest(folderPreferencesRepository)
        preferenceSortFlowTest(subCategoryPreferencesRepository)
    }
}