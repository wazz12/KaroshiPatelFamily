package com.karoshi.patelfamily.viewModel

import android.content.Context
import com.karoshi.patelfamily.database.AppDatabase
import com.karoshi.patelfamily.database.FamilyDao

/**
 * Enables injection of data sources.
 */
object Injection {

    private fun provideFamilyDataSource(context: Context): FamilyDao {
        val database = AppDatabase.getInstance(context)
        return database.familyDao()
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val dataSource = provideFamilyDataSource(context)
        return ViewModelFactory(dataSource)
    }
}