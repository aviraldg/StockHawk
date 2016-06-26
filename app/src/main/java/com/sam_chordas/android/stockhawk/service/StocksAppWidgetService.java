package com.sam_chordas.android.stockhawk.service;

import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

/**
 * Created by aviraldg on 26/6/16.
 */

public class StocksAppWidgetService extends RemoteViewsService {
    RemoteViewsFactory remoteViewsFactory = new RemoteViewsFactory() {
        Cursor cursor = null;
        public CursorLoader cursorLoader;

        @Override
        public void onCreate() {
            cursorLoader = new CursorLoader(StocksAppWidgetService.this, QuoteProvider.Quotes.CONTENT_URI,
                    new String[]{ QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                            QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                    QuoteColumns.ISCURRENT + " = ?",
                    new String[]{"1"},
                    null);
        }

        @Override
        public void onDataSetChanged() {
            cursorLoader.reset();
            cursor = cursorLoader.loadInBackground();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return cursor == null ? 0 : cursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if(cursor == null || cursor.isClosed()) {
                cursorLoader.reset();
                cursor = cursorLoader.loadInBackground();
            }
            cursor.moveToFirst();
            cursor.move(position);
            RemoteViews rv = new RemoteViews(getPackageName(), R.layout.list_item_quote);
            rv.setTextViewText(R.id.stock_symbol, cursor.getString(cursor.getColumnIndex(QuoteColumns.SYMBOL)));
            rv.setTextViewText(R.id.bid_price, cursor.getString(cursor.getColumnIndex(QuoteColumns.BIDPRICE)));
            rv.setTextViewText(R.id.change, cursor.getString(cursor.getColumnIndex(QuoteColumns.PERCENT_CHANGE)));
            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    };

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return remoteViewsFactory;
    }
}
