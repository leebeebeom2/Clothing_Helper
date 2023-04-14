package com.leebeebeom.clothinghelper.ui.drawer

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.data.SignInEmail
import com.leebeebeom.clothinghelper.data.SignInPassword
import com.leebeebeom.clothinghelper.data.SignUpEmail
import com.leebeebeom.clothinghelper.data.SignUpName
import com.leebeebeom.clothinghelper.ui.*
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerExpandIconTag
import com.leebeebeom.clothinghelper.ui.drawer.component.SettingIconTag
import com.leebeebeom.clothinghelper.ui.drawer.content.DrawerTag
import com.leebeebeom.clothinghelper.ui.drawer.content.folder.DrawerFolderTag
import com.leebeebeom.clothinghelper.ui.main.essentialmenu.favorite.FavoriteScreenTag
import com.leebeebeom.clothinghelper.ui.main.essentialmenu.main.MainScreenTag
import com.leebeebeom.clothinghelper.ui.main.essentialmenu.seeall.SeeAllScreenTag
import com.leebeebeom.clothinghelper.ui.main.essentialmenu.trash.TrashScreenTag
import com.leebeebeom.clothinghelper.ui.main.menu.archive.ArchiveScreenTag
import com.leebeebeom.clothinghelper.ui.main.menu.brand.BrandMainScreenTag
import com.leebeebeom.clothinghelper.ui.main.menu.brand.brand.BrandScreenTag
import com.leebeebeom.clothinghelper.ui.main.menu.brand.shop.ShopScreenTag
import com.leebeebeom.clothinghelper.ui.main.menu.clothes.ClothesScreenTag
import com.leebeebeom.clothinghelper.ui.main.menu.clothes.closet.ClosetDetailScreenTag
import com.leebeebeom.clothinghelper.ui.main.menu.clothes.closet.ClosetScreenTag
import com.leebeebeom.clothinghelper.ui.main.menu.clothes.wish.WishDetailScreenTag
import com.leebeebeom.clothinghelper.ui.main.menu.clothes.wish.WishScreenTag
import com.leebeebeom.clothinghelper.ui.main.menu.outfit.OutfitScreenTag
import com.leebeebeom.clothinghelper.ui.main.menu.outfit.ootd.OotdScreenTag
import com.leebeebeom.clothinghelper.ui.main.menu.outfit.reference.ReferenceScreenTag
import com.leebeebeom.clothinghelper.ui.setting.SettingScreenTag
import com.leebeebeom.clothinghelper.ui.signin.ui.signin.SignInScreenTag
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

const val SignInName = "테스트 유저 1"

class DrawerTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    private val settingIcon by lazy { rule.onNodeWithTag(SettingIconTag) }

    @Before
    fun init() {
        rule.setContent { MainNavHost() }
        if (Firebase.auth.currentUser == null) rule.signIn()
    }

    @After
    fun removeData() {
        val uid = Firebase.auth.currentUser?.uid
        uid?.let { Firebase.database.reference.child(it).removeValue() }
    }

    @Test
    fun signInOutTest() {
        rule.signOut() // include assert settingIcon exist
        settingIcon.assertIsNotDisplayed() // drawer auto close

        rule.waitTagExist(SignInScreenTag)
        rule.drawerOpen()
        settingIcon.assertIsNotDisplayed()

        rule.signIn()
        rule.waitTagExist(MainScreenTag)
        rule.drawerOpen()
        settingIcon.assertIsDisplayed()
    }

    @Test
    fun drawerUserInfoTest() {
        rule.drawerOpen()
        rule.onNodeWithText("$SignInName($SignInEmail)").assertExists()

        rule.signOut()

        rule.onNodeWithStringRes(R.string.sign_up_with_email).performClick()
        rule.onNodeWithStringRes(R.string.email).performTextInput(SignUpEmail)
        rule.onNodeWithStringRes(R.string.nickname).performTextInput(SignUpName)
        rule.onNodeWithStringRes(R.string.password).performTextInput(SignInPassword)
        rule.onNodeWithStringRes(R.string.password_confirm).performTextInput(SignInPassword)
        rule.onNodeWithStringRes(R.string.sign_up).performClick()

        rule.waitTagExist(MainScreenTag)
        rule.drawerOpen()
        rule.waitTextExist("$SignUpName($SignUpEmail)")

        Firebase.auth.currentUser!!.delete()
    }

    @Test
    fun essentialMenuNavigationAndBackStackTest() { // with drawer close
        val menuList = listOf(
            rule.onNodeWithTag(SettingIconTag) to SettingScreenTag,
            rule.onNodeWithStringRes(R.string.favorite) to FavoriteScreenTag,
            rule.onNodeWithStringRes(R.string.see_all) to SeeAllScreenTag,
            rule.onNodeWithStringRes(R.string.trash) to TrashScreenTag,
            rule.onNodeWithStringRes(R.string.brand_cap) to BrandMainScreenTag,
            rule.onNodeWithStringRes(R.string.clothes_cap) to ClothesScreenTag,
            rule.onNodeWithStringRes(R.string.outfit_cap) to OutfitScreenTag,
            rule.onNodeWithStringRes(R.string.archive_cap) to ArchiveScreenTag
        )

        menuList.forEach(::navigateBackstackTest)

        rule.drawerOpen()
        rule.onAllNodesWithTag(DrawerExpandIconTag)[0].performClick()
        rule.onAllNodesWithTag(DrawerExpandIconTag)[1].performClick()
        rule.onAllNodesWithTag(DrawerExpandIconTag)[4].performClick()

        val subMenuList = listOf(
            rule.onAllNodeWithStringRes(R.string.brand_cap)[1] to BrandScreenTag,
            rule.onNodeWithStringRes(R.string.shop_cap) to ShopScreenTag,
            rule.onNodeWithStringRes(R.string.closet_cap) to ClosetScreenTag,
            rule.onNodeWithStringRes(R.string.wish_cap) to WishScreenTag,
            rule.onNodeWithStringRes(R.string.ootd_cap) to OotdScreenTag,
            rule.onNodeWithStringRes(R.string.reference_cap) to ReferenceScreenTag
        )

        subMenuList.forEach(::navigateBackstackTest)

        rule.drawerOpen()
        rule.onAllNodesWithTag(DrawerExpandIconTag)[2].performClick()
        rule.onAllNodesWithTag(DrawerExpandIconTag)[3].performClick()
        rule.onRoot().performTouchInput { swipeUp(startY = bottom / 2, durationMillis = 10) }

        val clothesMenuList = listOf(
            rule.onAllNodeWithStringRes(R.string.tops_cap)[0] to ClosetDetailScreenTag,
            rule.onAllNodeWithStringRes(R.string.bottoms_cap)[0] to ClosetDetailScreenTag,
            rule.onAllNodeWithStringRes(R.string.outers_cap)[0] to ClosetDetailScreenTag,
            rule.onAllNodeWithStringRes(R.string.etc_cap)[0] to ClosetDetailScreenTag,
            rule.onAllNodeWithStringRes(R.string.tops_cap)[1] to WishDetailScreenTag,
            rule.onAllNodeWithStringRes(R.string.bottoms_cap)[1] to WishDetailScreenTag,
            rule.onAllNodeWithStringRes(R.string.outers_cap)[1] to WishDetailScreenTag,
            rule.onAllNodeWithStringRes(R.string.etc_cap)[1] to WishDetailScreenTag,
        )

        clothesMenuList.forEach(::navigateBackstackTest)

        rule.drawerOpen()
        rule.onNodeWithTag(SettingIconTag).performClick()

        repeat(3) {
            rule.drawerOpen()
            rule.onNodeWithStringRes(R.string.main_screen).performClick()
            rule.onNodeWithStringRes(R.string.main_screen).assertIsNotDisplayed()
        }

        device.pressBack()
        rule.waitTagExist(SettingScreenTag)
    }

    private fun navigateBackstackTest(pair: Pair<SemanticsNodeInteraction, String>) = repeat(3) {
        rule.drawerOpen()
        pair.first.performClick()
        pair.first.assertIsNotDisplayed()
        rule.onNodeWithTag(pair.second).assertExists()

        device.pressBack()
        rule.waitTagExist(MainScreenTag)
    }

    @Test
    fun folderNavigationAndBackStackTest() { // with dropdown menu, add, edit folder
        rule.drawerOpen()
        rule.onAllNodesWithTag(DrawerExpandIconTag)[0].performClick() // brand open

        folderNavigationAndBackStackTest(
            node = rule.onAllNodeWithStringRes(R.string.brand_cap)[1],
            folderName = "브랜드",
            screenTag = BrandScreenTag
        )
        folderNavigationAndBackStackTest(
            node = rule.onNodeWithStringRes(R.string.shop_cap),
            folderName = "샵",
            screenTag = ShopScreenTag
        )

        rule.drawerOpen()
        rule.onAllNodesWithTag(DrawerExpandIconTag)[0].performClick() // brand close
        rule.onAllNodesWithTag(DrawerExpandIconTag)[1].performClick() // clothes open
        rule.onAllNodesWithTag(DrawerExpandIconTag)[2].performClick() // closet open

        folderNavigationAndBackStackTest(
            node = rule.onNodeWithStringRes(R.string.tops_cap),
            folderName = "클로젯 탑",
            screenTag = ClosetDetailScreenTag
        )
        rule.onAllNodesWithTag(DrawerExpandIconTag)[3].performClick() // closet top close

        folderNavigationAndBackStackTest(
            node = rule.onNodeWithStringRes(R.string.bottoms_cap),
            folderName = "클로젯 바텀",
            screenTag = ClosetDetailScreenTag
        )
        rule.onAllNodesWithTag(DrawerExpandIconTag)[4].performClick() // closet bottom close

        folderNavigationAndBackStackTest(
            node = rule.onNodeWithStringRes(R.string.outers_cap),
            folderName = "클로젯 아우터",
            screenTag = ClosetDetailScreenTag
        )
        rule.onAllNodesWithTag(DrawerExpandIconTag)[5].performClick() // closet outer close

        folderNavigationAndBackStackTest(
            node = rule.onNodeWithStringRes(R.string.etc_cap),
            folderName = "클로젯 이티씨",
            screenTag = ClosetDetailScreenTag
        )
        rule.onAllNodesWithTag(DrawerExpandIconTag)[6].performClick() // closet etc close

        rule.onAllNodesWithTag(DrawerExpandIconTag)[2].performClick() // closet close
        rule.onAllNodesWithTag(DrawerExpandIconTag)[3].performClick() // wish open

        folderNavigationAndBackStackTest(
            node = rule.onNodeWithStringRes(R.string.tops_cap),
            folderName = "위시 탑",
            screenTag = WishDetailScreenTag
        )
        rule.onAllNodesWithTag(DrawerExpandIconTag)[4].performClick() // wish top close

        folderNavigationAndBackStackTest(
            node = rule.onNodeWithStringRes(R.string.bottoms_cap),
            folderName = "위시 바텀",
            screenTag = WishDetailScreenTag
        )
        rule.onAllNodesWithTag(DrawerExpandIconTag)[5].performClick() // wish bottom close

        folderNavigationAndBackStackTest(
            node = rule.onNodeWithStringRes(R.string.outers_cap),
            folderName = "위시 아우터",
            screenTag = WishDetailScreenTag
        )
        rule.onAllNodesWithTag(DrawerExpandIconTag)[6].performClick() // wish outer close

        folderNavigationAndBackStackTest(
            node = rule.onNodeWithStringRes(R.string.etc_cap),
            folderName = "위시 이티씨",
            screenTag = WishDetailScreenTag
        )
        rule.onAllNodesWithTag(DrawerExpandIconTag)[6].performClick() // wish etc close

        rule.onAllNodesWithTag(DrawerExpandIconTag)[1].performClick() // clothes close

        rule.onAllNodesWithTag(DrawerExpandIconTag)[2].performClick() // outfit open

        folderNavigationAndBackStackTest(
            node = rule.onNodeWithStringRes(R.string.ootd_cap),
            folderName = "오오티디",
            screenTag = OotdScreenTag
        )
        folderNavigationAndBackStackTest(
            node = rule.onNodeWithStringRes(R.string.reference_cap),
            folderName = "레퍼런스",
            screenTag = ReferenceScreenTag
        )

        rule.onAllNodesWithTag(DrawerExpandIconTag)[2].performClick() // outfit close

        folderNavigationAndBackStackTest(
            node = rule.onNodeWithStringRes(R.string.archive_cap),
            folderName = "아카이브",
            screenTag = ArchiveScreenTag
        )
    }

    private fun folderNavigationAndBackStackTest(
        node: SemanticsNodeInteraction,
        folderName: String,
        screenTag: String
    ) {
        val folderName1 = "$folderName 0"
        addFolder(node = node, folderName = folderName1)
        folderNavigationTest(folderName = folderName1, screenTag = screenTag)

        val folderName2 = "$folderName 0-1"
        addFolder(node = rule.onNodeWithText(folderName1), folderName = folderName2)
        folderNavigationTest(folderName = folderName2, screenTag = screenTag)

        val folderName3 = "$folderName 1"
        val folderName4 = "$folderName 1-1"
        addFolder(node = node, folderName = folderName3)
        addFolder(node = rule.onNodeWithText(folderName3), folderName = folderName4)

        val folderName5 = "$folderName 2"
        val folderName6 = "$folderName 0-2"
        editFolderName(originFolderName = folderName1, newFolderName = folderName5)
        folderNavigationTest(folderName = folderName5, screenTag = screenTag)
        editFolderName(originFolderName = folderName2, newFolderName = folderName6)
        folderNavigationTest(folderName = folderName6, screenTag = screenTag)
    }

    private fun editFolderName(
        originFolderName: String,
        newFolderName: String
    ) {
        rule.drawerOpen()
        rule.onNodeWithText(originFolderName).performTouchInput { longClick() }
        rule.onNodeWithStringRes(R.string.dropdown_menu_edit).performClick()
        rule.onNodeWithStringRes(R.string.folder).performTextInput(newFolderName)
        rule.onNodeWithStringRes(R.string.check).performClick()
    }

    private fun addFolder(
        node: SemanticsNodeInteraction, folderName: String
    ) {
        rule.drawerOpen()
        node.performTouchInput { longClick() }
        rule.onNodeWithStringRes(R.string.dropdown_menu_add_folder).performClick()
        rule.onNodeWithStringRes(R.string.folder).performTextInput(folderName)
        rule.onNodeWithStringRes(R.string.check).performClick()
    }

    private fun folderNavigationTest(
        folderName: String, screenTag: String
    ) {
        repeat(2) {
            rule.drawerOpen()
            rule.onAllNodesWithTag(DrawerFolderTag).filterToOne(hasAnyChild(hasText(folderName)))
                .performClick()
            rule.onNodeWithTag(screenTag).assertExists()
            assert(rule.onAllNodesWithText(folderName).fetchSemanticsNodes().size == 2)
        }

        device.pressBack()
        rule.onNodeWithTag(MainScreenTag)
    }

    @Test
    fun drawerCountAndExpandIconTest() {
        rule.drawerOpen()
        assert(
            rule.onAllNodesWithTag(DrawerExpandIconTag).fetchSemanticsNodes().size == 3
        )

        val itemsText = rule.activity.getString(R.string.folders_items, 0, 0)
        rule.onNodeWithTag(DrawerTag).onChildren()
            .filterToOne(hasText(itemsText)).assertExists()

        repeat(5) {
            addFolder(rule.onNodeWithStringRes(R.string.archive_cap), "아카이브 $it")
        }

        assert(
            rule.onAllNodesWithTag(DrawerExpandIconTag).fetchSemanticsNodes().size == 4
        )

        val itemsText2 = rule.activity.getString(R.string.folders_items, 5, 0)
        rule.onNodeWithTag(DrawerTag).onChildren()
            .filterToOne(hasText(itemsText2)).assertExists()

        repeat(4) {
            addFolder(rule.onNodeWithText("아카이브 $it"), "아카이브 $it-${it + 1}")
            addFolder(rule.onNodeWithText("아카이브 $it"), "아카이브 $it-${it + 2}")
            rule.onNodeWithTag(DrawerTag).performTouchInput { swipeUp() }
        }

        val itemsText3 = rule.activity.getString(R.string.folders_items, 2, 0)
        assert(
            rule.onAllNodesWithTag(DrawerFolderTag).filter(hasAnyChild(hasText(itemsText3)))
                .fetchSemanticsNodes().size == 4
        )

        assert(
            rule.onAllNodesWithTag(DrawerExpandIconTag).fetchSemanticsNodes().size == 8
        )

        repeat(4) {
            addFolder(rule.onNodeWithText("아카이브 $it-${it + 1}"), "아카이브 $it-${it + 1}-${it + 1}")
            addFolder(rule.onNodeWithText("아카이브 $it-${it + 2}"), "아카이브 $it-${it + 2}-${it + 1}")
            rule.onNodeWithTag(DrawerTag).performTouchInput { swipeUp() }
        }

        val itemsText4 = rule.activity.getString(R.string.folders_items, 1, 0)
        assert(
            rule.onAllNodesWithTag(DrawerFolderTag).filter(hasAnyChild(hasText(itemsText4)))
                .fetchSemanticsNodes().size == 8
        )

        assert(
            rule.onAllNodesWithTag(DrawerExpandIconTag).fetchSemanticsNodes().size == 16
        )

        rule.onNodeWithTag(DrawerTag).performTouchInput { swipeDown() }
        rule.onAllNodesWithTag(DrawerExpandIconTag)[3].performClick() // archive close
        rule.onNodeWithTag(DrawerTag).onChildren() // 5 folders, 0 items
            .filterToOne(hasText(itemsText2)).assertExists()
        rule.onAllNodesWithTag(DrawerExpandIconTag)[3].performClick() // archive open

        assert(rule.onAllNodesWithTag(DrawerExpandIconTag).fetchSemanticsNodes().size == 8)
        assert(
            rule.onAllNodesWithTag(DrawerFolderTag).filter(hasAnyChild(hasText(itemsText3)))
                .fetchSemanticsNodes().size == 4
        )

        repeat(4) {
            rule.onAllNodesWithTag(DrawerFolderTag)
                .filter(hasAnyChild(hasText(itemsText3)))[it]
                .onChildren().filter(hasTestTag(DrawerExpandIconTag)).onFirst()
                .performClick() // folders open
            rule.onNodeWithTag(DrawerTag).performTouchInput { swipeUp() }
        }

        assert(rule.onAllNodesWithTag(DrawerExpandIconTag).fetchSemanticsNodes().size == 16)
        assert(
            rule.onAllNodesWithTag(DrawerFolderTag).filter(hasAnyChild(hasText(itemsText4)))
                .fetchSemanticsNodes().size == 8
        )
    }
}