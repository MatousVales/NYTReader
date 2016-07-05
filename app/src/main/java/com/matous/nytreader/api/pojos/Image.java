package com.matous.nytreader.api.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Matous on 29.06.2016.
 */
public class Image {

    @SerializedName("url")
    @Expose
    String url;

    @SerializedName("subtype")
    @Expose
    String type;

    public String getSubtype() {
        return type;
    }

    public String getUrl() {
        return url;
    }
}
