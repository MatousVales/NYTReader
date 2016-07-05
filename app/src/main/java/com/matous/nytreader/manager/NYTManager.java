package com.matous.nytreader.manager;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import android.util.Log;

import com.matous.nytreader.api.NYTClient;
import com.matous.nytreader.api.pojos.Article;
import com.matous.nytreader.api.pojos.Image;
import com.matous.nytreader.api.pojos.NYTResponse;
import com.matous.nytreader.data.NYTContract;
import com.matous.nytreader.data.NYTDatabase;
import com.matous.nytreader.events.GetArticlesEvent;
import com.matous.nytreader.events.SendArticlesEvent;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Matous on 01.07.2016.
 */
public class NYTManager {

    private Context mContext;
    private Bus mBus;
    private NYTClient mNYTClient;

    public NYTManager(Context context, Bus bus) {
        this.mContext = context;
        this.mBus = bus;
        mNYTClient = NYTClient.getClient();
    }

    @Subscribe
    public void onGetArticles(GetArticlesEvent getArticlesEvent) {
        Map<String, String> data = getArticlesEvent.getData();
        Call<NYTResponse> call = mNYTClient.getArticles(data);
        call.enqueue(new Callback<NYTResponse>() {
            @Override
            public void onResponse(Call<NYTResponse> call, Response<NYTResponse> response) {
                final SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                StringBuilder sb = new StringBuilder("http://nytimes.com/");

                ArrayList<Article> articles = response.body().getDoc().getArticles();
                ContentValues[] values = new ContentValues[articles.size()];
                List<ContentValues> valuesList = new ArrayList<>();

                for (Article a : articles) {
                    ContentValues cv = new ContentValues();
                    cv.put(NYTContract.NYTArticlesEntry.COLUMN_HEADLINE, a.getHeadline().getText());
                    cv.put(NYTContract.NYTArticlesEntry.COLUMN_KEYWORDS, a.getKeywords());
                    cv.put(NYTContract.NYTArticlesEntry.COLUMN_PEREX, a.getPerex());
                    cv.put(NYTContract.NYTArticlesEntry.COLUMN_SOURCE, a.getSource());
                    cv.put(NYTContract.NYTArticlesEntry.COLUMN_URL, a.getUrl());
                    cv.put(NYTContract.NYTArticlesEntry.COLUMN_DATE, outputFormat.format(a.getRawdatetime()));
                    for (Image i : a.getImages()) {
                        if (i.getSubtype().equals("xlarge")) {
                            cv.put(NYTContract.NYTArticlesEntry.COLUMN_LEADIMAGE, sb.append(i.getUrl()).toString());
                        } else if (i.getSubtype().equals("thumbnail")) {
                            cv.put(NYTContract.NYTArticlesEntry.COLUMN_THUMB, sb.append(i.getUrl()).toString());
                        }
                        sb.setLength(19);
                        sb.trimToSize();
                    }
                    valuesList.add(cv);
                }
                valuesList.toArray(values);
                mBus.post(new SendArticlesEvent(mContext.getContentResolver().bulkInsert(NYTContract.CONTENT_URI_NYTARTICLES, values)));
                clearCache(mContext.getContentResolver(), 50);
            }

            @Override
            public void onFailure(Call<NYTResponse> call, Throwable t) {
                mBus.post(new SendArticlesEvent(420));
            }
        });
    }

    protected void clearCache(ContentResolver cr, int cacheSize) {
        if (cacheSize < 0) {
            throw new IllegalArgumentException();
        }

        try {
            String selection = null;
            if (cacheSize > 0) {
                selection = NYTContract.NYTArticlesEntry.COLUMN_ID + " IN " +
                        "(SELECT " + NYTContract.NYTArticlesEntry.COLUMN_ID + " FROM " + NYTContract.NYTArticlesEntry.TABLE_NAME +
                        " ORDER BY " + NYTContract.NYTArticlesEntry.COLUMN_DATE + " DESC" +
                        " LIMIT -1 OFFSET " + String.valueOf(cacheSize) + ")";
            }
            cr.delete(NYTContract.CONTENT_URI_NYTARTICLES, selection, null);
        } catch (RuntimeException e) {
            Log.e(NYTDatabase.DB_NAME, "clearCache", e);
        }
    }
}
