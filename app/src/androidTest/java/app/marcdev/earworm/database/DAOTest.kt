package app.marcdev.earworm.database

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import app.marcdev.earworm.utils.SONG
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class DAOTest {

  private var database: AppDatabase? = null
  private var dao: DAO? = null

  // Default values
  private val testName = "Test Song Name"
  private val testAlbum = "Test Album Name"
  private val testArtist = "Test Artist Name"
  private val testGenre = "Test Genre Name"
  private val testImageName = "testimagename.jpg"
  private val testDay = 1
  private val testMonth = 1
  private val testYear = 2018
  private val testType: Int = SONG

  @Before
  fun setUp() {
    database =
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context, AppDatabase::class.java)
          .allowMainThreadQueries().build()
    dao = database!!.dao()
  }

  @After
  fun tearDown() {
    database?.close()
  }

  private fun createTestItem(): FavouriteItem {
    return FavouriteItem(testName, testAlbum, testArtist, testGenre, testDay, testMonth, testYear, testType, testImageName)
  }

  @Test
  fun insertOneSong_getAllItems() {
    val testItem = createTestItem()

    val returnedItemsWhenNothingInserted: MutableList<FavouriteItem> = database?.dao()!!.getAllItems()
    Assert.assertEquals(0, returnedItemsWhenNothingInserted.size)

    database?.dao()!!.insertOrUpdateItem(testItem)

    val returnedItemsWhenOneInserted: MutableList<FavouriteItem> = database?.dao()!!.getAllItems()
    Assert.assertEquals(1, returnedItemsWhenOneInserted.size)
  }

  @Test
  fun insertMultipleSongs_getAllItems() {
    val testItem1 = createTestItem()
    val testItem2 = createTestItem()

    val returnedItemsWhenNothingInserted: MutableList<FavouriteItem> = database?.dao()!!.getAllItems()
    Assert.assertEquals(0, returnedItemsWhenNothingInserted.size)

    database?.dao()!!.insertOrUpdateItem(testItem1)
    database?.dao()!!.insertOrUpdateItem(testItem2)

    val returnedItemsWhenOneInserted: MutableList<FavouriteItem> = database?.dao()!!.getAllItems()
    Assert.assertEquals(2, returnedItemsWhenOneInserted.size)
  }

  @Test
  fun insertOneSong_getItemById_deleteSong() {
    val testId = 1

    val testItem = createTestItem()
    testItem.id = testId

    val returnedItemsWhenNothingInserted: MutableList<FavouriteItem> = database?.dao()!!.getItemById(testId)
    Assert.assertEquals(0, returnedItemsWhenNothingInserted.size)

    database?.dao()!!.insertOrUpdateItem(testItem)

    val returnedItemsWhenOneInserted: MutableList<FavouriteItem> = database?.dao()!!.getItemById(testId)
    Assert.assertEquals(1, returnedItemsWhenOneInserted.size)

    database?.dao()!!.deleteItemById(testId)

    val returnedItemsWhenOneDeleted: MutableList<FavouriteItem> = database?.dao()!!.getItemById(testId)
    Assert.assertEquals(0, returnedItemsWhenOneDeleted.size)
  }

  @Test
  fun insertTwoSongs_deleteOneSong() {
    val testId1 = 1
    val testId2 = 2

    val testItem1 = createTestItem()
    testItem1.id = testId1
    val testItem2 = createTestItem()
    testItem2.id = testId2

    val returnedItemsWhenNothingInserted: MutableList<FavouriteItem> = database?.dao()!!.getAllItems()
    Assert.assertEquals(0, returnedItemsWhenNothingInserted.size)

    database?.dao()!!.insertOrUpdateItem(testItem1)
    database?.dao()!!.insertOrUpdateItem(testItem2)

    val returnedItemsWhenTwoInserted: MutableList<FavouriteItem> = database?.dao()!!.getAllItems()
    Assert.assertEquals(2, returnedItemsWhenTwoInserted.size)

    database?.dao()!!.deleteItemById(testId1)

    val returnedItemsWhenOneDeleted: MutableList<FavouriteItem> = database?.dao()!!.getAllItems()
    Assert.assertEquals(1, returnedItemsWhenOneDeleted.size)

    val returnedItemsWhenDeletedOneSearched: MutableList<FavouriteItem> = database?.dao()!!.getItemById(testId1)
    Assert.assertEquals(0, returnedItemsWhenDeletedOneSearched.size)
  }

  @Test
  fun insertMultipleSongs_getOneById() {
    val testId1 = 1
    val testId2 = 2

    val testItem1 = createTestItem()
    testItem1.id = testId1
    val testItem2 = createTestItem()
    testItem2.id = testId2

    val returnedItemsWhenNothingInserted: MutableList<FavouriteItem> = database?.dao()!!.getAllItems()
    Assert.assertEquals(0, returnedItemsWhenNothingInserted.size)

    database?.dao()!!.insertOrUpdateItem(testItem1)
    database?.dao()!!.insertOrUpdateItem(testItem2)

    val returnedAllItemsWhenTwoInserted: MutableList<FavouriteItem> = database?.dao()!!.getAllItems()
    Assert.assertEquals(2, returnedAllItemsWhenTwoInserted.size)

    val returnedItemsWhenSearchedById1: MutableList<FavouriteItem> = database?.dao()!!.getItemById(testId1)
    Assert.assertEquals(1, returnedItemsWhenSearchedById1.size)
  }

  @Test
  fun insertMultipleItemsUsingDifferentImages_countEach() {
    val testImage1 = "testImage1.jpg"
    val testImage2 = "testImage2.jpg"

    val testItem1 = createTestItem()
    testItem1.imageName = testImage1

    val testItem2 = createTestItem()
    testItem2.imageName = testImage2

    val testItem3 = createTestItem()
    testItem3.imageName = testImage2

    database?.dao()!!.insertOrUpdateItem(testItem1)
    database?.dao()!!.insertOrUpdateItem(testItem2)
    database?.dao()!!.insertOrUpdateItem(testItem3)

    val returnedValueWhenSearchedForTestImage1: Int = database?.dao()!!.getNumberOfEntriesUsingImage(testImage1)
    Assert.assertEquals(1, returnedValueWhenSearchedForTestImage1)

    val returnedValueWhenSearchedForTestImage2: Int = database?.dao()!!.getNumberOfEntriesUsingImage(testImage2)
    Assert.assertEquals(2, returnedValueWhenSearchedForTestImage2)
  }

  @Test
  fun insertMultipleItemsUsingDifferentImages_countEachAndDelete() {
    val testImage1 = "testImage1.jpg"
    val testImage2 = "testImage2.jpg"
    val testId1 = 1
    val testId2 = 2
    val testId3 = 3

    val testItem1 = createTestItem()
    testItem1.imageName = testImage1
    testItem1.id = testId1

    val testItem2 = createTestItem()
    testItem2.imageName = testImage2
    testItem2.id = testId2

    val testItem3 = createTestItem()
    testItem3.imageName = testImage2
    testItem3.id = testId3

    database?.dao()!!.insertOrUpdateItem(testItem1)
    database?.dao()!!.insertOrUpdateItem(testItem2)
    database?.dao()!!.insertOrUpdateItem(testItem3)

    val returnedValueWhenSearchedForTestImage1: Int = database?.dao()!!.getNumberOfEntriesUsingImage(testImage1)
    Assert.assertEquals(1, returnedValueWhenSearchedForTestImage1)

    val returnedValueWhenSearchedForTestImage2: Int = database?.dao()!!.getNumberOfEntriesUsingImage(testImage2)
    Assert.assertEquals(2, returnedValueWhenSearchedForTestImage2)

    database?.dao()!!.deleteItemById(testId2)

    val returnedValueWhenSearchedForTestImage1AfterDelete: Int = database?.dao()!!.getNumberOfEntriesUsingImage(testImage1)
    Assert.assertEquals(1, returnedValueWhenSearchedForTestImage1AfterDelete)

    val returnedValueWhenSearchedForTestImage2AfterDelete: Int = database?.dao()!!.getNumberOfEntriesUsingImage(testImage2)
    Assert.assertEquals(1, returnedValueWhenSearchedForTestImage2AfterDelete)
  }

  @Test
  fun insertMultipleItemsUsingDifferentImages_countEachAndDeleteOneCompletely() {
    val testImage1 = "testImage1.jpg"
    val testImage2 = "testImage2.jpg"
    val testId1 = 1
    val testId2 = 2
    val testId3 = 3

    val testItem1 = createTestItem()
    testItem1.imageName = testImage1
    testItem1.id = testId1

    val testItem2 = createTestItem()
    testItem2.imageName = testImage2
    testItem2.id = testId2

    val testItem3 = createTestItem()
    testItem3.imageName = testImage2
    testItem3.id = testId3

    database?.dao()!!.insertOrUpdateItem(testItem1)
    database?.dao()!!.insertOrUpdateItem(testItem2)
    database?.dao()!!.insertOrUpdateItem(testItem3)

    val returnedValueWhenSearchedForTestImage1: Int = database?.dao()!!.getNumberOfEntriesUsingImage(testImage1)
    Assert.assertEquals(1, returnedValueWhenSearchedForTestImage1)

    val returnedValueWhenSearchedForTestImage2: Int = database?.dao()!!.getNumberOfEntriesUsingImage(testImage2)
    Assert.assertEquals(2, returnedValueWhenSearchedForTestImage2)

    database?.dao()!!.deleteItemById(testId1)

    val returnedValueWhenSearchedForTestImage1AfterDelete: Int = database?.dao()!!.getNumberOfEntriesUsingImage(testImage1)
    Assert.assertEquals(0, returnedValueWhenSearchedForTestImage1AfterDelete)

    val returnedValueWhenSearchedForTestImage2AfterDelete: Int = database?.dao()!!.getNumberOfEntriesUsingImage(testImage2)
    Assert.assertEquals(2, returnedValueWhenSearchedForTestImage2AfterDelete)
  }
}