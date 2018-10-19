package app.marcdev.earworm.mainscreen

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.earworm.R
import timber.log.Timber

class MainRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
{
  private var textView: TextView = itemView.findViewById(R.id.txt_itemName)

  fun display(stringToDisplay: String)
  {
    Timber.d("Log: display: $stringToDisplay")
    textView.text = stringToDisplay
  }
}