package com.sampling.test.githubUser.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.net.toUri
import com.sampling.test.githubUser.R

class FavoriteUserWidget : AppWidgetProvider() {

    companion object {
        private const val TOAST_ACTION = "com.sampling.test.TOAST_ACTION"
        const val EXTRA_ITEM = "com.sampling.test.EXTRA_ITEM"
        const val UPDATE_ITEM = "com.sampling.test.githubUser.UPDATE"
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == TOAST_ACTION) {
            val name = intent.getStringExtra(EXTRA_ITEM)
            Toast.makeText(context, name, Toast.LENGTH_SHORT).show()
        } else if (intent.action == UPDATE_ITEM) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val ids = appWidgetManager.getAppWidgetIds(
                ComponentName(
                    context,
                    FavoriteUserWidget::class.java
                )
            )
            appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.stack_view)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(
                context,
                appWidgetManager,
                appWidgetId
            )
        }

    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val intent = Intent(context, StackWidgetService::class.java)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

        val views = RemoteViews(
            context.packageName,
            R.layout.favorite_user_widget
        )
        views.setRemoteAdapter(R.id.stack_view, intent)
        views.setEmptyView(
            R.id.stack_view,
            R.id.empty_view
        )

        val toastIntent = Intent(context, FavoriteUserWidget::class.java)
        toastIntent.action =
            TOAST_ACTION
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()
        val toastPendingIntent =
            PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}