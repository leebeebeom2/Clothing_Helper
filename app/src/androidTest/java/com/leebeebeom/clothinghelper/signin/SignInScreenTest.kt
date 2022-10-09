package com.leebeebeom.clothinghelper.signin


import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.signin.SignInActivity
import org.junit.Rule
import org.junit.Test

const val INVALID_EMAIL = "invalid email"
const val FAKE_EMAIL = "fake@fake.com"
const val FAKE_PASSWORD = "fakePassword"
const val EMAIL = "1@a.com"
const val PASSWORD = "111111"

class SignInScreenTest {
    @get:Rule
    val signInScreenTestRule = createAndroidComposeRule<SignInActivity>()

    @Test
    fun empty() {
        val loginBtn = getLoginBtn()
        val emailTextField = getEmailTextField()
        val passwordTextField = getPasswordTextField()

        empty(loginBtn)
        emailNotEmpty(emailTextField, loginBtn)
        passwordEmpty(passwordTextField, loginBtn)
        allNotEmpty(emailTextField, passwordTextField, loginBtn)
    }

    private fun allNotEmpty(
        emailTextField: SemanticsNodeInteraction,
        passwordTextField: SemanticsNodeInteraction,
        loginBtn: SemanticsNodeInteraction
    ) {
        emailTextField.performTextInput("a")
        passwordTextField.performTextInput("a")
        loginBtn.assertIsEnabled()
    }

    private fun passwordEmpty(
        passwordTextField: SemanticsNodeInteraction,
        loginBtn: SemanticsNodeInteraction
    ) {
        passwordTextField.performTextInput("a")
        loginBtn.assertIsNotEnabled()
        passwordTextField.performTextClearance()
    }

    private fun emailNotEmpty(
        emailTextField: SemanticsNodeInteraction,
        loginBtn: SemanticsNodeInteraction
    ) {
        emailTextField.performTextInput(EMAIL)
        loginBtn.assertIsNotEnabled()
        emailTextField.performTextClearance()
    }

    private fun empty(loginBtn: SemanticsNodeInteraction) {
        loginBtn.assertIsNotEnabled()
    }

    @Test
    fun passwordTextFieldVisibility() {
        val passwordTextField = getPasswordTextField()
        val invisibleIcon = signInScreenTestRule.onNode(hasContentDescription("invisible icon"))
        val visibleIcon = signInScreenTestRule.onNode(hasContentDescription("visible icon"))

        isInvisible(passwordTextField)
        onVisibleIconClick(visibleIcon)
        onInvisibleIconClick(invisibleIcon)
    }

    private fun onInvisibleIconClick(invisibleIcon: SemanticsNodeInteraction) {
        invisibleIcon.performClick()
        signInScreenTestRule.onNodeWithText("•").assertExists()
    }

    private fun onVisibleIconClick(visibleIcon: SemanticsNodeInteraction) {
        visibleIcon.performClick()
        signInScreenTestRule.onNodeWithText("a").assertExists()
    }

    private fun isInvisible(passwordTextField: SemanticsNodeInteraction) {
        passwordTextField.performTextInput("a")
        signInScreenTestRule.onNodeWithText("•").assertExists()
    }

