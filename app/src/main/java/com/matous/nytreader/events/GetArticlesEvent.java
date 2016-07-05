package com.matous.nytreader.events;

import java.util.Map;

/**
 * Created by Matous on 01.07.2016.
 */
public class GetArticlesEvent {

    private Map<String,String> data;

    public GetArticlesEvent(Map<String,String> queryoptions){
        this.data = queryoptions;
    }

    public Map<String,String> getData(){
        return data;
    }
}
