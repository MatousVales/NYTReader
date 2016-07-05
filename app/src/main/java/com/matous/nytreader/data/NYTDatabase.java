package com.matous.nytreader.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Matous on 29.06.2016.
 */
public class NYTDatabase extends SQLiteOpenHelper{
    public static final String DB_NAME = "articles";
    private static final int DB_VERSION = 1;

    public static final String CREATE_TABLE_ARTICLE = "CREATE TABLE " + NYTContract.NYTArticlesEntry.TABLE_NAME + "(" + NYTContract.NYTArticlesEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + NYTContract.NYTArticlesEntry.COLUMN_HEADLINE + " TEXT UNIQUE," + NYTContract.NYTArticlesEntry.COLUMN_KEYWORDS + " TEXT," + NYTContract.NYTArticlesEntry.COLUMN_PEREX + " TEXT," + NYTContract.NYTArticlesEntry.COLUMN_SOURCE + " TEXT," + NYTContract.NYTArticlesEntry.COLUMN_URL + " TEXT," + NYTContract.NYTArticlesEntry.COLUMN_THUMB + " TEXT," + NYTContract.NYTArticlesEntry.COLUMN_LEADIMAGE + " TEXT," + NYTContract.NYTArticlesEntry.COLUMN_DATE + " DATETIME)";

    public NYTDatabase(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ARTICLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NYTContract.NYTArticlesEntry.TABLE_NAME);

        onCreate(db);
    }

}
