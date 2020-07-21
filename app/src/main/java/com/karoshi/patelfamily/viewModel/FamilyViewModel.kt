package com.karoshi.patelfamily.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.karoshi.patelfamily.database.Family
import com.karoshi.patelfamily.database.FamilyDao
import io.reactivex.Flowable

class FamilyViewModel(private val dataSource: FamilyDao) : ViewModel() {

    /**
     * Get the family list.
     * @returns a List of family.
     */
    fun getFamilyList(searchString: String): Flowable<List<Family>> {
        return if (searchString.isBlank()) {
            dataSource.getFamilyList()
        } else {
            dataSource.getSearchedFamilyList("$searchString%")
        }
    }

    /**
     * Get the father name.
     * @returns a father name.
     */
    fun getFatherName(fatherId: String): Flowable<String> {
        return dataSource.getFatherName(fatherId)
    }
}
