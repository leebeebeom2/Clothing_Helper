package com.leebeebeom.clothinghelper

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.StateRestorationTester

private const val GOOGLE_SIGN_IN_TEXT = "구글 이메일로 시작하기"

val ComposeContentTestRule.googleSignInButton get() = onNodeWithText(GOOGLE_SIGN_IN_TEXT)

fun buttonFocusTest(
    focusNode: SemanticsNodeInteraction,
    button: SemanticsNodeInteraction,
    restoreTester: StateRestorationTester
) {
    buttonFocusTest(focusNode = focusNode, button = button)
    restoreTester.emulateSavedInstanceStateRestore()
    buttonFocusTest(focusNode = focusNode, button = button)
}

private fun buttonFocusTest(
    focusNode: SemanticsNodeInteraction,
    button: SemanticsNodeInteraction
) {
    focusNode.performClick()
    focusNode.assertIsFocused()
    button.performClick()
    focusNode.assertIsNotFocused()
}

fun buttonEnabledTest(
    button: SemanticsNodeInteraction,
    setEnable: (Boolean) -> Unit,
    restoreTester: StateRestorationTester
) {
    assertIsEnabled(button, restoreTester)

    setEnable(false)

    button.assertIsNotEnabled()
    restoreTester.emulateSavedInstanceStateRestore()
    button.assertIsNotEnabled()

    setEnable(true)

    assertIsEnabled(button, restoreTester)
}

private fun assertIsEnabled(
    button: SemanticsNodeInteraction,
    restoreTester: StateRestorationTester
) {
    button.assertIsEnabled()
    restoreTester.emulateSavedInstanceStateRestore()
    button.assertIsEnabled()
}