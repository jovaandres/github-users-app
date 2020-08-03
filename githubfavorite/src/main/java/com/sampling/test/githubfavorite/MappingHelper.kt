package com.sampling.test.githubfavorite

import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

object MappingHelper {

    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<FavoritesData> {
        val favList = ArrayList<FavoritesData>()
        cursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndex(Favorite.ID))
                val name = getString(getColumnIndex(Favorite.COLUMN_FAVORITE_NAME)).toString()
                val avatar = getString(getColumnIndex(Favorite.COLUMN_FAVORITE_AVATAR)).toString()
                val company = getString(getColumnIndex(Favorite.COLUMN_FAVORITE_COMPANY)).toString()
                val location = getString(getColumnIndex(Favorite.COLUMN_FAVORITE_LOCATION)).toString()
                favList.add(FavoritesData(id, name, avatar, location, company))
            }
            cursor.close()
        }
        return favList
    }

    fun toBitmap(string: String): Bitmap {
        val byteArray = Base64.decode(string, 0)
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}