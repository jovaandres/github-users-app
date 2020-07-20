package com.sampling.test.githubUser.helper

import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.sampling.test.githubUser.db.Favorite
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

object MappingHelper {

    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<Favorite> {
        val favList = ArrayList<Favorite>()
        cursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndex(Favorite.ID))
                val name = getString(getColumnIndex(Favorite.COLUMN_FAVORITE_NAME)).toString()
                val avatar = getString(getColumnIndex(Favorite.COLUMN_FAVORITE_AVATAR)).toString()
                val company = getString(getColumnIndex(Favorite.COLUMN_FAVORITE_COMPANY)).toString()
                val location =
                    getString(getColumnIndex(Favorite.COLUMN_FAVORITE_LOCATION)).toString()
                favList.add(Favorite(id, name, avatar, location, company))
            }
            cursor.close()
        }
        return favList
    }

    fun fromBitmap(url: String): String {
        val outputStream = ByteArrayOutputStream()
        val bitmap = Picasso.get()
            .load(url)
            .resize(300, 300)
            .get()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    fun toBitmap(string: String): Bitmap {
        val byteArray = Base64.decode(string, 0)
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

}