package app.marcdev.earworm.repository

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import app.marcdev.earworm.database.AppDatabase
import app.marcdev.earworm.database.DAO
import app.marcdev.earworm.database.FavouriteItem
import app.marcdev.earworm.utils.SONG
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class FavouriteItemRepositoryTest {

  private var database: AppDatabase? = null
  private var repository: FavouriteItemRepository? = null
  private var dao: DAO? = null

  @Before
  fun setUp() {
    database =
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context, AppDatabase::class.java)
          .allowMainThreadQueries().build()

    dao = database!!.dao()
    repository = FavouriteItemRepositoryImpl(dao!!)
  }

  @After
  fun tearDown() {
    database!!.close()
  }

  @Test
  fun insertMultipleSongs_getAllItems() = runBlocking {
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

    val returnedItemsWhenNothingInserted: MutableList<FavouriteItem> = repository!!.getAllItems()
    Assert.assertEquals(0, returnedItemsWhenNothingInserted.size)

    repository!!.insertOrUpdateItem(testItem1)
    repository!!.insertOrUpdateItem(testItem2)

    val returnedItemsWhenOneInserted: MutableList<FavouriteItem> = repository!!.getAllItems()
    Assert.assertEquals(2, returnedItemsWhenOneInserted.size)
  }

  @Test
  fun insertOneSong_getItemById_deleteSong() = runBlocking {
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

    val returnedItemsWhenNothingInserted: MutableList<FavouriteItem> = repository!!.getItem(testId)
    Assert.assertEquals(0, returnedItemsWhenNothingInserted.size)

    repository!!.insertOrUpdateItem(testItem)

    val returnedItemsWhenOneInserted: MutableList<FavouriteItem> = repository!!.getItem(testId)
    Assert.assertEquals(1, returnedItemsWhenOneInserted.size)

    repository!!.deleteItem(testId)

    val returnedItemsWhenOneDeleted: MutableList<FavouriteItem> = repository!!.getItem(testId)
    Assert.assertEquals(0, returnedItemsWhenOneDeleted.size)
  }

  @Test
  fun insertTwoSongs_deleteOneSong() = runBlocking {
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

    val returnedItemsWhenNothingInserted: MutableList<FavouriteItem> = repository!!.getAllItems()
    Assert.assertEquals(0, returnedItemsWhenNothingInserted.size)

    repository!!.insertOrUpdateItem(testItem1)
    repository!!.insertOrUpdateItem(testItem2)

    val returnedItemsWhenTwoInserted: MutableList<FavouriteItem> = repository!!.getAllItems()
    Assert.assertEquals(2, returnedItemsWhenTwoInserted.size)

    repository!!.deleteItem(testId1)

    val returnedItemsWhenOneDeleted: MutableList<FavouriteItem> = repository!!.getAllItems()
    Assert.assertEquals(1, returnedItemsWhenOneDeleted.size)

    val returnedItemsWhenDeletedOneSearched: MutableList<FavouriteItem> = repository!!.getItem(testId1)
    Assert.assertEquals(0, returnedItemsWhenDeletedOneSearched.size)
  }

  @Test
  fun insertMultipleSongs_getOneById() = runBlocking {
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

    val returnedItemsWhenNothingInserted: MutableList<FavouriteItem> = repository!!.getAllItems()
    Assert.assertEquals(0, returnedItemsWhenNothingInserted.size)

    repository!!.insertOrUpdateItem(testItem1)
    repository!!.insertOrUpdateItem(testItem2)

    val returnedAllItemsWhenTwoInserted: MutableList<FavouriteItem> = repository!!.getAllItems()
    Assert.assertEquals(2, returnedAllItemsWhenTwoInserted.size)

    val returnedItemsWhenSearchedById1: MutableList<FavouriteItem> = repository!!.getItem(testId1)
    Assert.assertEquals(1, returnedItemsWhenSearchedById1.size)
  }
}