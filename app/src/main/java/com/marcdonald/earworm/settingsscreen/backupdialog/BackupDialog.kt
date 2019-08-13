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

package com.marcdonald.earworm.settingsscreen.backupdialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.button.MaterialButton
import com.marcdonald.earworm.R
import com.marcdonald.earworm.internal.base.EarwormDialogFragment
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class BackupDialog : EarwormDialogFragment(), KodeinAware {
	override val kodein: Kodein by closestKodein()

	// <editor-fold desc="View Model">
	private val viewModelFactory: BackupDialogViewModelFactory by instance()
	private lateinit var viewModel: BackupDialogViewModel
	// </editor-fold>

	// <editor-fold desc="UI Components">
	private lateinit var loadingProgressBar: ProgressBar
	private lateinit var shareButton: MaterialButton
	private lateinit var successDisplay: ImageView
	private lateinit var dismissButton: MaterialButton
	private lateinit var title: TextView
	// </editor-fold>

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel = ViewModelProviders.of(this, viewModelFactory).get(BackupDialogViewModel::class.java)
		isCancelable = false
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.dialog_backup, container, false)
		bindViews(view)
		setupObservers()
		viewModel.backup()
		return view
	}

	private fun bindViews(view: View) {
		title = view.findViewById(R.id.txt_backup_title)
		loadingProgressBar = view.findViewById(R.id.prog_backup)
		successDisplay = view.findViewById(R.id.img_backup_success)
		dismissButton = view.findViewById(R.id.btn_backup_dismiss)
		dismissButton.setOnClickListener {
			dismiss()
		}
		shareButton = view.findViewById(R.id.btn_backup_share)
		shareButton.setOnClickListener {
			shareOnClick()
		}
	}

	private fun setupObservers() {
		viewModel.displayDismiss.observe(this, Observer { value ->
			value?.let { display ->
				if(display) {
					dismissButton.visibility = View.VISIBLE
					isCancelable = true
				} else {
					dismissButton.visibility = View.GONE
					isCancelable = false
				}
			}
		})

		viewModel.displayLoading.observe(this, Observer { value ->
			value?.let { display ->
				loadingProgressBar.visibility = if(display) View.VISIBLE else View.GONE
			}
		})

		viewModel.displayShareButton.observe(this, Observer { value ->
			value?.let { display ->
				shareButton.visibility = if(display) View.VISIBLE else View.GONE
			}
		})

		viewModel.displaySuccess.observe(this, Observer { value ->
			value?.let { display ->
				if(display) {
					successDisplay.visibility = View.VISIBLE
					title.text = resources.getString(R.string.backup_success)
				} else {
					successDisplay.visibility = View.GONE
					title.text = resources.getString(R.string.backing_up)
				}
			}
		})
	}

	private fun shareOnClick() {
		val uri = viewModel.localBackupURI
		uri?.let {
			val shareIntent = Intent(Intent.ACTION_SEND)
			shareIntent.type = "application/octet-stream"
			shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
			startActivity(shareIntent)
		}
		dismiss()
	}
}