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

package com.marcdonald.earworm.internal.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.marcdonald.earworm.R
import com.marcdonald.earworm.utils.ThemeUtils
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

abstract class EarwormActivity : AppCompatActivity(), KodeinAware {
	override val kodein: Kodein by closestKodein()

	private val themeUtils: ThemeUtils by instance()
	private var isLightTheme: Boolean = false

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		updateTheme()
	}

	private fun updateTheme() {
		isLightTheme = if(themeUtils.isLightMode()) {
			setTheme(R.style.Earworm_LightTheme)
			true
		} else {
			setTheme(R.style.Earworm_DarkTheme)
			false
		}
	}

	override fun onResume() {
		super.onResume()
		// Checks if the theme was changed while it was paused and then sees if the current theme of the activity matches
		val isLightThemeNow = themeUtils.isLightMode()
		if(isLightTheme != isLightThemeNow) {
			updateTheme()
			recreate()
		}
	}

	/**
	 * Replaces a fragment in a frame with another fragment
	 * @param fragment The fragment to display
	 * @param fragmentManager The Fragment Manager
	 * @param frameId The ID of the frame to display the new fragment in
	 */
	protected fun setFragment(fragment: Fragment, fragmentManager: FragmentManager, frameId: Int) {
		val fragmentTransaction = fragmentManager.beginTransaction()
		fragmentTransaction.replace(frameId, fragment)
		fragmentTransaction.commit()
	}
}