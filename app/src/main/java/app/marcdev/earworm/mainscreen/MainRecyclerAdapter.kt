package app.marcdev.earworm.mainscreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.earworm.R
import timber.log.Timber

class MainRecyclerAdapter(context: Context?) : RecyclerView.Adapter<MainRecyclerViewHolder>()
{
  private val items: List<String> = mutableListOf("One", "Two", "Three", "Four")
  private var inflater: LayoutInflater = LayoutInflater.from(context)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder
  {
    Timber.v("Log: onCreateViewHolder: Started")
    val view: View = inflater.inflate(R.layout.item_mainrecycler, parent, false)

    return MainRecyclerViewHolder(view)
  }

  override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int)
  {
    Timber.v("Log: onBindViewHolder: $position")
    holder.display(items[position])
  }

  override fun getItemCount(): Int
  {
    Timber.v("Log: getItemCount: ${items.size}")
    return items.size
  }
}