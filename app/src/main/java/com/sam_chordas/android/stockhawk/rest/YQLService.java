package com.sam_chordas.android.stockhawk.rest;

import com.sam_chordas.android.stockhawk.data.YQLResponse;
import com.sam_chordas.android.stockhawk.data.YQLResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface YQLService {
    @GET("yql")
    Call<YQLResponse> query(@Query("q") String query, @Query("format") String format, @Query("env") String env);
}
