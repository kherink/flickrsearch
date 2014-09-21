package com.karelherink.flickrsearch.feed;

import java.util.List;

public class FlickrFeed {

    private List<FlickrFeedItem> items;

    public List<FlickrFeedItem> getItems() {
        return items;
    }

    public void setItems(List<FlickrFeedItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FlickrFeed{");
        sb.append("items=").append(items);
        sb.append('}');
        return sb.toString();
    }
}
