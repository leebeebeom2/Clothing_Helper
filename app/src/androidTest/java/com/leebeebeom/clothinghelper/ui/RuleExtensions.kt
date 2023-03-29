package com.leebeebeom.clothinghelper.ui

import androidx.annotation.StringRes
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.data.SignInEmail
import com.leebeebeom.clothinghelper.data.SignInPassword
import com.leebeebeom.clothinghelper.ui.drawer.component.SettingIconTag

fun AndroidComposeTestRule<*, *>.onNodeWithStringRes(@StringRes res: Int) =
    onNodeWithText(activity.getString(res))

fun AndroidComposeTestRule<*, *>.signIn() {
    onNodeWithStringRes(R.string.email).performTextInput(SignInEmail)
    onNodeWithStringRes(R.string.password).performTextInput(SignInPassword)
    onNodeWithStringRes(R.string.sign_in).performClick()
}

fun AndroidComposeTestRule<*, *>.signOut() {
    onRoot().performTouchInput { swipeRight() }
    onNodeWithTag(SettingIconTag).performClick()
    onNodeWithStringRes(R.string.sign_out).performClick()
}

fun AndroidComposeTestRule<*, *>.waitTagExist(tag: String, time: Long = 5000) {
    waitUntil(time) { onAllNodesWithTag(tag).fetchSemanticsNodes().isNotEmpty() }
}

fun AndroidComposeTestRule<*, *>.waitTagNotExist(tag: String, time: Long = 5000) {
    waitUntil(time) { onAllNodesWithTag(tag).fetchSemanticsNodes().isEmpty() }
}

fun AndroidComposeTestRule<*, *>.waitTextExist(text: String, time: Long = 5000) {
    waitUntil(time) { onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty() }
}

fun AndroidComposeTestRule<*, *>.waitTextNotExist(text: String, time: Long = 5000) {
    waitUntil(time) { onAllNodesWithText(text).fetchSemanticsNodes().isEmpty() }
}

fun AndroidComposeTestRule<*, *>.waitStringResExist(@StringRes res: Int, time: Long = 5000) {
    waitUntil(time) {
        onAllNodesWithText(activity.getString(res)).fetchSemanticsNodes().isNotEmpty()
    }
}

fun AndroidComposeTestRule<*, *>.waitStringResNotExist(@StringRes res: Int, time: Long = 5000) {
    waitUntil(time) { onAllNodesWithText(activity.getString(res)).fetchSemanticsNodes().isEmpty() }
}


fun getInvisibleText(length: Int) = "•".repeat(length)