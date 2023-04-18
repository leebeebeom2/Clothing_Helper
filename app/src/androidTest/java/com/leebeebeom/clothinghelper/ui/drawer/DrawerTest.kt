package com.leebeebeom.clothinghelper.ui.drawer

import androidx.annotation.StringRes
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
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
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.R.string.archive_cap
import com.leebeebeom.clothinghelper.R.string.bottoms_cap
import com.leebeebeom.clothinghelper.R.string.brand_cap
import com.leebeebeom.clothinghelper.R.string.closet_cap
import com.leebeebeom.clothinghelper.R.string.clothes_cap
import com.leebeebeom.clothinghelper.R.string.dropdown_menu_add_folder
import com.leebeebeom.clothinghelper.R.string.dropdown_menu_edit
import com.leebeebeom.clothinghelper.R.string.email
import com.leebeebeom.clothinghelper.R.string.etc_cap
import com.leebeebeom.clothinghelper.R.string.favorite
import com.leebeebeom.clothinghelper.R.string.folder
import com.leebeebeom.clothinghelper.R.string.folders_items
import com.leebeebeom.clothinghelper.R.string.main_screen
import com.leebeebeom.clothinghelper.R.string.nickname
import com.leebeebeom.clothinghelper.R.string.ootd_cap
import com.leebeebeom.clothinghelper.R.string.outers_cap
import com.leebeebeom.clothinghelper.R.string.outfit_cap
import com.leebeebeom.clothinghelper.R.string.password
import com.leebeebeom.clothinghelper.R.string.password_confirm
import com.leebeebeom.clothinghelper.R.string.positive
import com.leebeebeom.clothinghelper.R.string.reference_cap
import com.leebeebeom.clothinghelper.R.string.see_all
import com.leebeebeom.clothinghelper.R.string.shop_cap
import com.leebeebeom.clothinghelper.R.string.sign_up
import com.leebeebeom.clothinghelper.R.string.sign_up_with_email
import com.leebeebeom.clothinghelper.R.string.tops_cap
import com.leebeebeom.clothinghelper.R.string.trash
import com.leebeebeom.clothinghelper.R.string.wish_cap
import com.leebeebeom.clothinghelper.data.SignInEmail
import com.leebeebeom.clothinghelper.data.SignInPassword
import com.leebeebeom.clothinghelper.data.SignUpEmail
import com.leebeebeom.clothinghelper.data.SignUpName
import com.leebeebeom.clothinghelper.drawerOpen
import com.leebeebeom.clothinghelper.getContext
import com.leebeebeom.clothinghelper.hasStringRes
import com.leebeebeom.clothinghelper.onAllNodeWithStringRes
import com.leebeebeom.clothinghelper.onNodeWithStringRes
import com.leebeebeom.clothinghelper.signIn
import com.leebeebeom.clothinghelper.signOut
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import com.leebeebeom.clothinghelper.ui.MainNavHost
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerExpandIconTag
import com.leebeebeom.clothinghelper.ui.drawer.component.SettingIconTag
import com.leebeebeom.clothinghelper.ui.drawer.content.DrawerTag
import com.leebeebeom.clothinghelper.ui.drawer.content.folder.DrawerFolderTag
import com.leebeebeom.clothinghelper.ui.drawer.content.mainmenu.DrawerMainMenuTag
import com.leebeebeom.clothinghelper.ui.drawer.content.submenu.DrawerSubMenuTag
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
import com.leebeebeom.clothinghelper.waitTagExist
import com.leebeebeom.clothinghelper.waitTextExist
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

const val SignInName = "테스트 유저 1"

class DrawerTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private val restorationTester = StateRestorationTester(rule)
    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    private val settingIcon = rule.onNodeWithTag(SettingIconTag)

    private val main = rule.onNodeWithStringRes(main_screen)
    private val favoriteNode = rule.onNodeWithStringRes(favorite)
    private val seeAll = rule.onNodeWithStringRes(see_all)
    private val trashNode = rule.onNodeWithStringRes(trash)

    private val brandMain =
        rule.onAllNodesWithTag(DrawerMainMenuTag).filterToOne(hasAnyChild(hasStringRes(brand_cap)))
    private val clothes =
        rule.onAllNodesWithTag(DrawerMainMenuTag)
            .filterToOne(hasAnyChild(hasStringRes(clothes_cap)))
    private val outfit =
        rule.onAllNodesWithTag(DrawerMainMenuTag).filterToOne(hasAnyChild(hasStringRes(outfit_cap)))
    private val archive =
        rule.onAllNodesWithTag(DrawerMainMenuTag)
            .filterToOne(hasAnyChild(hasStringRes(archive_cap)))

    private val brandMainExpandIcon = getMainMenuExpandIcon(brand_cap)
    private val clothesExpandIcon = getMainMenuExpandIcon(clothes_cap)
    private val outfitExpandIcon = getMainMenuExpandIcon(outfit_cap)
    private val archiveExpandIcon = getMainMenuExpandIcon(archive_cap)

    private val brandSub = rule.onAllNodesWithTag(DrawerSubMenuTag)
        .filterToOne(hasAnyChild(hasStringRes(brand_cap)))
    private val shop = rule.onAllNodesWithTag(DrawerSubMenuTag)
        .filterToOne(hasAnyChild(hasStringRes(shop_cap)))
    private val closet = rule.onAllNodesWithTag(DrawerSubMenuTag)
        .filterToOne(hasAnyChild(hasStringRes(closet_cap)))
    private val wish = rule.onAllNodesWithTag(DrawerSubMenuTag)
        .filterToOne(hasAnyChild(hasStringRes(wish_cap)))
    private val ootd = rule.onAllNodesWithTag(DrawerSubMenuTag)
        .filterToOne(hasAnyChild(hasStringRes(ootd_cap)))
    private val reference = rule.onAllNodesWithTag(DrawerSubMenuTag)
        .filterToOne(hasAnyChild(hasStringRes(reference_cap)))

    private val brandSubExpandIcon = getSubMenuExpandIcon(brand_cap)
    private val shopExpandIcon = getSubMenuExpandIcon(shop_cap)
    private val closetExpandIcon = getSubMenuExpandIcon(closet_cap)
    private val wishExpandIcon = getSubMenuExpandIcon(wish_cap)
    private val ootdExpandIcon = getSubMenuExpandIcon(ootd_cap)
    private val referenceExpandIcon = getSubMenuExpandIcon(reference_cap)

    private val tops = rule.onAllNodesWithTag(DrawerSubMenuTag).filterToOne(
        hasAnyChild(hasStringRes(tops_cap))
    )
    private val bottoms = rule.onAllNodesWithTag(DrawerSubMenuTag).filterToOne(
        hasAnyChild(hasStringRes(bottoms_cap))
    )
    private val outers = rule.onAllNodesWithTag(DrawerSubMenuTag).filterToOne(
        hasAnyChild(hasStringRes(outers_cap))
    )
    private val etc = rule.onAllNodesWithTag(DrawerSubMenuTag).filterToOne(
        hasAnyChild(hasStringRes(etc_cap))
    )

    private val topsExpandIcon = getExpandIcon(DrawerSubMenuTag, tops_cap)
    private val bottomsExpandIcon = getExpandIcon(DrawerSubMenuTag, bottoms_cap)
    private val outersExpandIcon = getExpandIcon(DrawerSubMenuTag, outers_cap)
    private val etcExpandIcon = getExpandIcon(DrawerSubMenuTag, etc_cap)

    private val addFolder = rule.onNodeWithStringRes(dropdown_menu_add_folder)

    @Before
    fun init() {
        restorationTester.setContent { MainNavHost() }
        if (Firebase.auth.currentUser == null) rule.signIn()
    }

    @After
    fun removeData() {
        val uid = Firebase.auth.currentUser?.uid
        uid?.let { Firebase.database.reference.child(it).removeValue() }
    }

    @Test
    fun drawerRestoreTest() {
        rule.drawerOpen()

        outfitExpandIcon.performClick()
        ootd.performTouchInput { longClick() }

        repeat(2) {
            settingIcon.assertIsDisplayed() // drawer opened
            ootd.assertIsDisplayed()
            addFolder.assertIsDisplayed()

            restorationTester.emulateSavedInstanceStateRestore()
        }
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

        rule.onNodeWithStringRes(sign_up_with_email).performClick()
        rule.onNodeWithStringRes(email).performTextInput(SignUpEmail)
        rule.onNodeWithStringRes(nickname).performTextInput(SignUpName)
        rule.onNodeWithStringRes(password).performTextInput(SignInPassword)
        rule.onNodeWithStringRes(password_confirm).performTextInput(SignInPassword)
        rule.onNodeWithStringRes(sign_up).performClick()

        rule.waitTagExist(MainScreenTag)
        rule.drawerOpen()
        rule.waitTextExist("$SignUpName($SignUpEmail)")

        Firebase.auth.currentUser!!.delete()
    }

    @Test
    fun essentialMenuNavigationAndBackStackTest() { // with drawer close
        val menuList = listOf(
            settingIcon to SettingScreenTag,
            favoriteNode to FavoriteScreenTag,
            seeAll to SeeAllScreenTag,
            trashNode to TrashScreenTag,
            brandMain to BrandMainScreenTag,
            clothes to ClothesScreenTag,
            outfit to OutfitScreenTag,
            archive to ArchiveScreenTag
        )

        menuList.forEach(::navigateBackstackTest)

        rule.drawerOpen()
        brandMainExpandIcon.performClick()
        clothesExpandIcon.performClick()
        outfitExpandIcon.performClick()

        val subMenuList = listOf(
            brandSub to BrandScreenTag,
            shop to ShopScreenTag,
            closet to ClosetScreenTag,
            wish to WishScreenTag,
            ootd to OotdScreenTag,
            reference to ReferenceScreenTag
        )

        subMenuList.forEach(::navigateBackstackTest)

        rule.drawerOpen()
        outfitExpandIcon.performClick()
        closetExpandIcon.performClick()
        wishExpandIcon.performClick()
        drawerSwipeUp()

        val clothesMenuList = listOf(
            rule.onAllNodeWithStringRes(tops_cap)[0] to ClosetDetailScreenTag,
            rule.onAllNodeWithStringRes(bottoms_cap)[0] to ClosetDetailScreenTag,
            rule.onAllNodeWithStringRes(outers_cap)[0] to ClosetDetailScreenTag,
            rule.onAllNodeWithStringRes(etc_cap)[0] to ClosetDetailScreenTag,
            rule.onAllNodeWithStringRes(tops_cap)[1] to WishDetailScreenTag,
            rule.onAllNodeWithStringRes(bottoms_cap)[1] to WishDetailScreenTag,
            rule.onAllNodeWithStringRes(outers_cap)[1] to WishDetailScreenTag,
            rule.onAllNodeWithStringRes(etc_cap)[1] to WishDetailScreenTag,
        )

        clothesMenuList.forEach(::navigateBackstackTest)

        rule.drawerOpen()
        settingIcon.performClick()

        repeat(3) {
            rule.drawerOpen()
            main.performClick()
            main.assertIsNotDisplayed()
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
        brandMainExpandIcon.performClick() // brand open

        folderNavigationAndBackStackTest(
            node = brandSub,
            folderName = "브랜드",
            screenTag = BrandScreenTag
        )
        brandSubExpandIcon.performClick()

        folderNavigationAndBackStackTest(
            node = shop,
            folderName = "샵",
            screenTag = ShopScreenTag
        )

        rule.drawerOpen()
        brandMainExpandIcon.performClick() // brand close
        clothesExpandIcon.performClick() // clothes open
        closetExpandIcon.performClick() // closet open

        folderNavigationAndBackStackTest(
            node = tops,
            folderName = "클로젯 탑",
            screenTag = ClosetDetailScreenTag
        )
        topsExpandIcon.performClick() // closet top close

        folderNavigationAndBackStackTest(
            node = bottoms,
            folderName = "클로젯 바텀",
            screenTag = ClosetDetailScreenTag
        )
        bottomsExpandIcon.performClick() // closet bottom close

        folderNavigationAndBackStackTest(
            node = outers,
            folderName = "클로젯 아우터",
            screenTag = ClosetDetailScreenTag
        )
        outersExpandIcon.performClick() // closet outer close

        folderNavigationAndBackStackTest(
            node = etc,
            folderName = "클로젯 이티씨",
            screenTag = ClosetDetailScreenTag
        )
        etcExpandIcon.performClick() // closet etc close

        closetExpandIcon.performClick() // closet close
        wishExpandIcon.performClick() // wish open

        folderNavigationAndBackStackTest(
            node = tops,
            folderName = "위시 탑",
            screenTag = WishDetailScreenTag
        )
        topsExpandIcon.performClick() // wish top close

        folderNavigationAndBackStackTest(
            node = bottoms,
            folderName = "위시 바텀",
            screenTag = WishDetailScreenTag
        )
        bottomsExpandIcon.performClick() // wish bottom close

        folderNavigationAndBackStackTest(
            node = outers,
            folderName = "위시 아우터",
            screenTag = WishDetailScreenTag
        )
        outersExpandIcon.performClick() // wish outer close

        folderNavigationAndBackStackTest(
            node = etc,
            folderName = "위시 이티씨",
            screenTag = WishDetailScreenTag
        )
        etcExpandIcon.performClick() // wish etc close

        clothesExpandIcon.performClick() // clothes close

        outfitExpandIcon.performClick() // outfit open

        folderNavigationAndBackStackTest(
            node = ootd,
            folderName = "오오티디",
            screenTag = OotdScreenTag
        )
        folderNavigationAndBackStackTest(
            node = reference,
            folderName = "레퍼런스",
            screenTag = ReferenceScreenTag
        )

        outfitExpandIcon.performClick() // outfit close

        folderNavigationAndBackStackTest(
            node = archive,
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
        rule.onNodeWithStringRes(dropdown_menu_edit).performClick()
        rule.onNodeWithStringRes(folder).performTextInput(newFolderName)
        rule.onNodeWithStringRes(positive).performClick()
    }

    private fun addFolder(
        node: SemanticsNodeInteraction, folderName: String
    ) {
        rule.drawerOpen()
        node.performTouchInput { longClick() }
        addFolder.performClick()
        rule.onNodeWithStringRes(folder).performTextInput(folderName)
        rule.onNodeWithStringRes(positive).performClick()
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
    fun drawerCountTest() {
        brandMainExpandIcon.performClick() // brand main open

        drawerCountTest(brandSub, brandSubExpandIcon)
        drawerCountTest(shop, shopExpandIcon)

        brandMainExpandIcon.performClick() // brand main close

        clothesExpandIcon.performClick() // clothes open
        closetExpandIcon.performClick() // closet open

        drawerCountTest(tops, topsExpandIcon)
        drawerCountTest(bottoms, bottomsExpandIcon)
        drawerCountTest(outers, outersExpandIcon)
        drawerCountTest(etc, etcExpandIcon)

        closetExpandIcon.performClick() // closet close
        wishExpandIcon.performClick() // wish open

        drawerCountTest(tops, topsExpandIcon)
        drawerCountTest(bottoms, bottomsExpandIcon)
        drawerCountTest(outers, outersExpandIcon)
        drawerCountTest(etc, etcExpandIcon)

        clothesExpandIcon.performClick() // clothes close

        outfitExpandIcon.performClick() // outfit open
        drawerCountTest(ootd, ootdExpandIcon)
        drawerCountTest(reference, referenceExpandIcon)

        outfitExpandIcon.performClick()

        drawerCountTest(archive, archiveExpandIcon)
    }

    private fun drawerCountTest(
        node: SemanticsNodeInteraction,
        expandIconNode: SemanticsNodeInteraction
    ) {
        repeat(3) { addFolder(node, "폴더 $it") }
        node.onChildren().filterToOne(hasText(getCountText(3))).assertExists()

        repeat(2) {
            addFolder(rule.onNodeWithText("폴더 $it"), "폴더 $it-${it + 1}")
            addFolder(rule.onNodeWithText("폴더 $it"), "폴더 $it-${it + 2}")
        }
        rule.onAllNodesWithTag(DrawerFolderTag).filterToOne(hasAnyChild(hasText("폴더 0")))
            .onChildren()
            .filterToOne(hasText(getCountText(2))).assertExists()
        rule.onAllNodesWithTag(DrawerFolderTag).filterToOne(hasAnyChild(hasText("폴더 1")))
            .onChildren()
            .filterToOne(hasText(getCountText(2))).assertExists()

        repeat(1) {
            addFolder(rule.onNodeWithText("폴더 $it-${it + 1}"), "폴더 $it-${it + 1}-${it + 2}")
        }

        rule.onAllNodesWithTag(DrawerFolderTag).filterToOne(hasAnyChild(hasText("폴더 0-1")))
            .onChildren()
            .filterToOne(hasText(getCountText(1))).assertExists()

        expandIconNode.performClick()
    }

    private fun drawerSwipeUp() {
        rule.onNodeWithTag(DrawerTag).performTouchInput { swipeUp() }
    }

    private fun getMainMenuExpandIcon(@StringRes res: Int) = getExpandIcon(DrawerMainMenuTag, res)

    private fun getSubMenuExpandIcon(@StringRes res: Int) = getExpandIcon(DrawerSubMenuTag, res)

    private fun getExpandIcon(tag: String, @StringRes res: Int) =
        rule.onAllNodesWithTag(tag).filterToOne(hasAnyChild(hasStringRes(res)))
            .onChildren()
            .filterToOne(hasTestTag(DrawerExpandIconTag))
}

fun getCountText(foldersSize: Int) =
    getContext().getString(folders_items, foldersSize, 0)