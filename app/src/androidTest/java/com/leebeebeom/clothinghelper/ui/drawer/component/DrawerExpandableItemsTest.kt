package com.leebeebeom.clothinghelper.ui.drawer.component

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import com.leebeebeom.clothinghelper.ui.drawer.content.folder.DrawerFolders
import com.leebeebeom.clothinghelper.ui.theme.Black
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DrawerExpandableItemsTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private var expand by mutableStateOf(false)
    private var draw by mutableStateOf(false)
    private val folders = List(1) { Folder(name = "folder") }.toImmutableList()

    private val folderNode = rule.onNodeWithText("folder")

    @Before
    fun init() {
        expand = false
        draw = false

        rule.setContent {
            ClothingHelperTheme {
                DrawerItemsWrapperWithExpandAnimation(
                    expand = { expand },
                    draw = { draw }
                ) {
                    DrawerFolders(
                        parentKey = "",
                        basePadding = 0.dp,
                        backgroundColor = Black,
                        onFolderClick = {},
                        folders = { folders },
                        folderNames = { persistentSetOf() },
                        foldersSize = { 0 },
                        itemsSize = { 0 },
                        addFolder = { _, _, _ -> },
                        editFolder = { _, _ -> },
                        deleteFolder = {}
                    )
                }
            }
        }
    }

    @Test
    fun drawerExpandableItemsTest() {
        folderNode.assertDoesNotExist()

        draw = true

        folderNode.assertDoesNotExist()

        expand = true

        folderNode.assertExists()
    }
}