package com.karelherink.flickrsearch;

import android.app.Application;
import android.content.Context;
import com.karelherink.flickrsearch.feed.FlickrFeed;
import com.karelherink.flickrsearch.util.FlickrFeedReader;

public class App extends Application {

    private static final int maxCacheItems = 1000;
    private static Context context;

    private SimpleUrlGetResponseCache<FlickrFeed> searchCache;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        searchCache = new SimpleUrlGetResponseCache<FlickrFeed>(new FlickrFeedReader<FlickrFeed>(), FlickrFeed.class, maxCacheItems);
        new Thread(searchCache).start();
    }

    public SimpleUrlGetResponseCache<FlickrFeed> getSearchCache() {
        return searchCache;
    }

    public static Context getContext() {
        return context;
    }
}
