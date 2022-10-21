package com.leebeebeom.clothinghelperdata.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.model.User
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class SubCategoryRepositoryImpl : SubCategoryRepository {
    private val root = Firebase.database.reference

    private lateinit var topSubCategories: MutableStateFlow<List<SubCategory>>
    override suspend fun getTopSubCategories(
        uid: String, onCancelled: (Int, String) -> Unit
    ): StateFlow<List<SubCategory>> = withContext(Dispatchers.IO) {
        if (!::topSubCategories.isInitialized) {
            topSubCategories = MutableStateFlow(emptyList())
            root.setSingleValueListener(
                uid = uid,
                subCategoryParent = SubCategoryParent.Top,
                subCategories = topSubCategories,
                onCancelled = onCancelled
            )
        }
        topSubCategories
    }

    private lateinit var bottomSubCategories: MutableStateFlow<List<SubCategory>>
    override suspend fun getBottomSubCategories(
        uid: String, onCancelled: (Int, String) -> Unit
    ): StateFlow<List<SubCategory>> = withContext(Dispatchers.IO) {
        if (!::bottomSubCategories.isInitialized) {
            bottomSubCategories = MutableStateFlow(emptyList())
            root.setSingleValueListener(
                uid = uid,
                subCategoryParent = SubCategoryParent.Bottom,
                subCategories = bottomSubCategories,
                onCancelled = onCancelled
            )
        }
        bottomSubCategories
    }

    private lateinit var outerSubCategories: MutableStateFlow<List<SubCategory>>
    override suspend fun getOuterSubCategories(
        uid: String, onCancelled: (Int, String) -> Unit
    ): StateFlow<List<SubCategory>> = withContext(Dispatchers.IO) {
        if (!::outerSubCategories.isInitialized) {
            outerSubCategories = MutableStateFlow(emptyList())
            root.setSingleValueListener(
                uid = uid,
                subCategoryParent = SubCategoryParent.OUTER,
                subCategories = outerSubCategories,
                onCancelled = onCancelled
            )
        }
        outerSubCategories
    }

    private lateinit var etcSubCategories: MutableStateFlow<List<SubCategory>>
    override suspend fun getEtcSubCategories(
        uid: String, onCancelled: (Int, String) -> Unit
    ): StateFlow<List<SubCategory>> = withContext(Dispatchers.IO) {
        if (!::etcSubCategories.isInitialized) {
            etcSubCategories = MutableStateFlow(emptyList())
            root.setSingleValueListener(
                uid = uid,
                subCategoryParent = SubCategoryParent.ETC,
                subCategories = etcSubCategories,
                onCancelled = onCancelled
            )
        }
        etcSubCategories
    }

    override suspend fun writeInitialSubCategory(user: User): Unit =
        withContext(Dispatchers.IO) {
            root.getUidRef(user.uid).setValue(user)
            root.getSubCategoriesRef(user.uid).setValue(getInitialSubCategories())
        }

    override suspend fun addSubCategory(
        uid: String,
        subCategoryParent: SubCategoryParent,
        name: String,
        onSuccess: () -> Unit,
        onFailed: () -> Unit
    ): Unit = withContext(Dispatchers.IO) {
        val timeStamp = System.currentTimeMillis()
        val newSubCategory = SubCategory(subCategoryParent, timeStamp, name)

        root.getSubCategoriesRef(uid).push().setValue(newSubCategory).addOnCompleteListener {
            if (it.isSuccessful) onSuccess() else onFailed()
        }
    }

    private fun getInitialSubCategories(): List<SubCategory> {
        var timeStamp = System.currentTimeMillis()

        return listOf(
            SubCategory(
                SubCategoryParent.Top, timeStamp++, "반팔"
            ), SubCategory(
                SubCategoryParent.Top, timeStamp++, "긴팔"
            ), SubCategory(
                SubCategoryParent.Top, timeStamp++, "셔츠"
            ), SubCategory(
                SubCategoryParent.Top, timeStamp++, "반팔 셔츠"
            ), SubCategory(
                SubCategoryParent.Top, timeStamp++, "니트"
            ), SubCategory(
                SubCategoryParent.Top, timeStamp++, "반팔 니트"
            ), SubCategory(
                SubCategoryParent.Top, timeStamp++, "니트 베스트"
            ), SubCategory(
                SubCategoryParent.Bottom, timeStamp++, "데님"
            ), SubCategory(
                SubCategoryParent.Bottom, timeStamp++, "반바지"
            ), SubCategory(
                SubCategoryParent.Bottom, timeStamp++, "슬랙스"
            ), SubCategory(
                SubCategoryParent.Bottom, timeStamp++, "스웻"
            ), SubCategory(
                SubCategoryParent.Bottom, timeStamp++, "나일론"
            ), SubCategory(
                SubCategoryParent.Bottom, timeStamp++, "치노"
            ), SubCategory(
                SubCategoryParent.OUTER, timeStamp++, "코트"
            ), SubCategory(
                SubCategoryParent.OUTER, timeStamp++, "자켓"
            ), SubCategory(
                SubCategoryParent.OUTER, timeStamp++, "바람막이"
            ), SubCategory(
                SubCategoryParent.OUTER, timeStamp++, "항공점퍼"
            ), SubCategory(
                SubCategoryParent.OUTER, timeStamp++, "블루종"
            ), SubCategory(
                SubCategoryParent.OUTER, timeStamp++, "점퍼"
            ), SubCategory(
                SubCategoryParent.OUTER, timeStamp++, "야상"
            ), SubCategory(
                SubCategoryParent.ETC, timeStamp++, "신발"
            ), SubCategory(
                SubCategoryParent.ETC, timeStamp++, "목걸이"
            ), SubCategory(
                SubCategoryParent.ETC, timeStamp++, "팔찌"
            ), SubCategory(
                SubCategoryParent.ETC, timeStamp++, "귀걸이"
            ), SubCategory(
                SubCategoryParent.ETC, timeStamp++, "볼캡"
            ), SubCategory(
                SubCategoryParent.ETC, timeStamp++, "비니"
            ), SubCategory(
                SubCategoryParent.ETC, timeStamp++, "머플러"
            ), SubCategory(
                SubCategoryParent.ETC, timeStamp, "장갑"
            )
        )
    }

    private fun DatabaseReference.getUidRef(uid: String) = child(uid)
    private fun DatabaseReference.getSubCategoriesRef(uid: String) =
        getUidRef(uid).child(DatabasePath.SUB_CATEGORIES)

    private fun DatabaseReference.setSingleValueListener(
        uid: String,
        subCategoryParent: SubCategoryParent,
        subCategories: MutableStateFlow<List<SubCategory>>,
        onCancelled: (Int, String) -> Unit
    ) {
        getSubCategoriesRef(uid).orderByChild("parent").equalTo(subCategoryParent.name)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val temp = mutableListOf<SubCategory>()
                    for (child in snapshot.children) {
                        child.getValue<SubCategory>()?.let { temp.add(it) }
                    }
                    subCategories.value = temp
                }

                override fun onCancelled(error: DatabaseError) =
                    onCancelled(error.code, error.message)
            })
    }
}

object DatabasePath {
    const val SUB_CATEGORIES = "sub categories"
    const val USER_INFO = "user info"
}