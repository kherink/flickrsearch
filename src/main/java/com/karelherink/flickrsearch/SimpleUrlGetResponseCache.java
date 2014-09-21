package com.karelherink.flickrsearch;

import android.util.Log;
import com.karelherink.flickrsearch.util.Cache;
import com.karelherink.flickrsearch.util.CacheResultListener;
import com.karelherink.flickrsearch.util.LruSizeLimitedOrderedMap;
import com.karelherink.flickrsearch.util.ResourceReader;
import com.karelherink.flickrsearch.util.UIUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SimpleUrlGetResponseCache<T> implements Cache<T>, Runnable {

    public static final String TAG = SimpleUrlGetResponseCache.class.getName();

    private final List<URL> requests = new ArrayList<URL>();

    private CacheResultListener<T> cacheResultListener;
    private ResourceReader<T> resourceReader;
    private LruSizeLimitedOrderedMap<URL, T> lruCacheImpl;

    private Class<T> clazz;

    public SimpleUrlGetResponseCache(ResourceReader<T> resourceReader, Class<T> clazz, int maxCachedResults) {
        this.clazz = clazz;
        this.resourceReader = resourceReader;
        this.lruCacheImpl = new LruSizeLimitedOrderedMap<URL, T>(maxCachedResults, true);
    }


    public void setCacheResultListener(CacheResultListener<T> cacheResultListener) {
        this.cacheResultListener = cacheResultListener;
    }

    public void clearCacheResultListener() {
        this.cacheResultListener = null;
    }

    @Override
    public void requestResult(final URL url) {
        Log.v(TAG, "requestResult for " + url);
        T response = lruCacheImpl.get(url);
        if (response != null && cacheResultListener != null) {
            cacheResultListener.onResultAvailable(url, response);
        }
        else {
            synchronized (requests) {
                requests.add(0, url);
                requests.notifyAll();
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            URL url;
            synchronized (requests) {
                while (requests.size() == 0) {
                    try {
                        requests.wait(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                url = requests.remove(0);
                Log.v(TAG, "going to fetch: " + url);
            }
            T response = lruCacheImpl.get(url);
            if (response == null) {
                try {
                    response = resourceReader.readResource(url, clazz);
                    lruCacheImpl.put(url, response);
                } catch (IOException e) {
                    Log.w(TAG, "Unable to read feed from: " + url, e);
                    UIUtils.toastOnUIThread(R.string.network_err);
                }
            }
            if (response != null && cacheResultListener != null) {
                cacheResultListener.onResultAvailable(url, response);
            }
        }
    }

}
