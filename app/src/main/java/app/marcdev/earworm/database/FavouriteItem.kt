package app.marcdev.earworm.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_items")
data class FavouriteItem(
  var songName: String,
  var albumName: String,
  var artistName: String,
  var genre: String,
  var date: String,
  var type: Int
) {

  @PrimaryKey(autoGenerate = true)
  var id: Int? = null
}