package app.marcdev.earworm.mainscreen.mainrecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.earworm.R
import app.marcdev.earworm.data.database.FavouriteItem
import app.marcdev.earworm.mainscreen.MainFragmentPresenter
import app.marcdev.earworm.utils.*
import timber.log.Timber

class MainRecyclerAdapter(context: Context) : RecyclerView.Adapter<MainRecyclerViewHolder>(), MainRecyclerView {

  private var items: List<FavouriteItem> = mutableListOf()
  private var inflater: LayoutInflater = LayoutInflater.from(context)

  override fun getItemViewType(position: Int): Int {
    Timber.v("Log: getItemViewType: Started")
    return items[position].type
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
    Timber.v("Log: onCreateViewHolder: Started")

    lateinit var viewHolder: MainRecyclerViewHolder

    when(viewType) {
      HEADER -> {
        Timber.v("Log: onCreateViewHolder: Type == Header")
        val view = inflater.inflate(R.layout.item_header, parent, false)
        viewHolder = MainRecyclerViewHolderHeader(view)
      }
      SONG -> {
        Timber.v("Log: onCreateViewHolder: Type == Song")
        val view = inflater.inflate(R.layout.item_mainrecycler_song, parent, false)
        viewHolder = MainRecyclerViewHolderSong(view)
      }

      ALBUM -> {
        Timber.v("Log: onCreateViewHolder: Type == Album")
        val view = inflater.inflate(R.layout.item_mainrecycler_album, parent, false)
        viewHolder = MainRecyclerViewHolderAlbum(view)
      }

      ARTIST -> {
        Timber.v("Log: onCreateViewHolder: Type == Artist")
        val view = inflater.inflate(R.layout.item_mainrecycler_artist, parent, false)
        viewHolder = MainRecyclerViewHolderArtist(view)
      }

      GENRE -> {
        Timber.v("Log: onCreateViewHolder: Type == Genre")
        val view = inflater.inflate(R.layout.item_mainrecycler_genre, parent, false)
        viewHolder = MainRecyclerViewHolderGenre(view)
      }
    }

    return viewHolder
  }

  override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int) {
    Timber.v("Log: onBindViewHolder: $position")
    holder.display(items[position])
  }

  override fun getItemCount(): Int {
    Timber.v("Log: getItemCount: ${items.size}")
    return items.size
  }

  override fun updateItems(items: List<FavouriteItem>) {
    Timber.d("Log: updateItems: Started")
    this.items = items
    notifyDataSetChanged()
  }
}