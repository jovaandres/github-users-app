package com.sampling.test.githubUser.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavoritesData(
    var id: Int = 0,
    var name: String = "",
    var avatar: String = "",
    var location: String = "",
    var company: String = ""
) : Parcelable