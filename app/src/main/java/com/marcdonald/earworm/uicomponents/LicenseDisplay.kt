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

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import com.marcdonald.earworm.R

class LicenseDisplay(context: Context, attributeSet: AttributeSet?, defStyle: Int) :
		MaterialCardView(context, attributeSet, defStyle) {

	private var titleText: TextView
	private var descriptionText: TextView
	private var licenseText: TextView

	init {
		val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
		val view = inflater.inflate(R.layout.view_license, this, true)
		titleText = view.findViewById(R.id.txt_license_title)
		descriptionText = view.findViewById(R.id.txt_license_description)
		licenseText = view.findViewById(R.id.txt_license_license)

		if(attributeSet != null) {
			val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.LicenseDisplay, defStyle, 0)

			val title = attributes.getString(R.styleable.LicenseDisplay_title)
			titleText.text = title

			val description = attributes.getString(R.styleable.LicenseDisplay_description)
			descriptionText.text = description

			val license = attributes.getString(R.styleable.LicenseDisplay_license)
			licenseText.text = license

			attributes.recycle()
		}
	}

	constructor(context: Context) : this(context, null)

	constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
}