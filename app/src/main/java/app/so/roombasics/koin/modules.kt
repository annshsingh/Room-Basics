package app.so.roombasics.koin

import android.app.Application
import app.so.roombasics.room.UsersDatabase
import app.so.roombasics.viewmodels.UsersViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

/**
 * Koin module
 */

val appModule = module {

    viewModel {
        UsersViewModel(get())
    }

    single {
        getDatabaseInstance(androidApplication()).getUserDao()
    }
}

/**
 * Method to get an Instance of our database
 */

private fun getDatabaseInstance(androidApplication: Application): UsersDatabase {
    return UsersDatabase.getDatabase(androidApplication)
}