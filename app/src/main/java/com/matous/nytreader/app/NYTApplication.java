package com.matous.nytreader.app;

import android.app.Application;

import com.matous.nytreader.bus.BusProvider;
import com.matous.nytreader.manager.NYTManager;
import com.squareup.otto.Bus;

/**
 * Created by Matous on 01.07.2016.
 */
public class NYTApplication extends Application {

    private Bus mBus = BusProvider.getInstance();

    @Override
    public void onCreate(){
        super.onCreate();

        NYTManager mNYTmanager = new NYTManager(this,mBus);
        mBus.register(mNYTmanager);
        mBus.register(this);
    }
}
