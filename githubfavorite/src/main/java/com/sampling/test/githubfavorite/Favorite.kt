package com.sampling.test.githubfavorite

import android.net.Uri
import android.provider.BaseColumns
import androidx.room.*
import com.sampling.test.githubfavorite.Favorite.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = ID)
    var id: Int? = null,

    @ColumnInfo(name = COLUMN_FAVORITE_NAME)
    var name: String? = "",

    @ColumnInfo(name = COLUMN_FAVORITE_AVATAR)
    var avatar: String? = "",

    @ColumnInfo(name = COLUMN_FAVORITE_COMPANY)
    var company: String? = "",

    @ColumnInfo(name = COLUMN_FAVORITE_LOCATION)
    var location: String? = ""
) {

    companion object : BaseColumns {
        const val TABLE_NAME = "favorites"
        const val ID = BaseColumns._ID
        const val COLUMN_FAVORITE_NAME = "name"
        const val COLUMN_FAVORITE_AVATAR = "avatar"
        const val COLUMN_FAVORITE_LOCATION = "location"
        const val COLUMN_FAVORITE_COMPANY = "company"

        val CONTENT_URI: Uri = Uri.parse("content://com.sampling.test.githubUser/$TABLE_NAME")
    }
}

