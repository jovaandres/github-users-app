package com.sampling.test.githubUser.db

import android.database.Cursor
import androidx.room.*
import com.sampling.test.githubUser.db.Favorite.Companion.ID
import com.sampling.test.githubUser.db.Favorite.Companion.TABLE_NAME

@Dao
interface FavoriteDao {

    @Query(value = "SELECT * FROM $TABLE_NAME")
    fun selectAll(): Cursor

    @Query(value = "SELECT * FROM $TABLE_NAME WHERE $ID = :id")
    fun selectById(id: Long?): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favorite: Favorite): Long

    @Query(value = "DELETE FROM $TABLE_NAME WHERE $ID = :id ")
    fun delete(id: Long?): Int

    @Query(value = "DELETE FROM $TABLE_NAME")
    fun deleteAll(): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(favorite: Favorite): Int
}