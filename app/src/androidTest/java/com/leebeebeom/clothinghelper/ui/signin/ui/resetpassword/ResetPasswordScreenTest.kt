package com.leebeebeom.clothinghelper.ui.signin.ui.resetpassword

import androidx.annotation.StringRes
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.R.string.*
import com.leebeebeom.clothinghelper.data.InvalidEmail
import com.leebeebeom.clothinghelper.data.NotFoundEmail
import com.leebeebeom.clothinghelper.data.SendPasswordEmail
import com.leebeebeom.clothinghelper.data.SignInEmail
import com.leebeebeom.clothinghelper.onNodeWithStringRes
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import com.leebeebeom.clothinghelper.ui.MainNavHost
import com.leebeebeom.clothinghelper.ui.component.CenterDotProgressIndicatorTag
import com.leebeebeom.clothinghelper.ui.signin.ui.signin.SignInScreenTag
import com.leebeebeom.clothinghelper.waitStringResExist
import com.leebeebeom.clothinghelper.waitTagExist
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ResetPasswordScreenTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private val restorationTester = StateRestorationTester(rule)
    private val emailTextField = rule.onNodeWithStringRes(email)
    private val sendButton = rule.onNodeWithStringRes(send)

    @Before
    fun init() {
        runBlocking {
            Firebase.auth.signOut()
            delay(1000)
        }
        restorationTester.setContent { MainNavHost() }

        rule.onNodeWithStringRes(forgot_password).performClick()
        rule.waitTagExist(ResetPasswordScreenTag)
    }

    @Test
    fun resetPasswordRestoreTest() { // with error, loading, button enabled
        sendButton.assertIsNotEnabled()

        fun localRestoreTest(
            email: String, @StringRes error: Int
        ) {
            emailTextField.performTextInput(email)
            sendButton.performClick()
            rule.waitTagExist(CenterDotProgressIndicatorTag)

            repeat(2) {
                rule.waitStringResExist(error)
                emailTextField.assert(hasText(email))
                sendButton.assertIsNotEnabled()

                restorationTester.emulateSavedInstanceStateRestore()
            }

            emailTextField.performTextClearance()
        }

        localRestoreTest(email = InvalidEmail, error = error_invalid_email)
        localRestoreTest(email = NotFoundEmail, error = error_user_not_found)
    }

    @Test
    fun resetPasswordBlockBlankTest() {
        emailTextField.performTextInput(SignInEmail)

        repeat(10) {
            emailTextField.performTextInput(" ")
            emailTextField.assert(hasText(SignInEmail))
        }
    }

    @Test
    fun resetPasswordSendTest() {
        emailTextField.performTextInput(SendPasswordEmail)
        sendButton.performClick()
        rule.waitTagExist(SignInScreenTag)
    }
}