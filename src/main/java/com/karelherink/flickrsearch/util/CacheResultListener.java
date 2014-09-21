package com.karelherink.flickrsearch.util;

import java.net.URL;

public interface CacheResultListener<T> {

    public void onResultAvailable(URL requestUrl, T result);

}
