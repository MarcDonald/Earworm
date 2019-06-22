package com.marcdonald.earworm.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FavouriteItems")
data class FavouriteItem(
  var songName: String,
  var albumName: String,
  var artistName: String,
  var genre: String,
  var day: Int,
  var month: Int,
  var year: Int,
  var type: Int,
  var imageName: String
) {

  @PrimaryKey(autoGenerate = true)
  var id: Int = 0
}