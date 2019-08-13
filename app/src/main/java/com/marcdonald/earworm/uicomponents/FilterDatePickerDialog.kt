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
import android.widget.DatePicker
import com.google.android.material.button.MaterialButton
import com.marcdonald.earworm.R
import com.marcdonald.earworm.internal.DEFAULT_FILTER
import com.marcdonald.earworm.internal.base.EarwormDialogFragment
import java.util.*

class FilterDatePickerDialog(private val okClick: (Int, Int, Int) -> Unit,
														 private val isStart: Boolean,
														 private val calendar: Calendar?)
	: EarwormDialogFragment() {

	// <editor-fold desc="UI Components">
	private lateinit var datePicker: DatePicker
	private lateinit var cancelButton: MaterialButton
	private lateinit var okButton: MaterialButton
	private lateinit var startEndButton: MaterialButton
	// </editor-fold>

	private val day: Int
		get() = datePicker.dayOfMonth
	private val month: Int
		get() = datePicker.month
	private val year: Int
		get() = datePicker.year

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.dialog_datepicker_filter, container, false)
		bindViews(view)
		setDate()
		return view
	}

	private fun bindViews(view: View) {
		datePicker = view.findViewById(R.id.datepicker_filter)
		cancelButton = view.findViewById(R.id.btn_datepicker_filter_cancel)
		cancelButton.setOnClickListener { dismiss() }
		okButton = view.findViewById(R.id.btn_datepicker_filter_ok)
		okButton.setOnClickListener {
			okClick(day, month, year)
		}
		startEndButton = view.findViewById(R.id.btn_datepicker_filter_start_end)
		startEndButton.setOnClickListener {
			if(isStart) {
				okClick(DEFAULT_FILTER.startDay, DEFAULT_FILTER.startMonth, DEFAULT_FILTER.startYear)
			} else {
				okClick(DEFAULT_FILTER.endDay, DEFAULT_FILTER.endMonth, DEFAULT_FILTER.endYear)
			}
			resetDate()
		}

		if(isStart)
			startEndButton.text = resources.getString(R.string.start)
		else
			startEndButton.text = resources.getString(R.string.end)
	}

	private fun resetDate() {
		val today = Calendar.getInstance()
		datePicker.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
	}

	private fun setDate() {
		calendar?.let { calendar ->
			datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
		}
	}
}