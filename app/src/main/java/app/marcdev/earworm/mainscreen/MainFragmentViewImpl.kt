package app.marcdev.earworm.mainscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.earworm.R
import app.marcdev.earworm.database.FavouriteItem
import app.marcdev.earworm.mainscreen.mainrecycler.MainRecyclerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import timber.log.Timber

class MainFragmentViewImpl : Fragment(), MainFragmentView {

  private lateinit var fab: FloatingActionButton
  private lateinit var recyclerAdapter: MainRecyclerAdapter
  private val presenter = MainFragmentPresenterImpl(this)

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.d("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.fragment_mainscreen, container, false)
    bindViews(view)
    setupRecycler(view)

    return view
  }

  private fun bindViews(view: View) {
    Timber.v("Log: bindViews: Started")
    this.fab = view.findViewById(R.id.fab_main)
    fab.setOnClickListener(fabOnClickListener)
    fab.setOnLongClickListener(fabOnLongClickListener)

    val nestedScrollView: NestedScrollView = view.findViewById(R.id.scroll_main)
    nestedScrollView.setOnScrollChangeListener(scrollViewOnScrollChangeListener)
  }

  private val fabOnClickListener = View.OnClickListener {
    presenter.fabClick()
  }
  private val fabOnLongClickListener = View.OnLongClickListener {
    presenter.fabLongClick()
    return@OnLongClickListener true
  }

  private var scrollViewOnScrollChangeListener = { _: View, _: Int, scrollY: Int, _: Int, oldScrollY: Int -> hideFabOnScroll(scrollY, oldScrollY) }

  private fun hideFabOnScroll(scrollY: Int, oldScrollY: Int) {
    if (scrollY > oldScrollY) {
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