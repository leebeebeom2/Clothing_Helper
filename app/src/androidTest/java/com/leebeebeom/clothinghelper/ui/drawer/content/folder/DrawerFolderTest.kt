package com.leebeebeom.clothinghelper.ui.drawer.content.folder

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R.string.cancel
import com.leebeebeom.clothinghelper.R.string.delete_folder_dialog_text2
import com.leebeebeom.clothinghelper.R.string.dropdown_menu_add_folder
import com.leebeebeom.clothinghelper.R.string.dropdown_menu_delete
import com.leebeebeom.clothinghelper.R.string.dropdown_menu_edit
import com.leebeebeom.clothinghelper.R.string.error_exist_folder_name
import com.leebeebeom.clothinghelper.R.string.folder
import com.leebeebeom.clothinghelper.R.string.positive
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.onNodeWithStringRes
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerExpandIconTag
import com.leebeebeom.clothinghelper.ui.drawer.getCountText
import com.leebeebeom.clothinghelper.ui.theme.BlackA3
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DrawerFolderTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private val restorationTester = StateRestorationTester(rule)
    private var foldersMap by getFoldersMap()
    private val folderNamesMap by getFolderNamesMap()
    private val foldersSizeMap by getFoldersSizeMap()

    private val dropdownEdit = rule.onNodeWithStringRes(dropdown_menu_edit)
    private val dropdownAdd = rule.onNodeWithStringRes(dropdown_menu_add_folder)
    private val dropdownDelete = rule.onNodeWithStringRes(dropdown_menu_delete)
    private val textField = rule.onNodeWithStringRes(folder)
    private val positiveButton = rule.onNodeWithStringRes(positive)
    private val cancelButton = rule.onNodeWithStringRes(cancel)

    private var lastKey = 11

    @Before
    fun init() {
        restorationTester.setContent {
            ClothingHelperTheme {
                DrawerFolders(parentKey = "-1",
                    basePadding = 0.dp,
                    backgroundColor = BlackA3,
                    onFolderClick = {},
                    folders = { foldersMap.getOrDefault(it, persistentListOf()) },
                    folderNames = { folderNamesMap.getOrDefault(it, persistentSetOf()) },
                    foldersSize = { foldersSizeMap.getOrDefault(it, 0) },
                    itemsSize = { 0 },
                    addFolder = { parentKey, name, menuType ->
                        val newFolder = Folder(
                            key = (++lastKey).toString(),
                            parentKey = parentKey,
                            name = name,
                            menuType = menuType
                        )
                        val mutableMap = foldersMap.toMutableMap()
                        val mutableList =
                            mutableMap.getOrDefault(parentKey, listOf()).toMutableList()
                        mutableList.add(newFolder)
                        mutableMap[parentKey] = mutableList.toImmutableList()
                        foldersMap = mutableMap
                    },
                    editFolder = { oldFolder, name ->
                        val newFolder = oldFolder.copy(name = name)

                        val mutableMap = foldersMap.toMutableMap()
                        val mutableList =
                            mutableMap.getOrDefault(newFolder.parentKey, listOf()).toMutableList()
                        mutableList.remove(oldFolder)
                        mutableList.add(newFolder)
                        mutableMap[newFolder.parentKey] = mutableList.toImmutableList()
                        foldersMap = mutableMap
                    },
                    deleteFolder = {
                        val mutableMap = foldersMap.toMutableMap()
                        val mutableList =
                            mutableMap.getOrDefault(it.parentKey, listOf()).toMutableList()
                        mutableList.remove(it)

                        mutableMap[it.parentKey] = mutableList.toImmutableList()
                        foldersMap = mutableMap
                    })
            }
        }

        rule.onAllNodesWithTag(DrawerExpandIconTag)[3].performClick()
        rule.onAllNodesWithTag(DrawerExpandIconTag)[2].performClick()
        rule.onAllNodesWithTag(DrawerExpandIconTag)[1].performClick()
        rule.onAllNodesWithTag(DrawerExpandIconTag)[0].performClick()

        rule.onAllNodesWithTag(DrawerExpandIconTag)[5].performClick()
        rule.onAllNodesWithTag(DrawerExpandIconTag)[3].performClick()
        rule.onAllNodesWithTag(DrawerExpandIconTag)[1].performClick()

        foldersMap.forEach { (_, element) ->
            element.forEach { rule.onNodeWithText(it.name).assertExists() }
        }
    }

    @After
    fun initData() {
        foldersMap = getFoldersMap().value
        lastKey = 11
    }

    @Test
    fun longClickTest() {
        rule.onAllNodesWithTag(DrawerFolderTag)[0].performTouchInput { longClick() }

        dropdownEdit.assertExists()
        dropdownAdd.assertExists()
    }

    @Test
    fun countTest() {
        fun folderSizeCheck(count1: Int, count2: Int, count3: Int) {
            assert(rule.onAllNodesWithText(getCountText(0)).fetchSemanticsNodes().size == count1)
            assert(rule.onAllNodesWithText(getCountText(1)).fetchSemanticsNodes().size == count2)
            assert(rule.onAllNodesWithText(getCountText(2)).fetchSemanticsNodes().size == count3)
        }

        folderSizeCheck(5, 7, 0)

        fun addFolder(nodeText: String) = addFolder(rule.onNodeWithText(nodeText), "추가")

        addFolder("folder lv 1-0")

        repeat(2) {
            folderSizeCheck(6, 6, 1)

            repeat(2) {
                rule.onAllNodesWithTag(DrawerExpandIconTag)[0].performClick()
            }
            rule.onAllNodesWithTag(DrawerExpandIconTag)[1].performClick()
        }

        addFolder("folder lv 2-0")

        repeat(2) {
            folderSizeCheck(7, 5, 2)

            repeat(2) {
                rule.onAllNodesWithTag(DrawerExpandIconTag)[0].performClick()
            }
            rule.onAllNodesWithTag(DrawerExpandIconTag)[1].performClick()
        }

        addFolder("folder lv 3-0")

        repeat(2) {
            folderSizeCheck(7, 6, 2)

            repeat(2) {
                rule.onAllNodesWithTag(DrawerExpandIconTag)[0].performClick()
            }
            rule.onAllNodesWithTag(DrawerExpandIconTag)[1].performClick()
            rule.onAllNodesWithTag(DrawerExpandIconTag)[2].performClick()
        }

        repeat(3) {
            deleteFolder(node = rule.onAllNodesWithText("추가")[0])

            when (it) {
                0 -> {
                    repeat(2) {
                        folderSizeCheck(7, 5, 2)

                        repeat(2) {
                            rule.onAllNodesWithTag(DrawerExpandIconTag)[0].performClick()
                        }
                        rule.onAllNodesWithTag(DrawerExpandIconTag)[1].performClick()
                    }
                }

                1 -> {
                    repeat(2) {
                        folderSizeCheck(6, 6, 1)

                        repeat(2) {
                            rule.onAllNodesWithTag(DrawerExpandIconTag)[0].performClick()
                        }
                        rule.onAllNodesWithTag(DrawerExpandIconTag)[1].performClick()
                    }
                }

                2 -> {
                    repeat(2) {
                        folderSizeCheck(5, 7, 0)

                        repeat(2) {
                            rule.onAllNodesWithTag(DrawerExpandIconTag)[0].performClick()
                        }
                        rule.onAllNodesWithTag(DrawerExpandIconTag)[1].performClick()
                    }
                }
            }
        }
    }

    @Test
    fun countTest2() {
        repeat(2) {
            addFolder(node = rule.onNodeWithText("folder lv 3-0"), addFolderName = "추가 $it")
        }

        repeat(2) { num1 ->
            repeat(num1 + 2) { num2 ->
                addFolder(rule.onNodeWithText("추가 $num1"), "추가 $num1-${num2 + 1}")
            }
        }

        fun folderSizeCheck(nodeText: String, folderSize: Int) {
            rule.onAllNodesWithTag(DrawerFolderTag).filterToOne(hasAnyChild(hasText(nodeText)))
                .onChildren().filterToOne(hasText(getCountText(folderSize))).assertExists()
        }

        folderSizeCheck(nodeText = "추가 0", folderSize = 2)
        folderSizeCheck(nodeText = "추가 1", folderSize = 3)

        editFolder(node = rule.onNodeWithText("추가 0"), editFolderName = "추가 2")

        folderSizeCheck(nodeText = "추가 2", folderSize = 2)

        repeat(2) {
            addFolder(rule.onNodeWithText("추가 2"), "추가 2-$it")
        }

        folderSizeCheck(nodeText = "추가 2", folderSize = 4)

        deleteFolder(node = rule.onNodeWithText("추가 2-0"))

        editFolder(node = rule.onNodeWithText("추가 2"), editFolderName = "추가 0")

        folderSizeCheck(nodeText = "추가 0", folderSize = 3)
    }

    @Test
    fun expandIconTest() {
        repeat(3) {
            addFolder(node = rule.onNodeWithText("folder lv 3-2"), addFolderName = "추가 $it")
        }

        fun assertExpandIconExist(nodeText: String, exist: Boolean = true) {
            val node =
                rule.onAllNodesWithTag(DrawerFolderTag)
                    .filterToOne(hasAnyChild(hasText(nodeText)))
                    .onChildren()
                    .filterToOne(hasTestTag(DrawerExpandIconTag))

            if (exist) node.assertExists() else node.assertDoesNotExist()
        }

        repeat(times = 3) { assertExpandIconExist("추가 $it", false) }

        addFolder(rule.onNodeWithText("추가 0"), "추가 0-1")
        addFolder(rule.onNodeWithText("추가 0-1"), "추가 0-1-1")

        assertExpandIconExist("추가 0")
        assertExpandIconExist("추가 0-1")

        editFolder(rule.onNodeWithText("추가 0"), "추가 3")

        assertExpandIconExist("추가 3")
        assertExpandIconExist("추가 0-1")

        editFolder(rule.onNodeWithText("추가 1"), "추가 4")
        assertExpandIconExist("추가 4", false)
        addFolder(rule.onNodeWithText("추가 4"), "추가 4-1")
        assertExpandIconExist("추가 4")

        editFolder(rule.onNodeWithText("추가 4-1"), "추가 4-2")
        assertExpandIconExist("추가 4-2", false)
        addFolder(rule.onNodeWithText("추가 4-2"), "추가 4-2-1")
        assertExpandIconExist("추가 4-2")
    }

    @Test
    fun editFolderTest() {
        val testNode = rule.onAllNodesWithTag(DrawerFolderTag)
            .filterToOne(hasAnyChild(hasText("folder lv 1-0")))

        fun initialTextTest(nodeText: String) {
            rule.onNodeWithText(nodeText).performTouchInput { longClick() }
            dropdownEdit.performClick()
            textField.assert(hasText(nodeText))
        }

        initialTextTest("folder lv 1-0")

        editFolder(testNode, "수정")

        initialTextTest("수정")

        textField.performTextInput("folder lv 1-0")
        rule.onNodeWithStringRes(error_exist_folder_name).assertDoesNotExist()

        // drawer open check
        rule.onNodeWithText("folder lv 2-0").assertIsDisplayed()
        rule.onNodeWithText("folder lv 3-0").assertIsDisplayed()
    }

    @Test
    fun dialogRestoreTest() {
        val testNode = rule.onAllNodesWithTag(DrawerFolderTag)
            .filterToOne(hasAnyChild(hasText("folder lv 1-0")))
        val errorNode = rule.onNodeWithStringRes(error_exist_folder_name)

        fun longClick() = testNode.performTouchInput { this.longClick() }

        longClick()
        dropdownAdd.performClick()
        textField.assert(hasText(""))
        textField.performTextInput("folder lv 2-0")

        repeat(2) {
            textField.assert(hasText("folder lv 2-0"))
            errorNode.assertExists()
            positiveButton.assertIsNotEnabled()

            restorationTester.emulateSavedInstanceStateRestore()
        }
        cancelButton.performClick()

        longClick()
        dropdownEdit.performClick()

        repeat(2) {
            textField.assert(hasText("folder lv 1-0"))
            errorNode.assertDoesNotExist()
            positiveButton.assertIsNotEnabled()

            restorationTester.emulateSavedInstanceStateRestore()
        }

        textField.performTextInput("folder lv 1-1")

        repeat(2) {
            textField.assert(hasText("folder lv 1-1"))
            errorNode.assertExists()
            positiveButton.assertIsNotEnabled()

            restorationTester.emulateSavedInstanceStateRestore()
        }

        textField.performTextInput("ㅎㅇ")

        repeat(2) {
            textField.assert(hasText("ㅎㅇ"))
            errorNode.assertDoesNotExist()
            positiveButton.assertIsEnabled()

            restorationTester.emulateSavedInstanceStateRestore()
        }
        cancelButton.performClick()

        longClick()
        dropdownDelete.performClick()

        repeat(2) {
            rule.onNodeWithStringRes(delete_folder_dialog_text2).assertExists()
            positiveButton.assertExists()

            restorationTester.emulateSavedInstanceStateRestore()
        }
    }

    @Test
    fun deletedTest() {
        rule.onNodeWithText("folder lv 2-0").assertExists()

        deleteFolder(rule.onNodeWithText("folder lv 2-0"))

        rule.onNodeWithText("folder lv 2-0").assertDoesNotExist()
    }

    private fun addFolder(node: SemanticsNodeInteraction, addFolderName: String) {
        node.performTouchInput { longClick() }
        dropdownAdd.performClick()
        textField.performTextInput(addFolderName)
        positiveButton.performClick()
    }

    private fun editFolder(node: SemanticsNodeInteraction, editFolderName: String) {
        node.performTouchInput { longClick() }
        dropdownEdit.performClick()
        textField.performTextInput(editFolderName)
        positiveButton.performClick()
    }

    private fun deleteFolder(node: SemanticsNodeInteraction) {
        node.performTouchInput { longClick() }
        dropdownDelete.performClick()
        positiveButton.performClick()
    }

    private fun getFoldersMap() = mutableStateOf(listOf(List(5) {
        Folder(
            parentKey = "-1", key = "$it", name = "folder lv 1-$it"
        )
    }, List(4) {
        Folder(
            parentKey = "$it", key = "${it + 5}", name = "folder lv 2-$it"
        )
    }, List(3) {
        Folder(
            parentKey = "${it + 5}", key = "${it + 9}", name = "folder lv 3-$it"
        )
    }).flatten().groupBy { it.parentKey }.mapValues { mapElement ->
        mapElement.value.filter { !it.deleted }.toImmutableList()
    })

    private fun getFolderNamesMap() = derivedStateOf {
        foldersMap.mapValues { mapElement -> mapElement.value.map { it.name }.toImmutableSet() }
    }

    private fun getFoldersSizeMap() = derivedStateOf {
        foldersMap.mapValues { mapElement -> mapElement.value.size }
    }
}