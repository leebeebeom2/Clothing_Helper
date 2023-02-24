package com.leebeebeom.clothinghelper.data.datasourse

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leebeebeom.clothinghelper.data.datasourse.folder.FolderDao
import com.leebeebeom.clothinghelper.data.datasourse.subcategory.SubCategoryDao
import com.leebeebeom.clothinghelper.data.datasourse.todo.TodoDao
import com.leebeebeom.clothinghelper.domain.model.RoomFolder
import com.leebeebeom.clothinghelper.domain.model.RoomSubCategory
import com.leebeebeom.clothinghelper.domain.model.RoomTodo

@Database(entities = [RoomSubCategory::class, RoomFolder::class, RoomTodo::class], version = 1)
abstract class LocalDataBase : RoomDatabase() {
    abstract fun subCategoryDao(): SubCategoryDao
    abstract fun folderDao(): FolderDao
    abstract fun todoDao(): TodoDao
}