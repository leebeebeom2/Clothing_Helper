package com.leebeebeom.clothinghelper.ui.signin.ui.resetpassword

import androidx.annotation.StringRes
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.data.InvalidEmail
import com.leebeebeom.clothinghelper.data.NotFoundEmail
import com.leebeebeom.clothinghelper.data.SendPasswordEmail
import com.leebeebeom.clothinghelper.data.SignInEmail
import com.leebeebeom.clothinghelper.ui.*
import com.leebeebeom.clothinghelper.ui.components.CenterDotProgressIndicatorTag
import com.leebeebeom.clothinghelper.ui.signin.ui.signin.SignInScreenTag
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ResetPasswordScreenTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private val restorationTester = StateRestorationTester(rule)
    private lateinit var emailTextField: SemanticsNodeInteraction
    private lateinit var sendButton: SemanticsNodeInteraction

    @Before
    fun init() {
        runBlocking {
            Firebase.auth.signOut()
            delay(1000)
        }
        restorationTester.setContent { MainNavHost() }

        rule.onNodeWithStringRes(R.string.forgot_password).performClick()
        rule.waitTagExist(ResetPasswordScreenTag)

        emailTextField = rule.onNodeWithStringRes(R.string.email)
        sendButton = rule.onNodeWithStringRes(R.string.send)
    }

    @Test
    fun resetPasswordRestoreTest() { // with error, loading, button enabled
        sendButton.assertIsNotEnabled()

        fun localRestoreTest(
            email: String,
            @StringRes error: Int
        ) {
            emailTextField.performTextInput(email)
            sendButton.performClick()
            rule.waitTagExist(CenterDotProgressIndicatorTag)

            repeat(2) {
                rule.waitStringResExist(error)
                rule.onNodeWithText(email).assertExists()
                sendButton.assertIsNotEnabled()

                restorationTester.emulateSavedInstanceStateRestore()
            }

            emailTextField.performTextClearance()
        }

        localRestoreTest(email = InvalidEmail, error = R.string.error_invalid_email)
        localRestoreTest(email = NotFoundEmail, error = R.string.error_user_not_found)
    }

    @Test
    fun resetPasswordBlockBlankTest() {
        emailTextField.performTextInput(SignInEmail)

        repeat(10) {
            emailTextField.performTextInput(" ")
            rule.onNodeWithText(SignInEmail).assertExists()
        }
    }

    @Test
    fun resetPasswordSendTest() {
        emailTextField.performTextInput(SendPasswordEmail)
        sendButton.performClick()
        rule.waitTagExist(SignInScreenTag)
    }
}