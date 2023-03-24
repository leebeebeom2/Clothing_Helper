package com.leebeebeom.clothinghelper.ui.signin.ui

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.data.InvalidEmail
import com.leebeebeom.clothinghelper.data.SignInEmail
import com.leebeebeom.clothinghelper.data.SignInPassword
import com.leebeebeom.clothinghelper.data.SignUpName
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import com.leebeebeom.clothinghelper.ui.components.CENTER_DOT_PROGRESS_INDICATOR_TAG
import com.leebeebeom.clothinghelper.ui.onNodeWithStringRes
import com.leebeebeom.clothinghelper.ui.signin.ui.resetpassword.ResetPasswordScreenTag
import com.leebeebeom.clothinghelper.ui.signin.ui.signin.SignInScreenTag
import com.leebeebeom.clothinghelper.ui.signin.ui.signup.SignUpScreenTag
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import com.leebeebeom.clothinghelper.ui.waitTagExist
import com.leebeebeom.clothinghelper.ui.waitTagNotExist
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignInNavHostTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private val restorationTester = StateRestorationTester(rule)
    private val device = UiDevice.getInstance(getInstrumentation())
    private lateinit var viewModel: SignInNavViewModel

    @Before
    fun init() {
        Firebase.auth.signOut()

        restorationTester.setContent {
            ClothingHelperTheme {
                viewModel = hiltViewModel()
                SignInNavHost(viewModel = viewModel)
            }
        }
    }

    @Test
    fun navigationAndRestoreTest() {
        rule.onNodeWithTag(SignInScreenTag).assertExists()
        restorationTester.emulateSavedInstanceStateRestore()
        rule.onNodeWithTag(SignInScreenTag).assertExists()

        rule.navigateToSignUp()
        rule.onNodeWithTag(SignUpScreenTag).assertExists()
        restorationTester.emulateSavedInstanceStateRestore()
        rule.onNodeWithTag(SignUpScreenTag).assertExists()
        device.pressBack()
        rule.onNodeWithTag(SignInScreenTag).assertExists()
        restorationTester.emulateSavedInstanceStateRestore()
        rule.onNodeWithTag(SignInScreenTag).assertExists()

        rule.navigateToResetPassword()
        rule.onNodeWithTag(ResetPasswordScreenTag).assertExists()
        restorationTester.emulateSavedInstanceStateRestore()
        rule.onNodeWithTag(ResetPasswordScreenTag).assertExists()
        device.pressBack()
        rule.onNodeWithTag(SignInScreenTag).assertExists()
        restorationTester.emulateSavedInstanceStateRestore()
        rule.onNodeWithTag(SignInScreenTag).assertExists()
    }

    @Test
    fun signInLoadingTest() {
        fun loadingCheck() {
            rule.waitTagExist(CENTER_DOT_PROGRESS_INDICATOR_TAG)
            rule.waitTagNotExist(CENTER_DOT_PROGRESS_INDICATOR_TAG)
        }

        // invalid email
        rule.onNodeWithStringRes(R.string.email).performTextInput(InvalidEmail)
        rule.onNodeWithStringRes(R.string.password).performTextInput(SignInPassword)
        rule.onNodeWithStringRes(R.string.sign_in).performClick()
        loadingCheck()

        rule.navigateToSignUp()

        // already use email
        rule.onNodeWithStringRes(R.string.email).performTextInput(SignInEmail)
        rule.onNodeWithStringRes(R.string.nickname).performTextInput(SignUpName)
        rule.onNodeWithStringRes(R.string.password).performTextInput(SignInPassword)
        rule.onNodeWithStringRes(R.string.password_confirm).performTextInput(SignInPassword)
        rule.onNodeWithStringRes(R.string.sign_up).performClick()
        loadingCheck()

        device.pressBack()
        rule.navigateToResetPassword()
        rule.onNodeWithStringRes(R.string.email).performTextInput(SignInEmail)
        rule.onNodeWithStringRes(R.string.send).performClick()
        loadingCheck()
    }

    private fun AndroidComposeTestRule<*, *>.navigateToSignUp() =
        onNodeWithStringRes(R.string.sign_up_with_email).performClick()

    private fun AndroidComposeTestRule<*, *>.navigateToResetPassword() =
        onNodeWithStringRes(R.string.forgot_password).performClick()
}