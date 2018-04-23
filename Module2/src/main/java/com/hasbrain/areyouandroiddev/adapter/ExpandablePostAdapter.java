package com.hasbrain.areyouandroiddev.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.hasbrain.areyouandroiddev.R;
import com.hasbrain.areyouandroiddev.model.RedditPost;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Simon Pham on 4/22/18.
 * Email: simonpham.dn@gmail.com
 */
public class ExpandablePostAdapter extends BaseExpandableListAdapter {

    private static final String REDDIT_PAGE_LINK = "https://www.reddit.com/r/androiddev/";

    private final long ONE_MINUTE = 60;
    private final long ONE_HOUR = 3600;
    private final long ONE_DAY = 86400;
    private final long ONE_WEEK = 604800;
    private final long ONE_MONTH = 2629743;
    private final long ONE_YEAR = 31556926;

    private final int ITEM_POST = 0;
    private final int ITEM_FOOTER = 1;

    private Context context;

    private final List<String> listDataHeader;
    private final HashMap<String, List<RedditPost>> listChildData;


    public ExpandablePostAdapter(Context context, List<String> listDataHeader, HashMap<String, List<RedditPost>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listChildData = listChildData;
    }

    @Override
    public int getGroupCount() {
        return 2;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listChildData.get(listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listChildData.get(listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.header_post_list, null);
        }

        TextView lblListHeader = convertView.findViewById(R.id.tvHeader);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        if (groupPosition == 0) {
            return super.getChildType(groupPosition, childPosition);
        } else if (childPosition < listChildData.get(listDataHeader.get(1)).size() - 1) {
            return ITEM_POST;
        } else {
            return ITEM_FOOTER;
        }
    }

    @Override
    public int getChildTypeCount() {
        return 2;
    }

    public class ViewHolder {

        TextView tvScore;
        TextView tvAuthor;
        TextView tvRedditName;
        TextView tvTitle;
        TextView tvCommentCount;
        View layout;

        ViewHolder(View v, int itemType) {
            this.layout = v;

            if (itemType == ITEM_POST) {
                tvScore = v.findViewById(R.id.tvScore);
                tvAuthor = v.findViewById(R.id.tvAuthor);
                tvRedditName = v.findViewById(R.id.tvRedditName);
                tvTitle = v.findViewById(R.id.tvTitle);
                tvCommentCount = v.findViewById(R.id.tvCommentCount);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final RedditPost post = (RedditPost) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            if (getChildType(groupPosition, childPosition) == ITEM_POST) {
                convertView = inflater.inflate(R.layout.item_reddit_post, null);
            } else {
                convertView = inflater.inflate(R.layout.item_footer, null);
            }
        }

        if (getChildType(groupPosition, childPosition) == ITEM_POST) {
            ViewHolder viewHolder = new ViewHolder(convertView, ITEM_POST);

            long time = post.getCreatedUTC();
            viewHolder.tvScore.setText(post.getScore() + "");
            viewHolder.tvAuthor.setText(post.getAuthor());
            viewHolder.tvRedditName.setText(post.getSubreddit());
            viewHolder.tvTitle.setText(post.getTitle());
            viewHolder.tvCommentCount.setText(post.getCommentCount()
                    + " Comments"
                    + " • "
                    + post.getDomain()
                    + " • "
                    + getDisplayTime(time));


            if (groupPosition == 0) {
                viewHolder.tvTitle.setTextColor(context.getResources().getColor(R.color.post_title_sticky));
            } else {
                viewHolder.tvTitle.setTextColor(context.getResources().getColor(R.color.textPrimary));
            }

        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @SuppressLint("SimpleDateFormat")
    private String getDisplayTime(long time) {
        String displayTime = getTimeAgo(time);
        if (!(displayTime.startsWith("-") || displayTime.startsWith("0"))) {
            return displayTime;
        }

        return new java.text.SimpleDateFormat("HH:mm:ss dd/MM/yyyy ").format(new java.util.Date(time * 1000));
    }

    @SuppressLint("SimpleDateFormat")
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

    @SuppressLint("SimpleDateFormat")
    private int toMinutes(long time) {
        long currentTime = System.currentTimeMillis() / 1000;
        return Integer.parseInt(new java.text.SimpleDateFormat("m").format(new java.util.Date(currentTime * 1000)))
                - Integer.parseInt(new java.text.SimpleDateFormat("m").format(new java.util.Date(time * 1000)));
    }

    @SuppressLint("SimpleDateFormat")
    private int toHours(long time) {
        long currentTime = System.currentTimeMillis() / 1000;
        return Integer.parseInt(new java.text.SimpleDateFormat("H").format(new java.util.Date(currentTime * 1000)))
                - Integer.parseInt(new java.text.SimpleDateFormat("H").format(new java.util.Date(time * 1000)));
    }

    @SuppressLint("SimpleDateFormat")
    private int toDays(long time) {
        long currentTime = System.currentTimeMillis() / 1000;
        return Integer.parseInt(new java.text.SimpleDateFormat("D").format(new java.util.Date(currentTime * 1000)))
                - Integer.parseInt(new java.text.SimpleDateFormat("D").format(new java.util.Date(time * 1000)));
    }

}
