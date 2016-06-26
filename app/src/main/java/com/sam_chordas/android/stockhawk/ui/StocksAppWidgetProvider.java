package com.sam_chordas.android.stockhawk.ui;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.service.StocksAppWidgetService;

public class StocksAppWidgetProvider extends AppWidgetProvider {
    private static final String TAG = "StocksAppWidgetProvider";
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int id: appWidgetIds) {
            Intent intent = new Intent(context, StocksAppWidgetService.class);
            // Add the app widget ID to the intent extras.
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.stocks_appwidget);

            remoteViews.setRemoteAdapter(id, R.id.listview, intent);

            // The empty view is displayed when the collection has no items.
            // It should be in the same layout used to instantiate the RemoteViews
            // object above.
            remoteViews.setEmptyView(R.id.listview, R.id.empty);

            //
            // Do additional processing specific to this app widget...
            //

            appWidgetManager.updateAppWidget(id, remoteViews);
        }
    }
}
