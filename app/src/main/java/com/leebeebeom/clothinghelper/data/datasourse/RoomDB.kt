package com.leebeebeom.clothinghelper.data.datasourse

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leebeebeom.clothinghelper.data.datasourse.folder.FolderDao
import com.leebeebeom.clothinghelper.data.datasourse.subcategory.SubCategoryDao
import com.leebeebeom.clothinghelper.data.datasourse.todo.TodoDao
import com.leebeebeom.clothinghelper.domain.model.DatabaseFolder
import com.leebeebeom.clothinghelper.domain.model.DatabaseSubCategory
import com.leebeebeom.clothinghelper.domain.model.DatabaseTodo

@Database(entities = [DatabaseSubCategory::class, DatabaseFolder::class, DatabaseTodo::class], version = 1)
abstract class RoomDB : RoomDatabase() {
    abstract fun subCategoryDao(): SubCategoryDao
    abstract fun folderDao(): FolderDao
    abstract fun todoDao(): TodoDao
}