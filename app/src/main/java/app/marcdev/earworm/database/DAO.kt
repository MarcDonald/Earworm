package app.marcdev.earworm.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DAO {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertOrUpdateItem(item: FavouriteItem)

  @Query("SELECT * FROM favourite_items")
  fun getAllItems(): MutableList<FavouriteItem>

  @Query("SELECT * FROM favourite_items where id = :id")
  fun getItemById(id: Int): MutableList<FavouriteItem>

  @Query("DELETE FROM favourite_items where id = :id")
  fun deleteItemById(id: Int)
}