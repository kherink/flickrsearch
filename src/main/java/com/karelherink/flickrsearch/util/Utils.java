package com.karelherink.flickrsearch.util;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.karelherink.flickrsearch.App;
import com.karelherink.flickrsearch.R;
import com.karelherink.flickrsearch.SearchModeInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static final String BASE_FLICKR_API_URL = "https://api.flickr.com/services/feeds/photos_public.gne?format=json";

    public static void toastOnUIThread(final int stringResId) {
        (new Handler(Looper.getMainLooper())).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(App.getContext(), stringResId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static URL buildSearchUrl(String tags, SearchModeInfo searchModeInfo) throws MalformedURLException {
        Uri.Builder builder = Uri.parse(BASE_FLICKR_API_URL).buildUpon();
        builder.appendQueryParameter("tags", toCommaSeparated(tags));
        builder.appendQueryParameter("tagmode", searchModeInfo.getParam());
        return new URL(builder.build().toString());
    }

    public static String toCommaSeparated(String spaceSeparated) {
        String sanitized = spaceSeparated.replaceAll("\\s+", " ").trim();
        return sanitized.replaceAll("\\s", ",");
    }

    public static List<SearchModeInfo> getSearchModes(Context context) {
        List<SearchModeInfo> searchModes = new ArrayList<SearchModeInfo>();
        String[] data = context.getResources().getStringArray(R.array.search_mode);
        for (String s : data) {
            String[] fields = s.split(",");
            searchModes.add(new SearchModeInfo(fields[0], fields[1]));
        }
        return searchModes;
    }
}
