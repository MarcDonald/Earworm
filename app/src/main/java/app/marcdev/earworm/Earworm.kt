package app.marcdev.earworm

import android.app.Application
import app.marcdev.earworm.additem.AddItemViewModelFactory
import app.marcdev.earworm.data.database.AppDatabase
import app.marcdev.earworm.data.database.DAO
import app.marcdev.earworm.data.database.ProductionAppDatabase
import app.marcdev.earworm.data.repository.FavouriteItemRepository
import app.marcdev.earworm.data.repository.FavouriteItemRepositoryImpl
import app.marcdev.earworm.mainscreen.MainFragmentViewModelFactory
import app.marcdev.earworm.settingsscreen.backupdialog.BackupDialogViewModelFactory
import app.marcdev.earworm.settingsscreen.restoredialog.RestoreDialogViewModelFactory
import app.marcdev.earworm.utils.FileUtils
import app.marcdev.earworm.utils.FileUtilsImpl
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import timber.log.Timber

class Earworm : Application(), KodeinAware {
  override val kodein = Kodein.lazy {
    import(androidXModule(this@Earworm))

    // <editor-fold desc="Database">
    bind<AppDatabase>() with singleton { ProductionAppDatabase.invoke(applicationContext) }
    bind<DAO>() with singleton { instance<AppDatabase>().dao() }
    bind<FavouriteItemRepository>() with singleton { FavouriteItemRepositoryImpl.getInstance(instance()) }
    // </editor-fold>

    // <editor-fold desc="Others">
    bind<FileUtils>() with provider { FileUtilsImpl(instance()) }
    // </editor-fold>

    // <editor-fold desc="View Model Factories">
    bind() from provider { MainFragmentViewModelFactory(instance(), instance()) }
    bind() from provider { AddItemViewModelFactory(instance(), instance()) }
    bind() from provider { BackupDialogViewModelFactory(instance()) }
    bind() from provider { RestoreDialogViewModelFactory(instance(), instance()) }
    // </editor-fold>
  }

  override fun onCreate() {
    super.onCreate()
    if(BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
      Timber.i("Log: Timber Debug Tree planted")
    }
  }
}