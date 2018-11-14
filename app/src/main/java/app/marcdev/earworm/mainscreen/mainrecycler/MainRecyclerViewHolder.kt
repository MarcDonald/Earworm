package app.marcdev.earworm.mainscreen.mainrecycler

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.preference.PreferenceManager
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.earworm.R
import app.marcdev.earworm.database.FavouriteItem
import app.marcdev.earworm.mainscreen.MainFragmentPresenter
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

open class MainRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  private lateinit var displayedItem: FavouriteItem
  private lateinit var presenter: MainFragmentPresenter
  private lateinit var editDialog: Dialog
  private val prefs = PreferenceManager.getDefaultSharedPreferences(itemView.context)

  private val snackbarActionListener = View.OnClickListener {
    Timber.d("Log: snackbarActionListener: Clicked")
    prefs.edit().putBoolean("pref_show_tips", false).apply()
  }

  private val itemClickListener = View.OnClickListener {
    if(prefs.getBoolean("pref_show_tips", true)) {
      val snackbar = Snackbar.make(it, itemView.resources.getString(R.string.long_click_hint), Snackbar.LENGTH_SHORT)
      snackbar.setAction(itemView.resources.getString(R.string.dont_show), snackbarActionListener)
      snackbar.show()
    }
  }

  private val itemLongClickListener = View.OnLongClickListener {
    editDialog.show()
    return@OnLongClickListener true
  }

  private val editOnClickListener = View.OnClickListener {
    Timber.d("Log: editOnClickListener: Clicked")
    editDialog.dismiss()
    presenter.editItemClick(displayedItem.id!!)
  }

  private val deleteOnClickListener = View.OnClickListener {
    Timber.d("Log: deleteOnClickListener: Clicked")
    presenter.deleteItem(displayedItem)
    editDialog.dismiss()
  }

  init {
    initEditDialog()
    itemView.setOnClickListener(itemClickListener)
    itemView.setOnLongClickListener(itemLongClickListener)
  }

  open fun display(favouriteItemToDisplay: FavouriteItem) {
    // TO BE OVERRIDDEN
  }

  fun bindPresenterAndItem(presenter: MainFragmentPresenter, item: FavouriteItem) {
    this.presenter = presenter
    this.displayedItem = item
  }

  private fun initEditDialog() {
    this.editDialog = Dialog(itemView.context)
    editDialog.setContentView(R.layout.dialog_edit_or_delete)
    editDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val editButton = editDialog.findViewById<MaterialButton>(R.id.btn_add_or_delete_edit)
    editButton.setOnClickListener(editOnClickListener)
    val deleteButton = editDialog.findViewById<MaterialButton>(R.id.btn_add_or_delete_delete)
    deleteButton.setOnClickListener(deleteOnClickListener)
  }
}