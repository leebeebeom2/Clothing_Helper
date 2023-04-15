package com.leebeebeom.clothinghelper.ui.drawer.content.folder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import com.leebeebeom.clothinghelper.ui.drawer.DrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.drawer.rememberDrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.onNodeWithStringRes
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import kotlinx.collections.immutable.toImmutableSet
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DrawerFolderDropdownMenuTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private val restorationTester = StateRestorationTester(rule)
    private lateinit var state: DrawerItemDropdownMenuState
    private val folderNames = List(5) { "$it" }.toImmutableSet()
    private val childFolderNames = List(5) { "${it + 5}" }.toImmutableSet()
    private var addFolder = ""
    private var editFolder = ""
    private val selectedFolder = Folder(name = "이름")

    @Before
    fun init() {
        restorationTester.setContent {
            ClothingHelperTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    state = rememberDrawerItemDropdownMenuState()

                    DrawerDownMenuEditAndAddFolder(state = state,
                        onDismiss = state::onDismissDropDownMenu,
                        expand = { state.showDropdownMenu },
                        folderNames = { folderNames },
                        childFolderNames = { childFolderNames },
                        addFolder = { _, _, _ ->
                            addFolder = "clicked"
                        },
                        editFolder = { _, _ ->
                            editFolder = "clicked"
                        },
                        selectedFolder = { selectedFolder })
                }
            }
        }
    }

    @Test
    fun folderDropdownMenuTestRestoreTest() {
        state.onLongClick(Offset.Zero)

        repeat(2) {
            rule.onNodeWithStringRes(R.string.dropdown_menu_edit).assertExists()
            rule.onNodeWithStringRes(R.string.dropdown_menu_add_folder).assertExists()

            restorationTester.emulateSavedInstanceStateRestore()
        }

        rule.onNodeWithStringRes(R.string.dropdown_menu_edit).performClick()

        repeat(2) {
            rule.onNodeWithStringRes(R.string.edit_folder).assertExists()
            rule.onNodeWithText("이름").assertExists()

            restorationTester.emulateSavedInstanceStateRestore()
        }

        rule.onNodeWithStringRes(R.string.cancel).performClick()

        state.onLongClick(Offset.Zero)
        rule.onNodeWithStringRes(R.string.dropdown_menu_add_folder).performClick()

        repeat(2) {
            rule.onNodeWithStringRes(R.string.add_folder).assertExists()

            restorationTester.emulateSavedInstanceStateRestore()
        }
    }
}