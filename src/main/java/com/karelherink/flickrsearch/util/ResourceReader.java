package com.karelherink.flickrsearch.util;

import java.io.IOException;
import java.net.URL;

public interface ResourceReader<T> {
    T readResource(URL url, Class<T> clazz) throws IOException;
}
