package com.matous.nytreader.api.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Matous on 29.06.2016.
 */

public class Article {

    @SerializedName("web_url")
    @Expose
    String url;

    @SerializedName("lead_paragraph")
    @Expose
    String perex;

    @SerializedName("source")
    @Expose
    String source;

    @SerializedName("multimedia")
    @Expose
    ArrayList<Image> images = new ArrayList<>();

    @SerializedName("headline")
    @Expose
    Headline headline;

    @SerializedName("keywords")
    @Expose
    ArrayList<Keyword> keywords = new ArrayList<>();

    @SerializedName("pub_date")
    @Expose
    Date rawdatetime;

    public String getUrl() {
        return url;
    }

    public String getPerex() {
        return perex;
    }

    public String getSource() {
        return source;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public Headline getHeadline() {
        return headline;
    }

    public String getKeywords() {
        StringBuilder sb = new StringBuilder();
        boolean pom = false;

        for (Keyword k : keywords){
            if(pom){
                sb.append("|");
                sb.append(k.getValue());
            } else {
                sb.append(k.getValue());
                pom = true;
            }
        }
        return sb.toString();
    }

    public Date getRawdatetime() {
        return rawdatetime;
    }
}
