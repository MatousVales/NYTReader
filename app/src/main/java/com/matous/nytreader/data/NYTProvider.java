package com.matous.nytreader.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;


/**
 * Created by Matous on 30.06.2016.
 */
public class NYTProvider extends ContentProvider {
    private NYTDatabase DBhelper = null;

    private static final int ARTICLES = 0;
    private static final int ARTICLE = 1;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(NYTContract.AUTHORITY, NYTContract.BASE_PATH, 0);
        sURIMatcher.addURI(NYTContract.AUTHORITY, NYTContract.BASE_PATH + "/#", 1);
    }


    @Override
    public boolean onCreate() {
        DBhelper = new NYTDatabase((getContext()));
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = DBhelper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        switch (sURIMatcher.match(uri)){
            case ARTICLES:
                builder.setTables(NYTContract.NYTArticlesEntry.TABLE_NAME);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = NYTContract.NYTArticlesEntry.SORT_ORDER_DEFAULT;
                }
                break;
            case ARTICLE:
                builder.setTables(NYTContract.NYTArticlesEntry.TABLE_NAME);
                builder.appendWhere(NYTContract.NYTArticlesEntry.COLUMN_ID + " = " + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
        return builder.query(db,projection,selection,selectionArgs,null,null,sortOrder);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = sURIMatcher.match(uri);

        switch (match){
            case ARTICLE:
                return NYTContract.NYTArticlesEntry.NYTARTICLE_MIME_TYPE;
            case ARTICLES:
                return NYTContract.NYTArticlesEntry.NYTARTICLES_MIME_TYPE;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = DBhelper.getWritableDatabase();
        final int match = sURIMatcher.match(uri);

        if(match == ARTICLES){
            long id = db.insertOrThrow(NYTContract.NYTArticlesEntry.TABLE_NAME,null,values);
            Uri newUri = ContentUris.withAppendedId(NYTContract.CONTENT_URI_NYTARTICLES,id);
            return newUri;
        }
        throw new IllegalArgumentException("Unknown URI " + uri);
    }

    @Nullable
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values){
        int numInserted = 0;
        final SQLiteDatabase db = DBhelper.getWritableDatabase();
        final int match = sURIMatcher.match(uri);

        if(match == ARTICLES){
            db.beginTransaction();
            try{
                for (ContentValues cv : values){
                    db.insertWithOnConflict(NYTContract.NYTArticlesEntry.TABLE_NAME,null,cv,SQLiteDatabase.CONFLICT_REPLACE);
                }
                db.setTransactionSuccessful();
                numInserted = values.length;
            } catch (SQLException e){
                Log.e(NYTDatabase.DB_NAME, e.toString());
            } finally{
                db.endTransaction();
            }
            return numInserted;
        }
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = DBhelper.getWritableDatabase();
        final int match = sURIMatcher.match(uri);

        if(match == ARTICLES) {
            return db.delete(NYTContract.NYTArticlesEntry.TABLE_NAME, selection, selectionArgs);
        } else {
            throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("not implemented");
    }
}
