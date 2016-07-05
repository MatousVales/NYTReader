package com.matous.nytreader.events;


/**
 * Created by Matous on 01.07.2016.
 */
public class SendArticlesEvent {

    private int result;

    public SendArticlesEvent(int i){
        this.result = i;
    }

    public int getResult(){
        return result;
    }
}
