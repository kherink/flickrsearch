package com.karelherink.flickrsearch;

import com.karelherink.flickrsearch.util.CacheResultListener;
import com.karelherink.flickrsearch.util.ResourceReader;
import junit.framework.TestCase;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class SimpleUrlGetResponseCacheTest extends TestCase {

    private static final int MAX_CACHE_SIZE = 3;

    public void testRequestResult() throws Exception {
        final ResourceReader<Integer> integerResourceReader = new ResourceReader<Integer>() {
            private int count;

            @Override
            public Integer readResource(URL url, Class<Integer> clazz) throws IOException {
                if (url == null && clazz == null) {
                    return count;
                }
                return count++;
            }
        };

        SimpleUrlGetResponseCache<Integer> cache = new SimpleUrlGetResponseCache<Integer>(
                integerResourceReader,
                Integer.class,
                MAX_CACHE_SIZE);
        Thread t = new Thread(cache);
        t.start();

        CacheResultListener<Integer> listener = new CacheResultListener<Integer>() {
            @Override
            public void onResultAvailable(URL requestUrl, Integer result) {
                System.out.println("received result: " + result);
            }
        };

        cache.setCacheResultListener(listener);

        //cache was created so that it can only hold up to MAX_CACHE_SIZE (3) values at a time

        //fill up the cache, the next 3 requests should hit the reader
        cache.requestResult(buildTestUrl("a"));
        cache.requestResult(buildTestUrl("b"));
        cache.requestResult(buildTestUrl("c"));

        //next 3 requests should be cached
        cache.requestResult(buildTestUrl("a"));
        cache.requestResult(buildTestUrl("b"));
        cache.requestResult(buildTestUrl("c"));
        Thread.sleep(100);

        //resource reader should only have 3 hits
        assertEquals(integerResourceReader.readResource(null, null), Integer.valueOf(3));

        //next 3 requests should go to the reader and displace the previous 3 (a,b,c)
        cache.requestResult(buildTestUrl("d"));
        cache.requestResult(buildTestUrl("e"));
        cache.requestResult(buildTestUrl("f"));
        Thread.sleep(100);

        //reader should now have 6 hits
        assertEquals(integerResourceReader.readResource(null, null), Integer.valueOf(6));

        //since the cache can no longer remember a,b,c the next 3 requests should go to the reader
        cache.requestResult(buildTestUrl("a"));
        cache.requestResult(buildTestUrl("b"));
        cache.requestResult(buildTestUrl("c"));
        Thread.sleep(100);

        assertEquals(integerResourceReader.readResource(null, null), Integer.valueOf(9));

        Thread.sleep(100);
        cache.requestResult(null);
        t.join();
    }

    private static URL buildTestUrl(String path) {
        URL url = null;
        try {
            url = new URL("http", "host", 1, path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

}
