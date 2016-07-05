package com.matous.nytreader.api.interfaces;

import com.matous.nytreader.api.pojos.NYTResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Matous on 01.07.2016.
 */

public interface INYTService {
    @GET("articlesearch.json")
    Call<NYTResponse> getArticles(@QueryMap() Map<String,String> options);
}
