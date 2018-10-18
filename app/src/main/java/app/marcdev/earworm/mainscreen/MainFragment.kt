package app.marcdev.earworm.mainscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.earworm.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import timber.log.Timber

class MainFragment : Fragment()
{
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
  {
    Timber.d("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.fragment_mainscreen, container, false)
    bindViews(view)

    return view
  }

  private fun bindViews(view: View)
  {
    Timber.v("Log: bindViews: Started")
    val fab: FloatingActionButton = view.findViewById(R.id.fab_main)
    fab.setOnClickListener(fabOnClickListener)

    val recycler: RecyclerView = view.findViewById(R.id.recycler_main)
  }

  private val fabOnClickListener = View.OnClickListener {
    Timber.d("Log: fabOnClickListener: Started")
    Toast.makeText(activity, "Fab Clicked", Toast.LENGTH_SHORT).show()
  }
}