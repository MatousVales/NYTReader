package com.matous.nytreader.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Matous on 30.06.2016.
 */
public class NYTContract {
    public static final String AUTHORITY = "com.matous.nytreader.data.NYTProvider";
    public static final String BASE_PATH = "articles";

    public static final Uri CONTENT_URI_NYTARTICLES = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);
    public static final Uri CONTENT_URI_NYTARTICLE = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH + "/");

    public static final class NYTArticlesEntry implements BaseColumns{
        public static final String TABLE_NAME = "articles";

        public static final String COLUMN_ID = "_ID";
        public static final String COLUMN_HEADLINE = "headline";
        public static final String COLUMN_PEREX = "perex";
        public static final String COLUMN_SOURCE = "source";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_DATE = "published_on";
        public static final String COLUMN_KEYWORDS = "keywords";
        public static final String COLUMN_THUMB = "thumbnail";
        public static final String COLUMN_LEADIMAGE = "lead_image";

        public static final String NYTARTICLE_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                + "/vnd.com.matous.NYTReader.articles";
        public static final String NYTARTICLES_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                + "/vnd.com.matous.NYTReader.articles";

        public static final String[] PROJECTION_PREVIEW =
                {COLUMN_ID,COLUMN_HEADLINE,COLUMN_DATE,COLUMN_THUMB};
        public static final String[] PROJECTION_DETAIL =
                {COLUMN_ID,COLUMN_PEREX,COLUMN_SOURCE,COLUMN_KEYWORDS,COLUMN_LEADIMAGE,COLUMN_DATE,COLUMN_URL};

        public static final String SORT_ORDER_DEFAULT = "date(" + COLUMN_DATE + ") DESC";
    }

}
