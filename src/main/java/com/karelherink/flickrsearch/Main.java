package com.karelherink.flickrsearch;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import com.karelherink.flickrsearch.feed.FlickrFeed;
import com.karelherink.flickrsearch.util.CacheResultListener;
import com.karelherink.flickrsearch.util.Utils;

import java.net.MalformedURLException;
import java.net.URL;

public class Main extends Activity implements CacheResultListener<FlickrFeed> {

    public static final String TAG = Main.class.getName();

    private EditText editText;
    private ListView listView;
    private Spinner searchModeSpinner;

    private SimpleUrlGetResponseCache<FlickrFeed> searchCache;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchCache = ((App) getApplication()).getSearchCache();
        searchCache.setCacheResultListener(this);

        setContentView(R.layout.main);

        final FlickrFeedListAdapter flickrFeedListAdapter = new FlickrFeedListAdapter(this, R.layout.list_item, R.id.textViewAuthor);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(flickrFeedListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String link = flickrFeedListAdapter.getItem(position).getLink();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(link));
                startActivity(i);
            }
        });

        searchModeSpinner = (Spinner) findViewById(R.id.searchModeSpinner);
        ArrayAdapter spinnerAdapter = new ArrayAdapter<SearchModeInfo>(this,
                android.R.layout.simple_spinner_item,
                Utils.getSearchModes(this));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchModeSpinner.setAdapter(spinnerAdapter);
        searchModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                search();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        editText = (EditText) findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            //used to make sure we aren't re-doing consecutive searches on the same string
            String lastString = null;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String currentString = s.toString().trim();
                if (!currentString.equals(lastString)) {
                    lastString = currentString;
                    search();
                }
            }
        });
    }


    private void search() {
        String tags = editText.getText().toString().trim();
        try {
            SearchModeInfo searchModeInfo = (SearchModeInfo) searchModeSpinner.getSelectedItem();
            searchCache.requestResult(Utils.buildSearchUrl(tags, searchModeInfo));
        } catch (MalformedURLException e) {
            Log.v(TAG, "unable to for a URL using: " + tags, e);
        }

    }

    @Override
    protected void onDestroy() {
        searchCache.clearCacheResultListener();
        super.onDestroy();
    }

    @Override
    public void onResultAvailable(final URL requestUrl, final FlickrFeed result) {
        Log.v(TAG, result.toString());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String searchString = null;
                try {
                    searchString = editText.getText().toString();
                    SearchModeInfo searchModeInfo = (SearchModeInfo) searchModeSpinner.getSelectedItem();
                    URL currentSearchUrl = Utils.buildSearchUrl(searchString, searchModeInfo);
                    if (currentSearchUrl.equals(requestUrl)) {
                        ((FlickrFeedListAdapter) listView.getAdapter()).setFlickrFeed(result);
                    }
                } catch (MalformedURLException e) {
                    Log.v(TAG, "unable to for a URL using: " + searchString, e);
                }
            }
        });
    }

}
