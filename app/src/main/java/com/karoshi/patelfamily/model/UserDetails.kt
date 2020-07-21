package com.karoshi.patelfamily.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDetails(
    var user_id: String,
    var father_id: String,
    var user_name: String,
    var family_name: String,
    var father_name: String = "",
    var sosa: String
) : Parcelable