    @Test
    fun navigation() {
        onNavigateResetPassword()
        signInScreenTestRule.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }
        onNavigateSignUp()
    }

    private fun onNavigateSignUp() {
        signInScreenTestRule.onNodeWithText(getActivity().getString(R.string.sign_up_with_email))
            .performClick()
        signInScreenTestRule.onNodeWithText(getActivity().getString(R.string.sign_up))
            .assertExists()
    }

    private fun onNavigateResetPassword() {
        signInScreenTestRule.onNodeWithText(getActivity().getString(R.string.forget_password))
            .performClick()
        signInScreenTestRule.onNodeWithText(getActivity().getString(R.string.reset_password_text))
            .assertExists()
    }

    @Test
    fun login() {
        val activity = getActivity()
        val loginBtn = getLoginBtn()
        val emailTextField = getEmailTextField()
        val passwordTextField = getPasswordTextField()

        invalidEmail(emailTextField, passwordTextField, loginBtn, activity)
        userNotFound(emailTextField, passwordTextField, loginBtn, activity)
        wrongPassword(emailTextField, passwordTextField, loginBtn, activity)
        login(emailTextField, passwordTextField, loginBtn)
    }

    private fun login(
        emailTextField: SemanticsNodeInteraction,
        passwordTextField: SemanticsNodeInteraction,
        loginBtn: SemanticsNodeInteraction
    ) {
        emailTextField.performTextInput(EMAIL)
        passwordTextField.performTextInput(PASSWORD)
        loginBtn.performClick()
        //TODO 액티비티 종료 테스트
    }

    private fun wrongPassword(
        emailTextField: SemanticsNodeInteraction,
        passwordTextField: SemanticsNodeInteraction,
        loginBtn: SemanticsNodeInteraction,
        activity: SignInActivity
    ) {
        emailTextField.performTextInput(EMAIL)
        passwordTextField.performTextInput(FAKE_PASSWORD)
        loginBtn.performClick()
        signInScreenTestRule.waitUntilExists(hasText(activity.getString(R.string.error_wrong_password)))
        signInScreenTestRule.onNodeWithText(activity.getString(R.string.error_wrong_password))
            .assertExists()
        textClear(emailTextField, passwordTextField)
    }

    private fun userNotFound(
        emailTextField: SemanticsNodeInteraction,
        passwordTextField: SemanticsNodeInteraction,
        loginBtn: SemanticsNodeInteraction,
        activity: SignInActivity
    ) {
        emailTextField.performTextInput(FAKE_EMAIL)
        passwordTextField.performTextInput(FAKE_PASSWORD)
        loginBtn.performClick()
        signInScreenTestRule.waitUntilExists(hasText(activity.getString(R.string.error_user_not_found)))
        signInScreenTestRule.onNodeWithText(activity.getString(R.string.error_user_not_found))
            .assertExists()
        textClear(emailTextField, passwordTextField)
    }

    private fun invalidEmail(
        emailTextField: SemanticsNodeInteraction,
        passwordTextField: SemanticsNodeInteraction,
        loginBtn: SemanticsNodeInteraction,
        activity: SignInActivity
    ) {
        emailTextField.performTextInput(INVALID_EMAIL)
        passwordTextField.performTextInput(FAKE_PASSWORD)
        loginBtn.performClick()
        signInScreenTestRule.waitUntilExists(hasText(activity.getString(R.string.error_invalid_email)))
        signInScreenTestRule.onNodeWithText(activity.getString(R.string.error_invalid_email))
            .assertExists()
        textClear(emailTextField, passwordTextField)
    }

    private fun textClear(
        emailTextField: SemanticsNodeInteraction,
        passwordTextField: SemanticsNodeInteraction
    ) {
        emailTextField.performTextClearance()
        passwordTextField.performTextClearance()
    }

    private fun getActivity() = signInScreenTestRule.activity

    private fun getPasswordTextField() =
        signInScreenTestRule.onNodeWithText(getActivity().getString(R.string.password))

    private fun getEmailTextField() =
        signInScreenTestRule.onNodeWithText(getActivity().getString(R.string.email))

    private fun getLoginBtn() =
        signInScreenTestRule.onNodeWithText(getActivity().getString(R.string.login))
}

fun AndroidComposeTestRule<ActivityScenarioRule<SignInActivity>, SignInActivity>.waitUntilNodeCount(
    matcher: SemanticsMatcher,
    count: Int,
    timeoutMillis: Long = 5_000L
) {
    this.waitUntil(timeoutMillis) {
        this.onAllNodes(matcher).fetchSemanticsNodes().size == count
    }
}


fun AndroidComposeTestRule<ActivityScenarioRule<SignInActivity>, SignInActivity>.waitUntilExists(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = 5_000L
) {
    return this.waitUntilNodeCount(matcher, 1, timeoutMillis)
}

fun AndroidComposeTestRule<ActivityScenarioRule<SignInActivity>, SignInActivity>.waitUntilDoesNotExist(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = 5_000L
) {
    return this.waitUntilNodeCount(matcher, 0, timeoutMillis)
}