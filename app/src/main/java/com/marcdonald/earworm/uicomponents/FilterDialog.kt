/*
 * Copyright (c) 2019 Marc Donald
 *
 * The MIT License (MIT)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.marcdonald.earworm.uicomponents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.marcdonald.earworm.R
import com.marcdonald.earworm.internal.DEFAULT_FILTER
import com.marcdonald.earworm.internal.base.EarwormDialogFragment
import com.marcdonald.earworm.utils.ItemFilter
import com.marcdonald.earworm.utils.formatDateForDisplay
import java.util.*

class FilterDialog(private val okClick: (ItemFilter) -> Unit) : EarwormDialogFragment() {

	private lateinit var displaySongCheckbox: CheckBox
	private lateinit var displayAlbumCheckbox: CheckBox
	private lateinit var displayArtistCheckbox: CheckBox
	private lateinit var startDateDisplay: Chip
	private lateinit var endDateDisplay: Chip
	private lateinit var startDatePickerDialog: FilterDatePickerDialog
	private lateinit var endDatePickerDialog: FilterDatePickerDialog
	private var activeFilter: ItemFilter = DEFAULT_FILTER.copy()

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.dialog_filter, container, false)
		bindViews(view)
		initCheckboxes()
		return view
	}

	private fun bindViews(view: View) {
		startDateDisplay = view.findViewById(R.id.chip_filter_start)
		startDateDisplay.setOnClickListener {
			startDatePickerDialog.show(requireFragmentManager(), "Start Date Picker")
		}

		this.endDateDisplay = view.findViewById(R.id.chip_filter_end)
		endDateDisplay.setOnClickListener {
			endDatePickerDialog.show(requireFragmentManager(), "End Date Picker")
		}

		this.displaySongCheckbox = view.findViewById(R.id.chk_filter_song)
		displaySongCheckbox.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
			activeFilter.includeSongs = isChecked
		}

		this.displayAlbumCheckbox = view.findViewById(R.id.chk_filter_album)
		displayAlbumCheckbox.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
			activeFilter.includeAlbums = isChecked
		}

		this.displayArtistCheckbox = view.findViewById(R.id.chk_filter_artist)
		displayArtistCheckbox.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
			activeFilter.includeArtists = isChecked
		}

		val submitButton: MaterialButton = view.findViewById(R.id.btn_filter_ok)
		submitButton.setOnClickListener {
			okClick(activeFilter)
			dismiss()
		}

		initStartDatePickerDialog()
		initEndDatePickerDialog()
	}

	private fun initStartDatePickerDialog() {
		if(activeFilter.startDay == DEFAULT_FILTER.startDay && activeFilter.startMonth == DEFAULT_FILTER.startMonth && activeFilter.startYear == DEFAULT_FILTER.startYear) {
			startDateDisplay.text = resources.getString(R.string.start)
			this.startDatePickerDialog = FilterDatePickerDialog(::startDatePickerOkClick, true, null)
		} else {
			startDateDisplay.text = formatDateForDisplay(activeFilter.startDay, activeFilter.startMonth, activeFilter.startYear)
			val calendar = Calendar.getInstance()
			calendar.set(Calendar.DAY_OF_MONTH, activeFilter.startDay)
			calendar.set(Calendar.MONTH, activeFilter.startMonth)
			calendar.set(Calendar.YEAR, activeFilter.startYear)
			this.startDatePickerDialog = FilterDatePickerDialog(::startDatePickerOkClick, true, calendar)
		}
	}

	private fun initEndDatePickerDialog() {
		if(activeFilter.endDay == DEFAULT_FILTER.endDay && activeFilter.endMonth == DEFAULT_FILTER.endMonth && activeFilter.endYear == DEFAULT_FILTER.endYear) {
			endDateDisplay.text = resources.getString(R.string.end)
			this.endDatePickerDialog = FilterDatePickerDialog(::endDatePickerOkClick, false, null)
		} else {
			endDateDisplay.text = formatDateForDisplay(activeFilter.endDay, activeFilter.endMonth, activeFilter.endYear)
			val calendar = Calendar.getInstance()
			calendar.set(Calendar.DAY_OF_MONTH, activeFilter.endDay)
			calendar.set(Calendar.MONTH, activeFilter.endMonth)
			calendar.set(Calendar.YEAR, activeFilter.endYear)
			this.endDatePickerDialog = FilterDatePickerDialog(::endDatePickerOkClick, false, calendar)
		}
	}

	private fun startDatePickerOkClick(day: Int, month: Int, year: Int) {
		activeFilter.startDay = day
		activeFilter.startMonth = month
		activeFilter.startYear = year
		if(day == DEFAULT_FILTER.startDay && month == DEFAULT_FILTER.startMonth && year == DEFAULT_FILTER.startYear) {
			startDateDisplay.text = resources.getString(R.string.start)
		} else {
			startDateDisplay.text = formatDateForDisplay(activeFilter.startDay, activeFilter.startMonth, activeFilter.startYear)
		}
		startDatePickerDialog.dismiss()
	}

	private fun endDatePickerOkClick(day: Int, month: Int, year: Int) {
		activeFilter.endDay = day
		activeFilter.endMonth = month
		activeFilter.endYear = year
		if(day == DEFAULT_FILTER.endDay && month == DEFAULT_FILTER.endMonth && year == DEFAULT_FILTER.endYear) {
			endDateDisplay.text = resources.getString(R.string.end)
		} else {
			endDateDisplay.text = formatDateForDisplay(activeFilter.endDay, activeFilter.endMonth, activeFilter.endYear)
		}
		endDatePickerDialog.dismiss()
	}

	private fun initCheckboxes() {
		displaySongCheckbox.isChecked = activeFilter.includeSongs
		displayAlbumCheckbox.isChecked = activeFilter.includeAlbums
		displayArtistCheckbox.isChecked = activeFilter.includeArtists
	}
}