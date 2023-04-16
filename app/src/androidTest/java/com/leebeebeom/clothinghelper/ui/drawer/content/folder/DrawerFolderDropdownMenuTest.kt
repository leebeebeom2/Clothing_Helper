package com.leebeebeom.clothinghelper.ui.drawer.content.folder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.R.string.add_folder
import com.leebeebeom.clothinghelper.R.string.dropdown_menu_add_folder
import com.leebeebeom.clothinghelper.R.string.dropdown_menu_edit
import com.leebeebeom.clothinghelper.R.string.edit_folder
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.onNodeWithStringRes
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import com.leebeebeom.clothinghelper.ui.drawer.DrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.drawer.rememberDrawerItemDropdownMenuState
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
    private val initialName = "초기 폴더명"
    private val selectedFolder = Folder(name = initialName)

    private val dropdownEdit = rule.onNodeWithStringRes(dropdown_menu_edit)
    private val dropdownAddFolder = rule.onNodeWithStringRes(dropdown_menu_add_folder)
    private val editTitle = rule.onNodeWithStringRes(edit_folder)
    private val addTitle = rule.onNodeWithStringRes(add_folder)
    private val textField = rule.onNodeWithStringRes(R.string.folder)
    private val positiveButton = rule.onNodeWithStringRes(R.string.positive)
    private val cancelButton = rule.onNodeWithStringRes(R.string.cancel)

    @Before
    fun init() {
        restorationTester.setContent {
            ClothingHelperTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    state = rememberDrawerItemDropdownMenuState()

                    DrawerDownMenuEditAndAddFolder(state = state,
                        onDismiss = state::onDismissDropDownMenu,
                        expand = state::expand,
                        folderNames = { folderNames },
                        childFolderNames = { childFolderNames },
                        addFolder = { _, _, _ -> },
                        editFolder = { _, _ -> },
                        deleteFolder = {},
                        selectedFolder = { selectedFolder })
                }
            }
        }
    }

    @Test
    fun folderDropdownMenuTestRestoreTest() {
        state.onLongClick(Offset.Zero)

        repeat(2) {
            dropdownEdit.assertExists()
            dropdownAddFolder.assertExists()

            restorationTester.emulateSavedInstanceStateRestore()
        }

        dropdownEdit.performClick()

        repeat(2) {
            editTitle.assertExists()
            textField.assert(hasText(initialName))

            restorationTester.emulateSavedInstanceStateRestore()
        }

        cancelButton.performClick()

        state.onLongClick(Offset.Zero)
        dropdownAddFolder.performClick()

        repeat(2) {
            addTitle.assertExists()
            textField.assert(!hasText(initialName))

            restorationTester.emulateSavedInstanceStateRestore()
        }
    }

    @Test
    fun addFolderExpandTest() {
        state.onLongClick(Offset.Zero)
        assert(!state.expanded)

        dropdownAddFolder.performClick()
        textField.performTextInput("이름")
        positiveButton.performClick()

        assert(state.expanded)
    }
}