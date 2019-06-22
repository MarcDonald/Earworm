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
