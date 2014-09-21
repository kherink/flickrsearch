package com.karelherink.flickrsearch.util;

import android.util.Log;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FlickrFeedReader<T> implements ResourceReader<T> {

    private static final String TAG = FlickrFeedReader.class.getName();

    private Gson gson = new Gson();

    @Override
    public T readResource(URL url, Class<T> clazz) throws IOException {
        Log.i(TAG, "going to network for: " + url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            String resp = asString(conn.getInputStream());

            //unbelievable they couldn't make this plain JSON
            resp = resp.replace("jsonFlickrFeed(", "");
            resp = resp.substring(resp.indexOf("{"), resp.length() - 2);

            return gson.fromJson(resp, clazz);
        }
        throw new IOException("Connection to " + url + " returned resp code: " + conn.getResponseCode() + " with: " + conn.getResponseMessage());
    }

    private static String asString(InputStream in) throws IOException {
        StringBuilder inputStringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = bufferedReader.readLine();
        while(line != null){
            inputStringBuilder.append(line).append("\n");
            line = bufferedReader.readLine();
        }
        return inputStringBuilder.toString();
    }
}
