package com.leebeebeom.clothinghelper.data.repository.preference

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.leebeebeom.clothinghelper.di.AppScope
import com.leebeebeom.clothinghelper.domain.repository.preference.MainScreenPreferencesRepository
import com.leebeebeom.clothinghelper.ui.drawer.content.mainmenu.MainMenuType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainScreenPreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    @AppScope appScope: CoroutineScope
) :
    MainScreenPreferencesRepository {
    private val mainScreenDataStore = context.mainScreenDataStore

    override val brandExpandFlow = mainScreenDataStore.getFlow(appScope) {
        it[ExpandPreferenceKeys.brandExpand] ?: false
    }
    override val outfitExpandFlow = mainScreenDataStore.getFlow(appScope) {
        it[ExpandPreferenceKeys.outfitExpand] ?: false
    }

    override val clothesExpandFlow = mainScreenDataStore.getFlow(appScope) {
        it[ExpandPreferenceKeys.clothesExpand] ?: false
    }

    private object ExpandPreferenceKeys {
        val brandExpand = booleanPreferencesKey("brand expand")
        val outfitExpand = booleanPreferencesKey("outfit expand")
        val clothesExpand = booleanPreferencesKey("clothes expand")
    }

    override suspend fun expandToggle(mainMenuType: MainMenuType) {
        mainScreenDataStore.edit {
            when (mainMenuType) {
                MainMenuType.Brand -> {
                    it[ExpandPreferenceKeys.brandExpand] = !brandExpandFlow.first()
                }

                MainMenuType.Outfit -> {
                    it[ExpandPreferenceKeys.outfitExpand] = !outfitExpandFlow.first()
                }

                MainMenuType.Clothes -> {
                    it[ExpandPreferenceKeys.clothesExpand] = !clothesExpandFlow.first()
                }

                else -> throw IllegalStateException()
            }
        }
    }
}

private const val MainScreenDataStoreKey = "main screen data store"
private val Context.mainScreenDataStore by preferencesDataStore(name = MainScreenDataStoreKey)