package com.leebeebeom.clothinghelper.ui.component.dialog.component

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import com.leebeebeom.clothinghelper.R.string.cancel
import com.leebeebeom.clothinghelper.R.string.positive
import com.leebeebeom.clothinghelper.R.string.test_dialog_title
import com.leebeebeom.clothinghelper.onNodeWithStringRes
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CustomDialogTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()

    private var show by mutableStateOf(true)
    private var positiveButtonEnabled by mutableStateOf(true)

    private val title = rule.onNodeWithStringRes(test_dialog_title)
    private val positiveButton = rule.onNodeWithStringRes(positive)
    private val cancelButton = rule.onNodeWithStringRes(cancel)

    @Before
    fun init() {
        rule.setContent {
            if (show)
                CustomDialog(
                    onDismiss = { show = false },
                    title = test_dialog_title,
                    content = { },
                    onPositiveButtonClick = { },
                    positiveButtonEnabled = { positiveButtonEnabled }
                )
        }
    }

    @Test
    fun buttonClickTest() {
        title.assertExists()
        assert(show)

        positiveButton.performClick()

        title.assertDoesNotExist()
        assert(!show)

        show = true

        cancelButton.performClick()

        title.assertDoesNotExist()
        assert(!show)
    }

    @Test
    fun positiveButtonEnabledTest() {
        positiveButton.assertIsEnabled()

        positiveButtonEnabled = false

        positiveButton.assertIsNotEnabled()

        positiveButtonEnabled = true

        positiveButton.assertIsEnabled()
    }
}