package app.marcdev.earworm.database

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import app.marcdev.earworm.SONG
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class DAOTest {

  private var database: AppDatabase? = null
  private var dao: DAO? = null

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

  @Test
  fun insertOneSong_getAllItems() {
    val testName = "Test Song Name"
    val testAlbum = "Test Album Name"
    val testArtist = "Test Artist Name"
    val testGenre = "Test Genre Name"
    val testDay = 1
    val testMonth = 1
    val testYear = 2018
    val testType: Int = SONG

    val testItem = FavouriteItem(testName, testAlbum, testArtist, testGenre, testDay, testMonth, testYear, testType)

    val returnedItemsWhenNothingInserted: MutableList<FavouriteItem> = database?.dao()!!.getAllItems()
    Assert.assertEquals(0, returnedItemsWhenNothingInserted.size)

    database?.dao()!!.insertOrUpdateItem(testItem)

    val returnedItemsWhenOneInserted: MutableList<FavouriteItem> = database?.dao()!!.getAllItems()
    Assert.assertEquals(1, returnedItemsWhenOneInserted.size)
  }

  @Test
  fun insertMultipleSongs_getAllItems() {
    val testName = "Test Song Name"
    val testAlbum = "Test Album Name"
    val testArtist = "Test Artist Name"
    val testGenre = "Test Genre Name"
    val testDay = 1
    val testMonth = 1
    val testYear = 2018
    val testType: Int = SONG

    val testItem1 = FavouriteItem(testName, testAlbum, testArtist, testGenre, testDay, testMonth, testYear, testType)
    val testItem2 = FavouriteItem(testName, testAlbum, testArtist, testGenre, testDay, testMonth, testYear, testType)

    val returnedItemsWhenNothingInserted: MutableList<FavouriteItem> = database?.dao()!!.getAllItems()
    Assert.assertEquals(0, returnedItemsWhenNothingInserted.size)

    database?.dao()!!.insertOrUpdateItem(testItem1)
    database?.dao()!!.insertOrUpdateItem(testItem2)

    val returnedItemsWhenOneInserted: MutableList<FavouriteItem> = database?.dao()!!.getAllItems()
    Assert.assertEquals(2, returnedItemsWhenOneInserted.size)
  }

  @Test
  fun insertOneSong_getItemById_deleteSong() {
    val testName = "Test Song Name"
    val testAlbum = "Test Album Name"
    val testArtist = "Test Artist Name"
    val testGenre = "Test Genre Name"
    val testDay = 1
    val testMonth = 1
    val testYear = 2018
    val testType: Int = SONG
    val testId = 1

    val testItem = FavouriteItem(testName, testAlbum, testArtist, testGenre, testDay, testMonth, testYear, testType)
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
    val testName = "Test Song Name"
    val testAlbum = "Test Album Name"
    val testArtist = "Test Artist Name"
    val testGenre = "Test Genre Name"
    val testDay = 1
    val testMonth = 1
    val testYear = 2018
    val testType: Int = SONG
    val testId1 = 1
    val testId2 = 2

    val testItem1 = FavouriteItem(testName, testAlbum, testArtist, testGenre, testDay, testMonth, testYear, testType)
    testItem1.id = testId1
    val testItem2 = FavouriteItem(testName, testAlbum, testArtist, testGenre, testDay, testMonth, testYear, testType)
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
    val testName = "Test Song Name"
    val testAlbum = "Test Album Name"
    val testArtist = "Test Artist Name"
    val testGenre = "Test Genre Name"
    val testDay = 1
    val testMonth = 1
    val testYear = 2018
    val testType: Int = SONG
    val testId1 = 1
    val testId2 = 2

    val testItem1 = FavouriteItem(testName, testAlbum, testArtist, testGenre, testDay, testMonth, testYear, testType)
    testItem1.id = testId1
    val testItem2 = FavouriteItem(testName, testAlbum, testArtist, testGenre, testDay, testMonth, testYear, testType)
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
}