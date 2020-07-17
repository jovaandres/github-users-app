package com.sampling.test.githubUser.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.sampling.test.githubUser.R
import com.sampling.test.githubUser.db.Favorite
import com.squareup.picasso.Picasso

class StackWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext)

    internal class StackRemoteViewsFactory(private val context: Context) : RemoteViewsFactory {

        private val widgetItem = ArrayList<Bitmap>()
        private val bitmapString = ArrayList<String>()
        private val nameList = ArrayList<String>()

        override fun onCreate() {
        }

        override fun getLoadingView(): RemoteViews? = null

        override fun getItemId(position: Int): Long = 0

        override fun onDataSetChanged() {
            val long = Binder.clearCallingIdentity()
            try {
                loadData()
            } finally {
                Binder.restoreCallingIdentity(long)
            }
        }

        override fun hasStableIds(): Boolean = false

        override fun getViewAt(position: Int): RemoteViews {
            val rv = RemoteViews(
                context.packageName,
                R.layout.widget_item
            )
            rv.setImageViewBitmap(R.id.imageView, widgetItem[position])

            val extras = bundleOf(FavoriteUserWidget.EXTRA_ITEM to nameList[position])
            val fillIntent = Intent()
            fillIntent.putExtras(extras)

            rv.setOnClickFillInIntent(R.id.imageView, fillIntent)
            return rv
        }

        override fun getCount(): Int = widgetItem.size

        override fun getViewTypeCount(): Int = 1

        override fun onDestroy() {
        }

        private fun loadData() {
            widgetItem.clear()
            bitmapString.clear()
            nameList.clear()
            val cursor = context.contentResolver.query(Favorite.CONTENT_URI, null, null, null, null)
            cursor?.apply {
                while (moveToNext()) {
                    val name =
                        cursor.getString(getColumnIndex(Favorite.COLUMN_FAVORITE_NAME)).toString()
                    val avatar =
                        cursor.getString(getColumnIndex(Favorite.COLUMN_FAVORITE_AVATAR)).toString()
                    bitmapString.add(avatar)
                    nameList.add(name)
                }
            }
            cursor?.close()
            for (i in 0 until bitmapString.size) {
                widgetItem.add(
                    Picasso.get()
                        .load(bitmapString[i])
                        .resize(300, 300)
                        .get()
                )
            }
        }
    }
}