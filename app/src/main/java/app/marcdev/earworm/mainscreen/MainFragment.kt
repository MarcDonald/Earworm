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
import app.marcdev.earworm.mainscreen.mainrecycler.MainRecyclerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import timber.log.Timber

class MainFragment : Fragment()
{
  private lateinit var fab: FloatingActionButton

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
  {
    Timber.d("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.fragment_mainscreen, container, false)
    bindViews(view)
    setupRecycler(view)

    return view
  }

  private fun bindViews(view: View)
  {
    Timber.v("Log: bindViews: Started")
    this.fab = view.findViewById(R.id.fab_main)
    fab.setOnClickListener(fabOnClickListener)

    val nestedScrollView: NestedScrollView = view.findViewById(R.id.scroll_main)
    nestedScrollView.setOnScrollChangeListener(scrollViewOnScrollChangeListener)
  }

  private val fabOnClickListener = View.OnClickListener {
    Timber.d("Log: fabOnClickListener: Started")
    Toast.makeText(activity, "Fab Clicked", Toast.LENGTH_SHORT).show()
  }

  private var scrollViewOnScrollChangeListener = { _: View, _: Int, scrollY: Int, _: Int, oldScrollY: Int -> hideFabOnScroll(scrollY, oldScrollY) }

  private fun hideFabOnScroll(scrollY: Int, oldScrollY: Int)
  {
    if(scrollY > oldScrollY)
    {
      fab.hide()
    } else
    {
      fab.show()
    }
  }

  private fun setupRecycler(view: View)
  {
    Timber.v("Log: setupRecycler: Started")
    val recycler: RecyclerView = view.findViewById(R.id.recycler_main)
    recycler.adapter = MainRecyclerAdapter(context)
    recycler.layoutManager = LinearLayoutManager(context)
  }
}