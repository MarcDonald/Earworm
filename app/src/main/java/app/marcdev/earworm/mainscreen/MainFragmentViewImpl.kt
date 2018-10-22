package app.marcdev.earworm.mainscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.earworm.R
import app.marcdev.earworm.database.FavouriteItem
import app.marcdev.earworm.mainscreen.additem.AddItemBottomSheet
import app.marcdev.earworm.mainscreen.mainrecycler.MainRecyclerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import timber.log.Timber

class MainFragmentViewImpl : Fragment(), MainFragmentView {

  private lateinit var fab: FloatingActionButton
  private lateinit var noEntriesWarning: TextView
  private lateinit var recyclerAdapter: MainRecyclerAdapter
  private lateinit var presenter: MainFragmentPresenter

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.d("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.fragment_mainscreen, container, false)
    presenter = MainFragmentPresenterImpl(this, activity!!.applicationContext)
    bindViews(view)
    setupRecycler(view)
    fillData()

    return view
  }

  private fun bindViews(view: View) {
    Timber.v("Log: bindViews: Started")
    this.fab = view.findViewById(R.id.fab_main)
    fab.setOnClickListener(fabOnClickListener)
    fab.setOnLongClickListener(fabOnLongClickListener)

    noEntriesWarning = view.findViewById(R.id.txt_noEntries)

    val nestedScrollView: NestedScrollView = view.findViewById(R.id.scroll_main)
    nestedScrollView.setOnScrollChangeListener(scrollViewOnScrollChangeListener)
  }

  private val fabOnClickListener = View.OnClickListener {
    Timber.d("Log: Fab Clicked")
    val addDialog = AddItemBottomSheet()
    addDialog.show(fragmentManager, "Add Item Bottom Sheet Dialog")
  }
  private val fabOnLongClickListener = View.OnLongClickListener {
    Timber.d("Log: Fab Long Clicked")
    presenter.fabLongClick()
    return@OnLongClickListener true
  }

  private var scrollViewOnScrollChangeListener = { _: View, _: Int, scrollY: Int, _: Int, oldScrollY: Int -> hideFabOnScroll(scrollY, oldScrollY) }

  private fun hideFabOnScroll(scrollY: Int, oldScrollY: Int) {
    if(scrollY > oldScrollY) {
      fab.hide()
    } else {
      fab.show()
    }
  }

  private fun setupRecycler(view: View) {
    Timber.v("Log: setupRecycler: Started")
    val recycler: RecyclerView = view.findViewById(R.id.recycler_main)
    this.recyclerAdapter = MainRecyclerAdapter(context)
    recycler.adapter = recyclerAdapter
    recycler.layoutManager = LinearLayoutManager(context)
  }

  private fun fillData() {
    Timber.d("Log: fillData: Started")
    presenter.getAllItems()
  }

  override fun displayNoEntriesWarning(display: Boolean) {
    Timber.d("Log: displayNoEntriesWarning: Started")

    if(display) {
      Timber.d("Log: displayNoEntriesWarning: Displaying")
      noEntriesWarning.visibility = View.VISIBLE
    } else {
      Timber.d("Log: displayNoEntriesWarning: Hiding")
      noEntriesWarning.visibility = View.GONE
    }
  }

  override fun updateRecycler(items: MutableList<FavouriteItem>) {
    Timber.d("Log: updateRecycler: Started")
    recyclerAdapter.updateItems(items)
  }

  override fun displayAddedToast() {
    Timber.d("Log: displayAddedToast: Started")
    Toast.makeText(activity, resources.getString(R.string.item_added), Toast.LENGTH_SHORT).show()
  }

  override fun displayClearedToast() {
    Timber.d("Log: displayClearedToast: Started")
    Toast.makeText(activity, resources.getString(R.string.item_deleted), Toast.LENGTH_SHORT).show()
  }
}