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

package com.marcdonald.earworm.settingsscreen.updatedialog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
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

class UpdateDialog : EarwormDialogFragment(), KodeinAware {
	override val kodein: Kodein by closestKodein()

	// <editor-fold desc="View Model">
	private val viewModelFactory: UpdateDialogViewModelFactory by instance()
	private lateinit var viewModel: UpdateDialogViewModel
	// </editor-fold>

	// <editor-fold desc="UI Components">
	private lateinit var noUpdateAvailable: ImageView
	private lateinit var errorDisplay: ImageView
	private lateinit var updateAvailable: ImageView
	private lateinit var noConnection: LinearLayout
	private lateinit var loadingProgressBar: ProgressBar
	private lateinit var openButton: MaterialButton
	private lateinit var dismissButton: MaterialButton
	private lateinit var title: TextView
	private lateinit var message: TextView
	// </editor-fold>

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel = ViewModelProviders.of(this, viewModelFactory).get(UpdateDialogViewModel::class.java)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.dialog_update, container, false)
		bindViews(view)
		setupObservers()
		viewModel.check()
		return view
	}

	private fun bindViews(view: View) {
		title = view.findViewById(R.id.txt_update_title)
		message = view.findViewById(R.id.txt_update_message)
		message.visibility = View.GONE
		noConnection = view.findViewById(R.id.lin_update_no_connection)
		noUpdateAvailable = view.findViewById(R.id.img_no_update_available)
		updateAvailable = view.findViewById(R.id.img_update_available)
		updateAvailable.visibility = View.GONE
		errorDisplay = view.findViewById(R.id.img_update_error)
		errorDisplay.visibility = View.GONE
		loadingProgressBar = view.findViewById(R.id.prog_update)
		dismissButton = view.findViewById(R.id.btn_update_dismiss)
		dismissButton.setOnClickListener {
			dismiss()
		}
		openButton = view.findViewById(R.id.btn_update_open)
		openButton.setOnClickListener {
			val uriUrl = Uri.parse("https://github.com/MarcDonald/Earworm/releases/latest")
			val launchBrowser = Intent(Intent.ACTION_VIEW)
			launchBrowser.data = uriUrl
			startActivity(launchBrowser)

		}
	}

	private fun setupObservers() {
		viewModel.displayDismiss.observe(this, Observer { value ->
			value?.let { display ->
				dismissButton.visibility = if(display) View.VISIBLE else View.GONE
			}
		})

		viewModel.displayLoading.observe(this, Observer { value ->
			value?.let { display ->
				loadingProgressBar.visibility = if(display) View.VISIBLE else View.GONE
				if(display)
					title.text = resources.getString(R.string.checking_for_updates)
			}
		})

		viewModel.displayOpenButton.observe(this, Observer { value ->
			value?.let { display ->
				openButton.visibility = if(display) View.VISIBLE else View.GONE
			}
		})

		viewModel.displayNoUpdateAvailable.observe(this, Observer { value ->
			value?.let { display ->
				noUpdateAvailable.visibility = if(display) View.VISIBLE else View.GONE
				if(display) {
					title.text = resources.getString(R.string.no_update_title)
					message.text = resources.getString(R.string.no_update_message)
					message.visibility = View.VISIBLE
				}
			}
		})

		viewModel.displayNoConnection.observe(this, Observer { value ->
			value?.let { display ->
				noConnection.visibility = if(display) View.VISIBLE else View.GONE
				if(display) {
					title.text = resources.getString(R.string.no_connection_warning)
				}
			}
		})

		viewModel.newVersionName.observe(this, Observer { value ->
			value?.let { versionName ->
				title.text = resources.getString(R.string.new_version_available_title)
				message.text = resources.getString(R.string.new_version_available_message, versionName)
				message.visibility = View.VISIBLE
				updateAvailable.visibility = View.VISIBLE
			}
		})

		viewModel.displayError.observe(this, Observer { value ->
			value?.let { display ->
				errorDisplay.visibility = if(display) View.VISIBLE else View.GONE
				if(display)
					title.text = resources.getString(R.string.error)
			}
		})
	}
}