package com.marcdonald.earworm

import android.app.Application
import com.marcdonald.earworm.additem.AddItemViewModelFactory
import com.marcdonald.earworm.data.database.AppDatabase
import com.marcdonald.earworm.data.database.DAO
import com.marcdonald.earworm.data.database.ProductionAppDatabase
import com.marcdonald.earworm.data.network.ConnectivityInterceptor
import com.marcdonald.earworm.data.network.ConnectivityInterceptorImpl
import com.marcdonald.earworm.data.network.github.GithubAPIService
import com.marcdonald.earworm.data.repository.FavouriteItemRepository
import com.marcdonald.earworm.data.repository.FavouriteItemRepositoryImpl
import com.marcdonald.earworm.mainscreen.MainFragmentViewModelFactory
import com.marcdonald.earworm.settingsscreen.backupdialog.BackupDialogViewModelFactory
import com.marcdonald.earworm.settingsscreen.restoredialog.RestoreDialogViewModelFactory
import com.marcdonald.earworm.settingsscreen.updatedialog.UpdateDialogViewModelFactory
import com.marcdonald.earworm.utils.FileUtils
import com.marcdonald.earworm.utils.FileUtilsImpl
import com.marcdonald.earworm.utils.ThemeUtils
import com.marcdonald.earworm.utils.ThemeUtilsImpl
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
    // <editor-fold desc="Network">
    bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
    bind<GithubAPIService>() with singleton { GithubAPIService(instance()) }
    // </editor-fold>
    // <editor-fold desc="Others">
    bind<FileUtils>() with provider { FileUtilsImpl(instance()) }
    bind<ThemeUtils>() with provider { ThemeUtilsImpl(instance()) }
    // </editor-fold>
    // <editor-fold desc="View Model Factories">
    bind() from provider { MainFragmentViewModelFactory(instance(), instance()) }
    bind() from provider { AddItemViewModelFactory(instance(), instance()) }
    bind() from provider { BackupDialogViewModelFactory(instance()) }
    bind() from provider { RestoreDialogViewModelFactory(instance(), instance()) }
    bind() from provider { UpdateDialogViewModelFactory(instance()) }
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