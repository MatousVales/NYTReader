package com.matous.nytreader.api.pojos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Matous on 29.06.2016.
 */
public class Docs {

    @SerializedName("docs")
    @Expose
    public ArrayList<Article> articles = new ArrayList<>();

    public ArrayList<Article> getArticles() {
        return articles;
    }


}
