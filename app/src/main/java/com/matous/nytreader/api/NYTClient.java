package com.matous.nytreader.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.matous.nytreader.api.interfaces.INYTService;
import com.matous.nytreader.api.pojos.NYTResponse;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Matous on 01.07.2016.
 */
public class NYTClient {

    private static final String BASE_URL="http://api.nytimes.com/svc/search/v2/";

    private static NYTClient mNYTClient;
    private static Retrofit mRestAdapter;

    public static NYTClient getClient(){
        if(mNYTClient == null){
            mNYTClient = new NYTClient();
        }
        return mNYTClient;
    }

    private NYTClient(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyy-MM-dd'T'HH:mm:ss'Z'")
                .create();

        mRestAdapter = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(new OkHttpClient.Builder().connectTimeout(2,TimeUnit.SECONDS).readTimeout(2,TimeUnit.SECONDS).build())
                .build();
    }

    public Call<NYTResponse> getArticles(Map<String,String> data){
        INYTService service = mRestAdapter.create(INYTService.class);
        return service.getArticles(data);
    }
}
