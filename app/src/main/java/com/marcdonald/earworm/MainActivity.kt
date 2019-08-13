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

package com.marcdonald.earworm

import android.os.Bundle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.marcdonald.earworm.internal.base.EarwormActivity
import com.marcdonald.earworm.mainscreen.MainFragment
import timber.log.Timber

class MainActivity : EarwormActivity() {
	private lateinit var mainFrame: CoordinatorLayout

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		bindViews()
		setDefaultFragment()
	}

	private fun bindViews() {
		this.mainFrame = findViewById(R.id.frame_main)
	}

	private fun setDefaultFragment() {
		val fragment = MainFragment()

		if(intent.action == "com.marcdonald.earworm.intent.ADD_ITEM") {
			Timber.d("Log: onCreate: Started from Add Item app shortcut")
			val args = Bundle()
			args.putBoolean("add_item", true)
			fragment.arguments = args
		}

		setFragment(fragment, supportFragmentManager, R.id.frame_main)
	}
}
