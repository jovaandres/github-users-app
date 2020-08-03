package com.sampling.test.githubUser.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserListData(
    var avatar_url: String? = "",
    var login: String = "",
    var offlineAvatar: String? =""
) : Parcelable