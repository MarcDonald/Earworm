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

package com.marcdonald.earworm.settingsscreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.marcdonald.earworm.R
import com.marcdonald.earworm.internal.base.EarwormActivity
import com.marcdonald.earworm.uicomponents.LicenseDisplay

class LicensesActivity : EarwormActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_licenses)

		bindViews()
	}

	private fun bindViews() {
		findViewById<ImageView>(R.id.img_backFromSettings)
			.setOnClickListener { finish() }

		findViewById<TextView>(R.id.txt_settingsToolbarTitle)
			.text = resources.getString(R.string.licenses)

		findViewById<CardView>(R.id.license_glide)
			.setOnClickListener { launchUrl("https://github.com/bumptech/glide") }

		findViewById<LicenseDisplay>(R.id.license_timber)
			.setOnClickListener { launchUrl("https://github.com/JakeWharton/timber") }

		findViewById<CardView>(R.id.license_material_design_icons)
			.setOnClickListener { launchUrl("https://github.com/google/material-design-icons") }

		findViewById<CardView>(R.id.license_material_design_components)
			.setOnClickListener { launchUrl("https://github.com/material-components/material-components-android") }

		findViewById<CardView>(R.id.license_android_file_picker)
			.setOnClickListener { launchUrl("https://github.com/DroidNinja/Android-FilePicker") }

		findViewById<CardView>(R.id.license_kodein)
			.setOnClickListener { launchUrl("https://github.com/Kodein-Framework/Kodein-DI") }

		findViewById<CardView>(R.id.license_open_sans)
			.setOnClickListener { launchUrl("https://fonts.google.com/specimen/Open+Sans") }

		findViewById<LicenseDisplay>(R.id.license_retrofit)
			.setOnClickListener { launchUrl("https://github.com/square/retrofit") }

		findViewById<LicenseDisplay>(R.id.license_gson)
			.setOnClickListener { launchUrl("https://github.com/google/gson") }
	}

	private fun launchUrl(url: String) {
		val uriUrl = Uri.parse(url)
		val launchBrowser = Intent(Intent.ACTION_VIEW)
		launchBrowser.data = uriUrl
		startActivity(launchBrowser)
	}
}
