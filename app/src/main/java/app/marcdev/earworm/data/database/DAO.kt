package app.marcdev.earworm.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DAO {

  @Insert(onConflict = OnConflictStrategy.FAIL)
  fun insertItem(item: FavouriteItem)

  @Update
  fun updateItem(item: FavouriteItem)

  @Query("SELECT * FROM FavouriteItems ORDER BY year DESC, month DESC, day DESC, id DESC")
  fun getAllItems(): LiveData<List<FavouriteItem>>

  @Query("SELECT * FROM FavouriteItems where id = :id")
  fun getItemById(id: Int): FavouriteItem

  @Query("DELETE FROM FavouriteItems where id = :id")
  fun deleteItemById(id: Int)

  @Query("SELECT COUNT(*) FROM FavouriteItems WHERE imageName = :imageName")
  fun getNumberOfEntriesUsingImage(imageName: String): Int
}