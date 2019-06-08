package app.marcdev.earworm.mainscreen.mainrecycler

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.earworm.R
import app.marcdev.earworm.data.database.FavouriteItem
import app.marcdev.earworm.internal.*

class MainRecyclerAdapter(context: Context,
                          private val itemClick: (Int) -> Unit,
                          private val itemLongClick: (FavouriteItem) -> Unit,
                          private val theme: Resources.Theme)
  : RecyclerView.Adapter<MainRecyclerViewHolder>() {

  private var items: List<FavouriteItem> = mutableListOf()
  private var inflater: LayoutInflater = LayoutInflater.from(context)

  override fun getItemViewType(position: Int): Int {
    return items[position].type
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
    lateinit var viewHolder: MainRecyclerViewHolder

    when(viewType) {
      HEADER -> {
        val view = inflater.inflate(R.layout.item_header, parent, false)
        viewHolder = MainRecyclerViewHolderHeader(view)
      }
      SONG -> {
        val view = inflater.inflate(R.layout.item_mainrecycler_song, parent, false)
        viewHolder = MainRecyclerViewHolderSong(view, itemClick, itemLongClick, theme)
      }

      ALBUM -> {
        val view = inflater.inflate(R.layout.item_mainrecycler_album, parent, false)
        viewHolder = MainRecyclerViewHolderAlbum(view, itemClick, itemLongClick, theme)
      }

      ARTIST -> {
        val view = inflater.inflate(R.layout.item_mainrecycler_artist, parent, false)
        viewHolder = MainRecyclerViewHolderArtist(view, itemClick, itemLongClick, theme)
      }

      GENRE -> {
        val view = inflater.inflate(R.layout.item_mainrecycler_genre, parent, false)
        viewHolder = MainRecyclerViewHolderGenre(view, itemClick, itemLongClick, theme)
      }
    }

    return viewHolder
  }

  override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int) {
    holder.display(items[position])
  }

  override fun getItemCount(): Int {
    return items.size
  }

  fun updateItems(items: List<FavouriteItem>) {
    this.items = items
    notifyDataSetChanged()
  }
}