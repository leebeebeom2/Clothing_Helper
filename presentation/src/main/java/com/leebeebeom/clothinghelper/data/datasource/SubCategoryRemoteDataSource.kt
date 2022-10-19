package com.leebeebeom.clothinghelper.data.datasource

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.data.model.MainCategory
import com.leebeebeom.clothinghelper.data.model.SubCategory
import com.leebeebeom.clothinghelper.data.model.User
import com.leebeebeom.clothinghelper.domain.usecase.user.UserInfoUserCase

class SubCategoryRemoteDataSource(private val userInfoUserCase: UserInfoUserCase) {
    private val dataBase = Firebase.database.reference
    private val userRef = dataBase.child(userInfoUserCase.uid.value)

    fun writeInitialData() {
        val user = User(userInfoUserCase.email.value, userInfoUserCase.name.value)
        userRef.child("user info").setValue(user)
        userRef.child("subCategory").setValue(getInitialSubCategories())
    }

    private fun getInitialSubCategories(): List<SubCategory> {
        var timeStamp = System.currentTimeMillis()

        return listOf(
            SubCategory(MainCategory.Top, timeStamp++, "반팔"),
            SubCategory(MainCategory.Top, timeStamp++, "긴팔"),
            SubCategory(MainCategory.Top, timeStamp++, "셔츠"),
            SubCategory(MainCategory.Top, timeStamp++, "반팔 셔츠"),
            SubCategory(MainCategory.Top, timeStamp++, "니트"),
            SubCategory(MainCategory.Top, timeStamp++, "반팔 니트"),
            SubCategory(MainCategory.Top, timeStamp++, "니트 베스트"),
            SubCategory(MainCategory.Bottom, timeStamp++, "데님"),
            SubCategory(MainCategory.Bottom, timeStamp++, "반바지"),
            SubCategory(MainCategory.Bottom, timeStamp++, "슬랙스"),
            SubCategory(MainCategory.Bottom, timeStamp++, "스웻"),
            SubCategory(MainCategory.Bottom, timeStamp++, "나일론"),
            SubCategory(MainCategory.Bottom, timeStamp++, "치노"),
            SubCategory(MainCategory.OUTER, timeStamp++, "코트"),
            SubCategory(MainCategory.OUTER, timeStamp++, "자켓"),
            SubCategory(MainCategory.OUTER, timeStamp++, "바람막이"),
            SubCategory(MainCategory.OUTER, timeStamp++, "항공점퍼"),
            SubCategory(MainCategory.OUTER, timeStamp++, "블루종"),
            SubCategory(MainCategory.OUTER, timeStamp++, "점퍼"),
            SubCategory(MainCategory.OUTER, timeStamp++, "야상"),
            SubCategory(MainCategory.ETC, timeStamp++, "신발"),
            SubCategory(MainCategory.ETC, timeStamp++, "목걸이"),
            SubCategory(MainCategory.ETC, timeStamp++, "팔찌"),
            SubCategory(MainCategory.ETC, timeStamp++, "귀걸이"),
            SubCategory(MainCategory.ETC, timeStamp++, "볼캡"),
            SubCategory(MainCategory.ETC, timeStamp++, "비니"),
            SubCategory(MainCategory.ETC, timeStamp++, "머플러"),
            SubCategory(MainCategory.ETC, timeStamp, "장갑")
        )
    }
}