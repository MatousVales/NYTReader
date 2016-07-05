package com.matous.nytreader.api.pojos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Matous on 29.06.2016.
 */
public class NYTResponse {
    @SerializedName("response")
    @Expose
    Docs doc;

    public Docs getDoc() {
        return doc;
    }
}
