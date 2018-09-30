package com.example.megha.myapplication.rest;

import com.example.megha.myapplication.model.WikiSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("w/api.php")
    @Headers({"Accept: application/json", "Content-Type: application/json"})
    Call<WikiSearchResponse> getSerachResult(@Query("action") String action, @Query("format") String format,
                                             @Query("prop") String prop, @Query("generator") String generator, @Query("redirects") String redirects,
                                             @Query("formatversion") String formatversion, @Query("piprop") String piprop, @Query("pithumbsize") String pithumbsize,
                                             @Query("pilimit") String pilimit, @Query("wbptterms") String wbptterms, @Query("gpssearch") String gpssearch);


    //https://en.wikipedia.org/wiki/send Title ---- to open web View
}
