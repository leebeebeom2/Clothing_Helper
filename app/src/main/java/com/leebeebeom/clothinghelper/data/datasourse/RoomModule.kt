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
    fun roomDb(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, LocalDataBase::class.java, "clothing_helper_db").build()

    @Provides
    fun subCategoryDao(roomDb: LocalDataBase) = roomDb.subCategoryDao()

    @Provides
    fun folderDao(roomDb: LocalDataBase) = roomDb.folderDao()

    @Provides
    fun todoDao(roomDb: LocalDataBase) = roomDb.todoDao()

    @Provides
    fun userDao(roomDb: LocalDataBase) = roomDb.userDao()
}