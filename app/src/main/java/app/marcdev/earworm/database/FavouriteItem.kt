package app.marcdev.earworm.database

data class FavouriteItem(
  var songName: String,
  var albumName: String,
  var artistName: String,
  var genre: String,
  var date: String,
  var type: Int)
{
  var id: Int? = null
}