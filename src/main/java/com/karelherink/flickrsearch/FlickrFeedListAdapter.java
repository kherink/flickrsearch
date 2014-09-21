package com.karelherink.flickrsearch;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.karelherink.flickrsearch.feed.FlickrFeed;
import com.karelherink.flickrsearch.feed.FlickrFeedItem;
import com.squareup.picasso.Picasso;

public class FlickrFeedListAdapter extends ArrayAdapter<FlickrFeedItem> {

    private final int width;
    private final int height;

    public FlickrFeedListAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        Resources resources = getContext().getResources();
        width = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                resources.getDimension(R.dimen.thumb_width),
                resources.getDisplayMetrics()
        );
        height = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                resources.getDimension(R.dimen.thumb_height),
                resources.getDisplayMetrics()
        );
    }

    public void setFlickrFeed(FlickrFeed flickrFeed) {
        this.clear();
        for (FlickrFeedItem flickrFeedItem : flickrFeed.getItems()) {
            this.add(flickrFeedItem);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FlickrFeedItem flickrFeedItem = getItem(position);


        RelativeLayout relativeLayout;
        ViewHolder viewHolder;
        if (convertView != null && convertView instanceof RelativeLayout) {
            relativeLayout = (RelativeLayout) convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        else {
            viewHolder = new ViewHolder();
            relativeLayout = (RelativeLayout) View.inflate(getContext(),  R.layout.list_item, null);
            viewHolder.imageView = (ImageView) relativeLayout.findViewById(R.id.imageView);
            viewHolder.textViewAuthor = (TextView) relativeLayout.findViewById(R.id.textViewAuthor);
            viewHolder.textViewTags = (TextView) relativeLayout.findViewById(R.id.textViewTags);
            relativeLayout.setTag(viewHolder);
        }


        Picasso.with(getContext())
                .load(flickrFeedItem.getMedia().getM())
                .centerCrop()
                .placeholder(R.drawable.camera)
                .resize(width, height)
                .into(viewHolder.imageView);
        viewHolder.textViewAuthor.setText(getContext().getString(R.string.by, flickrFeedItem.getAuthor().replace("nobody@flickr.com ","")));
        String tags = flickrFeedItem.getTags();
        viewHolder.textViewTags.setText(tags);
        return relativeLayout;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView textViewAuthor;
        TextView textViewTags;
    }
}
