package com.matous.nytreader.api.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Matous on 29.06.2016.
 */
public class Keyword {

    @SerializedName("value")
    @Expose
    String value;

    public String getValue() {
        return value;
    }
}
