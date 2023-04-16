package com.leebeebeom.clothinghelper.ui.drawer.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DrawerExpandIconTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private var dataSize by mutableStateOf(0)

    private val expandIcon = rule.onNodeWithTag(DrawerExpandIconTag)

    @Before
    fun init() {
        rule.setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                DrawerExpandIcon(
                    expanded = { false },
                    toggleExpand = { /*TODO*/ },
                    dataSize = { dataSize })
            }
        }
    }

    @Test
    fun drawerExpandIconVisibleTest() {
        expandIcon.assertDoesNotExist()

        repeat(5) {
            dataSize = it + 1
            expandIcon.assertExists()
        }

        dataSize = 0
        expandIcon.assertDoesNotExist()
    }
}