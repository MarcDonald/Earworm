package app.marcdev.earworm.settingsscreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import app.marcdev.earworm.R
import app.marcdev.earworm.internal.base.EarwormActivity
import app.marcdev.earworm.uicomponents.LicenseDisplay

class LicensesActivity : EarwormActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_licenses)

    bindViews()
  }

  private fun bindViews() {
    val backButton = findViewById<ImageView>(R.id.img_backFromSettings)
    backButton.setOnClickListener { finish() }

    val toolbarTitle = findViewById<TextView>(R.id.txt_settingsToolbarTitle)
    toolbarTitle.text = resources.getString(R.string.licenses)

    val glideCard = findViewById<CardView>(R.id.license_glide)
    glideCard.setOnClickListener { launchUrl("https://github.com/bumptech/glide") }

    val timberCard = findViewById<LicenseDisplay>(R.id.license_timber)
    timberCard.setOnClickListener { launchUrl("https://github.com/JakeWharton/timber") }

    val materialIconsCard = findViewById<CardView>(R.id.license_material_design_icons)
    materialIconsCard.setOnClickListener { launchUrl("https://github.com/google/material-design-icons") }

    val materialComponentsCard = findViewById<CardView>(R.id.license_material_design_components)
    materialComponentsCard.setOnClickListener { launchUrl("https://github.com/material-components/material-components-android") }

    val filePickerCard = findViewById<CardView>(R.id.license_android_file_picker)
    filePickerCard.setOnClickListener { launchUrl("https://github.com/DroidNinja/Android-FilePicker") }

    val kodeinCard = findViewById<CardView>(R.id.license_kodein)
    kodeinCard.setOnClickListener { launchUrl("https://github.com/Kodein-Framework/Kodein-DI") }

    val openSansCard = findViewById<CardView>(R.id.license_open_sans)
    openSansCard.setOnClickListener { launchUrl("https://fonts.google.com/specimen/Open+Sans") }
  }

  private fun launchUrl(url: String) {
    val uriUrl = Uri.parse(url)
    val launchBrowser = Intent(Intent.ACTION_VIEW)
    launchBrowser.data = uriUrl
    startActivity(launchBrowser)
  }
}
