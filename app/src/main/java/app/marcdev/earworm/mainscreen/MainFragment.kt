package app.marcdev.earworm.mainscreen

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.earworm.R
import app.marcdev.earworm.additem.AddItemBottomSheet
import app.marcdev.earworm.data.database.FavouriteItem
import app.marcdev.earworm.internal.PREF_SHOW_TIPS
import app.marcdev.earworm.mainscreen.mainrecycler.MainRecyclerAdapter
import app.marcdev.earworm.settingsscreen.SettingsActivity
import app.marcdev.earworm.uicomponents.BinaryOptionDialog
import app.marcdev.earworm.uicomponents.FilterDialog
import app.marcdev.earworm.utils.ItemFilter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class MainFragment : Fragment(), KodeinAware {
  override val kodein by closestKodein()

  // <editor-fold desc="View Model">
  private val viewModelFactory: MainFragmentViewModelFactory by instance()
  private lateinit var viewModel: MainFragmentViewModel
  // </editor-fold>

  // <editor-fold desc="UI Components">
  private lateinit var fab: FloatingActionButton
  private lateinit var noEntriesWarning: TextView
  private lateinit var noEntriesWarningImage: ImageView
  private lateinit var noFilteredResultsWarning: TextView
  private lateinit var noFilteredResultsWarningImage: ImageView
  private lateinit var progressBar: ProgressBar
  private lateinit var searchInput: EditText
  private lateinit var searchButton: ImageView
  private lateinit var filterButton: ImageView
  private lateinit var settingsButton: ImageView
  private lateinit var filterDialog: FilterDialog
  private lateinit var recycler: RecyclerView
  private lateinit var recyclerAdapter: MainRecyclerAdapter
  // </editor-fold>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainFragmentViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_mainscreen, container, false)
    bindViews(view)
    setupRecycler()
    setupObservers()

    requireActivity().addOnBackPressedCallback(this, OnBackPressedCallback {
      onBackPress()
    })

    // If arguments is not null, see if the app has been opened from an app shortcut
    arguments?.let {
      if(arguments!!.getBoolean("add_item", false)) {
        fabOnClickListener.onClick(view)
      }
    }

    return view
  }

  private fun bindViews(view: View) {
    this.fab = view.findViewById(R.id.fab_main)
    fab.setOnClickListener(fabOnClickListener)

    this.noEntriesWarning = view.findViewById(R.id.txt_noEntries)
    this.noEntriesWarningImage = view.findViewById(R.id.img_noEntries)

    this.noFilteredResultsWarning = view.findViewById(R.id.txt_noFilteredResults)
    this.noFilteredResultsWarning.visibility = View.GONE
    this.noFilteredResultsWarningImage = view.findViewById(R.id.img_noFilteredResults)
    this.noFilteredResultsWarningImage.visibility = View.GONE

    this.progressBar = view.findViewById(R.id.prog_main)

    this.searchInput = view.findViewById(R.id.edt_filter_input)
    searchInput.setOnKeyListener(searchOnEnterListener)
    searchInput.addTextChangedListener(searchOnTextChangedListener)

    this.searchButton = view.findViewById(R.id.img_search)
    searchButton.setOnClickListener(searchOnClickListener)

    this.filterButton = view.findViewById(R.id.img_filter)
    filterButton.setOnClickListener(filterOnClickListener)

    recycler = view.findViewById(R.id.recycler_main)
    recycler.addOnScrollListener(scrollListener)

    this.filterDialog = FilterDialog(::filterOkClick)

    this.settingsButton = view.findViewById(R.id.img_settings)
    settingsButton.setOnClickListener(settingsOnClickListener)
  }

  private val fabOnClickListener = View.OnClickListener {
    val addDialog = AddItemBottomSheet()
    addDialog.show(requireFragmentManager(), "Add Item Bottom Sheet Dialog")
  }

  private val settingsOnClickListener = View.OnClickListener {
    val intent = Intent(requireContext(), SettingsActivity::class.java)
    startActivity(intent)
  }

  private val searchOnEnterListener: View.OnKeyListener = View.OnKeyListener { _: View, keyCode: Int, keyEvent: KeyEvent ->
    testIfSubmitButtonClicked(keyEvent, keyCode)
  }

  private val searchOnTextChangedListener = object : TextWatcher {
    override fun afterTextChanged(s: Editable?) { /* Necessary */
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { /* Necessary */
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
      if(s.isNullOrBlank()) {
        viewModel.search("")
      }
    }
  }

  private fun testIfSubmitButtonClicked(keyEvent: KeyEvent, keyCode: Int): Boolean {
    if((keyEvent.action == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
      viewModel.search(searchInput.text.toString())
      return true
    }
    return false
  }

  private val searchOnClickListener = View.OnClickListener {
    viewModel.search(searchInput.text.toString())
  }

  private val clearSearchClickListener = View.OnClickListener {
    searchInput.setText("")
  }

  private val filterOnClickListener = View.OnClickListener {
    filterDialog.show(requireFragmentManager(), "Filter Dialog")
  }

  private val scrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
      hideFabOnScroll(dy, dx)
    }
  }

  private fun hideFabOnScroll(scrollY: Int, oldScrollY: Int) {
    if(scrollY > oldScrollY) {
      fab.hide()
    } else {
      fab.show()
    }
  }

  private fun filterOkClick(filter: ItemFilter) {
    viewModel.filter(filter)
  }

  private fun setupRecycler() {
    this.recyclerAdapter = MainRecyclerAdapter(requireContext(), ::itemClick, ::itemLongClick, requireActivity().theme)
    recycler.adapter = recyclerAdapter
    recycler.layoutManager = LinearLayoutManager(context)
  }

  private fun setupObservers() {
    viewModel.displayLoading.observe(this, Observer { value ->
      value?.let { show ->
        progressBar.visibility = if(show) View.VISIBLE else View.GONE
      }
    })

    viewModel.displayNoEntries.observe(this, Observer { value ->
      value?.let { show ->
        noEntriesWarning.visibility = if(show) View.VISIBLE else View.GONE
        noEntriesWarningImage.visibility = if(show) View.VISIBLE else View.GONE
      }
    })

    viewModel.displayNoFilteredResults.observe(this, Observer { value ->
      value?.let { show ->
        noFilteredResultsWarning.visibility = if(show) View.VISIBLE else View.GONE
        noFilteredResultsWarningImage.visibility = if(show) View.VISIBLE else View.GONE
      }
    })

    viewModel.displayData.observe(this, Observer { items ->
      items?.let {
        recyclerAdapter.updateItems(items)
      }
    })

    viewModel.colorFilterIcon.observe(this, Observer { value ->
      value?.let { colorIt ->
        if(colorIt)
          filterButton.setColorFilter(resources.getColor(R.color.lightThemeColorAccent, null))
        else
          filterButton.clearColorFilter()
      }
    })

    viewModel.displaySearchIcon.observe(this, Observer
    { value ->
      value?.let { show ->
        if(show) {
          searchButton.setImageDrawable(resources.getDrawable(R.drawable.ic_search_24px, requireActivity().theme))
          searchButton.setOnClickListener(searchOnClickListener)
        } else {
          searchButton.setImageDrawable(resources.getDrawable(R.drawable.ic_close_24px, requireActivity().theme))
          searchButton.setOnClickListener(clearSearchClickListener)
        }
      }
    })
  }

  // Takes an int as the parameter as it wasn't being called without a parameter for some reason
  private fun itemClick(itemId: Int) {
    val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
    if(prefs.getBoolean(PREF_SHOW_TIPS, true)) {
      val snackbar = Snackbar.make(requireView(), resources.getString(R.string.long_click_hint), Snackbar.LENGTH_SHORT)
      snackbar.setAction(resources.getString(R.string.dont_show)) {
        prefs.edit().putBoolean("pref_show_tips", false).apply()
      }
      snackbar.show()
    }
  }

  private fun itemLongClick(favouriteItem: FavouriteItem) {
    val dialogBuilder = BinaryOptionDialog.Builder()
    dialogBuilder
      .setTitle(resources.getString(R.string.edit_or_delete))
      .setMessageVisible(false)
      .setNegativeButton(resources.getString(R.string.delete), {
        deleteClick(favouriteItem)
      }, true)
      .setPositiveButton(resources.getString(R.string.edit), {
        editClick(favouriteItem)
      }, true)
    dialogBuilder.build().show(requireFragmentManager(), "Edit or Delete Dialog")
  }

  private fun editClick(favouriteItem: FavouriteItem) {
    val addDialog = AddItemBottomSheet()

    val args = Bundle()
    args.putInt("item_id", favouriteItem.id)
    addDialog.arguments = args

    addDialog.show(requireFragmentManager(), "Add Item Bottom Sheet Dialog")
  }

  private fun deleteClick(favouriteItem: FavouriteItem) {
    viewModel.deleteItem(favouriteItem)
  }

  private fun onBackPress(): Boolean {
    return if(searchInput.text.isNotBlank() || searchInput.hasFocus()) {
      searchInput.text.clear()
      searchInput.clearFocus()
      true
    } else {
      false
    }
  }
}