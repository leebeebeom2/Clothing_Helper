package com.leebeebeom.clothinghelper.ui.drawer.content

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerExpandIconTag
import com.leebeebeom.clothinghelper.ui.onAllNodeWithStringRes
import com.leebeebeom.clothinghelper.ui.onNodeWithStringRes
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import kotlinx.collections.immutable.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val TopSubCategorySize = 10
private const val BottomSubCategorySize = 9
private const val OuterSubCategorySize = 8
private const val EtcSubCategorySize = 0
private val subCategoryInitialSizes = listOf(
    TopSubCategorySize, BottomSubCategorySize, OuterSubCategorySize, EtcSubCategorySize
)

class DrawerMainCategoryTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private val restorationTester = StateRestorationTester(rule)
    private val mainCategories = getMainCategories()
    private var subCategories by mutableStateOf(getImmutableSubCategories())
    private val subCategoryNamesMap by getSubCategoryNames()
    private val subCategorySizeMap by getSubCategorySize()

    @Before
    fun init() {
        restorationTester.setContent {
            ClothingHelperTheme {

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    drawerMainCategories(
                        mainCategories = getMainCategories(),
                        onMainCategoryClick = {},
                        allSubCategories = { subCategories },
                        subCategoryNamesMap = { subCategoryNamesMap },
                        subCategorySizeMap = { subCategorySizeMap },
                        addSubCategory = { name, parent ->
                            subCategories = subCategories.getNewList {
                                it.add(SubCategory(name = name, mainCategoryType = parent))
                            }
                        },
                        drawerSubCategories = { subCategories, subCategoryNames ->
                            DrawerSubCategories(
                                subCategories = subCategories,
                                subCategoryNames = subCategoryNames,
                                folderNames = { persistentMapOf() },
                                folderSize = { persistentMapOf() },
                                chartSize = { persistentMapOf() },
                                onSubCategoryClick = {},
                                editSubCategory = { oldSubCategory, name -> },
                                addFolder = { parentKey, subCategoryKey, name, mainCategoryType -> }
                            )
                        }
                    )
                }
            }
        }
    }

    @After
    fun testFinished() {
        subCategories = getImmutableSubCategories()
    }

    @Test
    fun restoreTest() {
        val expandIcons = rule.onAllNodesWithTag(DrawerExpandIconTag)

        expandIcons[0].performClick()

        rule.onNodeWithText("subCategory 1").assertExists()

        rule.onNodeWithStringRes(R.string.bottom).performTouchInput { longClick() }
        rule.onNodeWithStringRes(R.string.add_category).performClick()

        rule.onNodeWithStringRes(R.string.category).assertExists()

        restorationTester.emulateSavedInstanceStateRestore()

        rule.onNodeWithStringRes(R.string.category).assertExists()

        rule.onNodeWithStringRes(R.string.cancel).performClick()
        rule.onNodeWithText("subCategory 1").assertExists()

        expandIcons[0].performClick()

        rule.onNodeWithText("subCategory 1").assertDoesNotExist()

        restorationTester.emulateSavedInstanceStateRestore()

        rule.onNodeWithText("subCategory 1").assertDoesNotExist()
    }

    @Test
    fun drawerMainCategoryCountTest() {
        rule.onNodeWithText("($TopSubCategorySize)").assertExists()
        rule.onNodeWithText("($BottomSubCategorySize)").assertExists()
        rule.onNodeWithText("($OuterSubCategorySize)").assertExists()
        rule.onNodeWithText("($EtcSubCategorySize)").assertExists()

        subCategories = subCategories.getNewList {
            it.addAll(List(3) { index ->
                SubCategory(
                    name = "added subCategory",
                    mainCategoryType = enumValues<MainCategoryType>()[index]
                )
            })
        }

        rule.onNodeWithText("(${TopSubCategorySize + 1})").assertExists()
        rule.onNodeWithText("(${BottomSubCategorySize + 1})").assertExists()
        rule.onNodeWithText("(${OuterSubCategorySize + 1})").assertExists()
        rule.onNodeWithText("($EtcSubCategorySize)").assertExists()
    }

    @Test
    fun drawerMainCategoryExpandIconExistTest() {
        assert(rule.onAllNodesWithTag(DrawerExpandIconTag).fetchSemanticsNodes().size == 3)

        subCategories = subCategories.getNewList {
            it.add(
                SubCategory(
                    name = "added subCategory",
                    mainCategoryType = MainCategoryType.Etc
                )
            )
        }

        assert(rule.onAllNodesWithTag(DrawerExpandIconTag).fetchSemanticsNodes().size == 4)

        subCategories = subCategories.getNewList { mutableList ->
            mutableList.removeIf { it.mainCategoryType == MainCategoryType.Top || it.mainCategoryType == MainCategoryType.Bottom }
        }

        assert(rule.onAllNodesWithTag(DrawerExpandIconTag).fetchSemanticsNodes().size == 2)
    }

    @Test
    fun expandIconTest() {
        (0..2).forEach { index ->
            val expandIcon = rule.onAllNodesWithTag(DrawerExpandIconTag)[index]
            expandIcon.performClick()
            subCategories.filter { it.mainCategoryType == enumValues<MainCategoryType>()[index] }
                .forEach {
                    rule.onNodeWithText(it.name).assertExists()
                }
            expandIcon.performClick()
        }
    }

    @Test
    fun drawerMainCategoryAddSubCategoryTest() {
        mainCategories.forEachIndexed { index, mainCategory ->
            if (mainCategory.name == R.string.etc) return

            rule.onNodeWithStringRes(mainCategory.name).performTouchInput { longClick() }
            rule.onNodeWithStringRes(R.string.add_category).performClick()

            assert( // 드랍다운메뉴 사라지고 다이얼로그 생성
                rule.onAllNodeWithStringRes(R.string.add_category).fetchSemanticsNodes().size == 1
            )

            // 빈칸
            rule.onNodeWithStringRes(R.string.check).assertIsNotEnabled()

            rule.onNodeWithStringRes(R.string.category).performTextInput("subCategory")
            rule.onNodeWithStringRes(R.string.check).assertIsEnabled()

            rule.onNodeWithStringRes(R.string.category).performTextClearance()
            rule.onNodeWithStringRes(R.string.category).performTextInput("subCategory 1")
            rule.onNodeWithStringRes(R.string.error_exist_category_name).assertExists()
            rule.onNodeWithStringRes(R.string.check).assertIsNotEnabled()

            rule.onNodeWithStringRes(R.string.category).performTextClearance()
            rule.onNodeWithStringRes(R.string.error_exist_category_name).assertDoesNotExist()

            rule.onNodeWithStringRes(R.string.category).performTextInput("added subCategory")
            rule.onNodeWithStringRes(R.string.check).assertIsEnabled()

            rule.onNodeWithStringRes(R.string.check).performClick()

            rule.onNodeWithStringRes(R.string.category).assertDoesNotExist()

            assert(
                subCategories.filter { subCategory -> subCategory.mainCategoryType == mainCategory.type }.size == subCategoryInitialSizes[index] + 1
            )
            // expand
            rule.onNodeWithText("subCategory 1").assertExists()
            rule.onAllNodesWithTag(DrawerExpandIconTag)[index].performClick()
            // shrink
            rule.onNodeWithText("subCategory 1").assertDoesNotExist()

            rule.onNodeWithText("(${subCategoryInitialSizes[index] + 1})").assertExists()

            rule.onNodeWithStringRes(mainCategory.name).performTouchInput { longClick() }
            rule.onNodeWithStringRes(R.string.add_category).performClick()
            rule.onNodeWithStringRes(R.string.cancel).performClick()
            rule.onNodeWithStringRes(R.string.cancel).assertDoesNotExist()
        }
    }

    @Test
    fun addSubCategoryTest() {
        mainCategories.forEachIndexed { index, mainCategory ->
            if (mainCategory.type == MainCategoryType.Etc) return@forEachIndexed
            val expandIcon = rule.onAllNodesWithTag(DrawerExpandIconTag)[index]
            expandIcon.performClick()

            rule.onNodeWithStringRes(mainCategory.name).performTouchInput { longClick() }
            rule.onNodeWithStringRes(R.string.add_category).performClick()
            rule.onNodeWithStringRes(R.string.category).performTextInput("added subCategory")
            rule.onNodeWithStringRes(R.string.check).performClick()

            rule.onNodeWithText("added subCategory").assertExists()
            expandIcon.performClick()
        }
    }

    private fun getImmutableSubCategories(): ImmutableList<SubCategory> {
        val topSubCategories = List(TopSubCategorySize) {
            SubCategory(name = "subCategory $it", mainCategoryType = MainCategoryType.Top)
        }
        val bottomSubCategories = List(BottomSubCategorySize) {
            SubCategory(name = "subCategory $it", mainCategoryType = MainCategoryType.Bottom)
        }
        val outerSubCategories = List(OuterSubCategorySize) {
            SubCategory(name = "subCategory $it", mainCategoryType = MainCategoryType.Outer)
        }
        val etcSubCategories = List(EtcSubCategorySize) {
            SubCategory(name = "subCategory $it", mainCategoryType = MainCategoryType.Etc)
        }
        return (topSubCategories + bottomSubCategories + outerSubCategories + etcSubCategories).toImmutableList()
    }

    private fun getSubCategoryNames() =
        derivedStateOf {
            subCategories.groupBy { element -> element.mainCategoryType }
                .mapValues { mapElement ->
                    mapElement.value.map { element -> element.name }.toImmutableSet()
                }
                .toImmutableMap()
        }

    private fun getSubCategorySize() =
        derivedStateOf {
            subCategories.groupingBy { element -> element.mainCategoryType }.eachCount()
                .toImmutableMap()
        }

    private fun <T> ImmutableList<T>.getNewList(task: (MutableList<T>) -> Unit): ImmutableList<T> {
        val mutable = this.toMutableList()
        task(mutable)
        return mutable.toImmutableList()
    }
}