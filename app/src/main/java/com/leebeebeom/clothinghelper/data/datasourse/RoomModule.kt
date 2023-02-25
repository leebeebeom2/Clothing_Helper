package com.leebeebeom.clothinghelper.data.datasourse

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {
    @Provides
    fun roomDB(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, RoomDB::class.java, "clothing_helper_db").build()

    @Provides
    fun subCategoryDao(roomDB: RoomDB) = roomDB.subCategoryDao()

    @Provides
    fun folderDao(roomDB: RoomDB) = roomDB.folderDao()

    @Provides
    fun todoDao(roomDB: RoomDB) = roomDB.todoDao()
}