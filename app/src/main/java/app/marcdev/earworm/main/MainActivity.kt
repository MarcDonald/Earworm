package app.marcdev.earworm.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.marcdev.earworm.R
import timber.log.Timber

class MainActivity : AppCompatActivity()
{
  override fun onCreate(savedInstanceState: Bundle?)
  {
    super.onCreate(savedInstanceState)
    Timber.d("Log: onCreate: Started")
    setContentView(R.layout.activity_main)
  }
}
