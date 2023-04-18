package com.leebeebeom.clothinghelper.ui.drawer.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import com.leebeebeom.clothinghelper.ui.component.textfield.TestText
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DrawerTextWithDoubleCountTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private val restorationTester = StateRestorationTester(rule)
    private var foldersSize by mutableStateOf(5)
    private var itemsSize by mutableStateOf(10)

    @Before
    fun init() {
        restorationTester.setContent {
            Row(
                Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DrawerTextWithDoubleCount(
                    text = { TestText },
                    style = MaterialTheme.typography.body1,
                    foldersSize = { foldersSize },
                    itemsSize = { itemsSize })
            }
        }
    }

    @Test
    fun restoreTest() {
        repeat(2) {
            val countTest = rule.activity.getString(R.string.folders_items, 5, 10)

            rule.onNodeWithText(TestText).assertExists()
            rule.onNodeWithText(countTest).assertExists()
        }
    }

    @Test
    fun countChangeTest() {
        val countTest = rule.activity.getString(R.string.folders_items, 5, 10)

        rule.onNodeWithText(TestText).assertExists()
        rule.onNodeWithText(countTest).assertExists()

        foldersSize = 2
        itemsSize = 7

        val countTest2 = rule.activity.getString(R.string.folders_items, 2, 7)
        rule.onNodeWithText(countTest2).assertExists()
    }
}
