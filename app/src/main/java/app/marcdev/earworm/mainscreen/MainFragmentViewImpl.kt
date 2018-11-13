package app.marcdev.earworm.mainscreen

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.earworm.R
import app.marcdev.earworm.additem.AddItemBottomSheet
import app.marcdev.earworm.additem.RecyclerUpdateView
import app.marcdev.earworm.database.FavouriteItem
import app.marcdev.earworm.mainscreen.mainrecycler.MainRecyclerAdapter
import app.marcdev.earworm.settingsscreen.SettingsActivity
import app.marcdev.earworm.uicomponents.FilterDialog
import app.marcdev.earworm.utils.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import timber.log.Timber

class MainFragmentViewImpl : Fragment(), MainFragmentView, RecyclerUpdateView {

  private lateinit var fab: FloatingActionButton
  private lateinit var noEntriesWarning: TextView
  private lateinit var noEntriesWarningImage: ImageView
  private lateinit var noFilteredResultsWarning: TextView
  private lateinit var noFilteredResultsWarningImage: ImageView
  private lateinit var progressBar: ProgressBar
  private lateinit var searchInput: EditText
  private lateinit var searchButton: ImageButton
  private lateinit var filterButton: ImageButton
  private lateinit var settingsButton: ImageButton
  private lateinit var filterDialog: FilterDialog
  private lateinit var recyclerAdapter: MainRecyclerAdapter
  private lateinit var presenter: MainFragmentPresenter
  private var isSearchMode = true

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.d("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.fragment_mainscreen, container, false)

    presenter = MainFragmentPresenterImpl(this, activity!!.applicationContext)
    bindViews(view)

    if(getTheme(requireContext()) == DARK_THEME) {
      Timber.d("Log: onCreateView: Is dark mode, converting")
      convertToDarkMode()
    }

    setupRecycler(view)
    fillData()

    return view
  }

  private fun bindViews(view: View) {
    Timber.v("Log: bindViews: Started")
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

    val nestedScrollView: NestedScrollView = view.findViewById(R.id.scroll_main)
    nestedScrollView.setOnScrollChangeListener(scrollViewOnScrollChangeListener)

    this.filterDialog = FilterDialog(requireActivity(), presenter)

    this.settingsButton = view.findViewById(R.id.img_settings)
    settingsButton.setOnClickListener(settingsOnClickListener)
  }

  private val fabOnClickListener = View.OnClickListener {
    Timber.d("Log: Fab Clicked")
    val addDialog = AddItemBottomSheet()
    addDialog.bindRecyclerUpdateView(this)
    addDialog.show(fragmentManager, "Add Item Bottom Sheet Dialog")
  }

  private val settingsOnClickListener = View.OnClickListener {
    Timber.d("Log: Settings Clicked")
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
        presenter.search("")
      }
    }
  }

  private fun testIfSubmitButtonClicked(keyEvent: KeyEvent, keyCode: Int): Boolean {
    Timber.d("Log: testIfSubmitButtonClicked: Submit button clicked")
    if((keyEvent.action == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
      presenter.search(searchInput.text.toString())
      return true
    }
    return false
  }

  private val searchOnClickListener = View.OnClickListener {
    Timber.d("Log: Search Clicked")
    if(isSearchMode) {
      presenter.search(searchInput.text.toString())
    } else {
      searchInput.setText("")
    }
  }

  private val filterOnClickListener = View.OnClickListener {
    Timber.d("Log: Filter Clicked")
    filterDialog.show()
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
    this.recyclerAdapter = MainRecyclerAdapter(context, presenter)
    recycler.adapter = recyclerAdapter
    recycler.layoutManager = LinearLayoutManager(context)
  }

  override fun fillData() {
    Timber.d("Log: fillData: Started")
    displayProgress(true)
    displayNoEntriesWarning(false)
    presenter.getAllItems()
  }

  override fun displayNoEntriesWarning(display: Boolean) {
    Timber.d("Log: displayNoEntriesWarning: Started with display = $display")

    if(display) {
      Timber.d("Log: displayNoEntriesWarning: Displaying")
      noEntriesWarning.visibility = View.VISIBLE
      noEntriesWarningImage.visibility = View.VISIBLE
    } else {
      Timber.d("Log: displayNoEntriesWarning: Hiding")
      noEntriesWarning.visibility = View.GONE
      noEntriesWarningImage.visibility = View.GONE
    }
  }

  override fun updateRecycler(items: List<FavouriteItem>) {
    Timber.d("Log: updateRecycler: Started")
    recyclerAdapter.updateItems(items)
  }

  override fun displayAddedToast() {
    Timber.d("Log: displayAddedToast: Started")
    Toast.makeText(activity, resources.getString(R.string.item_added), Toast.LENGTH_SHORT).show()
  }

  override fun displayItemDeletedToast() {
    Timber.d("Log: displayItemDeletedToast: Started")
    Toast.makeText(activity, resources.getString(R.string.item_deleted), Toast.LENGTH_SHORT).show()
  }

  override fun displayProgress(isVisible: Boolean) {
    Timber.d("Log: displayProgress: Started with isVisible = $isVisible")
    if(isVisible) {
      progressBar.visibility = View.VISIBLE
    } else {
      progressBar.visibility = View.GONE
    }
  }

  override fun displayEditItemSheet(itemId: Int) {
    Timber.d("Log: displayEditItemSheet: Started with itemId = $itemId")
    val addDialog = AddItemBottomSheet()
    addDialog.bindRecyclerUpdateView(this)

    val args = Bundle()
    args.putInt("item_id", itemId)
    addDialog.arguments = args

    addDialog.show(fragmentManager, "Add Item Bottom Sheet Dialog")
  }

  override fun displayNoFilteredResultsWarning(display: Boolean) {
    Timber.d("Log: displayNoFilteredResultsWarning: Started with display = $display")

    if(display && (noEntriesWarning.visibility == View.GONE)) {
      this.noFilteredResultsWarning.visibility = View.VISIBLE
      this.noFilteredResultsWarningImage.visibility = View.VISIBLE
    } else {
      this.noFilteredResultsWarning.visibility = View.GONE
      this.noFilteredResultsWarningImage.visibility = View.GONE
    }
  }

  override fun getActiveFilter(): ItemFilter {
    Timber.d("Log: getActiveFilter: Started")
    return filterDialog.activeFilter
  }

  override fun changeSearchIcon(isSearch: Boolean) {
    Timber.d("Log: changeSearchIcon: Started")
    if(isSearch) {
      searchButton.setImageDrawable(resources.getDrawable(R.drawable.ic_search_24px, null))
      this.isSearchMode = true
    } else {
      searchButton.setImageDrawable(resources.getDrawable(R.drawable.ic_close_24px, null))
      this.isSearchMode = false
    }
  }

  override fun activateFilterIcon(isActive: Boolean) {
    Timber.d("Log: activateFilterIcon: Started with isActive = $isActive")

    changeColorOfImageButtonDrawable(context!!, filterButton, isActive)
  }

  private fun convertToDarkMode() {
    changeColorOfImageButtonDrawable(requireContext(), filterButton, false)
    changeColorOfImageButtonDrawable(requireContext(), settingsButton, false)
    changeColorOfImageButtonDrawable(requireContext(), searchButton, false)
    changeColorOfDrawable(requireContext(), noEntriesWarningImage.drawable, false)
    changeColorOfDrawable(requireContext(), noFilteredResultsWarningImage.drawable, false)
  }
}