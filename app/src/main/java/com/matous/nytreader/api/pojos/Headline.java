package com.matous.nytreader.api.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Matous on 29.06.2016.
 */
public class Headline {

    @SerializedName("main")
    @Expose
    String text;

    public String getText() {
        return text;
    }
}
