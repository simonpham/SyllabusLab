package com.hasbrain.areyouandroiddev.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.hasbrain.areyouandroiddev.R;
import com.hasbrain.areyouandroiddev.model.RedditPost;

import java.util.List;

/**
 * Created by Simon Pham on 4/19/18.
 * Email: simonpham.dn@gmail.com
 */

public class ExpandedPostAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title

    private final List<RedditPost> postList;

    // Human readable time  https://www.epochconverter.com/
    private final long ONE_MINUTE = 60;
    private final long ONE_HOUR = 3600;
    private final long ONE_DAY = 86400;
    private final long ONE_WEEK = 604800;
    private final long ONE_MONTH = 2629743;
    private final long ONE_YEAR = 31556926;

    public ExpandedPostAdapter(Context context, List<String> listDataHeader,
                               List<RedditPost> listChildData) {
        this.context = context;
        this._listDataHeader = listDataHeader;
        this.postList = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.postList.get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.reddit_post_list_item, null);
        }

        TextView tvScore = convertView.findViewById(R.id.tvScore);
        TextView tvAuthor = convertView.findViewById(R.id.tvAuthor);
        TextView tvRedditName = convertView.findViewById(R.id.tvRedditName);
        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        TextView tvCommentCount = convertView.findViewById(R.id.tvCommentCount);

        if (postList != null) {

            long time = postList.get(childPosition).getCreatedUTC();
            tvScore.setText(postList.get(childPosition).getScore() + "");
            tvAuthor.setText(postList.get(childPosition).getAuthor());
            tvRedditName.setText(postList.get(childPosition).getSubreddit());
            tvTitle.setText(postList.get(childPosition).getTitle());
            tvCommentCount.setText(postList.get(childPosition).getCommentCount()
                    + " Comments"
                    + " • "
                    + postList.get(childPosition).getDomain()
                    + " • "
                    + getDisplayTime(time));

            if (postList.get(childPosition).isStickyPost()) {
                tvTitle.setTextColor(context.getResources().getColor(R.color.post_title_sticky));
            }
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.postList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.post_list_header, null);
        }

        TextView lblListHeader = convertView.findViewById(R.id.tvHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private String getDisplayTime(long time) {
        String displayTime = getTimeAgo(time);
        if (!(displayTime.startsWith("-") || displayTime.startsWith("0"))) {
            return displayTime;
        }

        return new java.text.SimpleDateFormat("HH:mm:ss dd/MM/yyyy ").format(new java.util.Date(time * 1000));
    }

    private String getTimeAgo(long time) {
        long currentTime = System.currentTimeMillis() / 1000;
        long elapsedTime = currentTime - time;

        if (elapsedTime >= 0 && elapsedTime < ONE_MINUTE) {
            return "A few seconds ago";
        } else if (elapsedTime < ONE_HOUR) {
            return toMinutes(time) + " minutes ago";
        } else if (elapsedTime < ONE_DAY) {
            return toHours(time) + " hours ago";
        } else if (elapsedTime < ONE_MONTH) {
            return toDays(time) + " days ago";
        }

        return new java.text.SimpleDateFormat("HH:mm:ss dd/MM/yyyy ").format(new java.util.Date(time * 1000));
    }

    private int toMinutes(long time) {
        long currentTime = System.currentTimeMillis() / 1000;
        return Integer.parseInt(new java.text.SimpleDateFormat("m").format(new java.util.Date(currentTime * 1000)))
                - Integer.parseInt(new java.text.SimpleDateFormat("m").format(new java.util.Date(time * 1000)));
    }

    private int toHours(long time) {
        long currentTime = System.currentTimeMillis() / 1000;
        return Integer.parseInt(new java.text.SimpleDateFormat("H").format(new java.util.Date(currentTime * 1000)))
                - Integer.parseInt(new java.text.SimpleDateFormat("H").format(new java.util.Date(time * 1000)));
    }

    private int toDays(long time) {
        long currentTime = System.currentTimeMillis() / 1000;
        return Integer.parseInt(new java.text.SimpleDateFormat("D").format(new java.util.Date(currentTime * 1000)))
                - Integer.parseInt(new java.text.SimpleDateFormat("D").format(new java.util.Date(time * 1000)));
    }
}