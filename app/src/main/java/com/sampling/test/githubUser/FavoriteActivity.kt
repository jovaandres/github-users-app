package com.sampling.test.githubUser

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.sampling.test.githubUser.adapter.FavoriteAdapter
import com.sampling.test.githubUser.db.Favorite.Companion.CONTENT_URI
import com.sampling.test.githubUser.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_favorite.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class FavoriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        rv_fav.setHasFixedSize(true)
        rv_fav.layoutManager = GridLayoutManager(this, 2)

        loadAsync()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

    }

    private fun loadAsync() {
        doAsync {
            val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
            val list = MappingHelper.mapCursorToArrayList(cursor)

            uiThread {
                val adapter = FavoriteAdapter(this@FavoriteActivity)
                adapter.listFav = list
                rv_fav.adapter = adapter
            }
        }
    }
}