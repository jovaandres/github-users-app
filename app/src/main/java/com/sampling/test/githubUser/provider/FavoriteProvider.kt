package com.sampling.test.githubUser.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.sampling.test.githubUser.db.Favorite
import com.sampling.test.githubUser.db.Favorite.Companion.TABLE_NAME
import com.sampling.test.githubUser.db.FavoriteDatabase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class FavoriteProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.sampling.test.githubUser"
        const val FAVORITE = 100
        const val FAVORITE_ID = 101
    }

    private val fUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    private val favoriteDao by lazy {
        context?.let {
            FavoriteDatabase.getInstance(it)?.favoriteDao()
        }
    }


    init {
        fUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAVORITE)
        fUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", FAVORITE_ID)
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val cursor = when (fUriMatcher.match(uri)) {
            FAVORITE -> {
                favoriteDao?.selectAll()
            }
            else -> throw IllegalArgumentException("Unknown uri $uri")
        }
        cursor?.setNotificationUri(context?.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        when (fUriMatcher.match(uri)) {
            FAVORITE -> {
                var result: Uri? = null
                doAsync {
                    val id = favoriteDao?.insert(favoritesContentValues(values))
                    uiThread {
                        context?.contentResolver?.notifyChange(uri, null)
                        result = id?.let { ContentUris.withAppendedId(uri, it) }
                    }
                }
                return result
            }
            FAVORITE_ID -> throw IllegalArgumentException("Invalid URI: Insert failed")
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        when (fUriMatcher.match(uri)) {
            FAVORITE -> {
                val count = favoriteDao?.update(favoritesContentValues(values))
                context?.contentResolver?.notifyChange(uri, null)
                return count ?: 0
            }
            FAVORITE_ID -> throw IllegalArgumentException("Invalid URI: cannot update")
            else -> throw IllegalArgumentException("Unknown uri: $uri")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        when (fUriMatcher.match(uri)) {
            FAVORITE -> {
                throw IllegalArgumentException("Invalid uri: Cannot delete data")
            }
            FAVORITE_ID -> {
                val count = favoriteDao?.delete(ContentUris.parseId(uri))
                context?.contentResolver?.notifyChange(uri, null)
                return count ?: 0
            }
            else -> throw IllegalArgumentException("Unknown uri $uri")
        }
    }

    private fun favoritesContentValues(contentValues: ContentValues?): Favorite {
        val name = contentValues?.getAsString(Favorite.COLUMN_FAVORITE_NAME).toString()
        val avatar = contentValues?.getAsString(Favorite.COLUMN_FAVORITE_AVATAR).toString()
        val company = contentValues?.getAsString(Favorite.COLUMN_FAVORITE_COMPANY).toString()
        val location = contentValues?.getAsString(Favorite.COLUMN_FAVORITE_LOCATION).toString()
        return Favorite(name = name, avatar = avatar, company = company, location = location)
    }
}