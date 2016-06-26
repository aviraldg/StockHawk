package com.sam_chordas.android.stockhawk.ui;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.db.chart.model.ChartSet;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.LineChartView;
import com.db.chart.view.animation.Animation;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.Quote;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.data.YQLResponse;
import com.sam_chordas.android.stockhawk.data.YQLResult;
import com.sam_chordas.android.stockhawk.rest.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockDetailActivity extends Activity {
    public static final String SYMBOL = "symbol";
    private LineChartView lineChartView;
    private String symbol;

    public static Intent getIntent(Context context, String symbol) {
        Intent i = new Intent(context, StockDetailActivity.class);
        i.putExtra(SYMBOL, symbol);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);

        symbol = getIntent().getStringExtra(SYMBOL);
        if(symbol == null) {
            throw new IllegalStateException("symbol == null");
        }

        initUi();
    }

    private void initUi() {
        ((TextView) findViewById(R.id.stock_symbol)).setText(symbol);
        lineChartView = (LineChartView) findViewById(R.id.linechart);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar gc = GregorianCalendar.getInstance();
        String end = sdf.format(gc.getTime());
        gc.add(GregorianCalendar.YEAR, -1);
        String start = sdf.format(gc.getTime());
        String query = String.format("select * from yahoo.finance.historicaldata where symbol = \"%s\" and startDate = \"%s\" and endDate = \"%s\"",
                symbol, start, end);
        Log.d("StockDetailActivity", query);
        Utils.getYQLService().query(query,
                "json", "store://datatables.org/alltableswithkeys")
                .enqueue(new Callback<YQLResponse>() {
                    @Override
                    public void onResponse(Call<YQLResponse> call, Response<YQLResponse> response) {
                        if(response.isSuccessful()) {

                            LineSet lineSet = new LineSet();
                            List<Quote> data = response.body().query.results.quote;

                            for (Quote q: data) {
                                lineSet.addPoint(q.Date, Float.valueOf(q.Close));
                            }

                            int color = getResources()
                                    .getColor(R.color.material_blue_300);
                            int background = getResources()
                                    .getColor(R.color.material_blue_700);
                            lineSet.setColor(color)
                                    .setFill(color);

                            lineChartView.addData(lineSet);
                            lineChartView
                                    .setXAxis(false)
                                    .setYAxis(false)
                                    .setYLabels(AxisController.LabelPosition.NONE)
                                    .setXLabels(AxisController.LabelPosition.NONE);
                            lineChartView.setBackgroundColor(background);
                            lineChartView.show(new Animation(1000));
                        }
                    }

                    @Override
                    public void onFailure(Call<YQLResponse> call, Throwable t) {

                    }
                });
    }

}
